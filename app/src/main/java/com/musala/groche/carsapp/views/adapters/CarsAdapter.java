package com.musala.groche.carsapp.views.adapters;

import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.List;

public class CarsAdapter extends RecyclerView.Adapter<CarsAdapter.ViewHolder> {

    private static final String TAG = "CarsAdapter";

    private List<Car> carsList;
    private List<Item> manufacturersList;
    static Drawable tmpImage;

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
        GetCarImg task = (GetCarImg) new GetCarImg().execute(car.getImgurl());
        Item manufacturer = findItem(car.getManufacturer(), manufacturersList);
        Log.d(TAG, "Car's manufacturer: \n" + manufacturer.toString());
        holder.manufacturer.setText(manufacturer.getLabel());
        holder.model.setText(car.getModel());
        if (tmpImage != null) {
            holder.carImg.setImageDrawable(tmpImage);
        }
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

    static  class GetCarImg extends AsyncTask<String, Void, Drawable> {

        @Override
        protected Drawable doInBackground(String... strings) {

            String url = strings[0];
            if (!url.equals("NA")) {
                try {
                    InputStream is = (InputStream) new URL(url).getContent();
                    return Drawable.createFromStream(is, "src name");
                } catch (Exception e) {
                    Log.e(TAG, "Unable to get image content:");
                    e.printStackTrace();
                    return null;
                }
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(Drawable drawable) {
            if (isCancelled()) {
                tmpImage = null;
            }
            tmpImage = drawable;
        }
    }
}
