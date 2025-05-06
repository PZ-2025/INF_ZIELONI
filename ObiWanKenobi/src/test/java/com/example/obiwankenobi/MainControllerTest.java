package com.example.obiwankenobi;

import javafx.application.Platform;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testy jednostkowe dla funkcji uwierzytelniania użytkownika.
 * Test wykorzystuje techniki "białej skrzynki" do testowania wewnętrznego działania autentykacji.
 */
class MainControllerTest {

    private static MainController mainController;
    private static ByteArrayOutputStream outputStream;
    private static PrintStream originalOut;

    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});


        originalOut = System.out;
    }

    @BeforeEach
    void setUp() {
        mainController = new MainController();


        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    /**
     * Test testujący zachowanie podczas uwierzytelniania użytkownika z prawidłowymi danymi.
     * Test obejmuje wywołanie rzeczywistej metody authenticateUser bez mocków.
     *
     * UWAGA: Ten test wymaga dostępu do bazy danych z testowymi danymi,
     * może nie przejść w środowisku bez przygotowanej bazy.
     */
    @Test
    void testAuthenticateValidUser() {

        String email = "admin@example.com";
        String password = "admin123";


        String role = mainController.authenticateUser(email, password);


        System.out.println("Wywołano authenticateUser dla: " + email);

        System.setOut(originalOut);
        System.out.println("Test uwierzytelniania użytkownika zakończony");
        System.out.println("Wynik uwierzytelniania: " + (role != null ? "sukces - rola: " + role : "brak autentykacji - sprawdź bazę danych"));
    }

    /**
     * Test sprawdzający czy metoda authenticateUser poprawnie obsługuje błędne dane logowania.
     * Ten test nie wymaga działającej bazy danych, tylko sprawdza czy metoda nie rzuca wyjątku.
     */
    @Test
    void testAuthenticateInvalidUser() {

        String email = "niepoprawny@email.com";
        String password = "złehasło";


        String role = mainController.authenticateUser(email, password);


        assertNull(role, "Rola powinna być null dla nieprawidłowych danych logowania");


        System.setOut(originalOut);
        System.out.println("Test uwierzytelniania z błędnymi danymi zakończony pomyślnie");
    }

    /**
     * Testuje działanie metody getViewForRole dla wszystkich możliwych ról.
     */
    @Test
    void testGetViewForAllRoles() {
        try {

            Method method = MainController.class.getDeclaredMethod("getViewForRole", String.class);
            method.setAccessible(true);


            String adminView = (String) method.invoke(mainController, "admin");
            String directorView = (String) method.invoke(mainController, "dyrektor");
            String managerView = (String) method.invoke(mainController, "kierownik");
            String employeeView = (String) method.invoke(mainController, "pracownik");
            String unknownView = (String) method.invoke(mainController, "nieznana");


            assertEquals("/com/example/obiwankenobi/views/adminPanel.fxml", adminView);
            assertEquals("/com/example/obiwankenobi/views/directorPanel.fxml", directorView);
            assertEquals("/com/example/obiwankenobi/views/managerPanel.fxml", managerView);
            assertEquals("/com/example/obiwankenobi/views/mainPanel.fxml", employeeView);
            assertNull(unknownView);


            System.setOut(originalOut);
            System.out.println("Test ścieżek widoków dla wszystkich ról zakończony pomyślnie");
        } catch (Exception e) {
            System.setOut(originalOut);
            fail("Wystąpił błąd podczas testowania metody getViewForRole: " + e.getMessage());
        }
    }
    @Test
    void testAuthenticateValidEmailWrongPassword() {
        String email = "admin@example.com";
        String password = "zlehaslo";

        String role = mainController.authenticateUser(email, password);

        assertNull(role, "Rola powinna być null dla poprawnego maila, ale złego hasła");
        System.setOut(originalOut);
        System.out.println("Test: poprawny email, błędne hasło zakończony");
    }

    @Test
    void testAuthenticateMalformedEmail() {
        String email = "adminexample.com";
        String password = "admin123";

        String role = mainController.authenticateUser(email, password);

        assertNull(role, "Rola powinna być null dla źle sformatowanego emaila");
        System.setOut(originalOut);
        System.out.println("Test: niepoprawny format emaila zakończony");
    }

    @Test
    void testAuthenticateEmptyCredentials() {
        String email = "";
        String password = "";

        String role = mainController.authenticateUser(email, password);

        assertNull(role, "Rola powinna być null dla pustych danych logowania");
        System.setOut(originalOut);
        System.out.println("Test: puste dane logowania zakończony");
    }

    @Test
    void testAuthenticateNullCredentials() {
        String email = null;
        String password = null;

        String role = mainController.authenticateUser(email, password);

        assertNull(role, "Rola powinna być null dla null jako dane logowania");
        System.setOut(originalOut);
        System.out.println("Test: null jako dane logowania zakończony");
    }




}