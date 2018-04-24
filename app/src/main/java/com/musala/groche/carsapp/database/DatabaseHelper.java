package com.musala.groche.carsapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.database.model.Car;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "cars_db";
    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper instance = null;
    private Context context;

    private static final String CREATE_MANUFACTURER_TABLE =
            "CREATE TABLE " + Item.MANUFACTURER_TABLE_NAME + "(" +
                    Item.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Item.COLUMN_LABEL + " TEXT, " +
                    Item.COLUMN_DESCRIPTION + " TEXT, " +
                    Item.COLUMN_IMGURL + " TEXT" +
                    ")";
    private static final String CREATE_ENGINE_TABLE =
            "CREATE TABLE " + Item.ENGINE_TABLE_NAME + "(" +
                    Item.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Item.COLUMN_LABEL + " TEXT, " +
                    Item.COLUMN_DESCRIPTION + " TEXT, " +
                    Item.COLUMN_IMGURL + " TEXT" +
                    ")";
    private static final String CREATE_FUEL_TABLE =
            "CREATE TABLE " + Item.FUEL_TABLE_NAME + "(" +
                    Item.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Item.COLUMN_LABEL + " TEXT, " +
                    Item.COLUMN_DESCRIPTION + " TEXT, " +
                    Item.COLUMN_IMGURL + " TEXT" +
                    ")";
    private static final String CREATE_TRANSMISSION_TABLE =
            "CREATE TABLE " + Item.TRANSMISSION_TABLE_NAME + "(" +
                    Item.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Item.COLUMN_LABEL + " TEXT, " +
                    Item.COLUMN_DESCRIPTION + " TEXT, " +
                    Item.COLUMN_IMGURL + " TEXT" +
                    ")";

    private DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
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
        db.execSQL(CREATE_MANUFACTURER_TABLE);
        db.execSQL(CREATE_ENGINE_TABLE);
        db.execSQL(CREATE_FUEL_TABLE);
        db.execSQL(CREATE_TRANSMISSION_TABLE);

        AssetManager assetManager = context.getAssets();
        String[] itemFiles = {"manufacturer", "engine", "fuel", "transmission"};
        int i = 0;
        try {
            for (;i < 4; i++) {
//            InputStream inputStream = assetManager.open("cars_data.csv");
                InputStream inputStream = assetManager.open(itemFiles[i] + "s.csv");
                InputStreamReader streamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(streamReader);
                String line;
                String[] values;
                while ((line = bufferedReader.readLine()) != null) {
                    values = line.split(";");
                    String insertCommand = String.format(
                            "INSERT INTO " + itemFiles[i] + "(label, description, imgurl) " +
                                    "VALUES ('%s', '%s', '%s')",
                            values[0], values[1], values[2]);
                    db.execSQL(insertCommand);
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to open data input file: " + itemFiles[i] + "s.csv");
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Car.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item.MANUFACTURER_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item.ENGINE_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item.FUEL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Item.TRANSMISSION_TABLE_NAME);
        onCreate(db);
    }

    // Items management

    public long insertItem(Item item, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Item.COLUMN_LABEL, item.getLabel());
        values.put(Item.COLUMN_DESCRIPTION, item.getDescription());
        values.put(Item.COLUMN_IMGURL, item.getImgurl());

        long id = db.insert(tableName, null, values);

        db.close();

        return id;
    }

    public Item getItemById(String tableName, long id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableName,
                new String[]{Item.COLUMN_ID,
                        Item.COLUMN_LABEL,
                        Item.COLUMN_DESCRIPTION,
                        Item.COLUMN_IMGURL},
                Item.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Item item = new Item(
                cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_LABEL)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_IMGURL))
        );

        cursor.close();
        db.close();

        return item;
    }

    public Item getItemByLabel(String tableName, String label) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(tableName,
                new String[]{Item.COLUMN_ID,
                        Item.COLUMN_LABEL,
                        Item.COLUMN_DESCRIPTION,
                        Item.COLUMN_IMGURL},
                Item.COLUMN_LABEL + "=?",
                new String[]{label}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Item item = new Item(
                cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_LABEL)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(Item.COLUMN_IMGURL))
        );

        cursor.close();
        db.close();

        return item;
    }

    public List<Item> getAllItems(String tableName) {
        List<Item> items = new ArrayList<>();

        String selectQuery = "SELECT * FROM " + tableName +
                " ORDER BY label ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Item item = new Item();
                item.setId(cursor.getInt(cursor.getColumnIndex(Item.COLUMN_ID)));
                item.setLabel(cursor.getString(cursor.getColumnIndex(Item.COLUMN_LABEL)));
                item.setDescription(cursor.getString(cursor.getColumnIndex(Item.COLUMN_DESCRIPTION)));
                item.setImgurl(cursor.getString(cursor.getColumnIndex(Item.COLUMN_IMGURL)));

                items.add(item);
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        Log.d(TAG, "Got " + tableName + " list: \n");
        for (Item item : items) {
            Log.d(TAG, item.toString());
        }

        return items;
    }

    public List<String> getAllItemLabels(String tableName) {
        List<String> labels = new ArrayList<>();

        String selectQuery = "SELECT " + Item.COLUMN_LABEL +
                " FROM " + tableName +
                " ORDER BY label ASC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

//        labels.add("Select " + tableName);
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(cursor.getColumnIndex(Item.COLUMN_LABEL)));
            } while (cursor.moveToNext());
        }

        db.close();
        cursor.close();

        return labels;
    }

    public int getItemsCount(String tableName) {
        String countQuery = "SELECT * FROM " + tableName;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(countQuery,  null);

        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    public int updateItem(Item item, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Item.COLUMN_LABEL, item.getLabel());
        values.put(Item.COLUMN_DESCRIPTION, item.getDescription());
        values.put(Item.COLUMN_IMGURL, item.getImgurl());

//        Log.d(TAG, values.toString());

        return db.update(tableName, values, Item.COLUMN_ID + " =?",
                new String[]{String.valueOf(item.getId())});
    }

    public void deleteItem(Item item, String tableName) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(tableName, Item.COLUMN_ID + " =?",
                new String[]{String.valueOf(item.getId())});
        db.close();
    }

    // Cars management

    public long insertCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(Car.COLUMN_MANUFACTURER, car.getManufacturer());
        values.put(Car.COLUMN_MODEL, car.getModel());
        values.put(Car.COLUMN_YEAR, car.getYear());
        values.put(Car.COLUMN_PRICE, car.getPrice());
        values.put(Car.COLUMN_ENGINE, car.getEngine());
        values.put(Car.COLUMN_FUEL, car.getFuel());
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
                        Car.COLUMN_YEAR, Car.COLUMN_PRICE, Car.COLUMN_ENGINE, Car.COLUMN_FUEL,
                        Car.COLUMN_TRANSMISSION, Car.COLUMN_DESCRIPTION, Car.COLUMN_IMGURL,
                        Car.COLUMN_FAVORITE},
                Car.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        Car car = new Car(
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ID)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_MANUFACTURER)),
                cursor.getString(cursor.getColumnIndex(Car.COLUMN_MODEL)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_YEAR)),
                cursor.getFloat(cursor.getColumnIndex(Car.COLUMN_PRICE)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ENGINE)),
                cursor.getInt(cursor.getColumnIndex(Car.COLUMN_FUEL)),
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
                car.setManufacturer(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_MANUFACTURER)));
                car.setModel(cursor.getString(cursor.getColumnIndex(Car.COLUMN_MODEL)));
                car.setYear(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_YEAR)));
                car.setPrice(cursor.getFloat(cursor.getColumnIndex(Car.COLUMN_PRICE)));
                car.setEngine(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ENGINE)));
                car.setFuel(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_FUEL)));
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
                car.setManufacturer(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_MANUFACTURER)));
                car.setModel(cursor.getString(cursor.getColumnIndex(Car.COLUMN_MODEL)));
                car.setYear(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_YEAR)));
                car.setPrice(cursor.getFloat(cursor.getColumnIndex(Car.COLUMN_PRICE)));
                car.setEngine(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_ENGINE)));
                car.setFuel(cursor.getInt(cursor.getColumnIndex(Car.COLUMN_FUEL)));
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
        values.put(Car.COLUMN_FUEL, car.getFuel());
        values.put(Car.COLUMN_TRANSMISSION, car.getTransmission());
        values.put(Car.COLUMN_DESCRIPTION, car.getDescription());
        values.put(Car.COLUMN_IMGURL, car.getImgurl());
        values.put(Car.COLUMN_FAVORITE, car.getFavorite());

        return db.update(Car.TABLE_NAME, values, Car.COLUMN_ID + " =?",
                new String[]{String.valueOf(car.getId())});
    }

    public void deleteCar(Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Car.TABLE_NAME, Car.COLUMN_ID + " =?",
                new String[]{String.valueOf(car.getId())});
        db.close();
    }
}
