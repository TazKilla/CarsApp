package com.musala.groche.carsapp.views.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Car;
import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.utils.DividerItemDecoration;
import com.musala.groche.carsapp.utils.RecyclerTouchListener;
import com.musala.groche.carsapp.utils.RecyclerViewItemClickInterface;
import com.musala.groche.carsapp.views.adapters.CarsAdapter;

import java.util.List;

public abstract class CarListingFragment extends BaseFragment {

    private final static String TAG = "CarListingFragment";

    private RecyclerView carsRecyclerView;
    protected CarsAdapter carsAdapter;
    private TextView noCarsView;
    private View rootView;

    private RecyclerViewItemClickInterface carClickListener;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            carClickListener = (RecyclerViewItemClickInterface) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    " must implement ClickInterface");
        }
    }

    public abstract int getCarsCount();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(getLayoutId(), container, false);

        init();
        setListeners();
        initDB();

        toggleEmptyCars();

        return rootView;
    }

    public void setCarsList(List<Car> carsList) {
        this.carsList = carsList;
    }

    public void setManufacturersList(List<Item> manufacturersList) {
        this.manufacturersList = manufacturersList;
    }

    public void showActionsDialog(final int position) {
        CharSequence options[] = new CharSequence[]{"Edit", "Delete"};

        if (getContext() == null) {
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getContext());
        builder.setTitle(R.string.title_dialog);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showCarDialog(true, carsList.get(position), position);
                } else {
                    deleteCar(position);
                }
            }
        });
        builder.show();
    }

    protected void showCarDialog(final boolean shouldUpdate, final Car car, final int position) {
        final ViewGroup nullParent = null;
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.car_dialog, nullParent);

        if (getContext() == null) {
            return;
        }

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this.getContext());
        alertDialogBuilderUserInput.setView(view);

        AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        };

        ArrayAdapter<String> manufacturerAdapter = setAdapter(this.getActivity(), Item.MANUFACTURER_TABLE_NAME);
        final Spinner manufacturerSpinner = setSpinner(view, manufacturerAdapter, R.id.manufacturer_spinner, itemSelectedListener);

        ArrayAdapter<String> engineAdapter = setAdapter(this.getActivity(), Item.ENGINE_TABLE_NAME);
        final Spinner engineSpinner = setSpinner(view, engineAdapter, R.id.engine_spinner, itemSelectedListener);

        ArrayAdapter<String> fuelAdapter = setAdapter(this.getActivity(), Item.FUEL_TABLE_NAME);
        final Spinner fuelSpinner = setSpinner(view, fuelAdapter, R.id.fuel_spinner, itemSelectedListener);

        ArrayAdapter<String> transmissionAdapter =setAdapter(this.getActivity(), Item.TRANSMISSION_TABLE_NAME);
        final Spinner transmissionSpinner = setSpinner(view, transmissionAdapter, R.id.transmission_spinner, itemSelectedListener);

        final EditText inputModel = view.findViewById(R.id.model);
        final EditText inputYear = view.findViewById(R.id.year);
        final EditText inputPrice = view.findViewById(R.id.price);
        final EditText inputDescription = view.findViewById(R.id.description);
        final EditText inputImgUrl = view.findViewById(R.id.imgurl);
        final SwitchCompat favoriteSwitch = view.findViewById(R.id.favorite);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);
        dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_car_title) : getString(R.string.lbl_edit_car_title));

        if (shouldUpdate && car != null) {

            Item manufacturer = databaseHelper.getItemById(Item.MANUFACTURER_TABLE_NAME, car.getManufacturer());
            Item engine = databaseHelper.getItemById(Item.ENGINE_TABLE_NAME, car.getEngine());
            Item fuel = databaseHelper.getItemById(Item.FUEL_TABLE_NAME, car.getFuel());
            Item transmission = databaseHelper.getItemById(Item.TRANSMISSION_TABLE_NAME, car.getTransmission());

            manufacturerSpinner.setSelection(manufacturerAdapter.getPosition(manufacturer.getLabel()));
            inputModel.setText(car.getModel());
            inputYear.setText(String.valueOf(car.getYear()));
            inputPrice.setText(String.valueOf(car.getPrice()));
            engineSpinner.setSelection(engineAdapter.getPosition(engine.getLabel()));
            fuelSpinner.setSelection(fuelAdapter.getPosition(fuel.getLabel()));
            transmissionSpinner.setSelection(transmissionAdapter.getPosition(transmission.getLabel()));
            inputDescription.setText(car.getDescription());
            inputImgUrl.setText(car.getImgurl());
            if (car.getFavorite() == 1) {
                favoriteSwitch.setChecked(true);
            } else {
                favoriteSwitch.setChecked(false);
            }
        }
        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton(shouldUpdate ? "update" : "save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogBox, int which) {

                    }
                })
                .setNegativeButton("cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogBox, int which) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(inputModel.getText().toString()) ||
                        TextUtils.isEmpty(inputYear.getText().toString()) ||
                        TextUtils.isEmpty(inputPrice.getText().toString()) ||
                        TextUtils.isEmpty(inputDescription.getText().toString()) ||
                        TextUtils.isEmpty(inputImgUrl.getText().toString())) {

                    Toast.makeText(
                            getActivity(),
                            R.string.toast_fill_every_fields,
                            Toast.LENGTH_SHORT
                    ).show();

                    return;
                } else {
                    alertDialog.dismiss();
                }

                Car mCar = new Car();

                Item mManufacturer = databaseHelper.getItemByLabel(
                        Item.MANUFACTURER_TABLE_NAME,
                        manufacturerSpinner.getSelectedItem().toString()
                );
                Item mEngine = databaseHelper.getItemByLabel(
                        Item.ENGINE_TABLE_NAME,
                        engineSpinner.getSelectedItem().toString()
                );
                Item mFuel = databaseHelper.getItemByLabel(
                        Item.FUEL_TABLE_NAME,
                        fuelSpinner.getSelectedItem().toString()
                );
                Item mTransmission = databaseHelper.getItemByLabel(
                        Item.TRANSMISSION_TABLE_NAME,
                        transmissionSpinner.getSelectedItem().toString()
                );

                mCar.setManufacturer(mManufacturer.getId());
                mCar.setModel(inputModel.getText().toString());
                mCar.setYear(Integer.valueOf(inputYear.getText().toString()));
                mCar.setPrice(Float.valueOf(inputPrice.getText().toString()));
                mCar.setEngine(mEngine.getId());
                mCar.setFuel(mFuel.getId());
                mCar.setTransmission(mTransmission.getId());
                mCar.setDescription(inputDescription.getText().toString());
                mCar.setImgurl(inputImgUrl.getText().toString());
                if (favoriteSwitch.isChecked()) {
                    mCar.setFavorite(1);
                } else {
                    mCar.setFavorite(0);
                }

                if (shouldUpdate && car != null) {
                    mCar.setId(car.getId());
                    updateCar(mCar, position);
                } else {
                    createCar(mCar);
                }
            }
        });
    }

    public void toggleEmptyCars() {
        if (databaseHelper != null) {
            int count = getCarsCount();
            if (count > 0) {
                noCarsView.setVisibility(View.INVISIBLE);
            } else {
                noCarsView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d(TAG, "databaseHelper is null, please initialise fragment...");
        }
    }

    private void init() {

        int columnNumberGridView = 2;
        RecyclerView.LayoutManager mLayoutManager;

        if (getContext() == null) {
            return;
        }

        SharedPreferences sharedPreferences =getContext().getSharedPreferences(
                getString(R.string.preferences_file_key),
                Context.MODE_PRIVATE
        );
        String selectedViewType = sharedPreferences.getString(
                getString(R.string.settings_selected_view),
                getString(R.string.settings_opt_list)
        );

        floatingActionButton = rootView.findViewById(R.id.floating_action_button);
        noCarsView = rootView.findViewById(R.id.empty_car_view);
        carsRecyclerView = rootView.findViewById(R.id.recycler_view);

        switch (selectedViewType) {
            case "grid":
                mLayoutManager = new GridLayoutManager(this.getActivity(), columnNumberGridView);
                break;
            default:
                mLayoutManager = new LinearLayoutManager(this.getActivity());
                break;
        }

        switch (selectedViewType) {
            case "grid":
                carsRecyclerView.setHasFixedSize(true);
                break;
            default:
                carsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                carsRecyclerView.addItemDecoration(
                        new DividerItemDecoration(
                                this.getContext(),
                                LinearLayoutManager.VERTICAL,
                                R.dimen.activity_margin
                        )
                );
                break;
        }

        carsRecyclerView.setLayoutManager(mLayoutManager);

        carsAdapter = new CarsAdapter(getActivity(), this.carsList, this.manufacturersList, selectedViewType);
        carsRecyclerView.setAdapter(carsAdapter);
    }

    private void setListeners() {

        carsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(),
                carsRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Car car = carsList.get(position);

                carClickListener.carElementClicked(car.getId());
            }

            @Override
            public void onLongClick(View view, int position) {
                showActionsDialog(position);
            }
        }));

        if (floatingActionButton != null) {
            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCarDialog(false, null, -1);
                }
            });
        }
    }

    private void initDB() {

        if (getActivity() == null) {
            return;
        }

        databaseHelper = DatabaseHelper.getInstance(this.getActivity(), this.getActivity().getAssets());
    }

    public void updateCar(Car c, int position) {

//        Log.d(TAG, "Car to be updated: " + c.toString());
        int result = databaseHelper.updateCar(c);
        Log.d(TAG, result == 1 ? "Car updated on database" : "Unable to update car on database: " + result);

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

    private Spinner setSpinner(View view, ArrayAdapter itemAdapter, int spinnerId,
                               AdapterView.OnItemSelectedListener itemSelectedListener) {
        final Spinner itemSpinner = view.findViewById(spinnerId);
        itemSpinner.setAdapter(itemAdapter);
        itemSpinner.setOnItemSelectedListener(itemSelectedListener);

        return itemSpinner;
    }

    private ArrayAdapter<String> setAdapter(Context context, String tableName) {
        List<String> itemOptions = databaseHelper.getAllItemLabels(tableName);
        return new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, itemOptions);
    }
}
