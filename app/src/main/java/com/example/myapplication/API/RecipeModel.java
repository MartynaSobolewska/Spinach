package com.example.myapplication.API;

import com.example.myapplication.models.Recipe;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class RecipeModel {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("results")
    @Expose
    private ArrayList<Recipe> results;

    public RecipeModel(int count, ArrayList<Recipe> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<Recipe> getResults() {
        return results;
    }

    public void setResults(ArrayList<Recipe> results) {
        this.results = results;
    }
}
