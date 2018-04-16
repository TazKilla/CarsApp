package com.musala.groche.carsapp.views;

import android.os.Bundle;
import android.support.annotation.*;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.utils.RecyclerViewItemClickInterface;
import com.musala.groche.carsapp.views.fragments.BaseFragment;
import com.musala.groche.carsapp.views.fragments.CarListingFragment;
import com.musala.groche.carsapp.views.fragments.CarsFragment;
import com.musala.groche.carsapp.views.fragments.DetailFragment;
import com.musala.groche.carsapp.views.fragments.FavoritesFragment;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements RecyclerViewItemClickInterface {

    private static final String TAG = "HomeActivity";
    private static boolean isFirstFrag = true;

    private List<Car> carsList = new ArrayList<>();
    private TextView tabTitleView;
    private String currentFragName = CarsFragment.NAME;
    private CarsFragment carsFragment;
    private FavoritesFragment favoritesFragment;
    private DetailFragment detailsFragment;
    private BottomNavigationView bottomNavigationView;

    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initUI();
        super.onCreate(savedInstanceState);
        initDB();
        setListener();
    }

    @Override
    public void carItemClicked(int carId) {

        Car car = databaseHelper.getCar(carId);
        int detailsFragmentID = getFragmentID(DetailFragment.NAME);

        detailsFragment =
                (DetailFragment) getSupportFragmentManager().findFragmentById(detailsFragmentID);

        if (detailsFragment == null) {
            detailsFragment = DetailFragment.newInstance(car);
        } else {
            detailsFragment.setCar(car);
        }

        switchFragment(detailsFragment);
    }

    private void switchFragment(BaseFragment newFragment) {

        // Cleaning backStack to ensure that back button will quit the app if not in Details view
        if (newFragment.isRoot()) {
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
            tabTitleView.setText(newFragment.getTitle());
        }
        fragmentTransaction.commit();
    }

    private void initDB() {

        Log.d(TAG, "Initializing database...");
        databaseHelper =DatabaseHelper.getInstance(this);

        carsList.addAll(databaseHelper.getAllCars());
        Log.d(TAG, "Database initialized...");
    }

    private void initUI() {

        Log.d(TAG, "Initializing app UI...");
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_home);

        tabTitleView = findViewById(R.id.tab_title);
        bottomNavigationView = findViewById(R.id.navigation);
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
                                selectMenuItem(carsFragment, CarsFragment.NAME);
                                break;

                            case R.id.navigation_favorites:
                                carsList = databaseHelper.getAllFavCars();
                                selectMenuItem(favoritesFragment, FavoritesFragment.NAME);
                                break;

                            case R.id.navigation_settings:
                                Toast.makeText(getApplicationContext(), "Sorry, no settings option integrated yet...", Toast.LENGTH_LONG).show();
                                break;
                        }

                        return true;
                    }
                }
        );

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

    private void selectMenuItem(CarListingFragment newFragment, String fragmentName) {
        int fragmentID = getFragmentID(fragmentName);
        if (!currentFragName.equals(fragmentName)
                || (detailsFragment != null && detailsFragment.isVisible())) {

            if (newFragment == null) {
                switch (fragmentName) {
                    case FavoritesFragment.NAME:
                        newFragment = FavoritesFragment.newInstance(
                                carsList,
                                fragmentID
                        );
                        favoritesFragment = (FavoritesFragment) newFragment;
                        break;
                    case CarsFragment.NAME:
                        newFragment = CarsFragment.newInstance(
                                carsList,
                                fragmentID
                        );
                        carsFragment = (CarsFragment) newFragment;
                        break;
                    default:
                        Log.d(TAG, "Unable to define witch fragment to use...");
                        break;
                }
            } else {
                newFragment.setCarsList(carsList);
            }

            if (newFragment != null) {
                tabTitleView.setText(newFragment.getTitle());
                switchFragment(newFragment);
            }
        } else if (detailsFragment != null && detailsFragment.isVisible()) {
            getFragmentManager().popBackStack();
        } else {
            Log.d(TAG,"No way to display this tab: " + currentFragName + ", " + fragmentID);
        }
    }
}
