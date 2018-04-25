package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class CarsFragment extends CarListingFragment {

    public static String TAG = "CarsFragment";

    public static CarsFragment newInstance(List<Car> carsList, List<Item> manufacturersList) {

        CarsFragment fragment = new CarsFragment();
        fragment.setCarsList(carsList);
        fragment.setManufacturersList(manufacturersList);
        fragment.usesCars = true;

        Log.d(TAG, "New CarsFragment instance: \n");
//        for (Car car : fragment.carsList) {
//            Log.d(TAG, car.toString() + "\n");
//        }

        return fragment;
    }

    @Override
    public boolean isRoot() {
        return true;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_cars);
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

