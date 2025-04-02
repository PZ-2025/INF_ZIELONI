package com.example.obiwankenobi;

public class Warehouse {

    private int id;

    private int itemAmount;

    private String depName;

    private String itemName;

    public Warehouse(String departmentName, String itemName, int quantity) {
        this.depName = departmentName;
        this.itemName = itemName;
        this.itemAmount = quantity;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(int itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getDepName() {
        return depName;
    }

    public void setDepName(String depName) {
        this.depName = depName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
