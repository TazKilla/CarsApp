package com.musala.groche.carsapp.views.fragments;

import android.util.Log;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class FavoritesFragment extends CarListingFragment {

    public static String TAG = "FavoritesFragment";
    private final boolean root = true;

    public static FavoritesFragment newInstance(List<Car> carsList, List<Item> manufacturersList) {

        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setCarsList(carsList);
        fragment.setManufacturersList(manufacturersList);
        fragment.usesCars = true;

        Log.d(TAG, "New FavoritesFragment instance: \n");
        for (Car car : fragment.carsList) {
            Log.d(TAG, car.toString() + "\n");
        }

        return fragment;
    }

    @Override
    public boolean isRoot() {
        return root;
    }

    @Override
    public String getTitle() {
        return getString(R.string.title_favorites);
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_favs_frag;
    }

    @Override
    public int getCarsCount() {
        return databaseHelper.getFavCarsCount();
    }
}
