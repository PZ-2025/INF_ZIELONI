package com.example.obiwankenobi;

import java.sql.Timestamp;

/**
 * Model reprezentujący zadanie w systemie.
 * Zawiera informacje takie jak tytuł, opis, status, termin realizacji oraz datę utworzenia.
 */
public class Task {

    /** Identyfikator zadania */
    private int id;

    /** Tytuł zadania */
    private String title;

    /** Opis zadania */
    private String description;

    /** Status zadania (np. "Nowe", "W trakcie", "Zakończone") */
    private String status;

    /** Termin realizacji zadania */
    private Timestamp deadline;

    private String priority;

    /** Data utworzenia zadania */
    private Timestamp createdAt;

    /** Konstruktor domyślny */
    public Task() {
    }

    /**
     * Konstruktor zadania z wszystkimi polami.
     *
     * @param id identyfikator zadania
     * @param title tytuł zadania
     * @param description opis zadania
     * @param status status zadania
     * @param deadline termin realizacji
     * @param createdAt data utworzenia
     */
    public Task(int id, String title, String description, String status, String priority, Timestamp deadline, Timestamp createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    /**
     * Pobiera priorytet zadania.
     * @return priorytet zadania
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Ustawia priorytet zadania.
     * @param priority priorytet zadania
     */
    public void setPriority(String priority) {
        this.priority = priority;
    }

    /**
     * Pobiera identyfikator zadania.
     * @return identyfikator zadania
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator zadania.
     * @param id identyfikator zadania
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Pobiera tytuł zadania.
     * @return tytuł zadania
     */
    public String getTitle() {
        return title;
    }

    /**
     * Ustawia tytuł zadania.
     * @param title tytuł zadania
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Pobiera status zadania.
     * @return status zadania
     */
    public String getStatus() {
        return status;
    }

    /**
     * Ustawia status zadania.
     * @param status status zadania
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Pobiera opis zadania.
     * @return opis zadania
     */
    public String getDescription() {
        return description;
    }

    /**
     * Ustawia opis zadania.
     * @param description opis zadania
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Pobiera datę utworzenia zadania.
     * @return data utworzenia zadania
     */
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    /**
     * Ustawia datę utworzenia zadania.
     * @param createdAt data utworzenia zadania
     */
    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * Pobiera termin realizacji zadania.
     * @return termin realizacji zadania
     */
    public Timestamp getDeadline() {
        return deadline;
    }

    /**
     * Ustawia termin realizacji zadania.
     * @param deadline termin realizacji zadania
     */
    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
}
