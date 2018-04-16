package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.database.model.BaseItem;
import com.musala.groche.carsapp.database.model.Car;

import com.musala.groche.carsapp.database.model.Engine;
import com.musala.groche.carsapp.database.model.Fuel;
import com.musala.groche.carsapp.database.model.Manufacturer;
import com.musala.groche.carsapp.database.model.Transmission;

import org.modelmapper.ModelMapper;

import java.util.List;

public class SettingsFragment extends BaseFragment {

    public static final String NAME = "content_settings_frag";
    public static final String TAG = "SettingsFragment";

    private String title = "Settings";

    private boolean root = false;

    public static SettingsFragment newInstance(List<BaseItem> settingsList, int layoutId, int settingType) {

        ModelMapper modelMapper = new ModelMapper();
        SettingsFragment fragment = new SettingsFragment();
        fragment.fragmentLayoutId = layoutId;

        switch (settingType) {
            case 0:
//                List<Manufacturer> manufacturerList = Manufacturer.getList(settingsList);
                fragment.setItemsList(settingsList, settingType);
                break;
            case 1:
//                List<Engine> engineList = Engine.getList(settingsList);
                fragment.setItemsList(settingsList, settingType);
                break;
            case 2:
//                List<Fuel> fuelList = Fuel.getList(settingsList);
                fragment.setItemsList(settingsList, settingType);
                break;
            case 3:
//                List<Transmission> transmissionList = Transmission.getList(settingsList);
                fragment.setItemsList(settingsList, settingType);
                break;
        }

        fragment.fragmentLayoutName = NAME;
        fragment.usesCars = false;

        Log.d(TAG, "New SettingsFragment instance: \n");
        for (Car car : fragment.carsList) {
            Log.d(TAG, Car.toString(car) + "\n");
        }

        return fragment;
    }

    private static List<BaseItem> getList(List<BaseItem> settingsList, int listType) {
        switch (listType) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }

        return null;
    }

    public void setItemsList(List<BaseItem> itemsList, int itemType) {
        switch (itemType) {
            case 0:
//                this.itemsList = Manufacturer.getList(itemsList);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
        }
        this.itemsList = itemsList;
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
