package com.musala.groche.carsapp.database.model;

import java.util.ArrayList;
import java.util.List;

public class Manufacturer extends BaseItem {

    private static String tableName = "manufacturer";

    public Manufacturer() {}

    public Manufacturer(int id, String label, String description, String imgurl) {
        super(tableName, id, label, description, imgurl);
    }

    public static List<Manufacturer> getList(List<BaseItem> itemList) {

        List<Manufacturer> newList = new ArrayList<>();
        for (BaseItem item : itemList) {
            newList.add((Manufacturer) item);
        }

        return newList;
    }
}
