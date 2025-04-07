package com.example.obiwankenobi;

/**
 * Reprezentuje magazyn w systemie.
 * Zawiera informacje o przedmiotach przechowywanych w magazynie, ich ilości oraz nazwie działu.
 */
public class Warehouse {

    /** Identyfikator magazynu */
    private int id;

    /** Ilość przedmiotów w magazynie */
    private int itemAmount;

    /** Nazwa działu, do którego należy magazyn */
    private String depName;

    /** Nazwa przedmiotu przechowywanego w magazynie */
    private String itemName;

    /**
     * Konstruktor klasy Warehouse, który ustawia nazwę działu, nazwę przedmiotu oraz ilość.
     *
     * @param departmentName nazwa działu, do którego należy magazyn
     * @param itemName nazwa przedmiotu w magazynie
     * @param quantity ilość przedmiotów w magazynie
     */
    public Warehouse(String departmentName, String itemName, int quantity) {
        this.depName = departmentName;
        this.itemName = itemName;
        this.itemAmount = quantity;
    }

    /**
     * Zwraca identyfikator magazynu.
     *
     * @return identyfikator magazynu
     */
    public int getId() {
        return id;
    }

    /**
     * Ustawia identyfikator magazynu.
     *
     * @param id identyfikator magazynu
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Zwraca ilość przedmiotów w magazynie.
     *
     * @return ilość przedmiotów w magazynie
     */
    public int getItemAmount() {
        return itemAmount;
    }

    /**
     * Ustawia ilość przedmiotów w magazynie.
     *
     * @param itemAmount ilość przedmiotów w magazynie
     */
    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    /**
     * Zwraca nazwę działu, do którego należy magazyn.
     *
     * @return nazwa działu magazynu
     */
    public String getDepName() {
        return depName;
    }

    /**
     * Ustawia nazwę działu magazynu.
     *
     * @param depName nazwa działu magazynu
     */
    public void setDepName(String depName) {
        this.depName = depName;
    }

    /**
     * Zwraca nazwę przedmiotu przechowywanego w magazynie.
     *
     * @return nazwa przedmiotu w magazynie
     */
    public String getItemName() {
        return itemName;
    }

    /**
     * Ustawia nazwę przedmiotu przechowywanego w magazynie.
     *
     * @param itemName nazwa przedmiotu w magazynie
     */
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
