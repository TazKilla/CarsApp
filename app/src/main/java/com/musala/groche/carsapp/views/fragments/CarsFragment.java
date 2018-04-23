package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class CarsFragment extends CarListingFragment {

    public static final String NAME = "content_cars_frag";
    private static final String TAG = "CarsFragment";
    private final String title = "Cars";
    private final boolean root = true;

    public static CarsFragment newInstance(List<Car> carsList, int layoutId, List<Item> manufacturersList) {

        CarsFragment fragment = new CarsFragment();
        fragment.fragmentLayoutId = layoutId;
        fragment.setCarsList(carsList);
        fragment.setManufacturersList(manufacturersList);
        fragment.fragmentLayoutName = NAME;
        fragment.usesCars = true;

        Log.d(TAG, "New CarsFragment instance: \n");
        for (Car car : fragment.carsList) {
            Log.d(TAG, car.toString() + "\n");
        }

        return fragment;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRoot() {
        return root;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_cars_frag;
    }

    @Override
    public int getCarsCount() {
        return databaseHelper.getCarsCount();
    }
}

