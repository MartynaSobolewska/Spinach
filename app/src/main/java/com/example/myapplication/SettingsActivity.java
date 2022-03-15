package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import androidx.annotation.Nullable;
import androidx.core.app.NavUtils;

import com.example.myapplication.sharedPreferences.SharedPref;

public class SettingsActivity extends AppCompatActivity {
    CheckBox vegetarianCheck, veganCheck;
    SharedPref preferences;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        preferences = new SharedPref(this);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_activity);

        toolbar = (Toolbar) findViewById(R.id.settingsToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        veganCheck = findViewById(R.id.veganCheck);
        vegetarianCheck = findViewById(R.id.vegeterianCheck);

        veganCheck.setChecked(preferences.getVeganState());
        vegetarianCheck.setChecked(preferences.getVegetarianState());

        setCheckListeners();
    }
    private void setCheckListeners(){
        veganCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !preferences.getVeganState()){
                preferences.setVeganState(true);
                veganCheck.setChecked(true);
                vegetarianCheck.setChecked(false);
                preferences.setVegetarianState(false);
            }else {
                preferences.setVeganState(false);
                veganCheck.setChecked(false);
            }
        });
        vegetarianCheck.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !preferences.getVegetarianState()){
                preferences.setVegetarianState(true);
                vegetarianCheck.setChecked(true);
                veganCheck.setChecked(false);
                preferences.setVeganState(false);
            }else {
                preferences.setVegetarianState(false);
                vegetarianCheck.setChecked(false);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
