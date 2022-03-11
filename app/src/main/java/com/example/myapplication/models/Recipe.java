package com.example.myapplication.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Stores information about recipes obtained from Tasty API
 */
public class Recipe {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("slug")
    @Expose
    private String title; //slug
    @SerializedName("thumbnail_url")
    @Expose
    private String thumbnailURL;
    @SerializedName("language")
    @Expose
    private String language;
    @SerializedName("total_time_tier")
    @Expose
    private TimeTier totalTime;
//    private String[][] ingredients;
    @SerializedName("num_servings")
    @Expose
    private int numberOfServings;
    @SerializedName("instructions")
    @Expose
    private Instruction[] instructions;
    @SerializedName("nutrition")
    @Expose
    private Nutrition nutrition;
    @SerializedName("sections")
    @Expose
    private Ingredients[] ingredients;

    public Recipe(int id, String title, String thumbnailURL, TimeTier timeTier, String language, Ingredients[] ingredients, Nutrition nutrition, int numberOfServings, Instruction[] instructions) {
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.totalTime = timeTier;
        this.language = language;
        this.nutrition = nutrition;
        this.ingredients = ingredients;
        this.numberOfServings = numberOfServings;
        this.instructions = instructions;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnailURL) {
        this.thumbnailURL = thumbnailURL;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public TimeTier getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(TimeTier totalTime) {
        this.totalTime = totalTime;
    }

    public int getNumberOfServings() {
        return numberOfServings;
    }

    public void setNumberOfServings(int numberOfServings) {
        this.numberOfServings = numberOfServings;
    }

    public Instruction[] getInstructions() {
        return instructions;
    }

    public void setInstructions(Instruction[] instructions) {
        this.instructions = instructions;
    }

    public Nutrition getNutrition() {
        return nutrition;
    }

    public void setNutrition(Nutrition nutrition) {
        this.nutrition = nutrition;
    }

    public Ingredients[] getIngredients() {
        return ingredients;
    }

    public void setIngredients(Ingredients[] ingredients) {
        this.ingredients = ingredients;
    }
}
