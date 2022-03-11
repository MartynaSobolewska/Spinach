package com.example.myapplication.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface tastyAPIHandler {
    @GET
    Call<RecipeModel> getRecipesWithIngredients(@Url String url);
}
