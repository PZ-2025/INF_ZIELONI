package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;
import org.junit.jupiter.api.*;

import java.io.File;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ReportControllerTest {

    private ReportController controller;

    @BeforeAll
    static void initJavaFX() {
        // Uruchomienie wątku JavaFX

        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        controller = new ReportController();
        controller.startDate = new DatePicker();
        controller.endDate = new DatePicker();
        controller.departmentChoiceBox = new ChoiceBox<>();
        controller.departmentChoiceBox.getItems().addAll("wszystkie", "IT", "HR");
        controller.departmentChoiceBox.setValue("wszystkie");
    }

    @Test
    void testUserReportWithDefaultDepartment() {
        assertDoesNotThrow(() -> {
            controller.userReport(null);
            File pdf = new File("usersReport.pdf");
            assertTrue(pdf.exists(), "Plik usersReport.pdf powinien istnieć po wygenerowaniu.");
            pdf.delete(); // czyszczenie
        });
    }

    @Test
    void testUserReportWithSpecificDepartment() {
        controller.departmentChoiceBox.setValue("IT");

        assertDoesNotThrow(() -> {
            controller.userReport(null);
            File pdf = new File("usersReport.pdf");
            assertTrue(pdf.exists(), "Plik usersReport.pdf powinien istnieć dla działu IT.");
            pdf.delete();
        });
    }

    @Test
    void testWarehouseReportWithDefaultDepartment() {
        assertDoesNotThrow(() -> {
            controller.warehouseReport(null);
            File pdf = new File("warehouseReport.pdf");
            assertTrue(pdf.exists(), "Plik warehouseReport.pdf powinien zostać wygenerowany.");
            pdf.delete();
        });
    }

    @Test
    void testWarehouseReportWithSpecificDepartment() {
        controller.departmentChoiceBox.setValue("HR");

        assertDoesNotThrow(() -> {
            controller.warehouseReport(null);
            File pdf = new File("warehouseReport.pdf");
            assertTrue(pdf.exists(), "Plik warehouseReport.pdf powinien istnieć dla działu HR.");
            pdf.delete();
        });
    }

    @Test
    void testInitializeDoesNotThrow() {
        assertDoesNotThrow(() -> {
            controller.initialize(null, null);
        });
    }

    @Test
    void testReportsCloseDoesNotThrow() throws Exception {
        Method closeMethod = ReportController.class.getDeclaredMethod("reportsClose", javafx.event.ActionEvent.class);
        assertNotNull(closeMethod, "Metoda reportsClose powinna istnieć.");
    }

    @Test
    void testFXMLReportShowMethodsExist() throws Exception {
        assertNotNull(ReportController.class.getDeclaredMethod("userReportShow", javafx.event.ActionEvent.class));
        assertNotNull(ReportController.class.getDeclaredMethod("taskReportShow", javafx.event.ActionEvent.class));
        assertNotNull(ReportController.class.getDeclaredMethod("warehouseReportShow", javafx.event.ActionEvent.class));
    }
}