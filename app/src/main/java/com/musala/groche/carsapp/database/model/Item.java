package com.musala.groche.carsapp.database.model;

import java.io.Serializable;

public class Item implements Serializable {

    public static final String MANUFACTURER_TABLE_NAME = "manufacturer";
    public static final String ENGINE_TABLE_NAME = "engine";
    public static final String FUEL_TABLE_NAME = "fuel";
    public static final String TRANSMISSION_TABLE_NAME = "transmission";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMGURL = "imgurl";

    protected int id;
    protected String label;
    protected String description;
    protected String imgurl;

    public Item() {}

    public Item(int id, String label, String description, String imgurl) {
        this.id = id;
        this.label = label;
        this.description = description;
        this.imgurl = imgurl;
    }

    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public String getDescription() {
        return description;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String toString() {
        return "Item description: \n" +
                "ID: " + this.getId() + "\n" +
                "Label: " + this.getLabel() + "\n" +
                "Description: " + this.getDescription() + "\n" +
                "Image URL: " + this.getImgurl() + "\n";
    }
}
