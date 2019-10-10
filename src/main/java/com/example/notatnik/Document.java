package com.example.notatnik;

public class Document {
    private int id;
    private String name;
    private String type;
    private int itemsCount;

    //TO DO
    //ADD COLOR SETTING


    public Document(int id, String name, String type, int itemsCount) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.itemsCount = itemsCount;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getItemsCount() {
        return itemsCount;
    }

    public void setItemsCount(int itemsCount) {
        this.itemsCount = itemsCount;
    }
}

