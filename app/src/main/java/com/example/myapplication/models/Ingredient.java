package com.example.myapplication.models;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * A class containing ingredient information. A list of instances stored in Ingredients class.
 * Necessary due to the structure of json returned by the API.
 * An ingredient object can be easily converted into a pair of values see: @getPair()
 */
public class Ingredient implements Serializable {
    @SerializedName("raw_text")
    @Expose
    String ingredientText;
    @SerializedName("extra_comment")
    @Expose
    String comment;

    public Ingredient(String ingredientText, String comment) {
        this.ingredientText = ingredientText;
        this.comment = comment;
    }

    public String getIngredientText() {
        return ingredientText;
    }

    public void setIngredientText(String ingredientTexts) {
        this.ingredientText = ingredientTexts;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Turns the information stored in the ingredient class into a pair object.
     * Useful to hide multi-layer json structure.
     * @return a pair containing ingredient info (first: ingredient name, second: additional comment)
     */
    public Pair<String, String> getPair() {
        return Pair.create(ingredientText, comment);
    }
}
