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
    protected DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
    protected int fragmentLayoutId;
    protected String fragmentLayoutName;
    protected boolean usesCars;
    protected String title;

    public abstract String getName();

    public abstract String getTitle();

    public abstract boolean isRoot();
}
