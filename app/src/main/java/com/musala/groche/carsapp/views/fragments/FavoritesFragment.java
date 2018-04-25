package com.musala.groche.carsapp.views.fragments;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class FavoritesFragment extends CarListingFragment {

    public static String TAG = "FavoritesFragment";

    public static FavoritesFragment newInstance(List<Car> carsList, List<Item> manufacturersList) {

        FavoritesFragment fragment = new FavoritesFragment();
        fragment.setCarsList(carsList);
        fragment.setManufacturersList(manufacturersList);
        fragment.usesCars = true;

        return fragment;
    }

    @Override
    public boolean isRoot() {
        return true;
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
