package com.musala.groche.carsapp.utils;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

public class OnSpinnerItemSelectedListener implements AdapterView.OnItemSelectedListener {

    public void onItemSelected(AdapterView<?> parent, View view,int position, long id) {
//        Toast.makeText(parent.getContext(), "Item selected: " + parent.getItemAtPosition(position).toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {

    }
}
