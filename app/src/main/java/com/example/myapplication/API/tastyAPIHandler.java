package com.example.myapplication.API;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface tastyAPIHandler {
    @GET("recipes/list?")
    Call<ResponseModel> getRecipesWithIngredients(
            @Query("from") int from,
            @Query("size") int size,
            @Query("rapidapi-key") String key,
            @Query("q") String ingredients
    );
    @GET("recipes/list?")
    Call<ResponseModel> getRecipesWithIngredientsAndTags(
            @Query("from") int from,
            @Query("size") int size,
            @Query("rapidapi-key") String key,
            @Query("q") String ingredients,
            @Query("tags") String tags
    );
    @GET("recipes/list?")
    Call<ResponseModel> getAllRecipes(
            @Query("from") int from,
            @Query("size") int size,
            @Query("rapidapi-key") String key
    );
    @GET("recipes/list?")
    Call<ResponseModel> getAllRecipesWithTags(
            @Query("from") int from,
            @Query("size") int size,
            @Query("rapidapi-key") String key,
            @Query("tags") String tags
    );

}
