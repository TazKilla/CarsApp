package com.musala.groche.carsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Engine;
import com.musala.groche.carsapp.database.model.Fuel;
import com.musala.groche.carsapp.database.model.Manufacturer;
import com.musala.groche.carsapp.database.model.Transmission;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "cars_db";
    private static DatabaseHelper instance = null;

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Car.CREATE_TABLE);
//        db.execSQL(Manufacturer.CREATE_TABLE);
//        db.execSQL(Engine.CREATE_TABLE);
//        db.execSQL(Fuel.CREATE_TABLE);
//        db.execSQL(Transmission.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Car.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + Manufacturer.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + Engine.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + Fuel.TABLE_NAME);
//        db.execSQL("DROP TABLE IF EXISTS " + Transmission.TABLE_NAME);
        onCreate(db);
    }

    public long insertCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Car.COLUMN_MANUFACTURER, car.getManufacturer());
        values.put(Car.COLUMN_MODEL, car.getModel());
        values.put(Car.COLUMN_YEAR, car.getYear());
        values.put(Car.COLUMN_PRICE, car.getPrice());
        values.put(Car.COLUMN_ENGINE, car.getEngine());
        values.put(Car.COLUMN_TRANSMISSION, car.getTransmission());
        values.put(Car.COLUMN_DESCRIPTION, car.getDescription());
        values.put(Car.COLUMN_IMGURL, car.getImgurl());
        values.put(Car.COLUMN_FAVORITE, car.getFavorite());

        long id = db.insert(Car.TABLE_NAME, null, values);

        db.close();

        return id;
    }

    public Car getCar(long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Car.TABLE_NAME,
                new String[]{Car.COLUMN_ID, Car.COLUMN_MANUFACTURER, Car.COLUMN_MODEL,
                        Car.COLUMN_YEAR, Car.COLUMN_PRICE, Car.COLUMN_ENGINE,
                        Car.COLUMN_TRANSMISSION, Car.COLUMN_DESCRIPTION, Car.COLUMN_IMGURL,
                        Car.COLUMN_FAVORITE},
                Car.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Car car = new Car(
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Car.COLUMN_MANUFACTURER)),
                cursor.getString(cursor.getColumnIndex(Car.COLUMN_MODEL)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_YEAR)),
                cursor.getFloat(cursor.getColumnIndex(Car.COLUMN_PRICE)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ENGINE)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_TRANSMISSION)),
                cursor.getString(cursor.getColumnIndex(Car.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Car.COLUMN_IMGURL)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_FAVORITE))
        );

        cursor.close();
        db.close();

        return car;
    }

    public List<Car> getAllCars() {
        List<Car> cars = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Car.TABLE_NAME +
                " ORDER BY " + Car.COLUMN_MANUFACTURER + " ASC, " + Car.COLUMN_MODEL + " ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Car car = new Car();
                car.setId(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ID)));
                car.setManufacturer(cursor.getString(cursor.getColumnIndex(Car.COLUMN_MANUFACTURER)));
                car.setModel(cursor.getString(cursor.getColumnIndex(Car.COLUMN_MODEL)));
                car.setYear(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_YEAR)));
                car.setPrice(cursor.getFloat(cursor.getColumnIndex(Car.COLUMN_PRICE)));
                car.setEngine(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ENGINE)));
                car.setTransmission(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_TRANSMISSION)));
                car.setDescription(cursor.getString(cursor.getColumnIndex(Car.COLUMN_DESCRIPTION)));
                car.setImgurl(cursor.getString(cursor.getColumnIndex(Car.COLUMN_IMGURL)));
                car.setFavorite(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_FAVORITE)));

                cars.add(car);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return cars;
    }

    public List<Car> getAllFavCars() {
        List<Car> favCars = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + Car.TABLE_NAME +
                " WHERE " + Car.COLUMN_FAVORITE + " = 1" +
                " ORDER BY " + Car.COLUMN_MANUFACTURER + " ASC, " + Car.COLUMN_MODEL + " ASC ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Car car = new Car();
                car.setId(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ID)));
                car.setManufacturer(cursor.getString(cursor.getColumnIndex(Car.COLUMN_MANUFACTURER)));
                car.setModel(cursor.getString(cursor.getColumnIndex(Car.COLUMN_MODEL)));
                car.setYear(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_YEAR)));
                car.setPrice(cursor.getFloat(cursor.getColumnIndex(Car.COLUMN_PRICE)));
                car.setEngine(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ENGINE)));
                car.setTransmission(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_TRANSMISSION)));
                car.setDescription(cursor.getString(cursor.getColumnIndex(Car.COLUMN_DESCRIPTION)));
                car.setImgurl(cursor.getString(cursor.getColumnIndex(Car.COLUMN_IMGURL)));
                car.setFavorite(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_FAVORITE)));

                favCars.add(car);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return favCars;
    }

    public int getCarsCount() {
        String countQuery = "SELECT * FROM " + Car.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery,  null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public int getFavCarsCount() {
        String countQuery = "SELECT * FROM " + Car.TABLE_NAME + " WHERE " + Car.COLUMN_FAVORITE + " = 1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery,  null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public int updateCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Car.COLUMN_MANUFACTURER, car.getManufacturer());
        values.put(Car.COLUMN_MODEL, car.getModel());
        values.put(Car.COLUMN_YEAR, car.getYear());
        values.put(Car.COLUMN_PRICE, car.getPrice());
        values.put(Car.COLUMN_ENGINE, car.getEngine());
        values.put(Car.COLUMN_TRANSMISSION, car.getTransmission());
        values.put(Car.COLUMN_DESCRIPTION, car.getDescription());
        values.put(Car.COLUMN_IMGURL, car.getImgurl());
        values.put(Car.COLUMN_FAVORITE, car.getFavorite());

//        Log.d(TAG, values.toString());

        return db.update(Car.TABLE_NAME, values, Car.COLUMN_ID + " =?",
                new String[]{String.valueOf(car.getId())});
    }

    public void deleteCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Car.TABLE_NAME, Car.COLUMN_ID + " =?",
                new String[]{String.valueOf(car.getId())});
        db.close();
    }

    /*


    public void updateCar(Car c, int position) {

        Log.d(TAG, "Car to be updated: " + Car.toString(c));
        int result = databaseHelper.updateCar(c);
        Log.d(TAG, result == 1 ? "Car updated on databaseHelper" : "Unable to update car on databaseHelper: " + result);

        if (c.getFavorite() == 1) {
            carsList.set(position, c);
        } else {
            carsList.remove(position);
        }

        carsAdapter.notifyItemChanged(position);
        toggleEmptyCars();
    }

    private void createCar(Car car) {

        long id = databaseHelper.insertCar(car);

        car = databaseHelper.getCar(id);

        if (car != null) {
            carsList.add(0, car);
            carsAdapter.notifyDataSetChanged();
            toggleEmptyCars();
        }
    }

    public void deleteCar(int position) {
        databaseHelper.deleteCar(carsList.get(position));
        carsList.remove(position);
        carsAdapter.notifyItemRemoved(position);
        toggleEmptyCars();
    }
     */
}
