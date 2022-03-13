package com.example.myapplication.models;

import android.util.Pair;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

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
    private int ingredientsOtherThanSearched;
    private String secondaryTitle;

    public Recipe(int id, String title, String thumbnailURL, TimeTier timeTier,
                  String language, Ingredients[] ingredients, Nutrition nutrition,
                  int numberOfServings, Instruction[] instructions) {
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.totalTime = timeTier;
        this.language = language;
        this.nutrition = nutrition;
        this.ingredients = ingredients;
        this.numberOfServings = numberOfServings;
        this.instructions = instructions;
        this.ingredientsOtherThanSearched = -1;
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

    public Pair<String, String>[] getFormattedIngredients(){
        if (ingredients == null || this.ingredients[0].getIngredients() == null){
            return null;
        }
        return this.getIngredients()[0].getIngredients();
    }

    public void setIngredients(Ingredients[] ingredients) {
        this.ingredients = ingredients;
    }

    public String getSecondaryTitle() {
        return secondaryTitle;
    }

    public void setSecondaryTitle(String secondaryTitle) {
        this.secondaryTitle = secondaryTitle;
    }

    public int getIngredientsOtherThanSearched() {
        return ingredientsOtherThanSearched;
    }

    /**
     * Finds the number of ingredients that do not match the searched key words
     * @param ingredients ingredients searched for
     */
    public void setIngredientsOtherThanSearched(ArrayList<String> ingredients) {
        Recipe r = this;
        if (r==null || r.ingredients == null || r.ingredients[0].getIngredients() == null) {
            // error value
            this.ingredientsOtherThanSearched = -2;
            return;
        }
        int allIngredients = r.ingredients[0].getIngredients().length;
        if(ingredients == null || ingredients.isEmpty()){
            // all ingredients are other than searched
            this.ingredientsOtherThanSearched = allIngredients;
            return;
        }
        int matchingIngredients = 0;
        Pair<String,String>[] ingredientsAndDescriptions = r.getIngredients()[0].getIngredients();

        // count the ingredients containing key words (from ingredients list)
        for (int i = 0; i < ingredientsAndDescriptions.length; i++) {
            for (String ingredient :
                    ingredients) {
                if (ingredientsAndDescriptions[i].first.contains(ingredient))
                    matchingIngredients ++;
            }
        }
        this.ingredientsOtherThanSearched = allIngredients - matchingIngredients;
    }

    /**
     * Sets the number of ingredients other than searched for and returns it for a given recipe.
     * @param r recipe to find the number for
     * @param ingredients ingredients searched for
     * @return  number of ingredients other than searched for
     */
    public static int getAndSetNumberOfIngredientsOtherThanGiven(Recipe r,
                                                                 ArrayList<String> ingredients){
        r.setIngredientsOtherThanSearched(ingredients);
        int otherThanSearched = r.getIngredientsOtherThanSearched();
        return otherThanSearched;
    }
}
