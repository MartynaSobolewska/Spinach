package com.example.myapplication;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.models.Ingredients;
import com.example.myapplication.models.Instruction;
import com.example.myapplication.util.Utils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class RecipeDetailsActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener{

    private ImageView imgView;
    private TextView appbarTitle, title, secondaryTitle;
    private ListView ingredients, instructions;
    private boolean toolbarIsHidden = false;
    private AppBarLayout appBarLayout;
    private Toolbar toolbar;
    // data to fill out the fields
    private Ingredients[] ingredientsData;
    private Instruction[] instructionsData;
    private String titleData, secondaryTitleData, imgUrl, videoUrl;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_detail);

        // toolbar setup
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final CollapsingToolbarLayout collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");

        appBarLayout = findViewById(R.id.appbar);
        appBarLayout.addOnOffsetChangedListener(this);

        // find all other views
        imgView = findViewById(R.id.backdrop);
        appbarTitle = findViewById(R.id.appbarTitle);
        title = findViewById(R.id.title);
        secondaryTitle = findViewById(R.id.secondaryTitle);
        ingredients = findViewById(R.id.ingredientsList);
        instructions = findViewById(R.id.instructionsList);

        //get intent data
        Intent intent = getIntent();
        imgUrl = intent.getStringExtra("imgUrl");
        videoUrl = intent.getStringExtra("videoUrl");
        titleData = intent.getStringExtra("title");
        secondaryTitleData = intent.getStringExtra("secondaryTitle");
        instructionsData = (Instruction[]) intent.getSerializableExtra("instructions");
        ingredientsData = (Ingredients[]) intent.getSerializableExtra("ingredients");

        // load an img
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.error(Utils.getRandomDrawbleColor());

        Glide.with(this)
                .load(imgUrl)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(imgView);

        titleData = titleData.replace("-", " ");
        appbarTitle.setText(titleData);
        title.setText(titleData);
        secondaryTitle.setText(secondaryTitleData);

        // populate list views
        //ingredients
        Pair<String,String>[] formattedIngredients = ingredientsData[0].getIngredients();
        String[] ingredientStringArray = new String[formattedIngredients.length];
        for (int i = 0; i < ingredientStringArray.length; i++) {
            ingredientStringArray[i] = formattedIngredients[i].first + " " + formattedIngredients[i].second;
        }
        ArrayAdapter ingredientAdapter =
                new ArrayAdapter<String>(this, R.layout.activity_listview, ingredientStringArray);
        ingredients.setAdapter(ingredientAdapter);

        // instructions
        String[] instructionsStringArray = new String[instructionsData.length];
        for (int i = 0; i < instructionsStringArray.length; i++) {
            instructionsStringArray[i] = i+1 + ". " + instructionsData[i].getText();
        }
        ArrayAdapter instructionsAdapter =
                new ArrayAdapter<String>(this, R.layout.activity_listview, instructionsStringArray);
        instructions.setAdapter(instructionsAdapter);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

        if (percentage == 1f && toolbarIsHidden){
            appbarTitle.setVisibility(View.VISIBLE);
            toolbarIsHidden = !toolbarIsHidden;
        } else if(percentage < 1f && !toolbarIsHidden){
            appbarTitle.setVisibility(View.GONE);
            toolbarIsHidden = !toolbarIsHidden;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_recipe, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.seeVideo){
            // check if there is video content
            if (videoUrl != null && !videoUrl.isEmpty()){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(videoUrl));
                // if other browser on the phone, it will ask user which one to use
                startActivity(Intent.createChooser(i, "View in: "));
                return true;
            }else {
                Toast.makeText(this,
                        "No video to show for this recipe.", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }
}
