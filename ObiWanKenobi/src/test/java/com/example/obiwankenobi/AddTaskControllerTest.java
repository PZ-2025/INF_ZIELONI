package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class AddTaskControllerTest {

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    private AddTaskController controller;

    @BeforeEach
    void setUp() {
        controller = new AddTaskController();

        controller.taskTitleField = new TextField();
        controller.taskDescriptionField = new TextArea();
        controller.employeeChoiceBox = new ChoiceBox<>();
        controller.taskDeadlineField = new DatePicker();
        controller.priorityChoiceBox = new ChoiceBox<>();
    }

    @Test
    void shouldInvalidateEmptyTitle() {
        controller.taskTitleField.setText("");
        controller.taskDescriptionField.setText("Opis");
        controller.employeeChoiceBox.getItems().add("1: Jan Kowalski");
        controller.employeeChoiceBox.setValue("1: Jan Kowalski");
        controller.taskDeadlineField.setValue(LocalDate.now().plusDays(1));

        assertFalse(controller.validateInputData(), "Brak tytułu zadania powinien zwrócić false");
        System.out.println("Brak tytułu - niepoprawne dane");
    }

    @Test
    void shouldInvalidateEmptyDescription() {
        controller.taskTitleField.setText("Tytuł");
        controller.taskDescriptionField.setText("");
        controller.employeeChoiceBox.getItems().add("1: Jan Kowalski");
        controller.employeeChoiceBox.setValue("1: Jan Kowalski");
        controller.taskDeadlineField.setValue(LocalDate.now().plusDays(1));

        assertFalse(controller.validateInputData(), "Brak opisu powinien zwrócić false");
        System.out.println("Brak opisu - niepoprawne dane");
    }

    @Test
    void shouldInvalidateNoEmployeeSelected() {
        controller.taskTitleField.setText("Tytuł");
        controller.taskDescriptionField.setText("Opis");
        controller.taskDeadlineField.setValue(LocalDate.now().plusDays(1));

        assertFalse(controller.validateInputData(), "Brak wybranego pracownika powinien zwrócić false");
        System.out.println("Brak pracownika - niepoprawne dane");
    }

    @Test
    void shouldInvalidatePastDeadline() {

            controller.taskTitleField.setText("Tytuł");
            controller.taskDescriptionField.setText("Opis");
            controller.employeeChoiceBox.getItems().add("1: Jan Kowalski");
            controller.employeeChoiceBox.setValue("1: Jan Kowalski");
            controller.taskDeadlineField.setValue(LocalDate.now().minusDays(1));

            assertFalse(controller.validateInputData(), "Termin zadania w przeszłości powinien zwrócić false");
            System.out.println("Termin w przeszłości - niepoprawne dane");

    }

    @Test
    void shouldValidateDeadlineToday() {
        controller.taskTitleField.setText("Tytuł");
        controller.taskDescriptionField.setText("Opis");
        controller.employeeChoiceBox.getItems().add("1: Jan Kowalski");
        controller.employeeChoiceBox.setValue("1: Jan Kowalski");
        controller.taskDeadlineField.setValue(LocalDate.now());

        assertTrue(controller.validateInputData(), "Dzisiejsza data jako deadline powinna być akceptowalna");
        System.out.println("Dzisiejsza data - poprawne dane");
    }
}
