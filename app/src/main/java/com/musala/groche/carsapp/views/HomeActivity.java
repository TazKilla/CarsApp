package com.musala.groche.carsapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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
import java.util.Locale;

import net.hockeyapp.android.UpdateManager;
import net.hockeyapp.android.CrashManager;

public class HomeActivity extends AppCompatActivity implements RecyclerViewItemClickInterface {

    private static final String TAG = "HomeActivity";
    private static boolean isFirstFrag = true;

    private List<Car> carsList = new ArrayList<>();
    private List<Item> itemsList = new ArrayList<>();
    private List<Item> manufacturersList = new ArrayList<>();
    private TextView tabTitleView;
    private int currentFragId;
    private String previousRootTitle;
    private String itemTable;
    private CarsFragment carsFragment;
    private FavoritesFragment favoritesFragment;
    private DetailFragment detailsFragment;
    private BottomNavigationView bottomNavigationView;
    private ImageButton backBtn;
    private ImageButton moreBtn;
    private PopupMenu popupItemsMenu;
    private PopupMenu popupSettingsMenu;

    public DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initDB();
        initUI();
        super.onCreate(savedInstanceState);
        setListener();
        checkForUpdates();
        checkForCrashes();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterManagers();
    }

    @Override
    public void onBackPressed() {
        if (previousRootTitle.equals(tabTitleView.getText().toString())) {
            finishAffinity();
        } else {
            tabTitleView.setText(previousRootTitle);
            backBtn.setVisibility(View.GONE);
            super.onBackPressed();
        }
    }

    @Override
    public void carElementClicked(int carId) {

        Car car = databaseHelper.getCar(carId);

        detailsFragment =
                (DetailFragment) getSupportFragmentManager().findFragmentByTag(DetailFragment.TAG);

        if (detailsFragment == null) {
            detailsFragment = DetailFragment.newInstance(this, car);
        } else {
            detailsFragment.setCar(car);
        }

        tabTitleView.setText(detailsFragment.getTitle());
        switchFragment(detailsFragment);
    }

    @Override
    public void itemElementClicked(String itemTable, int itemId) {

        this.itemTable = itemTable;

        if (itemTable != null) {
            Log.d(TAG, "Asking for item id " + itemId + " on table " + itemTable);
            Item item = databaseHelper.getItemById(itemTable, itemId);

            detailsFragment =
                    (DetailFragment) getSupportFragmentManager().findFragmentByTag(DetailFragment.TAG);

            if (detailsFragment == null) {
                detailsFragment = DetailFragment.newInstance(this, item);
            } else {
                detailsFragment.setItem(item);
            }

            tabTitleView.setText(detailsFragment.getTitle());
            switchFragment(detailsFragment);
        } else {
            Log.d(TAG, "Unable to load item details, no table have been set");
        }
    }

    private void switchFragment(BaseFragment newFragment) {

        // Cleaning backStack to ensure that back button will quit the app if in root view
        if (newFragment.isRoot()) {
            Log.d(TAG, "New fragment is root");
            previousRootTitle = (String) tabTitleView.getText();
            Log.d(TAG, "New root fragment title: " + previousRootTitle);
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        currentFragId = newFragment.getLayoutId();
        if (isFirstFrag) {
            fragmentTransaction.add(R.id.frame_layout, newFragment, newFragment.TAG);
            isFirstFrag = false;
        } else {
            fragmentTransaction.replace(R.id.frame_layout, newFragment, newFragment.TAG);
        }
        if (!newFragment.isRoot()) {
            Log.d(TAG, "Transaction added to back stack");
            fragmentTransaction.addToBackStack(null);
            backBtn.setVisibility(View.VISIBLE);
        }
        fragmentTransaction.commit();
    }

    private void initDB() {

        Log.d(TAG, "Initializing database...");
        databaseHelper =DatabaseHelper.getInstance(this, this.getAssets());

        carsList.addAll(databaseHelper.getAllCars());
        manufacturersList.addAll(databaseHelper.getAllItems(Item.MANUFACTURER_TABLE_NAME));
        Log.d(TAG, "Database initialized...");
    }

    private void initUI() {

        SharedPreferences sharedPreferences;
        String locale;
        Configuration configuration;

        Log.d(TAG, "Initializing app UI...");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.AppTheme);

        sharedPreferences = this.getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        locale = sharedPreferences.getString(getString(R.string.settings_selected_lang), getString(R.string.settings_opt_en));
        configuration = new Configuration(getResources().getConfiguration());

        switch (locale) {
            case "en":
                configuration.setLocale(Locale.ENGLISH);
                break;
            case "fr_FR":
                configuration.setLocale(Locale.FRANCE);
                break;
        }

        // TODO refactor with Context createConfigurationContext()
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());

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
                                selectMenuItem(carsFragment, R.layout.content_cars_frag);
                                break;

                            case R.id.navigation_favorites:
                                carsList = databaseHelper.getAllFavCars();
                                tabTitleView.setText(R.string.title_favorites);
                                selectMenuItem(favoritesFragment, R.layout.content_favs_frag);
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
        popupItemsMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_manufacturer:
                        tabTitleView.setText(R.string.title_manufacturers);
                        switchFragment(getItemsFragment(Item.MANUFACTURER_TABLE_NAME));
                        return true;
                    case R.id.menu_engine:
                        tabTitleView.setText(R.string.title_engines);
                        switchFragment(getItemsFragment(Item.ENGINE_TABLE_NAME));
                        return true;
                    case R.id.menu_fuel:
                        tabTitleView.setText(R.string.title_fuels);
                        switchFragment(getItemsFragment(Item.FUEL_TABLE_NAME));
                        return true;
                    case R.id.menu_transmission:
                        tabTitleView.setText(R.string.title_transmissions);
                        switchFragment(getItemsFragment(Item.TRANSMISSION_TABLE_NAME));
                        return true;
                }
                return false;
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
//        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
//        bottomNavigationView.setSelectedItemId(R.id.navigation_cars);
        switchFragment(CarsFragment.newInstance(carsList, manufacturersList));
        Log.d(TAG, "Listeners initialized...");
    }

    private void selectMenuItem(BaseFragment newFragment, int fragmentID) {
        backBtn.setVisibility(View.GONE);
        if (currentFragId != fragmentID || (detailsFragment != null && detailsFragment.isVisible())) {

            if (newFragment == null) {
                switch (fragmentID) {
                    case R.layout.content_favs_frag:

                        newFragment = FavoritesFragment.newInstance(
                                carsList,
                                manufacturersList
                        );
                        favoritesFragment = (FavoritesFragment) newFragment;
                        break;
                    case R.layout.content_cars_frag:
                        newFragment = CarsFragment.newInstance(
                                carsList,
                                manufacturersList
                        );
                        carsFragment = (CarsFragment) newFragment;
                        break;
                    case R.layout.content_items_frag:
                        newFragment = ItemsFragment.newInstance(
                                itemsList,
                                itemTable
                        );
                        break;
                    default:
                        Log.d(TAG, "Unable to define witch fragment to use...");
                        break;
                }
            } else {
                if (fragmentID == R.layout.content_items_frag) {
                    ((ItemListingFragment) newFragment).setItemsList(itemsList);
                    ((ItemListingFragment) newFragment).setItemTable(itemTable);
                } else {
                    ((CarListingFragment) newFragment).setCarsList(carsList);
                    ((CarListingFragment) newFragment).setManufacturersList(manufacturersList);
                }
            }

            if (newFragment != null) {
                switchFragment(newFragment);
            }
        } else if (detailsFragment != null && detailsFragment.isVisible()) {
//            switchFragment(currentFrag);
            getFragmentManager().popBackStack();
        } else {
            Log.d(TAG,"No way to display this tab: " + currentFragId + ", " + fragmentID);
        }
    }

    public void showSettings() {
        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
        startActivity(intent);
    }

    private ItemsFragment getItemsFragment(String itemTable) {
        itemsList = databaseHelper.getAllItems(itemTable);
        return ItemsFragment.newInstance(itemsList, itemTable);
    }

    private void checkForUpdates() {
        UpdateManager.register(this);
    }

    private void checkForCrashes() {
        CrashManager.register(this);
    }

    private void unregisterManagers() {
        UpdateManager.unregister();
    }
}
