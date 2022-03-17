package com.example.myapplication.models;

import android.os.Build;
import android.util.Pair;

import androidx.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;

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
    private final String thumbnailURL;
    @SerializedName("total_time_tier")
    @Expose
    private TimeTier totalTime;
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
    @SerializedName("original_video_url")
    @Expose
    private String videoUrl;
    private int ingredientsOtherThanSearched;
    private String secondaryTitle;

    public Recipe(int id, String title, String thumbnailURL, TimeTier timeTier,
                  Ingredients[] ingredients, Nutrition nutrition,
                  int numberOfServings, Instruction[] instructions, String videoUrl) {
        this.id = id;
        this.title = title;
        this.thumbnailURL = thumbnailURL;
        this.totalTime = timeTier;
        this.nutrition = nutrition;
        this.ingredients = ingredients;
        this.numberOfServings = numberOfServings;
        this.instructions = instructions;
        this.videoUrl = videoUrl;
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

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    /**
     * Finds the number of ingredients that do not match the searched key words
     * @param ingredients ingredients searched for
     */
    public void setIngredientsOtherThanSearched(ArrayList<String> ingredients) {
        Recipe r = this;
        if (r.ingredients == null || r.ingredients[0].getIngredients() == null) {
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
        for (Pair<String, String> ingredientsAndDescription : ingredientsAndDescriptions) {
            for (String ingredient :
                    ingredients) {
                if (ingredientsAndDescription.first.contains(ingredient))
                    matchingIngredients++;
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
        return r.getIngredientsOtherThanSearched();
    }
    /**
     * Sort a given list of recipes by the least amount of ingredients other than searched for.
     * @param recipes list of recipes to filter
     * @param ingredients list of ingredients that were searched for
     * @return sorted list of recipes
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Recipe> sortByLeastAmountOfOtherIngredients(ArrayList<Recipe> recipes, ArrayList<String> ingredients){
        if (recipes == null || recipes.size() < 2)
            return recipes;
        recipes.sort(Comparator.comparingInt(r -> getAndSetNumberOfIngredientsOtherThanGiven(r, ingredients)));
        return recipes;
    }

    /**
     * Filters out  recipes that are not complete (without instructions, ingredients)
     * @param recipes list of recipes to filter
     * @return filtered list of correct recipes
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static ArrayList<Recipe> filterOutIncompleteRecipes(ArrayList<Recipe> recipes){
        if (recipes == null || recipes.isEmpty())
            return recipes;
        // get recipes with 1 or more instructions and 0 or more ingredients other than searched
        return recipes.stream()
                .filter(r -> (r.getIngredientsOtherThanSearched() >= 0
                        && r.getInstructions() != null
                        && r.getInstructions().length > 0)).
                        collect(Collectors.toCollection(ArrayList::new));
    }
}
