package com.example.myapplication.models;

public class Nutrition {
    private int protein;
    private int fat;
    private int calories;
    private int sugar;
    private int carbohydrates;
    private int fiber;

    public Nutrition(int protein, int fat, int calories, int sugar, int carbohydrates, int fiber) {
        this.protein = protein;
        this.fat = fat;
        this.calories = calories;
        this.sugar = sugar;
        this.carbohydrates = carbohydrates;
        this.fiber = fiber;
    }

    public int getProtein() {
        return protein;
    }

    public void setProtein(int protein) {
        this.protein = protein;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getSugar() {
        return sugar;
    }

    public void setSugar(int sugar) {
        this.sugar = sugar;
    }

    public int getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(int carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public int getFiber() {
        return fiber;
    }

    public void setFiber(int fiber) {
        this.fiber = fiber;
    }
}
