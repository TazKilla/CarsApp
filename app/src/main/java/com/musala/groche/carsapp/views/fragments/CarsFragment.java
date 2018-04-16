package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.database.model.Car;

import java.util.List;

public class CarsFragment extends CarListingFragment {

    public static final String NAME = "content_cars_frag";
    private static final String TAG = "CarsFragment";

    private String title = "Cars";

    private boolean root = true;

    public static CarsFragment newInstance(List<Car> carsList, int layoutId) {

        CarsFragment fragment = new CarsFragment();
        fragment.fragmentLayoutId = layoutId;
        fragment.setCarsList(carsList);
        fragment.fragmentLayoutName = NAME;
        fragment.usesCars = true;

        Log.d(TAG, "New CarsFragment instance: \n");
        for (Car car : fragment.carsList) {
            Log.d(TAG, Car.toString(car) + "\n");
        }

        return fragment;
    }

    public String getName() {
        return NAME;
    }

    public boolean isRoot() {
        return root;
    }

    @Override
    public String getTitle() {
        return title;
    }
}

