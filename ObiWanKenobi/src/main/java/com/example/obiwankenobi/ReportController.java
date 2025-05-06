package com.example.obiwankenobi;

import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Kontroler odpowiedzialny za generowanie raportów w formacie PDF w aplikacji.
 * Obsługuje generowanie raportów na podstawie danych zadania, użytkowników i działów.
 */
public class ReportController implements Initializable {

    private static final DeviceRgb HEADER_BACKGROUND = new DeviceRgb(200, 200, 200);

    @FXML
    private DatePicker startDate;

    @FXML
    private DatePicker endDate;

    @FXML
    private ChoiceBox<String> departmentChoiceBox;

    /**
     * Inicjalizuje kontroler. Wczytuje listę działów do rozwijanej listy (ChoiceBox).
     *
     * @param location Adres URL kontrolera
     * @param resources Zasoby kontrolera
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadDeps();
    }

    /**
     * Wczytuje listę działów z bazy danych i dodaje je do rozwijanej listy (ChoiceBox).
     * Domyślnie dodaje również opcję "wszystkie".
     */
    private void loadDeps() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String sql = "SELECT name FROM departments ORDER BY name";
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            departmentChoiceBox.getItems().add("wszystkie");

            while (rs.next()) {
                String departmentName = rs.getString("name");
                departmentChoiceBox.getItems().add(departmentName);
            }

            departmentChoiceBox.setValue("wszystkie");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Zamknięcie okna raportów.
     *
     * @param event Zdarzenie wywołujące zamknięcie okna
     */
    @FXML
    void reportsClose(ActionEvent event) {
        Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        currentStage.close();
    }

