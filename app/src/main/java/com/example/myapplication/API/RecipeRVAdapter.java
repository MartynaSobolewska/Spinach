package com.example.myapplication.API;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.example.myapplication.R;
import com.example.myapplication.models.Recipe;
import com.example.myapplication.util.Utils;

import java.util.ArrayList;

/**
 *  view adapter to display recipe cards
 */
public class RecipeRVAdapter extends RecyclerView.Adapter<RecipeRVAdapter.RecipeRVHolder> {
    ArrayList<Recipe> recipes;
    Context context;

    public RecipeRVAdapter(Context ct, ArrayList<Recipe> recipes){
        context = ct;
        this.recipes = recipes;
    }
    @NonNull
    @Override
    public RecipeRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_card, parent, false);
        return new RecipeRVHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeRVHolder holder, int position) {
        final RecipeRVHolder Viewholder = holder;

        // for image loading
        RequestOptions requestOptions = new RequestOptions();
        // default image - a color
        requestOptions.placeholder(Utils.getRandomDrawbleColor());
        requestOptions.error(Utils.getRandomDrawbleColor());
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.centerCrop();

        // load image
        Glide.with(context)
                .load(recipes.get(position).getThumbnailURL())
                .apply(requestOptions)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.image);

        holder.title.setText(recipes.get(position).getTitle().replaceAll("-", " "));
        int servings = recipes.get(position).getNumberOfServings();
        int otherIng = recipes.get(position).getIngredientsOtherThanSearched();
        String bottomText = "";
        if (servings > 1)
            bottomText += (servings + " servings");
        else
            bottomText += (servings + " serving");
        if (otherIng == 1)
            bottomText += (", 1 other ingredient.");
        else if (otherIng == 0)
            System.out.println("");
        else
            bottomText += (", " + otherIng + " other ingredients.");
        recipes.get(position).setSecondaryTitle(bottomText);
        holder.portions.setText(bottomText);
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeRVHolder extends RecyclerView.ViewHolder{
        private TextView title, portions;
        private ImageView image;

        public RecipeRVHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            portions = itemView.findViewById(R.id.portions);
            image = itemView.findViewById(R.id.recipeThumbnail);
        }
    }
}
