package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WarehouseReportControllerTest {

    private WarehouseReportController controller;
    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }
    @BeforeEach
    void setUp() {
        // Wymuszenie inicjalizacji JavaFX


        controller = new WarehouseReportController();

        // Inicjalizacja ręczna kontrolerów (bo nie ładujemy FXML)
        controller.categoryChoiceBox = new ChoiceBox<>();
        controller.menagerChoiceBox = new ChoiceBox<>();
        controller.minQuantityField = new TextField();
        controller.maxQuantityField = new TextField();

        controller.categoryChoiceBox.getItems().addAll("wszystkie", "Magazyn A");
        controller.menagerChoiceBox.getItems().addAll("wszyscy", "Jan Kowalski");
    }

    @Test
    void shouldHandleInvalidMinQuantityGracefully() {
        controller.minQuantityField.setText("abc");  // Nieprawidłowy tekst
        controller.maxQuantityField.setText("100");
        controller.categoryChoiceBox.setValue("Magazyn A");
        controller.menagerChoiceBox.setValue("Jan Kowalski");

        assertDoesNotThrow(() -> controller.generateWarehouseButton());
    }

    @Test
    void shouldHandleNegativeMinQuantityGracefully() {
        controller.minQuantityField.setText("-5");
        controller.maxQuantityField.setText("100");
        controller.categoryChoiceBox.setValue("Magazyn A");
        controller.menagerChoiceBox.setValue("Jan Kowalski");

        assertDoesNotThrow(() -> controller.generateWarehouseButton());
    }

    @Test
    void shouldHandleInvalidMaxQuantityGracefully() {
        controller.minQuantityField.setText("10");
        controller.maxQuantityField.setText("abc"); // nieprawidłowy tekst
        controller.categoryChoiceBox.setValue("Magazyn A");
        controller.menagerChoiceBox.setValue("Jan Kowalski");

        assertDoesNotThrow(() -> controller.generateWarehouseButton());
    }

    @Test
    void shouldHandleNegativeMaxQuantityGracefully() {
        controller.minQuantityField.setText("10");
        controller.maxQuantityField.setText("-50");
        controller.categoryChoiceBox.setValue("Magazyn A");
        controller.menagerChoiceBox.setValue("Jan Kowalski");

        assertDoesNotThrow(() -> controller.generateWarehouseButton());
    }

    @Test
    void shouldHandleMinGreaterThanMaxGracefully() {
        controller.minQuantityField.setText("100");
        controller.maxQuantityField.setText("50");
        controller.categoryChoiceBox.setValue("Magazyn A");
        controller.menagerChoiceBox.setValue("Jan Kowalski");

        assertDoesNotThrow(() -> controller.generateWarehouseButton());
    }

    @Test
    void shouldGenerateReportWithValidInputs() {
        controller.minQuantityField.setText("10");
        controller.maxQuantityField.setText("50");
        controller.categoryChoiceBox.setValue("Magazyn A");
        controller.menagerChoiceBox.setValue("Jan Kowalski");

        assertDoesNotThrow(() -> controller.generateWarehouseButton());
    }
}
