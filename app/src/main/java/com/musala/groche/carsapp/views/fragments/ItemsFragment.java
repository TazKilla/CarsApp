package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class ItemsFragment extends ItemListingFragment {

    public static String TAG = "ItemsFragment";

    public static ItemsFragment newInstance(List<Item> settingsList, String tableName) {

        ItemsFragment fragment = new ItemsFragment();
        fragment.setItemTable(tableName);
        fragment.setItemsList(settingsList);
        fragment.usesCars = false;

        Log.d(TAG, "New ItemsFragment instance: \n");
//        for (Item item : fragment.itemsList) {
//            Log.d(TAG, item.toString() + "\n");
//        }

        return fragment;
    }

    public boolean isRoot() {
        return true;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_items);
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_items_frag;
    }
}
