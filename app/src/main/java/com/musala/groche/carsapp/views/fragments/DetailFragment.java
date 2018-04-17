package com.musala.groche.carsapp.views.fragments;

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

    private static final String TAG = "DetailFragment";
    public static final String NAME = "content_car_dtls_frag";
    public final String title = "Car details";
    private final boolean root = false;

    private Car car;
    private Item item;
    private TextView favBtn;
    private FloatingActionButton fab;
    private TextView favTextView;
    private View rootView;

    private DatabaseHelper databaseHelper;

//    @Override
//    public void onAttach(Activity activity) {
//        super.onAttach(activity);
//        try{
//            backBtnListener = (RecyclerViewItemClickInterface) activity;
//        } catch (ClassCastException e) {
//            throw new ClassCastException(activity.toString() +
//                    " must implement ClickInterface");
//        }
//    }

    public static DetailFragment newInstance(Car car) {

        DetailFragment fragment = new DetailFragment();
        fragment.car = car;

        Log.d(TAG, "New DetailFragment instance: \n" + car.toString());

        return fragment;
    }

    public static DetailFragment newInstance(Item item) {

        DetailFragment fragment = new DetailFragment();
        fragment.item = item;

        Log.d(TAG, "New DetailFragment instance: \n" + item.toString());

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (car != null) {
            rootView = inflater.inflate(R.layout.content_car_dtls_frag, container, false);
        } else {
            rootView = inflater.inflate(R.layout.content_item_dtls_frag, container, false);
        }

        init();
        setListeners();
        initDB();

        return rootView;
    }

    public void updateCar(Car c) {

        Log.d(NAME, "Car to be updated: " + c.toString());
        int result = databaseHelper.updateCar(c);
        Log.d(NAME, result == 1 ? "Car updated on databaseHelper" : "Unable to update car on databaseHelper: " + result);
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public boolean isRoot() {
        return root;
    }

    @Override
    public String getTitle() {
        return title;
    }

    private void init() {

        TextView textView;

        if (car != null) {
            favTextView = rootView.findViewById(R.id.detail_favorite);
            favBtn = rootView.findViewById(R.id.favbtn);
            if (car.getFavorite() == 1) {
                favBtn.setText("-");
            } else {
                favBtn.setText("+");
            }
            fab = rootView.findViewById(R.id.details_fab);

            textView = rootView.findViewById(R.id.detail_manufacturer);
            textView.setText(this.car.getManufacturer());
            textView = rootView.findViewById(R.id.detail_model);
            textView.setText(this.car.getModel());
            textView = rootView.findViewById(R.id.detail_year);
            textView.setText(String.valueOf(this.car.getYear()));
            textView = rootView.findViewById(R.id.detail_price);
            textView.setText(String.valueOf(this.car.getPrice()));
            textView = rootView.findViewById(R.id.detail_engine);
            switch (this.car.getEngine()) {
                case 0:
                    textView.setText(Car.EngineType.GASOLINE.toString());
                    break;
                case 1:
                    textView.setText(Car.EngineType.DIESEL.toString());
                    break;
            }
            textView = rootView.findViewById(R.id.detail_transmission);
            switch (this.car.getTransmission()) {
                case 0:
                    textView.setText(Car.TransmissionType.MANUAL.toString());
                    break;
                case 1:
                    textView.setText(Car.TransmissionType.AUTOMATIC.toString());
                    break;
            }
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

            textView = rootView.findViewById(R.id.detail_label);
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
}
