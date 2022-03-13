package com.example.myapplication;

import static com.example.myapplication.models.Recipe.getAndSetNumberOfIngredientsOtherThanGiven;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.API.RecipeModel;
import com.example.myapplication.API.RecipeRVAdapter;
import com.example.myapplication.API.RecyclerItemClickListener;
import com.example.myapplication.API.tastyAPIHandler;
import com.example.myapplication.models.Recipe;
import com.example.myapplication.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main activity setup - feed and recipe fetching
 */
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
        initListener();

        ingredients.add("spinach");
        ingredients.add("tomato");
        getRecipes(ingredients);
    }

    /**
     * Listener for Recipe card in RecyclerView
     */
    private void initListener(){
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        System.out.println("CLICK *******************************************");
                        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                        Recipe recipe = recipes.get(position);
                        // pass the recipe data
                        intent.putExtra("ingredients", recipe.getIngredients());
                        intent.putExtra("instructions", recipe.getInstructions());
                        intent.putExtra("title", recipe.getTitle());
                        intent.putExtra("secondaryTitle", recipe.getSecondaryTitle());
                        intent.putExtra("imgUrl", recipe.getThumbnailURL());

                        startActivity(intent);
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do whatever
                    }
                }));
    }

    /**
     * Sets up options menu and search option
     * @param menu menu object
     * @return false if set up fails, true if it succeeds.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.actionSearch).getActionView();
        MenuItem searchMenuItem = menu.findItem(R.id.actionSearch);

        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("spinach, tomato...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(query.length() > 2){
                    ArrayList<String> search;
                    // get ingredients as a list
                    search = new ArrayList<>(Arrays.asList(query
                            .split(",")));
                    // get rid of unnecessary whitespace
                    search.forEach(i -> i.trim());
                    getRecipes(search);

                }
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        searchMenuItem.getIcon().setVisible(false, false);
        return true;
    }

    /**
     * Fetches recipes from the API, refreshes the feed view
     * @param ingredients additional parameters for the query (key words, ingredients to search for)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getRecipes(ArrayList<String> ingredients){
        recipes.clear();

        String apiKey = Constants.API_KEY;
        String baseUrl = Constants.BASE_URL ;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tastyAPIHandler tastyAPIHandler = retrofit.create(tastyAPIHandler.class);
        Call<RecipeModel> call;

        // get ingredients into the url
        String parameters = "";
        if (ingredients != null && ingredients.size() > 0){
            parameters = ingredients.stream()
                    .map(ingredient -> ingredient + "%20")
                    .reduce("", String::concat);
            parameters = parameters.substring(0, parameters.length() - 3);
            call = tastyAPIHandler.getRecipesWithIngredients(0, 40, apiKey, parameters);
        }else
            call = tastyAPIHandler.getAllRecipes(0, 40, apiKey);

        call.enqueue(new Callback<RecipeModel>() {
            @Override
            public void onResponse(Call<RecipeModel> call, Response<RecipeModel> response) {
                RecipeModel recipeModel = response.body();
                if (response.isSuccessful() && !response.body().getResults().isEmpty()){
                    if (!recipes.isEmpty()){
                        recipes.clear();
                    }
                    recipes = sortByLeastAmountOfOtherIngredients(recipeModel.getResults(), ingredients);
                    recipes = filterOutIncompleteRecipes(recipes);
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

    /**
     * Sort a given list of recipes by the least amount of ingredients other than searched for.
     * @param recipes list of recipes to filter
     * @param ingredients list of ingredients that were searched for
     * @return sorted list of recipes
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private ArrayList<Recipe> sortByLeastAmountOfOtherIngredients(ArrayList<Recipe> recipes, ArrayList<String> ingredients){
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
    private ArrayList<Recipe> filterOutIncompleteRecipes(ArrayList<Recipe> recipes){
        if (recipes == null || recipes.isEmpty())
            return recipes;
        // get recipes with 1 or more instructions and 0 or more ingredients other than searched
        return new ArrayList<Recipe>(recipes
                .stream()
                .filter(r -> (r.getIngredientsOtherThanSearched() >= 0
                        && r.getInstructions() != null
                        && r.getInstructions().length > 0))
                .collect(Collectors.toList()));
    }
}