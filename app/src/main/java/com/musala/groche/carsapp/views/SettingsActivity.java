package com.musala.groche.carsapp.views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.musala.groche.carsapp.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initUI();
        super.onCreate(savedInstanceState);
    }

    private void initUI() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_settings);
    }
}