    /**
     * Generuje raport o wykonanych zadaniach, zapisując go do pliku PDF.
     * Raport uwzględnia filtry daty oraz działu.
     *
     * @param event Zdarzenie wywołujące generowanie raportu
     */
    @FXML
    void tasksDoneReport(ActionEvent event) {
        String outputPath = "tasksDone.pdf";

        LocalDate startDateVal = startDate.getValue();
        LocalDate endDateVal = endDate.getValue();
        String selectedDepartment = departmentChoiceBox.getValue();

        try (Connection connection = DatabaseConnection.getConnection();
             PdfWriter writer = new PdfWriter(outputPath)) {

            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50, 30, 50, 30);

            document.add(new Paragraph("Raport wykonanych zadan").setBold().setFontSize(18));
            document.add(new Paragraph("\n"));

            StringBuilder sql = new StringBuilder("""
            SELECT 
                d.name AS department_name,
                t.id AS task_id,
                t.title,
                t.description,
                t.deadline,
                u.first_name AS user_first_name,
                u.last_name AS user_last_name,
                mgr.first_name AS mgr_first_name,
                mgr.last_name AS mgr_last_name
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            LEFT JOIN departments d ON u.department_id = d.id
            LEFT JOIN users mgr ON d.manager_id = mgr.id
            WHERE t.status = 'zakończone'
        """);

            List<Object> params = new ArrayList<>();

            if (startDateVal != null) {
                sql.append(" AND t.deadline >= ? ");
                params.add(Date.valueOf(startDateVal));
            }

            if (endDateVal != null) {
                sql.append(" AND t.deadline <= ? ");
                params.add(Date.valueOf(endDateVal));
            }

            if (selectedDepartment != null && !selectedDepartment.isBlank() && !selectedDepartment.equals("wszystkie")) {
                sql.append(" AND d.name = ? ");
                params.add(selectedDepartment);
            }

            sql.append(" ORDER BY d.name, t.deadline ASC");

            PreparedStatement stmt = connection.prepareStatement(sql.toString());

            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            ResultSet rs = stmt.executeQuery();

            String currentDepartment = "";
            int taskCount = 0;
            String managerFullName = "";

            Table table = null;

            while (rs.next()) {
                String departmentName = rs.getString("department_name") != null ? rs.getString("department_name") : "Brak dzialu";

                if (!departmentName.equals(currentDepartment)) {
                    if (!currentDepartment.equals("")) {
                        document.add(table);

                        Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
                        summaryTable.setWidth(UnitValue.createPercentValue(100));
                        summaryTable.addCell(new Cell()
                                .add(new Paragraph("Laczna liczba zadan: " + taskCount))
                                .setTextAlignment(TextAlignment.LEFT)
                                .setBorder(Border.NO_BORDER))
                                .setBold();
                        summaryTable.addCell(new Cell()
                                .add(new Paragraph("Kierownik dzialu: " + managerFullName))
                                .setTextAlignment(TextAlignment.RIGHT)
                                .setBorder(Border.NO_BORDER)
                                .setBold());
                        document.add(summaryTable);
                        document.add(new Paragraph("\n"));
                    }

                    currentDepartment = departmentName;
                    taskCount = 0;
                    managerFullName = (rs.getString("mgr_first_name") != null && rs.getString("mgr_last_name") != null)
                            ? rs.getString("mgr_first_name") + " " + rs.getString("mgr_last_name")
                            : "Brak";

                    document.add(new Paragraph("Dzial: " + currentDepartment).setBold().setFontSize(14));

                    table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 4, 2, 2}));
                    table.setWidth(UnitValue.createPercentValue(100));
                    table.addHeaderCell(new Cell().add(new Paragraph("ID")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                    table.addHeaderCell(new Cell().add(new Paragraph("Tytul")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                    table.addHeaderCell(new Cell().add(new Paragraph("Opis")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                    table.addHeaderCell(new Cell().add(new Paragraph("Termin")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                    table.addHeaderCell(new Cell().add(new Paragraph("Przydzielono")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                }

                table.addCell(String.valueOf(rs.getInt("task_id")));
                table.addCell(rs.getString("title"));
                table.addCell(rs.getString("description"));
                table.addCell(rs.getString("deadline") != null ? rs.getString("deadline") : "Brak");
                String assignee = (rs.getString("user_first_name") != null && rs.getString("user_last_name") != null)
                        ? rs.getString("user_first_name") + " " + rs.getString("user_last_name")
                        : "Brak";
                table.addCell(assignee);

                taskCount++;
            }

            if (table != null) {
                document.add(table);

                Table summaryTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}));
                summaryTable.setWidth(UnitValue.createPercentValue(100));
                summaryTable.addCell(new Cell()
                        .add(new Paragraph("Laczna liczba zadan: " + taskCount))
                        .setTextAlignment(TextAlignment.LEFT)
                        .setBorder(Border.NO_BORDER))
                        .setBold();
                summaryTable.addCell(new Cell()
                        .add(new Paragraph("Kierownik dzialu: " + managerFullName))
                        .setTextAlignment(TextAlignment.RIGHT)
                        .setBorder(Border.NO_BORDER))
                        .setBold();
                document.add(summaryTable);
            }

            document.add(new Paragraph("\n\n"));

            Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}));
            signatureTable.setWidth(UnitValue.createPercentValue(100));

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedDate = now.format(formatter);

            Cell footer = new Cell()
                    .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                    .setBold()
                    .setMarginTop(30)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER);

            Cell empty = new Cell().setBorder(Border.NO_BORDER);

            Cell signature = new Cell()
                    .add(new Paragraph("________________________________"))
                    .add(new Paragraph("podpis osoby odbierajacej"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER);

            signatureTable.addCell(footer);
            signatureTable.addCell(empty);
            signatureTable.addCell(signature);

            document.add(signatureTable);
            document.close();

            System.out.println("Raport wykonanych zadan zostal wygenerowany.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generuje raport zadań w trakcie realizacji w formacie PDF.
     *
     * @param event Zdarzenie wywołujące generowanie raportu
     */
    @FXML
    void tasksInProgressReport(ActionEvent event) {
        String outputPath = "tasksInProgressReport.pdf";

        try (Connection connection = DatabaseConnection.getConnection();
             PdfWriter writer = new PdfWriter(outputPath)) {

            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50, 30, 50, 30);

            document.add(new Paragraph("Raport zadan w trakcie realizacji").setBold().setFontSize(18));
            document.add(new Paragraph("\n"));

            String sql = """
            SELECT 
                t.id,
                t.title,
                t.description,
                t.deadline,
                u.first_name,
                u.last_name
            FROM tasks t
            LEFT JOIN users u ON t.user_id = u.id
            WHERE t.status = 'w trakcie' 
            OR t.status = 'oczekujące'
            ORDER BY t.deadline ASC
        """;

            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 3, 4, 2, 2}));
            table.setWidth(UnitValue.createPercentValue(100));

            table.addHeaderCell(new Cell().add(new Paragraph("ID")).setBold().setBackgroundColor(HEADER_BACKGROUND));
            table.addHeaderCell(new Cell().add(new Paragraph("Tytul")).setBold().setBackgroundColor(HEADER_BACKGROUND));
            table.addHeaderCell(new Cell().add(new Paragraph("Opis")).setBold().setBackgroundColor(HEADER_BACKGROUND));
            table.addHeaderCell(new Cell().add(new Paragraph("Termin")).setBold().setBackgroundColor(HEADER_BACKGROUND));
            table.addHeaderCell(new Cell().add(new Paragraph("Przydzielono")).setBold().setBackgroundColor(HEADER_BACKGROUND));

            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("title"));
                table.addCell(rs.getString("description"));
                table.addCell(rs.getString("deadline") != null ? rs.getString("deadline") : "Brak");
                String assignee = (rs.getString("first_name") != null && rs.getString("last_name") != null)
                        ? rs.getString("first_name") + " " + rs.getString("last_name")
                        : "Brak";
                table.addCell(assignee);
            }

            document.add(table);
            document.add(new Paragraph("\n\n"));

            Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}));
            signatureTable.setWidth(UnitValue.createPercentValue(100));

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedDate = now.format(formatter);

            Cell footer = new Cell()
                    .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                    .setBold()
                    .setMarginTop(30)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER);

            Cell empty = new Cell().setBorder(Border.NO_BORDER);

            Cell signature = new Cell()
                    .add(new Paragraph("________________________________"))
                    .add(new Paragraph("podpis osoby odbierajacej"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER);

            signatureTable.addCell(footer);
            signatureTable.addCell(empty);
            signatureTable.addCell(signature);

            document.add(signatureTable);
            document.close();

            System.out.println("Raport zadan w trakcie realizacji zostal wygenerowany.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generuje raport użytkowników w formacie PDF.
     * Raport zawiera dane użytkowników, takie jak ID, imię, nazwisko, email oraz przypisany dział.
     * W zależności od wybranego działu, raport może być filtrowany.
     *
     * @param event Zdarzenie wywołujące generowanie raportu
     * @throws FileNotFoundException Jeśli nie uda się zapisać pliku PDF
     */
    @FXML
    void userReport(ActionEvent event) throws FileNotFoundException {
        String outputPath = "usersReport.pdf";
        String selectedDepartment = departmentChoiceBox.getValue();

        try (Connection connection = DatabaseConnection.getConnection();
             PdfWriter writer = new PdfWriter(outputPath)) {

            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50, 30, 50, 30);

            document.add(new Paragraph("Raport uzytkownikow").setBold().setFontSize(18));
            document.add(new Paragraph("\n"));

            Table table = new Table(UnitValue.createPercentArray(new float[]{1, 2, 2, 4, 3}));
            table.setWidth(UnitValue.createPercentValue(100));

            Cell idHeader = new Cell()
                    .add(new Paragraph("ID"))
                    .setBold()
                    .setBackgroundColor(HEADER_BACKGROUND);

            Cell fistNameHeader = new Cell()
                    .add(new Paragraph("Imie"))
                    .setBold()
                    .setBackgroundColor(HEADER_BACKGROUND);

            Cell lastNameHeader = new Cell()
                    .add(new Paragraph("Nazwisko"))
                    .setBold()
                    .setBackgroundColor(HEADER_BACKGROUND);

            Cell emailHeader = new Cell()
                    .add(new Paragraph("Email"))
                    .setBold()
                    .setBackgroundColor(HEADER_BACKGROUND);

            Cell depHeader = new Cell()
                    .add(new Paragraph("Dzial"))
                    .setBold()
                    .setBackgroundColor(HEADER_BACKGROUND);

            table.addCell(idHeader);
            table.addCell(fistNameHeader);
            table.addCell(lastNameHeader);
            table.addCell(emailHeader);
            table.addCell(depHeader);

            String sql = "SELECT u.id, u.first_name, u.last_name, u.email, d.name FROM users u LEFT JOIN departments d on u.department_id = d.id";

            if (selectedDepartment != null && !selectedDepartment.isEmpty() && !selectedDepartment.equals("wszystkie")) {
                sql += " WHERE d.name = ?";
            }

            PreparedStatement stmt = connection.prepareStatement(sql);

            if (selectedDepartment != null && !selectedDepartment.isEmpty() && !selectedDepartment.equals("wszystkie")) {
                stmt.setString(1, selectedDepartment);
            }

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                table.addCell(String.valueOf(rs.getInt("id")));
                table.addCell(rs.getString("first_name"));
                table.addCell(rs.getString("last_name"));
                table.addCell(rs.getString("email"));
                String dept = rs.getString("name");
                table.addCell(dept != null ? dept : "brak");
            }

            document.add(table);
            document.add(new Paragraph("\n\n"));

            Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}));
            signatureTable.setWidth(UnitValue.createPercentValue(100));

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedDate = now.format(formatter);

            Cell footer = new Cell()
                    .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                    .setBold()
                    .setMarginTop(30)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER);

            Cell empty = new Cell()
                    .setBorder(Border.NO_BORDER);

            Cell siganture = new Cell()
                    .add(new Paragraph("________________________________"))
                    .add(new Paragraph("podpis osoby odbierajacej"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER);

            signatureTable.addCell(footer);
            signatureTable.addCell(empty);
            signatureTable.addCell(siganture);

            document.add(signatureTable);
            document.close();

            System.out.println("Raport zostal wygenerowany.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generuje raport magazynowy w formacie PDF.
     * Raport zawiera dane dotyczące magazynów, takie jak ID magazynu, nazwa magazynu, przedmioty w magazynie oraz ich ilości.
     * Dodatkowo uwzględnia dane o kierowniku działu przypisanego do magazynu.
     * W zależności od wybranego działu, raport może być filtrowany.
     *
     * @param event Zdarzenie wywołujące generowanie raportu
     */
    @FXML
    void warehouseReport(ActionEvent event) {
        String outputPath = "warehouseReport.pdf";
        String selectedDepartment = departmentChoiceBox.getValue();

        try (Connection connection = DatabaseConnection.getConnection();
             PdfWriter writer = new PdfWriter(outputPath)) {

            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4);
            document.setMargins(50, 30, 50, 30);

            document.add(new Paragraph("Stany magazynowy").setBold().setFontSize(18));
            document.add(new Paragraph("\n"));

            String sql = """
                SELECT 
                    w.id AS warehouse_id,
                    w.name AS warehouse_name,
                    i.name AS item_name,
                    i.quantity,
                    u.first_name,
                    u.last_name
                FROM items i
                JOIN warehouses w ON i.warehouse_id = w.id
                JOIN departments d ON w.department_id = d.id
                LEFT JOIN users u ON d.manager_id = u.id
            """;

            if (selectedDepartment != null && !selectedDepartment.isEmpty() && !selectedDepartment.equals("wszystkie")) {
                sql += " WHERE d.name = ?";
            }

            PreparedStatement stmt = connection.prepareStatement(sql);

            if (selectedDepartment != null && !selectedDepartment.isEmpty() && !selectedDepartment.equals("wszystkie")) {
                stmt.setString(1, selectedDepartment);
            }

            ResultSet rs = stmt.executeQuery();

            int currentWarehouseId = -1;
            Table table = null;

            while (rs.next()) {
                int warehouseId = rs.getInt("warehouse_id");

                if (warehouseId != currentWarehouseId) {
                    if (table != null) {
                        document.add(table);
                        document.add(new Paragraph("\n"));
                    }

                    String warehouseName = rs.getString("warehouse_name");
                    String managerName = rs.getString("first_name");
                    String managerLastName = rs.getString("last_name");

                    String managerDisplay = (managerName != null && managerLastName != null)
                            ? managerName + " " + managerLastName
                            : "Brak";

                    Table headerTable = new Table(UnitValue.createPercentArray(new float[]{1, 1}))
                            .setWidth(UnitValue.createPercentValue(100))
                            .setMarginTop(10)
                            .setMarginBottom(5);

                    Cell warehouseCell = new Cell()
                            .add(new Paragraph("Magazyn: " + warehouseName))
                            .setBold()
                            .setFontSize(14)
                            .setBorder(Border.NO_BORDER)
                            .setTextAlignment(TextAlignment.LEFT);

                    Cell managerCell = new Cell()
                            .add(new Paragraph("Kierownik: " + managerDisplay))
                            .setBold()
                            .setFontSize(14)
                            .setBorder(Border.NO_BORDER)
                            .setTextAlignment(TextAlignment.RIGHT);

                    headerTable.addCell(warehouseCell);
                    headerTable.addCell(managerCell);

                    document.add(headerTable);

                    table = new Table(UnitValue.createPercentArray(new float[]{3, 2}))
                            .setWidth(UnitValue.createPercentValue(100));

                    table.addHeaderCell(new Cell().add(new Paragraph("Nazwa przedmiotu")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                    table.addHeaderCell(new Cell().add(new Paragraph("Ilosc")).setBold().setBackgroundColor(HEADER_BACKGROUND));

                    currentWarehouseId = warehouseId;
                }

                table.addCell(rs.getString("item_name"));
                table.addCell(String.valueOf(rs.getInt("quantity")));
            }

            if (table != null) {
                document.add(table);
            }

            document.add(new Paragraph("\n\n"));

            Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}));
            signatureTable.setWidth(UnitValue.createPercentValue(100));

            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
            String formattedDate = now.format(formatter);

            Cell footer = new Cell()
                    .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                    .setBold()
                    .setMarginTop(30)
                    .setTextAlignment(TextAlignment.LEFT)
                    .setBorder(Border.NO_BORDER);

            Cell empty = new Cell().setBorder(Border.NO_BORDER);

            Cell signature = new Cell()
                    .add(new Paragraph("________________________________"))
                    .add(new Paragraph("podpis osoby odbierajacej"))
                    .setBorder(Border.NO_BORDER)
                    .setTextAlignment(TextAlignment.CENTER);

            signatureTable.addCell(footer);
            signatureTable.addCell(empty);
            signatureTable.addCell(signature);

            document.add(signatureTable);
            document.close();

            System.out.println("Raport zostal wygenerowany.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
