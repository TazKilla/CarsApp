package com.musala.groche.carsapp.views.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private static final String TAG = "CarsAdapter";

    private List<Car> carsList;
    private List<Item> manufacturersList;
    static Drawable tmpImage;
    private String selectedViewType;
    private Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView manufacturer;
        public TextView model;
        public ImageView carImg;

        ViewHolder(View view) {
            super(view);
            manufacturer = view.findViewById(R.id.manufacturer);
            model = view.findViewById(R.id.model);
            carImg = view.findViewById(R.id.car_img);
        }
    }

    public CarsAdapter(Context context, List<Car> carsList, List<Item> manufacturersList, String selectedViewType) {
        this.carsList = carsList;
        this.manufacturersList = manufacturersList;
        this.selectedViewType = selectedViewType;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView;

        switch (selectedViewType) {
            case "grid":
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.car_grid_element, parent, false);
                break;
            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.car_list_row, parent, false);
                break;
        }

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Car car = carsList.get(position);
        Item manufacturer = findItem(car.getManufacturer(), manufacturersList);
        Log.d(TAG, "Car's manufacturer: \n" + manufacturer.toString());
        holder.manufacturer.setText(manufacturer.getLabel());
        holder.model.setText(car.getModel());
        holder.carImg.setImageDrawable(tmpImage);
        Picasso.with(context)
                .load(car.getImgurl())
                .placeholder(R.drawable.ic_if_sedan_285810)
                .into(holder.carImg);
    }

    @Override
    public int getItemCount() {
        if (carsList != null) {
            return carsList.size();
        } else {
            return 0;
        }
    }

    private Item findItem(int id, List<Item> itemList) {
        for (Item item : itemList) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }
}
