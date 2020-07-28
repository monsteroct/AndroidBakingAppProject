package com.salab.project.projectbakingrecipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.salab.project.projectbakingrecipe.Model.Recipe;

import java.util.List;

public class RecipeListAdapter extends RecyclerView.Adapter<RecipeListAdapter.RecipeListViewHolder> {

    private List<Recipe> mRecipeList;

    public RecipeListAdapter(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
    }

    @NonNull
    @Override
    public RecipeListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recipe_item, parent, false);
        return new RecipeListViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeListViewHolder holder, int position) {

        String recipeName = mRecipeList.get(position).getName();
        String recipeImage = mRecipeList.get(position).getImage();

        holder.mRecipeNameTextView.setText(recipeName);
        if (recipeImage != null){
            //no images for now. Reserve space
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipeList == null) {
            return 0;
        } else {
            return mRecipeList.size();
        }
    }

    class RecipeListViewHolder extends RecyclerView.ViewHolder{
        protected ImageView mRecipeImageImageView;
        protected TextView mRecipeNameTextView;

        public RecipeListViewHolder(@NonNull View itemView) {
            super(itemView);
            mRecipeImageImageView = itemView.findViewById(R.id.iv_item_recipe_image);
            mRecipeNameTextView = itemView.findViewById(R.id.tv_item_recipe_name);
        }
    }

}
