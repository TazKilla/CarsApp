package com.musala.groche.carsapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.database.model.Car;

public class DetailFragment extends BaseFragment {

    public static String TAG = "DetailFragment";
    public static String title;
    private final boolean root = false;
    private Context context;

    private Car car;
    private Item item;
    private TextView favBtn;
    private FloatingActionButton fab;
    private TextView favTextView;
    private View rootView;

    public static DetailFragment newInstance(Context context, Car car) {

        DetailFragment fragment = new DetailFragment();
        fragment.car = car;
        fragment.context = context;

        Log.d(TAG, "New DetailFragment instance: \n" + car.toString());

        return fragment;
    }

    public static DetailFragment newInstance(Context context, Item item) {

        DetailFragment fragment = new DetailFragment();
        fragment.item = item;
        fragment.context = context;

        Log.d(TAG, "New DetailFragment instance: \n" + item.toString());

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.content_dtls_frag, container, false);

        init();
        setListeners();
        initDB();

        return rootView;
    }

    public void updateCar(Car c) {

        Log.d(TAG, "Car to be updated: " + c.toString());
        int result = databaseHelper.updateCar(c);
        Log.d(TAG, result == 1 ? "Car updated on databaseHelper" : "Unable to update car on databaseHelper: " + result);
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public boolean isRoot() {
        return root;
    }

    @Override
    public String getTitle() {
        if (car != null) {
            return context.getString(R.string.title_car_details);
        } else if (item != null) {
            return context.getString(R.string.title_item_details);
        } else {
            return "";
        }
    }

    private void init() {

        TextView textView;
        favTextView = rootView.findViewById(R.id.detail_favorite);
        favBtn = rootView.findViewById(R.id.favbtn);
        fab = rootView.findViewById(R.id.details_fab);

        if (car != null) {
            if (car.getFavorite() == 1) {
                favBtn.setText("-");
            } else {
                favBtn.setText("+");
            }

            textView = rootView.findViewById(R.id.detail_manufacturer);
            textView.setText(databaseHelper.getItemById(
                    Item.MANUFACTURER_TABLE_NAME,
                    this.car.getManufacturer()).getLabel()
            );
            textView = rootView.findViewById(R.id.detail_model);
            textView.setText(this.car.getModel());
            textView = rootView.findViewById(R.id.detail_year);
            textView.setText(String.valueOf(this.car.getYear()));
            textView = rootView.findViewById(R.id.detail_price);
            textView.setText(String.valueOf(this.car.getPrice()));
            textView = rootView.findViewById(R.id.detail_engine);
            textView.setText(databaseHelper.getItemById(
                    Item.ENGINE_TABLE_NAME,
                    this.car.getEngine()).getLabel()
            );
            textView = rootView.findViewById(R.id.detail_fuel);
            textView.setText(databaseHelper.getItemById(
                    Item.FUEL_TABLE_NAME,
                    this.car.getFuel()).getLabel()
            );
            textView = rootView.findViewById(R.id.detail_transmission);
            textView.setText(databaseHelper.getItemById(
                    Item.TRANSMISSION_TABLE_NAME,
                    this.car.getTransmission()).getLabel()
            );
            textView = rootView.findViewById(R.id.detail_description);
            textView.setText(this.car.getDescription());
            textView = rootView.findViewById(R.id.detail_imgurl);
            textView.setText(this.car.getImgurl());
            switch (this.car.getFavorite()) {
                case 0:
                    favTextView.setText("");
                    break;
                case 1:
                    favTextView.setText(R.string.lbl_favorite);
                    break;
            }
        } else {

            textView = rootView.findViewById(R.id.detail_manufacturer);
            textView.setVisibility(View.GONE);
            textView = rootView.findViewById(R.id.detail_year);
            textView.setVisibility(View.GONE);
            textView = rootView.findViewById(R.id.detail_price);
            textView.setVisibility(View.GONE);
            textView = rootView.findViewById(R.id.detail_engine);
            textView.setVisibility(View.GONE);
            textView = rootView.findViewById(R.id.detail_fuel);
            textView.setVisibility(View.GONE);
            textView = rootView.findViewById(R.id.detail_transmission);
            textView.setVisibility(View.GONE);
            favTextView.setVisibility(View.GONE);
            favBtn.setVisibility(View.GONE);
            fab.setVisibility(View.GONE);

            textView = rootView.findViewById(R.id.detail_model);
            textView.setText(this.item.getLabel());
            textView = rootView.findViewById(R.id.detail_description);
            textView.setText(this.item.getDescription());
            textView = rootView.findViewById(R.id.detail_imgurl);
            textView.setText(this.item.getImgurl());
        }
    }

    private void setListeners() {

        if (car != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (car.getFavorite() == 1) {
                        car.setFavorite(0);
                        updateCar(car);
                        favTextView.setText("");
                        favBtn.setText("+");
                    } else {
                        car.setFavorite(1);
                        updateCar(car);
                        favTextView.setText(R.string.lbl_favorite);
                        favBtn.setText("-");
                    }
                }
            });
        }
    }

    private void initDB() {

        databaseHelper = DatabaseHelper.getInstance(this.getActivity());
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_dtls_frag;
    }
}
