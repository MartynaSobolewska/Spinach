package com.example.myapplication.models;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredients {
    @SerializedName("components")
    @Expose
    Ingredient[] ingredients;

    public Pair<String,String>[] getIngredients(){
        int amount = ingredients.length;
        Pair<String,String>[] ingredients = new Pair[amount];
        for (int i = 0; i < amount; i++) {
            ingredients[i] = this.ingredients[i].getPair();
        }
        return ingredients;
    }
}
