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
import com.bumptech.glide.module.AppGlideModule;
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
    private OnItemClickListener onItemClickListener;

    public RecipeRVAdapter(Context ct, ArrayList<Recipe> recipes){
        context = ct;
        this.recipes = recipes;
    }
    @NonNull
    @Override
    public RecipeRVHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_card, parent, false);
        return new RecipeRVHolder(view, onItemClickListener);
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

        holder.title.setText(recipes.get(position).getTitle());
        holder.description.setText(recipes.get(position).getLanguage());
    }

    @Override
    public int getItemCount() {
        return recipes.size();
    }

    public static class RecipeRVHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView title, description;
        private ImageView image;
        private ProgressBar progressBar;
        private OnItemClickListener onItemClickListener;

        public RecipeRVHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            image = itemView.findViewById(R.id.recipeThumbnail);
            progressBar = itemView.findViewById(R.id.progressLoadRecipes);

            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
