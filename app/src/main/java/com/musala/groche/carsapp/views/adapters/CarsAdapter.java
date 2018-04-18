package com.musala.groche.carsapp.views.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;

import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private static final String TAG = "CarsAdapter";

    private List<Car> carsList;
    private List<Item> manufacturersList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView manufacturer;
        public TextView model;
        public TextView dot;

        ViewHolder(View view) {
            super(view);
            manufacturer = view.findViewById(R.id.manufacturer);
            model = view.findViewById(R.id.model);
            dot = view.findViewById(R.id.dot);
        }
    }

    public CarsAdapter(List<Car> carsList, List<Item> manufacturersList) {
        this.carsList = carsList;
        this.manufacturersList = manufacturersList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.car_list_row, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Car car = carsList.get(position);
        Item manufacturer = findItem(car.getManufacturer(), manufacturersList);
        Log.d(TAG, "Car's manufacturer: \n" + manufacturer.toString());
        holder.manufacturer.setText(manufacturer.getLabel());
        holder.model.setText(car.getModel());
        holder.dot.setText(Html.fromHtml("&#8226;"));
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
