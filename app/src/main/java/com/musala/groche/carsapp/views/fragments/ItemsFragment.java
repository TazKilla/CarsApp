package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class ItemsFragment extends ItemListingFragment {

    public static final String NAME = "content_items_frag";
    public static final String TAG = "ItemsFragment";

    private String title = "Items";

    private boolean root = true;

    public static ItemsFragment newInstance(List<Item> settingsList, int layoutId, String tableName) {

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
