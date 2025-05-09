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

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getDeadline() {
        return deadline;
    }

    public void setDeadline(Timestamp deadline) {
        this.deadline = deadline;
    }
}
