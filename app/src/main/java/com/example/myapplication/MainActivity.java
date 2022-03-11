package com.example.myapplication;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.widget.Toast;

import com.example.myapplication.API.RecipeModel;
import com.example.myapplication.API.RecipeRVAdapter;
import com.example.myapplication.API.tastyAPIHandler;
import com.example.myapplication.models.Recipe;
import com.example.myapplication.util.Constants;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    // main toolbar
    private Toolbar mToolbar;
    // feed recyclerview
    RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    // store the data for recyclerview
    ArrayList<Recipe> recipes = new ArrayList<>();
    // list of ingredients to search for
    ArrayList<String> ingredients = new ArrayList<>();
    RecipeRVAdapter recipeRVAdapter;

    // due to Tasty API requests
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // main toolbar
        mToolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(mToolbar);

        // feed recyclerview
        recyclerView = findViewById(R.id.recipeView);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recipeRVAdapter = new RecipeRVAdapter(this, recipes);
        recyclerView.setAdapter(recipeRVAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);

        ingredients.add("onion");
        ingredients.add("tomato");
        getRecipes(ingredients);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getRecipes(ArrayList<String> ingredients){
        recipes.clear();

        String baseUrl = Constants.BASE_URL;
        String listUrl = Constants.LIST_URL + "&rapidapi-key=" + Constants.API_KEY;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tastyAPIHandler tastyAPIHandler = retrofit.create(tastyAPIHandler.class);
        Call<RecipeModel> call;

        // get ingredients into the url
        String parameters = "";
        if (ingredients.size() != 0){
            parameters = "&p=" + ingredients.stream()
                    .map(ingredient -> ingredient.toString() + "%20")
                    .reduce("", String::concat);
            parameters = parameters.substring(0, parameters.length() - 3);
        }
        listUrl = listUrl.concat(parameters);
        System.out.println("URL !!!!!!!!!!!!!!!!!!!!!!!!! : "+  listUrl);
        call = tastyAPIHandler.getRecipesWithIngredients(listUrl);
        call.enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                RecipeModel recipeModel = response.body();
                if (response.isSuccessful() && !response.body().getResults().isEmpty()){
                    if (!recipes.isEmpty()){
                        recipes.clear();
                    }
                    recipes = recipeModel.getResults();
                    recipeRVAdapter = new RecipeRVAdapter(MainActivity.this, recipes);
                    recyclerView.setAdapter(recipeRVAdapter);
                    recipeRVAdapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(MainActivity.this, "No results!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RecipeModel> call, Throwable t) {
                Toast.makeText(MainActivity.this, "API ERROR!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}