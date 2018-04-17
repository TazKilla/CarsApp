package com.musala.groche.carsapp.database.model;

import java.io.Serializable;

public abstract class BaseItem implements Serializable {

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMGURL = "imgurl";

    protected int id;
    protected String label;
    protected String description;
    protected String imgurl;

    public BaseItem() {}

    public BaseItem(int id, String label, String description, String imgurl) {
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
