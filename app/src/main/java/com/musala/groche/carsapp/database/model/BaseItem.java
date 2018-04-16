package com.musala.groche.carsapp.database.model;

import java.io.Serializable;

public class BaseItem implements Serializable {

    public static String TABLE_NAME;

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_LABEL = "label";
    public static final String COLUMN_DESCRIPTION = "description";
    public static final String COLUMN_IMGURL = "imgurl";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LABEL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMGURL + " TEXT, " +
                    ")";

    protected int id;
    protected String label;
    protected String description;
    protected String imgurl;

    public BaseItem() {}

    public BaseItem(String tableName, int id, String label, String description, String imgurl) {
        TABLE_NAME = tableName;
        this.id = id;
        this.label = label;
        this.description = description;
        this.imgurl = imgurl;
    }
}
