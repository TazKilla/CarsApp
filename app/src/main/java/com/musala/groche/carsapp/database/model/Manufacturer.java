package com.musala.groche.carsapp.database.model;

import java.util.ArrayList;
import java.util.List;

public class Manufacturer extends BaseItem {

    public final static String TABLE_NAME = "manufacturer";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LABEL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMGURL + " TEXT" +
                    ")";

    public Manufacturer() {}

    public Manufacturer(int id, String label, String description, String imgurl) {
        super(id, label, description, imgurl);
    }

    public static List<Manufacturer> getList(List<BaseItem> itemList) {

        List<Manufacturer> newList = new ArrayList<>();
        for (BaseItem item : itemList) {
            newList.add((Manufacturer) item);
        }

        return newList;
    }
}
