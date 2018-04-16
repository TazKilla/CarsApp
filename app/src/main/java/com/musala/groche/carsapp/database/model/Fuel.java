package com.musala.groche.carsapp.database.model;

import java.util.ArrayList;
import java.util.List;

public class Fuel extends BaseItem {

    private static String tableName = "fuel";

    public Fuel() {}

    public Fuel(int id, String label, String description, String imgurl) {
        super(tableName, id, label, description, imgurl);
    }

    public static List<Fuel> getList(List<BaseItem> itemList) {

        List<Fuel> newList = new ArrayList<>();
        for (BaseItem item : itemList) {
            newList.add((Fuel) item);
        }

        return newList;
    }
}
