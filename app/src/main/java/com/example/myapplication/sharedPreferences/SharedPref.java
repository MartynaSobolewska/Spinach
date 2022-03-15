package com.example.myapplication.sharedPreferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.myapplication.R;

public class SharedPref {
    SharedPreferences preferences;

    public SharedPref(Context context){
        preferences = context.getSharedPreferences("userPrefs", Context.MODE_PRIVATE);
    }

    /**
     * Sets vegan preference to given value
     * @param setVegan true if set to vegan, false otherwise
     */
    public void setVeganState(boolean setVegan){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Vegan", setVegan);
        editor.commit();
        editor.apply();
    }

    /**
     * Gets vegan preference value
     * @return vegan preference value
     */
    public boolean getVeganState(){
        boolean state = preferences.getBoolean("Vegan", false);
        return state;
    }

    /**
     * Sets vegetarian preference to given value
     * @param setVegetarian true if set to vegetarian, false otherwise
     */
    public void setVegetarianState(boolean setVegetarian){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Vegetarian", setVegetarian);
        editor.commit();
        editor.apply();
    }

    /**
     * Gets vegetarian preference value
     * @return vegetarian preference value
     */
    public boolean getVegetarianState(){
        boolean state = preferences.getBoolean("Vegetarian", false);
        return state;
    }
}
