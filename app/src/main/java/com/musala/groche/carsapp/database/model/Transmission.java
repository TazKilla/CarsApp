package com.musala.groche.carsapp.database.model;

import java.util.ArrayList;
import java.util.List;

public class Transmission extends BaseItem {

    private static String tableName = "transmission";

    public Transmission() {}

    public Transmission(int id, String label, String description, String imgurl) {
        super(tableName, id, label, description, imgurl);
    }

    public static List<Transmission> getList(List<BaseItem> itemList) {

        List<Transmission> newList = new ArrayList<>();
        for (BaseItem item : itemList) {
            newList.add((Transmission) item);
        }

        return newList;
    }
}
