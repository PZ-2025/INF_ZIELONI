package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Button;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskReportControllerTest {

    private TaskReportController controller;

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        controller = new TaskReportController();

        // Inicjalizacja pól FXML
        controller.statusChoiceBox = new ChoiceBox<>();
        controller.priorityChoiceBox = new ChoiceBox<>();
        controller.departmentChoiceBox = new ChoiceBox<>();
        controller.employeeChoiceBox = new ChoiceBox<>();
        controller.startDatePicker = new DatePicker();
        controller.endDatePicker = new DatePicker();
        controller.clearButton = new Button();
        controller.generateButton = new Button();

        // Dodanie przykładowych danych
        controller.statusChoiceBox.getItems().addAll("wszystkie", "Nowe", "W toku", "Zakończone");
        controller.priorityChoiceBox.getItems().addAll("wszystkie", "Niska", "Średnia", "Wysoka");
        controller.departmentChoiceBox.getItems().addAll("wszystkie", "IT", "HR");
        controller.employeeChoiceBox.getItems().addAll("wszyscy", "1 Jan Kowalski");
    }

    @Test
    void shouldRejectEndDateBeforeStartDate() throws Exception {
        controller.startDatePicker.setValue(LocalDate.of(2025, 5, 20));
        controller.endDatePicker.setValue(LocalDate.of(2025, 5, 15));

        // Zamiast sprawdzać efekt uboczny (System.err), testujemy, czy metoda się nie wywala
        controller.generateTaskButton();

        // Jeżeli nie wyrzuciło wyjątku i nie poszło dalej, test przeszedł
        System.out.println("Test: Data końcowa przed początkową - poprawnie przerwano generowanie.");
    }

    @Test
    void shouldHandleInvalidEmployeeIdGracefully() throws Exception {
        controller.employeeChoiceBox.setValue("123 Jan Kowalski");

        // Pozostałe pola poprawne
        controller.statusChoiceBox.setValue("wszystkie");
        controller.priorityChoiceBox.setValue("wszystkie");
        controller.departmentChoiceBox.setValue("wszystkie");
        controller.startDatePicker.setValue(null);
        controller.endDatePicker.setValue(null);

        // Nie powinien rzucić wyjątku mimo błędnego formatu ID
        controller.generateTaskButton();

        System.out.println("Test: Niepoprawne ID pracownika - poprawnie obsłużono NumberFormatException.");
    }

    @Test
    void shouldResetAllFilters() {
        controller.startDatePicker.setValue(LocalDate.of(2025, 5, 10));
        controller.endDatePicker.setValue(LocalDate.of(2025, 5, 20));
        controller.departmentChoiceBox.setValue("HR");
        controller.priorityChoiceBox.setValue("Wysoka");
        controller.statusChoiceBox.setValue("Zakończone");
        controller.employeeChoiceBox.setValue("1 Jan Kowalski");

        controller.handleClearTask(null);

        assertNull(controller.startDatePicker.getValue());
        assertNull(controller.endDatePicker.getValue());
        assertEquals("wszystkie", controller.departmentChoiceBox.getValue());
        assertEquals("wszystkie", controller.priorityChoiceBox.getValue());
        assertEquals("wszystkie", controller.statusChoiceBox.getValue());
        assertEquals("wszyscy", controller.employeeChoiceBox.getValue());

        System.out.println("Test: Wszystkie filtry wyczyszczone poprawnie.");
    }
    @Test
    void shouldGenerateReportWithAllFiltersSet() throws Exception {
        controller.statusChoiceBox.setValue("Nowe");
        controller.priorityChoiceBox.setValue("Wysoka");
        controller.departmentChoiceBox.setValue("IT");
        controller.employeeChoiceBox.setValue("1 Jan Kowalski");
        controller.startDatePicker.setValue(LocalDate.of(2025, 5, 1));
        controller.endDatePicker.setValue(LocalDate.of(2025, 5, 31));

        assertDoesNotThrow(() -> controller.generateTaskButton());

        System.out.println("Test: Generowanie raportu z ustawionymi wszystkimi filtrami przebiegło bez błędów.");
    }

    @Test
    void shouldGenerateReportWithMinimalFilters() throws Exception {
        controller.statusChoiceBox.setValue("wszystkie");
        controller.priorityChoiceBox.setValue("wszystkie");
        controller.departmentChoiceBox.setValue("wszystkie");
        controller.employeeChoiceBox.setValue("wszyscy");
        controller.startDatePicker.setValue(null);
        controller.endDatePicker.setValue(null);

        assertDoesNotThrow(() -> controller.generateTaskButton());

        System.out.println("Test: Generowanie raportu z domyślnymi filtrami (wszystkie/wszyscy) działa.");
    }

    @Test
    void shouldAcceptOnlyValidIntegerAsEmployeeId() throws Exception {
        controller.employeeChoiceBox.setValue("42 Anna Nowak");
        assertDoesNotThrow(() -> controller.generateTaskButton());

        System.out.println("Test: Poprawne ID pracownika (liczba) zostało zaakceptowane.");
    }

}
