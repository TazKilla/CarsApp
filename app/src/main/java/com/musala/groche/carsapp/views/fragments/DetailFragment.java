package com.musala.groche.carsapp.views.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.database.model.Car;
import com.squareup.picasso.Picasso;

public class DetailFragment extends BaseFragment {

    public static String TAG = "DetailFragment";
    public static String title;

    private Car car;
    private Item item;
    private View rootView;
    private SwitchCompat switchCompat;

    public static DetailFragment newInstance(Context context, Car car) {

        DetailFragment fragment = new DetailFragment();
        fragment.car = car;
        initDB(context);

        Log.d(TAG, "New DetailFragment instance: \n" + car.toString());

        return fragment;
    }

    public static DetailFragment newInstance(Context context, Item item) {

        DetailFragment fragment = new DetailFragment();
        fragment.item = item;
        initDB(context);

        Log.d(TAG, "New DetailFragment instance: \n" + item.toString());

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.content_dtls_frag, container, false);

        init();
        setListeners();

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
        return false;
    }

    @Override
    public String getTitle() {
        if (getContext() == null) {
            return "";
        }
        if (car != null) {
            return getContext().getString(R.string.title_car_details);
        } else if (item != null) {
            return getContext().getString(R.string.title_item_details);
        } else {
            return "";
        }
    }

    private void init() {

        TextView textView;
        ImageView imageView;
        String price;

        switchCompat = rootView.findViewById(R.id.detail_favorite);

        if (car != null) {

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
            price = String.valueOf(this.car.getPrice()) + " US$";
            textView.setText(price);
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
            imageView = rootView.findViewById(R.id.detail_img);
            Picasso.with(getContext())
                    .load(car.getImgurl())
                    .placeholder(R.drawable.ic_if_sedan_285810)
                    .into(imageView);
            switch (this.car.getFavorite()) {
                case 0:
                    switchCompat.setChecked(false);
                    break;
                case 1:
                    switchCompat.setChecked(true);
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
            switchCompat.setVisibility(View.GONE);

            textView = rootView.findViewById(R.id.detail_model);
            textView.setText(this.item.getLabel());
            textView = rootView.findViewById(R.id.detail_description);
            textView.setText(this.item.getDescription());
            imageView = rootView.findViewById(R.id.detail_img);
            Picasso.with(getContext())
                    .load(this.item.getImgurl())
                    .placeholder(R.drawable.ic_if_list_103613)
                    .into(imageView);
        }
    }

    private void setListeners() {

        if (car != null) {

            switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (car.getFavorite() == 1) {
                        car.setFavorite(0);
                        updateCar(car);
                    } else {
                        car.setFavorite(1);
                        updateCar(car);
                    }
                }
            });
        }
    }

    private static void initDB(Context context) {

        databaseHelper = DatabaseHelper.getInstance(context, context.getAssets());
    }

    @Override
    public int getLayoutId() {
        return R.layout.content_dtls_frag;
    }
}
