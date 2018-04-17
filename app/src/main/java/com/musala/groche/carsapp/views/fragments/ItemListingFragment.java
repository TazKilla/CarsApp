package com.musala.groche.carsapp.views.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.musala.groche.carsapp.R;
import com.musala.groche.carsapp.database.DatabaseHelper;
import com.musala.groche.carsapp.database.model.Item;
import com.musala.groche.carsapp.utils.DividerItemDecoration;
import com.musala.groche.carsapp.utils.RecyclerTouchListener;
import com.musala.groche.carsapp.utils.RecyclerViewItemClickInterface;
import com.musala.groche.carsapp.views.adapters.ItemsAdapter;

import java.util.List;

public abstract class ItemListingFragment extends BaseFragment {

    private final static String TAG = "ItemListingFragment";

    private RecyclerView itemsRecyclerView;
    protected ItemsAdapter itemAdapter;
    private TextView noItemsView;
    private View rootView;
    private String itemTable = null;

    private RecyclerViewItemClickInterface itemClickListener;
    private RecyclerView.LayoutManager mLayoutManager;
    private FloatingActionButton floatingActionButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try{
            itemClickListener = (RecyclerViewItemClickInterface) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() +
                    " must implement ClickInterface");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(fragmentLayoutId, container, false);

        init();
        setListeners();
        initDB();

        toggleEmptyItems();

        return rootView;
    }
    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public void setItemTable(String itemTable) {
        this.itemTable = itemTable;
    }

    public void showActionsDialog(final int position) {
        CharSequence options[] = new CharSequence[]{"Edit", "Delete"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle(R.string.title_dialog);

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    showItemDialog(true, itemsList.get(position), position);
                } else {
                    deleteItem(position);
                }
            }
        });
        builder.show();
    }

    protected void showItemDialog(final boolean shouldUpdate, final Item item, final int position) {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(this.getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.item_dialog, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilderUserInput.setView(view);

        final EditText inputLabel = view.findViewById(R.id.label);
        final EditText inputDescription = view.findViewById(R.id.description);
        final EditText inputImgUrl = view.findViewById(R.id.imgurl);

        TextView dialogTitle = view.findViewById(R.id.dialog_title);

        switch (itemTable) {
            case Item.MANUFACTURER_TABLE_NAME:
                dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_manufacturer_title) : getString(R.string.lbl_edit_manufacturer_title));
                break;
            case Item.ENGINE_TABLE_NAME:
                dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_engine_title) : getString(R.string.lbl_edit_engine_title));
                break;
            case Item.FUEL_TABLE_NAME:
                dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_fuel_title) : getString(R.string.lbl_edit_fuel_title));
                break;
            case Item.TRANSMISSION_TABLE_NAME:
                dialogTitle.setText(!shouldUpdate ? getString(R.string.lbl_new_transmission_title) : getString(R.string.lbl_edit_transmission_title));
                break;
        }

        if (shouldUpdate && item != null) {
            inputLabel.setText(item.getLabel());
            inputDescription.setText(item.getDescription());
            inputImgUrl.setText(item.getImgurl());
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
                if (TextUtils.isEmpty(inputLabel.getText().toString()) ||
                        TextUtils.isEmpty(inputDescription.getText().toString()) ||
                        TextUtils.isEmpty(inputImgUrl.getText().toString())) {
                    Toast.makeText(getActivity(), R.string.toast_fill_every_fields, Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    alertDialog.dismiss();
                }

                Item mItem = new Item();

                mItem.setLabel(inputLabel.getText().toString());
                mItem.setDescription(inputDescription.getText().toString());
                mItem.setImgurl(inputImgUrl.getText().toString());

                if (shouldUpdate && item != null) {
                    mItem.setId(item.getId());
                    updateItem(mItem, position);
                } else {
                    createItem(mItem);
                }
            }
        });
    }

    public void toggleEmptyItems() {
        if (databaseHelper != null) {
            int count = 0;
            if (itemTable != null) {
                Log.d(TAG, "Getting items count from db...");
                count = databaseHelper.getItemsCount(itemTable);
                Log.d(TAG, "There is " + count + " item(s)");
            }
            if (count > 0) {
                noItemsView.setVisibility(View.INVISIBLE);
            } else {
                noItemsView.setVisibility(View.VISIBLE);
            }
        } else {
            Log.d(TAG, "databaseHelper is null, please initialise fragment...");
        }
    }

    private void init() {

        floatingActionButton = rootView.findViewById(R.id.floating_action_button);

        mLayoutManager = new LinearLayoutManager(this.getActivity());

        noItemsView = rootView.findViewById(R.id.empty_items_view);
        itemsRecyclerView = rootView.findViewById(R.id.recycler_view);
        itemsRecyclerView.setLayoutManager(mLayoutManager);
        itemsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        itemsRecyclerView.addItemDecoration(
                new DividerItemDecoration(
                        this.getActivity(),
                        LinearLayoutManager.VERTICAL,
                        R.dimen.activity_margin
                )
        );

        itemAdapter = new ItemsAdapter(this.itemsList);
        itemsRecyclerView.setAdapter(itemAdapter);
    }

    private void setListeners() {

        itemsRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this.getActivity(),
                itemsRecyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

                Item item = itemsList.get(position);

                itemClickListener.itemElementClicked(itemTable, item.getId());
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
                    showItemDialog(false, null, -1);
                }
            });
        }
    }

    private void initDB() {

        databaseHelper = DatabaseHelper.getInstance(this.getActivity());
    }

    public void updateItem(Item item, int position) {

        Log.d(TAG, "Car to be updated: " + item.toString());
        int result = databaseHelper.updateItem(item, itemTable);
        Log.d(TAG, result == 1 ? "Item updated on database" : "Unable to update item on database: " + result);

        itemAdapter.notifyItemChanged(position);
        toggleEmptyItems();
    }

    private void createItem(Item item) {

        long id = databaseHelper.insertItem(item, itemTable);

        item = databaseHelper.getItem(itemTable, id);

        if (item != null) {
            itemsList.add(0, item);
            itemAdapter.notifyDataSetChanged();
            toggleEmptyItems();
        }
    }

    public void deleteItem(int position) {
        databaseHelper.deleteItem(itemsList.get(position), itemTable);
        itemsList.remove(position);
        itemAdapter.notifyItemRemoved(position);
        toggleEmptyItems();
    }
}
