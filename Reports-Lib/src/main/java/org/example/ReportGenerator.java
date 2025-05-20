package org.example;


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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportGenerator {

    private static final DeviceRgb HEADER_BACKGROUND = new DeviceRgb(200, 200, 200);

/**
 * Generuje raport zadań w trakcie realizacji w formacie PDF.
 *
 */
        public static void generateTask(String status, LocalDate startDate, LocalDate endDate,
                                        String priority, String department, Integer userId) {
            LocalDateTime nowTitle = LocalDateTime.now();
            DateTimeFormatter formatterTitle = DateTimeFormatter.ofPattern("dd_MM_yyyy_HHmmss");
            String formattedDateTitle = nowTitle.format(formatterTitle);

            String outputPath = "tasksReport_" + formattedDateTitle + ".pdf";

            StringBuilder sql = new StringBuilder("""
                            SELECT 
                                t.id,
                                t.title,
                                t.description,
                                t.deadline,
                                t.status,
                                t.priority,
                                u.first_name,
                                u.last_name,
                                d.name as department_name
                            FROM tasks t
                            LEFT JOIN users u ON t.user_id = u.id
                            LEFT JOIN departments d ON u.department_id = d.id
                            WHERE 1=1
                        """);

            if (status != null && !status.isEmpty()) sql.append(" AND t.status = ?");
            if (startDate != null) sql.append(" AND t.deadline >= ?");
            if (endDate != null) sql.append(" AND t.deadline <= ?");
            if (priority != null && !priority.isEmpty()) sql.append(" AND t.priority = ?");
            if (department != null && !department.equals("wszyscy")) sql.append(" AND d.name = ?");
            if (userId != null) sql.append(" AND u.id = ?");

            sql.append(" ORDER BY t.deadline ASC");

            try (Connection connection = DatabaseConnection.getConnection();
                 PdfWriter writer = new PdfWriter(outputPath);
                 PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

                int paramIndex = 1;

                if (status != null && !status.isEmpty()) stmt.setString(paramIndex++, status);
                if (startDate != null) stmt.setDate(paramIndex++, Date.valueOf(startDate));
                if (endDate != null) stmt.setDate(paramIndex++, Date.valueOf(endDate));
                if (priority != null && !priority.isEmpty()) stmt.setString(paramIndex++, priority);
                if (department != null && !department.equals("wszyscy")) stmt.setString(paramIndex++, department);
                if (userId != null) stmt.setInt(paramIndex++, userId);

                ResultSet rs = stmt.executeQuery();

                Document document = new Document(new PdfDocument(writer), PageSize.A4);
                document.setMargins(50, 30, 50, 30);

                document.add(new Paragraph("Raport zadan").setBold().setFontSize(18));
                document.add(new Paragraph("\n"));

                Table table = new Table(UnitValue.createPercentArray(new float[]{0.5f, 2, 3, 2, 2, 2, 2}));
                table.setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("ID")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Tytul")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Opis")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Termin")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Status")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Priorytet")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Przydzielony")).setBold().setBackgroundColor(HEADER_BACKGROUND));

                while (rs.next()) {
                    table.addCell(String.valueOf(rs.getInt("id")));
                    table.addCell(rs.getString("title"));
                    table.addCell(rs.getString("description"));
                    table.addCell(rs.getString("deadline") != null ? rs.getString("deadline") : "Brak");
                    table.addCell(rs.getString("status"));
                    table.addCell(rs.getString("priority"));
                    String assignee = (rs.getString("first_name") != null && rs.getString("last_name") != null)
                            ? rs.getString("first_name") + " " + rs.getString("last_name")
                            : "Brak";
                    table.addCell(assignee);
                }

                document.add(new Paragraph("\n"));

                if (department != null && !department.equals("wszyscy")) {
                    try (PreparedStatement deptStmt = connection.prepareStatement("""
                        SELECT d.name, u.first_name, u.last_name
                        FROM departments d
                        LEFT JOIN users u ON d.manager_id = u.id
                        WHERE d.name = ?
                        """)) {

                        deptStmt.setString(1, department);
                        ResultSet deptRs = deptStmt.executeQuery();
                        if (deptRs.next()) {
                            String deptName = deptRs.getString("name");
                            String manager = (deptRs.getString("first_name") != null && deptRs.getString("last_name") != null)
                                    ? deptRs.getString("first_name") + " " + deptRs.getString("last_name")
                                    : "Brak danych";

                            document.add(new Paragraph("Wybrany dzial: " + deptName)
                                    .setFontSize(12).setBold());
                            document.add(new Paragraph("Kierownik dzialu: " + manager)
                                    .setFontSize(12));
                        }
                    }
                }

                document.add(table);
                document.add(new Paragraph("\n\n"));

                Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}));
                signatureTable.setWidth(UnitValue.createPercentValue(100));

                String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                Cell footer = new Cell()
                        .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                        .setBold().setBorder(Border.NO_BORDER);
                Cell empty = new Cell().setBorder(Border.NO_BORDER);
                Cell signature = new Cell()
                        .add(new Paragraph("________________________________"))
                        .add(new Paragraph("podpis osoby odbierającej"))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER);

                signatureTable.addCell(footer);
                signatureTable.addCell(empty);
                signatureTable.addCell(signature);

                document.add(signatureTable);
                document.close();

                System.out.println("Raport zostal wygenerowany: " + outputPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        public static void generateUsers(String department, String city,
                                         Integer minSalary, Integer maxSalary,
                                         Integer minTasks, Integer maxTasks){
            LocalDateTime nowTitle = LocalDateTime.now();
            DateTimeFormatter formatterTitle = DateTimeFormatter.ofPattern("dd_MM_yyyy_HHmmss");
            String formattedDateTitle = nowTitle.format(formatterTitle);

            String outputPath = "usersReport_" + formattedDateTitle + ".pdf";

            StringBuilder sql = new StringBuilder("""
                    SELECT 
                        u.first_name,
                        u.last_name,
                        u.city,
                        d.name AS department,
                        u.salary,
                        (SELECT COUNT(*) FROM tasks t WHERE t.user_id = u.id) AS task_count
                    FROM users u
                    JOIN departments d ON u.department_id = d.id
                    WHERE 1=1
                """);

            boolean hasDept = department != null && !department.equals("wszystkie");
            boolean hasCity = city != null && !city.isBlank();

            if (hasDept) sql.append(" AND d.name = ?");
            if (hasCity) sql.append(" AND u.city = ?");
            if (minSalary != null) sql.append(" AND u.salary >= ?");
            if (maxSalary != null) sql.append(" AND u.salary <= ?");
            if (minTasks != null) sql.append(" AND (SELECT COUNT(*) FROM tasks t WHERE t.user_id = u.id) >= ?");
            if (maxTasks != null) sql.append(" AND (SELECT COUNT(*) FROM tasks t WHERE t.user_id = u.id) <= ?");

            sql.append(" ORDER BY d.name ASC, u.last_name ASC");

            try (Connection connection = DatabaseConnection.getConnection();
                 PdfWriter writer = new PdfWriter(outputPath);
                 PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

                int paramIndex = 1;
                if (hasDept) stmt.setString(paramIndex++, department);
                if (hasCity) stmt.setString(paramIndex++, city);
                if (minSalary != null) stmt.setInt(paramIndex++, minSalary);
                if (maxSalary != null) stmt.setInt(paramIndex++, maxSalary);
                if (minTasks != null) stmt.setInt(paramIndex++, minTasks);
                if (maxTasks != null) stmt.setInt(paramIndex++, maxTasks);

                ResultSet rs = stmt.executeQuery();

                Document document = new Document(new PdfDocument(writer), PageSize.A4);
                document.setMargins(50, 30, 50, 30);

                document.add(new Paragraph("Raport uzytkownikow").setBold().setFontSize(18));
                document.add(new Paragraph("\n"));

                Table table = new Table(UnitValue.createPercentArray(new float[]{2, 2, 2, 2, 1.5f, 1.5f}))
                        .setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Imie")).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Nazwisko")).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Miasto")).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Dzial")).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Pensja")).setBold());
                table.addHeaderCell(new Cell().add(new Paragraph("Zadania")).setBold());

                while (rs.next()) {
                    table.addCell(rs.getString("first_name"));
                    table.addCell(rs.getString("last_name"));
                    table.addCell(rs.getString("city"));
                    table.addCell(rs.getString("department"));
                    table.addCell(String.valueOf(rs.getInt("salary")));
                    table.addCell(String.valueOf(rs.getInt("task_count")));
                }

                document.add(table);
                document.add(new Paragraph("\n\n"));

                Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}));
                signatureTable.setWidth(UnitValue.createPercentValue(100));

                String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                Cell footer = new Cell()
                        .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                        .setBold().setBorder(Border.NO_BORDER);
                Cell empty = new Cell().setBorder(Border.NO_BORDER);
                Cell signature = new Cell()
                        .add(new Paragraph("________________________________"))
                        .add(new Paragraph("podpis osoby odbierającej"))
                        .setBorder(Border.NO_BORDER)
                        .setTextAlignment(TextAlignment.CENTER);

                signatureTable.addCell(footer);
                signatureTable.addCell(empty);
                signatureTable.addCell(signature);

                document.add(signatureTable);
                document.close();

                System.out.println("Raport zostal wygenerowany: " + outputPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }




        public static void generateWarehouse(String department, String managerFullName,
                                             Integer minQuantity, Integer maxQuantity){
            LocalDateTime nowTitle = LocalDateTime.now();
            DateTimeFormatter formatterTitle = DateTimeFormatter.ofPattern("dd_MM_yyyy_HHmmss");
            String formattedDateTitle = nowTitle.format(formatterTitle);

            String outputPath = "warehouseReport_" + formattedDateTitle + ".pdf";

            StringBuilder sql = new StringBuilder("""
                        SELECT 
                            d.name AS department_name,
                            u.first_name,
                            u.last_name,
                            i.name AS item_name,
                            i.quantity
                        FROM items i
                        JOIN warehouses w ON i.warehouse_id = w.id
                        JOIN departments d ON w.department_id = d.id
                        JOIN users u ON d.manager_id = u.id
                        WHERE 1=1
                    """);

            boolean hasDept = department != null && !department.equals("wszystkie");
            boolean hasManager = managerFullName != null && !managerFullName.equals("wszyscy");

            if (hasDept && hasManager) {
                sql.append(" AND (d.name = ? OR CONCAT(u.first_name, ' ', u.last_name) = ?)");
            } else {
                if (hasDept) sql.append(" AND d.name = ?");
                if (hasManager) sql.append(" AND CONCAT(u.first_name, ' ', u.last_name) = ?");
            }

            if (minQuantity != null) sql.append(" AND i.quantity >= ?");
            if (maxQuantity != null) sql.append(" AND i.quantity <= ?");
            sql.append(" ORDER BY d.name ASC");


            try (Connection connection = DatabaseConnection.getConnection();
                 PdfWriter writer = new PdfWriter(outputPath);
                 PreparedStatement stmt = connection.prepareStatement(sql.toString())) {

                int paramIndex = 1;

                if (hasDept && hasManager) {
                    stmt.setString(paramIndex++, department);
                    stmt.setString(paramIndex++, managerFullName);
                } else {
                    if (hasDept) stmt.setString(paramIndex++, department);
                    if (hasManager) stmt.setString(paramIndex++, managerFullName);
                }

                if (minQuantity != null) stmt.setInt(paramIndex++, minQuantity);
                if (maxQuantity != null) stmt.setInt(paramIndex++, maxQuantity);


                ResultSet rs = stmt.executeQuery();

                Document document = new Document(new PdfDocument(writer), PageSize.A4);
                document.setMargins(50, 30, 50, 30);

                document.add(new Paragraph("Raport magazynowy").setBold().setFontSize(18));
                document.add(new Paragraph("\n"));

                Table table = new Table(UnitValue.createPercentArray(new float[]{3, 2, 3, 2}))
                        .setWidth(UnitValue.createPercentValue(100));

                table.addHeaderCell(new Cell().add(new Paragraph("Dzial")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Kierownik")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Produkt")).setBold().setBackgroundColor(HEADER_BACKGROUND));
                table.addHeaderCell(new Cell().add(new Paragraph("Ilosc")).setBold().setBackgroundColor(HEADER_BACKGROUND));

                while (rs.next()) {
                    String deptName = rs.getString("department_name");
                    String manager = rs.getString("first_name") + " " + rs.getString("last_name");
                    String item = rs.getString("item_name");
                    int qty = rs.getInt("quantity");

                    table.addCell(deptName);
                    table.addCell(manager);
                    table.addCell(item);
                    table.addCell(String.valueOf(qty));
                }

                document.add(table);
                document.add(new Paragraph("\n\n"));

                Table signatureTable = new Table(UnitValue.createPercentArray(new float[]{2, 1, 2}))
                        .setWidth(UnitValue.createPercentValue(100));

                String formattedDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
                Cell footer = new Cell()
                        .add(new Paragraph("Raport zostal wygenerowany automatycznie dnia: " + formattedDate))
                        .setBold().setBorder(Border.NO_BORDER);
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

                System.out.println("Raport magazynowy zostal wygenerowany: " + outputPath);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

}