package com.example.notatnik;

import android.widget.Button;

public class Document {
    private int id;
    private String name;
    private String type;
    private String color;

    //TO DO
    //ADD COLOR SETTING
    public Document() {
    }
    public Document(Document document){
        this.id=document.getId();
        this.name=document.getName();
        this.type=document.getType();
        this.color=document.getColor();
    }
    public Document(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public Document(int id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
    }

    public Document(int id, String name, String type, String color) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.color = color;
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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

