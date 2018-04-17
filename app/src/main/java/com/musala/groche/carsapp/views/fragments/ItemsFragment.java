package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class ItemsFragment extends ItemListingFragment {

    public static final String NAME = "content_items_frag";
    public static final String TAG = "ItemsFragment";

    private String title = "Items";

    private boolean root = false;

    public static ItemsFragment newInstance(List<Item> settingsList, int layoutId, String tableName) {

//        ModelMapper modelMapper = new ModelMapper();
        ItemsFragment fragment = new ItemsFragment();
        fragment.fragmentLayoutId = layoutId;
        fragment.setItemTable(tableName);

        fragment.setItemsList(settingsList);

        fragment.fragmentLayoutName = NAME;
        fragment.usesCars = false;

        Log.d(TAG, "New ItemsFragment instance: \n");
        for (Item item : fragment.itemsList) {
            Log.d(TAG, item.toString() + "\n");
        }

        return fragment;
    }

//    private static List<Item> getList(List<Item> settingsList, int listType) {
//        switch (listType) {
//            case 0:
//                break;
//            case 1:
//                break;
//            case 2:
//                break;
//            case 3:
//                break;
//        }
//
//        return null;
//    }

//    public void setItemsList(List<Item> itemsList) {
//        this.itemsList = itemsList;
//    }

    public boolean isRoot() {
        return root;
    }

    public String getName() {
        return NAME;
    }

    @Override
    public String getTitle() {
        return title;
    }
}
