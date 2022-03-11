package com.example.myapplication.models;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Ingredient {
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

    public Pair<String, String> getPair() {
        return Pair.create(ingredientText, comment);
    }
}
