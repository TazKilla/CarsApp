package com.musala.groche.carsapp.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.utils.RecyclerViewItemClickInterface;
import com.musala.groche.carsapp.views.fragments.BaseFragment;
import com.musala.groche.carsapp.views.fragments.CarListingFragment;
import com.musala.groche.carsapp.views.fragments.CarsFragment;
import com.musala.groche.carsapp.views.fragments.DetailFragment;
import com.musala.groche.carsapp.views.fragments.FavoritesFragment;
import com.musala.groche.carsapp.views.fragments.ItemListingFragment;
import com.musala.groche.carsapp.views.fragments.ItemsFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecyclerViewItemClickInterface {

    private static final String TAG = "HomeActivity";
    private static boolean isFirstFrag = true;

    private List<Car> carsList = new ArrayList<>();
    private List<Item> itemsList = new ArrayList<>();
    private List<Item> manufacturersList = new ArrayList<>();
    private List<Item> enginesList = new ArrayList<>();
    private List<Item> fuelsList = new ArrayList<>();
    private List<Item> transmissionsList = new ArrayList<>();
    private TextView tabTitleView;
    private String currentFragName = CarsFragment.NAME;
    private String previousRootTitle;
    private String itemTable;
    private CarsFragment carsFragment;
    private FavoritesFragment favoritesFragment;
    private DetailFragment detailsFragment;
    private ItemsFragment itemsFragment;
    private BottomNavigationView bottomNavigationView;
    private Button backBtn;
    private ImageButton moreBtn;
    private PopupMenu popupItemsMenu;
    private PopupMenu popupSettingsMenu;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initUI();
        super.onCreate(savedInstanceState);
        initDB();
        setListener();
    }

    @Override
    public void onBackPressed() {
        tabTitleView.setText(previousRootTitle);
        backBtn.setVisibility(View.GONE);
        super.onBackPressed();
    }

    @Override
    public void carElementClicked(int carId) {

        Car car = databaseHelper.getCar(carId);
        int detailsFragmentID = getFragmentID(DetailFragment.NAME);

        detailsFragment =
                (DetailFragment) getSupportFragmentManager().findFragmentById(detailsFragmentID);

        if (detailsFragment == null) {
            detailsFragment = DetailFragment.newInstance(car);
        } else {
            detailsFragment.setCar(car);
        }

        tabTitleView.setText(R.string.title_details);
        switchFragment(detailsFragment);
    }

    @Override
    public void itemElementClicked(String itemTable, int itemId) {

        if (itemTable != null) {
            Log.d(TAG, "Asking for item id " + itemId + " on table " + itemTable);
            Item item = databaseHelper.getItemById(itemTable, itemId);
            int detailsFragmentID = getFragmentID(DetailFragment.NAME);

            detailsFragment =
                    (DetailFragment) getSupportFragmentManager().findFragmentById(detailsFragmentID);

            if (detailsFragment == null) {
                detailsFragment = DetailFragment.newInstance(item);
            } else {
                detailsFragment.setItem(item);
            }

            tabTitleView.setText(R.string.title_details);
            switchFragment(detailsFragment);
        } else {
            Log.d(TAG, "Unable to load item details, no table have been set");
        }
    }

    private void switchFragment(BaseFragment newFragment) {

        // Cleaning backStack to ensure that back button will quit the app if not in Details view
        if (newFragment.isRoot()) {
            previousRootTitle = (String) tabTitleView.getText();
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        currentFragName = newFragment.getName();
        if (isFirstFrag) {
            fragmentTransaction.add(R.id.frame_layout, newFragment);
            isFirstFrag = false;
        } else {
            fragmentTransaction.replace(R.id.frame_layout, newFragment);
        }
        if (!newFragment.isRoot()) {
            fragmentTransaction.addToBackStack(null);
            backBtn.setVisibility(View.VISIBLE);
        }
        fragmentTransaction.commit();
    }

    private void initDB() {

        Log.d(TAG, "Initializing database...");
        databaseHelper =DatabaseHelper.getInstance(this);

        carsList.addAll(databaseHelper.getAllCars());
        manufacturersList.addAll(databaseHelper.getAllItems(Item.MANUFACTURER_TABLE_NAME));
        enginesList.addAll(databaseHelper.getAllItems(Item.ENGINE_TABLE_NAME));
        fuelsList.addAll(databaseHelper.getAllItems(Item.FUEL_TABLE_NAME));
        transmissionsList.addAll(databaseHelper.getAllItems(Item.TRANSMISSION_TABLE_NAME));
        Log.d(TAG, "Database initialized...");
    }

    private void initUI() {

        Log.d(TAG, "Initializing app UI...");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home);

        tabTitleView = findViewById(R.id.tab_title);
        bottomNavigationView = findViewById(R.id.navigation);
        backBtn = findViewById(R.id.back_btn);
        moreBtn = findViewById(R.id.button_more);
        popupItemsMenu = new PopupMenu(HomeActivity.this, bottomNavigationView, Gravity.END);
        popupItemsMenu.inflate(R.menu.popup_items_menu);
        popupSettingsMenu = new PopupMenu(HomeActivity.this, moreBtn, Gravity.END);
        popupSettingsMenu.inflate(R.menu.popup_settings_menu);
        Log.d(TAG, "App UI initialized...");
    }

    private void setListener() {

        Log.d(TAG, "Initializing listeners...");
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_cars:
                                carsList = databaseHelper.getAllCars();
                                tabTitleView.setText(R.string.title_cars);
                                selectMenuItem(carsFragment, CarsFragment.NAME);
                                break;

                            case R.id.navigation_favorites:
                                carsList = databaseHelper.getAllFavCars();
                                tabTitleView.setText(R.string.title_favorites);
                                selectMenuItem(favoritesFragment, FavoritesFragment.NAME);
                                break;

                            case R.id.navigation_items:
                                popupItemsMenu.show();
                                break;
                        }

                        return true;
                    }
                }
        );

        // Back button listener, only visible on Details fragment
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupSettingsMenu.show();
            }
        });

        // Popup menu listeners
        popupItemsMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });

        popupItemsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_manufacturer:
                        itemTable = Item.MANUFACTURER_TABLE_NAME;
                        manufacturersList = databaseHelper.getAllItems(itemTable);
                        itemsList = manufacturersList;
                        itemsFragment = ItemsFragment.newInstance(itemsList, getFragmentID(ItemsFragment.NAME), itemTable);
                        tabTitleView.setText(R.string.title_manufacturers);
                        switchFragment(itemsFragment);
                        return true;
                    case R.id.menu_engine:
                        itemTable = Item.ENGINE_TABLE_NAME;
                        enginesList = databaseHelper.getAllItems(itemTable);
                        itemsList = enginesList;
                        itemsFragment = ItemsFragment.newInstance(itemsList, getFragmentID(ItemsFragment.NAME), itemTable);
                        tabTitleView.setText(R.string.title_engines);
                        switchFragment(itemsFragment);
                        return true;
                    case R.id.menu_fuel:
                        itemTable = Item.FUEL_TABLE_NAME;
                        fuelsList = databaseHelper.getAllItems(itemTable);
                        itemsList = fuelsList;
                        itemsFragment = ItemsFragment.newInstance(itemsList, getFragmentID(ItemsFragment.NAME), itemTable);
                        tabTitleView.setText(R.string.title_fuels);
                        switchFragment(itemsFragment);
                        return true;
                    case R.id.menu_transmission:
                        itemTable = Item.TRANSMISSION_TABLE_NAME;
                        transmissionsList = databaseHelper.getAllItems(itemTable);
                        itemsList = transmissionsList;
                        itemsFragment = ItemsFragment.newInstance(itemsList, getFragmentID(ItemsFragment.NAME), itemTable);
                        tabTitleView.setText(R.string.title_transmissions);
                        switchFragment(itemsFragment);
                        return true;
                }
                return false;
            }
        });

        popupSettingsMenu.setOnDismissListener(new PopupMenu.OnDismissListener() {
            @Override
            public void onDismiss(PopupMenu menu) {

            }
        });

        popupSettingsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_settings:
                        showSettings();
                        break;
                }
                return false;
            }
        });

        // Select the default tab - Cars
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
        bottomNavigationView.setSelectedItemId(R.id.navigation_cars);
        Log.d(TAG, "Listeners initialized...");
    }

    private int getFragmentID(String fragmentName) {

        return getResources().getIdentifier(
                fragmentName,
                "layout",
                getPackageName()
        );
    }

    private void selectMenuItem(BaseFragment newFragment, String fragmentName) {
        int fragmentID = getFragmentID(fragmentName);
        backBtn.setVisibility(View.GONE);
        if (!currentFragName.equals(fragmentName) || (detailsFragment != null && detailsFragment.isVisible())) {

            if (newFragment == null) {
                switch (fragmentName) {
                    case FavoritesFragment.NAME:

                        newFragment = FavoritesFragment.newInstance(
                                carsList,
                                fragmentID,
                                manufacturersList
                        );
                        favoritesFragment = (FavoritesFragment) newFragment;
                        break;
                    case CarsFragment.NAME:
                        newFragment = CarsFragment.newInstance(
                                carsList,
                                fragmentID,
                                manufacturersList
                        );
                        carsFragment = (CarsFragment) newFragment;
                        break;
                    case ItemsFragment.NAME:
                        newFragment = ItemsFragment.newInstance(
                                itemsList,
                                fragmentID,
                                itemTable
                        );
                        itemsFragment = (ItemsFragment) newFragment;
                        break;
                    default:
                        Log.d(TAG, "Unable to define witch fragment to use...");
                        break;
                }
            } else {
                if (fragmentName.equals(ItemsFragment.NAME)) {
                    ((ItemListingFragment) newFragment).setItemsList(itemsList);
                    ((ItemListingFragment) newFragment).setItemTable(itemTable);
                    itemsFragment = (ItemsFragment) newFragment;
                } else {
                    ((CarListingFragment) newFragment).setCarsList(carsList);
                    ((CarListingFragment) newFragment).setManufacturersList(manufacturersList);
                }
            }

            if (newFragment != null) {
                switchFragment(newFragment);
            }
        } else if (detailsFragment != null && detailsFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else {
            Log.d(TAG,"No way to display this tab: " + currentFragName + ", " + fragmentID);
        }
    }

    public void showSettings() {
//        Toast.makeText(this, "You tried to open settings activity...", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
//        finish();
    }
}
