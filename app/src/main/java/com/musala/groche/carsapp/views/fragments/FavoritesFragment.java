package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.database.model.Car;

import java.util.List;

public class FavoritesFragment extends CarListingFragment {

    public static final String NAME = "content_favs_frag";
    private static final String TAG = "FavoritesFragment";

    private String title = "Favorites";

    private boolean root = true;

    public static FavoritesFragment newInstance(List<Car> carsList, int layoutId) {

        FavoritesFragment fragment = new FavoritesFragment();
        fragment.fragmentLayoutId = layoutId;
        fragment.setCarsList(carsList);
        fragment.fragmentLayoutName = NAME;
        fragment.usesCars = true;

        Log.d(TAG, "New FavoritesFragment instance: \n");
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