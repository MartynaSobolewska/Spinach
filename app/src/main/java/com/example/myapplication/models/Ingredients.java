package com.example.myapplication.models;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * A class storing an array  of ingredients. Necessary due to json structure
 * and automatic json - java class conversion.
 */
public class Ingredients implements Serializable {
    @SerializedName("components")
    @Expose
    Ingredient[] ingredients;

    /**
     * Gets a formatted array of pairs containing ingredient information
     * @return array of pairs (first: ingredient name, second: additional comment)
     */
    public Pair<String,String>[] getIngredients(){
        int amount = ingredients.length;
        Pair<String,String>[] ingredients = new Pair[amount];
        for (int i = 0; i < amount; i++) {
            ingredients[i] = this.ingredients[i].getPair();
        }
        return ingredients;
    }
}
