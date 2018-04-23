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
import android.widget.CompoundButton;

import com.musala.groche.carsapp.R;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = "SettingsActivity";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private CheckBox listCheckBox;
    private CheckBox gridCheckBox;
    private CheckBox englishCheckBox;
    private CheckBox frenchCheckBox;
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

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        locale = sharedPreferences.getString(getString(R.string.settings_selected_lang), getString(R.string.settings_opt_en));
        viewType = sharedPreferences.getString(getString(R.string.settings_selected_view), getString(R.string.settings_opt_list));
        editor = sharedPreferences.edit();

        listCheckBox = findViewById(R.id.checkBox_list);
        gridCheckBox = findViewById(R.id.checkBox_grid);
        englishCheckBox = findViewById(R.id.checkBox_english);
        frenchCheckBox = findViewById(R.id.checkBox_french);

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
    }

    public void onCheckboxClicked(View view) {

        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkBox_list:
                if (checked) {
                    Log.d(TAG, "");
                    gridCheckBox.setChecked(false);
                    editor.putString(
                            getString(R.string.settings_selected_view),
                            getString(R.string.settings_opt_list)
                    );
                } else {
                    Log.d(TAG, "");
                    gridCheckBox.setChecked(true);
                    editor.putString(
                            getString(R.string.settings_selected_view),
                            getString(R.string.settings_opt_grid)
                    );
                }
                editor.commit();
                break;
            case R.id.checkBox_grid:
                if (checked) {
                    listCheckBox.setChecked(false);
                    editor.putString(getString(
                            R.string.settings_selected_view),
                            getString(R.string.settings_opt_grid)
                    );
                } else {
                    listCheckBox.setChecked(true);
                    editor.putString(getString(
                            R.string.settings_selected_view),
                            getString(R.string.settings_opt_list)
                    );
                }
                editor.commit();
                break;
            case R.id.checkBox_english:
                if (checked) {
                    frenchCheckBox.setChecked(false);
                    editor.putString(getString(
                            R.string.settings_selected_lang),
                            getString(R.string.settings_opt_en)
                    );
                } else {
                    frenchCheckBox.setChecked(true);
                    editor.putString(getString(
                            R.string.settings_selected_lang),
                            getString(R.string.settings_opt_fr)
                    );
                }
                editor.commit();
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.checkBox_french:
                if (checked) {
                    englishCheckBox.setChecked(false);
                    editor.putString(getString(
                            R.string.settings_selected_lang),
                            getString(R.string.settings_opt_fr)
                    );
                } else {
                    englishCheckBox.setChecked(true);
                    editor.putString(getString(
                            R.string.settings_selected_lang),
                            getString(R.string.settings_opt_en)
                    );
                }
                editor.commit();
                startActivity(new Intent(this, HomeActivity.class));
                break;
        }
    }
}
