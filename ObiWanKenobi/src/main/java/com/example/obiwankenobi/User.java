package com.example.obiwankenobi;

/**
 * Klasa reprezentująca użytkownika systemu.
 * Przechowuje informacje o użytkowniku, takie jak dane osobowe, email, hasło, rola oraz dział.
 */
public class User {

    /** Hasło użytkownika */
    private String password;

    /** Imię użytkownika */
    private String firstName;

    /** Nazwisko użytkownika */
    private String lastName;

    /** Identyfikator użytkownika */
    private int userId;

    /** Adres e-mail użytkownika */
    private String email;

    /** Rola użytkownika w systemie */
    private String role;

    /** Dział, do którego należy użytkownik */
    private String department;

    /**
     * Konstruktor inicjujący obiekt użytkownika.
     *
     * @param id identyfikator użytkownika
     * @param firstName imię użytkownika
     * @param lastName nazwisko użytkownika
     * @param email adres e-mail użytkownika
     * @param password hasło użytkownika
     * @param role rola użytkownika
     * @param department dział, do którego należy użytkownik
     */
    public User(int id, String firstName, String lastName, String email, String password, String role, String department) {
        this.userId = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.department = department;
    }

    /**
     * Konstruktor inicjujący obiekt użytkownika z podaniem danych bez identyfikatora.
     *
     * @param password hasło użytkownika
     * @param firstName imię użytkownika
     * @param lastName nazwisko użytkownika
     * @param email adres e-mail użytkownika
     * @param userId identyfikator użytkownika
     * @param role rola użytkownika
     * @param department dział, do którego należy użytkownik
     */
    public User(String password, String firstName, String lastName, String email, int userId, String role, String department){
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.userId = userId;
        this.role = role;
        this.department = department;
    }

    /**
     * Zwraca hasło użytkownika.
     *
     * @return hasło użytkownika
     */
    public String getPassword() {
        return password;
    }

    /**
     * Ustawia nowe hasło użytkownika.
     *
     * @param password nowe hasło użytkownika
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Zwraca imię użytkownika.
     *
     * @return imię użytkownika
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Ustawia imię użytkownika.
     *
     * @param firstName nowe imię użytkownika
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Zwraca nazwisko użytkownika.
     *
     * @return nazwisko użytkownika
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Ustawia nazwisko użytkownika.
     *
     * @param lastName nowe nazwisko użytkownika
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Zwraca identyfikator użytkownika.
     *
     * @return identyfikator użytkownika
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Ustawia identyfikator użytkownika.
     *
     * @param userId nowy identyfikator użytkownika
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * Zwraca adres e-mail użytkownika.
     *
     * @return adres e-mail użytkownika
     */
    public String getEmail() {
        return email;
    }

    /**
     * Ustawia adres e-mail użytkownika.
     *
     * @param email nowy adres e-mail użytkownika
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Zwraca rolę użytkownika w systemie.
     *
     * @return rola użytkownika
     */
    public String getRole() {
        return role;
    }

    /**
     * Ustawia rolę użytkownika w systemie.
     *
     * @param role nowa rola użytkownika
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * Zwraca dział, do którego należy użytkownik.
     *
     * @return dział użytkownika
     */
    public String getDepartment() {
        return department;
    }

    /**
     * Ustawia dział, do którego należy użytkownik.
     *
     * @param department nowy dział użytkownika
     */
    public void setDepartment(String department) {
        this.department = department;
    }
}
