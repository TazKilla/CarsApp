package com.musala.groche.carsapp.views.fragments;

import android.support.v4.app.Fragment;

import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.database.model.Car;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    protected List<Car> carsList;
    protected List<Item> itemsList;
    protected List<Item> manufacturersList;
    protected static DatabaseHelper databaseHelper;
    protected boolean usesCars;
    protected String title;
    public String TAG;

    public abstract String getTitle();

    public abstract boolean isRoot();

    public abstract int getLayoutId();
}
