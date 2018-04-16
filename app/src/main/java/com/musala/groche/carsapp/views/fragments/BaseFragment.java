package com.musala.groche.carsapp.views.fragments;

import android.support.v4.app.Fragment;

import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.BaseItem;
import com.musala.groche.carsapp.database.model.Car;
import java.util.List;

public abstract class BaseFragment extends Fragment {

    protected List<Car> carsList;
    protected List<BaseItem> itemsList;
    protected DatabaseHelper databaseHelper;
    protected int fragmentLayoutId;
    protected String fragmentLayoutName;
    protected boolean usesCars;
    protected String title;

    public abstract String getName();

    public abstract String getTitle();

    public abstract boolean isRoot();
}