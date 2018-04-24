package com.musala.groche.carsapp.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.TextView;

import com.musala.groche.carsapp.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox listCheckBox;
    private CheckBox gridCheckBox;
    private CheckBox englishCheckBox;
    private CheckBox frenchCheckBox;
    private TextView listTextView;
    private TextView gridTextView;
    private TextView englishTextView;
    private TextView frenchTextView;
    private String locale;
    private String viewType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        initUI();
        super.onCreate(savedInstanceState);
    }

    private void initUI() {

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getApplicationContext().getSharedPreferences(
                getString(R.string.preferences_file_key),
                Context.MODE_PRIVATE
        );
        locale = sharedPreferences.getString(
                getString(R.string.settings_selected_lang),
                getString(R.string.settings_opt_en)
        );
        viewType = sharedPreferences.getString(
                getString(R.string.settings_selected_view),
                getString(R.string.settings_opt_list)
        );
        editor = sharedPreferences.edit();

        listCheckBox = findViewById(R.id.checkBox_list);
        gridCheckBox = findViewById(R.id.checkBox_grid);
        englishCheckBox = findViewById(R.id.checkBox_english);
        frenchCheckBox = findViewById(R.id.checkBox_french);

        listTextView = findViewById(R.id.label_checkBox_list);
        gridTextView = findViewById(R.id.label_checkBox_grid);
        englishTextView = findViewById(R.id.label_checkBox_english);
        frenchTextView = findViewById(R.id.label_checkBox_french);

        // Sets the corresponding checkbox depending on the current settings
        switch (locale) {
            case "en":
                englishCheckBox.setChecked(true);
                break;
            case "fr_FR":
                frenchCheckBox.setChecked(true);
                break;
        }
        switch (viewType) {
            case "list":
                listCheckBox.setChecked(true);
                break;
            case "grid":
                gridCheckBox.setChecked(true);
                break;
        }

        // Sets up the views click listeners
        listTextView.setOnClickListener(viewTypeClickListener);
        listCheckBox.setOnClickListener(viewTypeClickListener);
        gridTextView.setOnClickListener(viewTypeClickListener);
        gridCheckBox.setOnClickListener(viewTypeClickListener);

        englishTextView.setOnClickListener(languageClickListener);
        englishCheckBox.setOnClickListener(languageClickListener);
        frenchTextView.setOnClickListener(languageClickListener);
        frenchCheckBox.setOnClickListener(languageClickListener);
    }

    // Handles language setting changes
    private View.OnClickListener languageClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((v.getId() == R.id.checkBox_english || v == englishTextView)
                    && locale.equals(getString(R.string.settings_opt_fr))) {
                Log.d(TAG, "Switching from french to english");
                updateSetting(
                        englishCheckBox,
                        true,
                        getString(R.string.settings_selected_lang),
                        getString(R.string.settings_opt_en));
                frenchCheckBox.setChecked(false);
                confirmSettingUpdate();
            } else if ((v.getId() == R.id.checkBox_french || v == frenchTextView)
                    && locale.equals(getString(R.string.settings_opt_en))) {
                Log.d(TAG, "Switching from english to french");
                updateSetting(
                        frenchCheckBox,
                        true,
                        getString(R.string.settings_selected_lang),
                        getString(R.string.settings_opt_fr)
                );
                englishCheckBox.setChecked(false);
                confirmSettingUpdate();
            } else if (v.getId() == R.id.checkBox_english
                    && locale.equals(getString(R.string.settings_opt_en))) {
                englishCheckBox.toggle();
            } else if (v.getId() == R.id.checkBox_french
                    && locale.equals(getString(R.string.settings_opt_fr))) {
                frenchCheckBox.toggle();
            }
        }
    };

    // Handles view type setting changes
    private View.OnClickListener viewTypeClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if ((v.getId() == R.id.checkBox_list || v == listTextView)
                    && viewType.equals(getString(R.string.settings_opt_grid))) {
                Log.d(TAG, "Switching from grid to list view");
                updateSetting(
                        listCheckBox,
                        true,
                        getString(R.string.settings_selected_view),
                        getString(R.string.settings_opt_list)
                );
                gridCheckBox.setChecked(false);
                confirmSettingUpdate();
            } else if ((v.getId() == R.id.checkBox_grid || v == gridTextView)
                    && viewType.equals(getString(R.string.settings_opt_list))) {
                Log.d(TAG, "Switching from list to grid view");
                updateSetting(
                        gridCheckBox,
                        true,
                        getString(R.string.settings_selected_view),
                        getString(R.string.settings_opt_grid));
                listCheckBox.setChecked(false);
                confirmSettingUpdate();
            } else if (v.getId() == R.id.checkBox_list
                    && viewType.equals(getString(R.string.settings_opt_list))) {
                listCheckBox.toggle();
            } else if (v.getId() == R.id.checkBox_grid
                    && viewType.equals(getString(R.string.settings_opt_grid))) {
                gridCheckBox.toggle();
            }
        }
    };

    // Updates any kind of setting in the shared preferences
    private void updateSetting(CheckBox checkBox, boolean checkBoxStatus,
                               String settingName, String settingValue) {
        checkBox.setChecked(checkBoxStatus);
        editor.putString(settingName, settingValue);
    }

    // Applies asynchronously new settings in preferences file
    // and restarts HomeApplication in order to update the app
    private void confirmSettingUpdate() {
        editor.apply();
        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        finishAffinity();
    }
}
