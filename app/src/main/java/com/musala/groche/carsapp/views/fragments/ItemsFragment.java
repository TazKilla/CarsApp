package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.database.model.BaseItem;

import org.modelmapper.ModelMapper;

import java.util.List;

public class ItemsFragment extends ItemListingFragment {

    public static final String NAME = "content_items_frag";
    public static final String TAG = "ItemsFragment";

    private String title = "Items";

    private boolean root = false;

    public static ItemsFragment newInstance(List<BaseItem> settingsList, int layoutId) {

//        ModelMapper modelMapper = new ModelMapper();
        ItemsFragment fragment = new ItemsFragment();
        fragment.fragmentLayoutId = layoutId;

        fragment.setItemsList(settingsList);

        fragment.fragmentLayoutName = NAME;
        fragment.usesCars = false;

        Log.d(TAG, "New ItemsFragment instance: \n");
        for (BaseItem item : fragment.itemsList) {
            Log.d(TAG, item.toString() + "\n");
        }

        return fragment;
    }

//    private static List<BaseItem> getList(List<BaseItem> settingsList, int listType) {
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

//    public void setItemsList(List<BaseItem> itemsList) {
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
