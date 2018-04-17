package com.musala.groche.carsapp.database.model;

import java.util.ArrayList;
import java.util.List;

public class Engine extends BaseItem {

    public final static String TABLE_NAME = "engine";

    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "(" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_LABEL + " TEXT, " +
                    COLUMN_DESCRIPTION + " TEXT, " +
                    COLUMN_IMGURL + " TEXT" +
                    ")";

    public Engine() {}

    public Engine(int id, String label, String description, String imgurl) {
        super(id, label, description, imgurl);
    }

    public static List<Engine> getList(List<BaseItem> itemList) {

        List<Engine> newList = new ArrayList<>();
        for (BaseItem item : itemList) {
            newList.add((Engine) item);
        }

        return newList;
    }
}
