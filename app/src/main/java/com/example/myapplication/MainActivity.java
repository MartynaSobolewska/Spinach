package com.example.myapplication;

import static com.example.myapplication.models.Recipe.filterOutIncompleteRecipes;
import static com.example.myapplication.models.Recipe.sortByLeastAmountOfOtherIngredients;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.API.ResponseModel;
import com.example.myapplication.API.RecipeRVAdapter;
import com.example.myapplication.API.RecyclerItemClickListener;
import com.example.myapplication.API.tastyAPIHandler;
import com.example.myapplication.models.Recipe;
import com.example.myapplication.sharedPreferences.SharedPref;
import com.example.myapplication.util.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Main activity setup - feed and recipe fetching
 */
public class MainActivity extends AppCompatActivity {

    // error
    private RelativeLayout errorLayout;
    private TextView errorTitle, errorMessage;
    private Button refreshOnErrorBtn;

    // shared preferences
    private SharedPref preferences;

    // feed recyclerview
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    // store the data for recyclerview
    private ArrayList<Recipe> recipes = new ArrayList<>();
    // list of ingredients to search for
    private final ArrayList<String> ingredients = new ArrayList<>();
    private RecipeRVAdapter recipeRVAdapter;

    // due to Tasty API requests
    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // error
        errorLayout = findViewById(R.id.errorLayout);
        errorTitle = findViewById(R.id.errorTitle);
        errorMessage = findViewById(R.id.errorMessage);
        refreshOnErrorBtn = findViewById(R.id.errorButton);

        // preferences
        preferences = new SharedPref(this);

        // fetch progress bar
        progressBar = findViewById(R.id.progressLoadRecipes);

        // main toolbar
        // main toolbar
        Toolbar mToolbar = findViewById(R.id.main_toolbar);
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
                progressBar.setVisibility(View.VISIBLE);
                recipes.clear();
                recipeRVAdapter.notifyDataSetChanged();
                if(query.length() > 2){
                    ArrayList<String> search;
                    // get ingredients as a list
                    search = new ArrayList<>(Arrays.asList(query
                            .split(",")));
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
     * Handles the option item in top menu
     * @param item the item clicked
     * @return true if succeeds menu item action
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.actionSettings) {
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    /**
     * Listener for Recipe card in RecyclerView
     */
    private void initListener(){
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, recyclerView ,new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        ImageView imageView = view.findViewById(R.id.recipeThumbnail);

                        Intent intent = new Intent(MainActivity.this, RecipeDetailsActivity.class);
                        Recipe recipe = recipes.get(position);
                        // pass the recipe data
                        intent.putExtra("ingredients", recipe.getIngredients());
                        intent.putExtra("instructions", recipe.getInstructions());
                        intent.putExtra("title", recipe.getTitle());
                        intent.putExtra("secondaryTitle", recipe.getSecondaryTitle());
                        intent.putExtra("imgUrl", recipe.getThumbnailURL());
                        intent.putExtra("videoUrl", recipe.getVideoUrl());

                        //image transition
                        Pair<View, String> pair =
                                Pair.create(imageView, ViewCompat.getTransitionName(imageView));
                        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                MainActivity.this,
                                pair
                        );
                        startActivity(intent, optionsCompat.toBundle());
                    }

                    @Override public void onLongItemClick(View view, int position) {
                        // do not do anything
                    }
                }));
    }

    /**
     * Fetches recipes from the API, refreshes the feed view
     * @param ingredients additional parameters for the query (key words, ingredients to search for)
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getRecipes(ArrayList<String> ingredients){
        errorLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);


        String API_KEY = Constants.API_KEY;
        String BASE_URL = Constants.BASE_URL ;
        int SIZE = Constants.SIZE;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tastyAPIHandler tastyAPIHandler = retrofit.create(tastyAPIHandler.class);
        Call<ResponseModel> call;

        // get ingredients into the url
        String parameters;
        if (ingredients != null && ingredients.size() > 0){
            parameters = ingredients.stream()
                    .map(ingredient -> ingredient + "%20")
                    .reduce("", String::concat);
            parameters = parameters.substring(0, parameters.length() - 3);
            if (preferences.getVegetarianState()){
                call = tastyAPIHandler.getRecipesWithIngredientsAndTags(0, SIZE, API_KEY,
                        parameters, "vegetarian");
            }else if (preferences.getVeganState()){
                call = tastyAPIHandler.getRecipesWithIngredientsAndTags(0, SIZE, API_KEY,
                        parameters, "vegan");
            }else
                call = tastyAPIHandler.getRecipesWithIngredients(0, SIZE, API_KEY, parameters);
        }else{
            if (preferences.getVegetarianState()){
                call = tastyAPIHandler.getAllRecipesWithTags(0, SIZE,
                        API_KEY, "vegetarian");
            }
            else if(preferences.getVeganState()){
                call = tastyAPIHandler.getAllRecipesWithTags(0, SIZE,
                        API_KEY, "vegan");
            }else
                call = tastyAPIHandler.getAllRecipes(0, SIZE, API_KEY);
        }

        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                ResponseModel responseModel = response.body();
                if (response.isSuccessful() &&
                        !Objects.requireNonNull(response.body()).getResults().isEmpty()) {
                    if (!recipes.isEmpty()) {
                        recipes.clear();
                    }
                    assert responseModel != null;
                    recipes = sortByLeastAmountOfOtherIngredients(responseModel.getResults(), ingredients);
                    recipes = filterOutIncompleteRecipes(recipes);
                    // sometimes there are results but they are only incomplete recipes
                    if (recipes.isEmpty()) {
                        showErrorLayout("No results",
                                "Search for ingredients separated with commas.", ingredients);
                    } else {
                        recipeRVAdapter = new RecipeRVAdapter(MainActivity.this, recipes);
                        recyclerView.setAdapter(recipeRVAdapter);
                        recipeRVAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.INVISIBLE);
                        errorLayout.setVisibility(View.INVISIBLE);
                    }
                } else {
                    String errorTitle, errorMessage;
                    switch (response.code()) {
                        case 404:
                            errorTitle = "404 not found";
                            errorMessage = "Try again later";
                            break;
                        case 500:
                            errorTitle = "500 server down";
                            errorMessage = "Try again later";
                            break;
                        default:
                            errorTitle = "No Results";
                            errorMessage = "Search for ingredients separated with commas.";
                    }
                    showErrorLayout(errorTitle, errorMessage, ingredients);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                showErrorLayout("Ooops...", "Network failure," +
                                " please check your network connection."
                        , ingredients);
            }
        });
    }

    /**
     * Displays an error layout in case of an API error
     * @param title Title of the error
     * @param message message of the error
     * @param ingredients that were searched for to pass if refresh button hit
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showErrorLayout(String title, String message, ArrayList<String> ingredients){
        if (errorLayout.getVisibility() == View.GONE)
            errorLayout.setVisibility(View.VISIBLE);

        errorTitle.setText(title);
        errorMessage.setText(message);

        refreshOnErrorBtn.setOnClickListener(v -> getRecipes(ingredients));
    }
}