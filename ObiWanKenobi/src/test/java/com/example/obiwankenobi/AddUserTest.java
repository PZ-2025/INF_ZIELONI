package com.example.obiwankenobi;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AddUserTest {
    @BeforeAll
    static void initJavaFX() {
        Platform.startup(() -> {});
    }

    private AddUser addUser;

    @BeforeEach
    void setUp() {
        addUser = new AddUser();
    }

    @Test
    void shouldValidateCorrectEmail() {
        addUser.addEmail = new TextField("test@example.com");
        assertTrue(addUser.isEmailValid(), "email powinien byc poprawny");
        System.out.println("poprawny mail");
    }

    @Test
    void shouldInvalidateIncorrectEmail() {
        addUser.addEmail = new TextField("kiepski mail");
        assertFalse(addUser.isEmailValid(), "email powinien byc niepoprawny");
        System.out.println("niepoprawny mail");
    }

    @Test
    void shouldValidateStrongPassword() {
        addUser.addPass = new TextField("zaq1@WSX");
        assertTrue(addUser.validatePassword(), "haslo powinno byc silne");
        System.out.println("poprawne haslo");
    }

    @Test
    void shouldInvalidateWeakPassword() {
        addUser.addPass = new TextField("lol");
        assertFalse(addUser.validatePassword(), "haslo powinno byc slabe");
        System.out.println("niepoprawne haslo");
    }

    @Test
    void shouldValidateNonEmptyNameAndSurname() {
        addUser.addName = new TextField("Jan");
        addUser.addScndName = new TextField("Kowalski");
        assertTrue(addUser.validateNameAndSurname(), "pola nie powinny byc puste");
        System.out.println("oba pola maja dane");
    }

    @Test
    void shouldInvalidateEmptyNameOrSurname() {
        addUser.addName = new TextField("");
        addUser.addScndName = new TextField("Kowalski");
        assertFalse(addUser.validateNameAndSurname(), "co najmniej jedno pole powinno byc puiste");
        System.out.println("co najmniej jedno pole nie ma danych");
    }

}
