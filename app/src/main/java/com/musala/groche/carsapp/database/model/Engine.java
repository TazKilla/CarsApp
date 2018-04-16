package com.musala.groche.carsapp.database.model;

import java.util.ArrayList;
import java.util.List;

public class Engine extends BaseItem {

    private static String tableName = "engine";

    public Engine() {}

    public Engine(int id, String label, String description, String imgurl) {
        super(tableName, id, label, description, imgurl);
    }

    public static List<Engine> getList(List<BaseItem> itemList) {

        List<Engine> newList = new ArrayList<>();
        for (BaseItem item : itemList) {
            newList.add((Engine) item);
        }

        return newList;
    }
}
