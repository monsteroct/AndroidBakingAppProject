package com.salab.project.projectbakingrecipe;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.Model.Recipe;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeListBinding;

import java.util.ArrayList;
import java.util.List;

import static com.salab.project.projectbakingrecipe.RecipeActivity.EXTRA_RECIPE_ID;

/**
 * Fragment to display list of recipes
 */
public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeItemClickListener{

    private static final String TAG = RecipeListFragment.class.getSimpleName();

    FragmentRecipeListBinding mBiding;
    RecipeListAdapter mAdapter;

    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout with ViewBiding
        mBiding = FragmentRecipeListBinding.inflate(inflater, container, false);

        initRecyclerView();

        return mBiding.getRoot();

    }

    public List<Recipe> createDummyDateSet(){
        //create dummy recipe list, only for build purpose
        List<Recipe> recipeList = new ArrayList<>();

        Recipe dummyRecipe = new Recipe();
        dummyRecipe.setName("Nutella Pie");
        dummyRecipe.setServings(8);
        dummyRecipe.setImage("");

        recipeList.add(dummyRecipe);

        return recipeList;
    }

    public void initRecyclerView(){
        // show loading progress bar
        showProgressBar(true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mBiding.rvRecipeList.setLayoutManager(manager);

        mBiding.rvRecipeList.setHasFixedSize(true);

        mAdapter = new RecipeListAdapter(createDummyDateSet(), this);
        mBiding.rvRecipeList.setAdapter(mAdapter);

        // hide loading progress bar
        showProgressBar(false);
    }

    private void showProgressBar(boolean isShowing) {
        if (isShowing) {
            mBiding.rvRecipeList.setVisibility(View.INVISIBLE);
            mBiding.progressRecipeList.setVisibility(View.VISIBLE);
        } else {
            mBiding.rvRecipeList.setVisibility(View.VISIBLE);
            mBiding.progressRecipeList.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onRecipeClick(int recipeId) {
        Intent recipeIntent = new Intent(getContext(), RecipeActivity.class);
        recipeIntent.putExtra(EXTRA_RECIPE_ID, recipeId);
        startActivity(recipeIntent);
    }
}