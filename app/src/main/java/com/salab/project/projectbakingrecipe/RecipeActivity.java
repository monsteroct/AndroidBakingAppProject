package com.salab.project.projectbakingrecipe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class RecipeActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "selected_recipe_id";
    private static final String TAG = RecipeActivity.class.getSimpleName();

    private int recipeId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra(EXTRA_RECIPE_ID)){
            // get recipe id to display recipe detail
            recipeId = receivedIntent.getIntExtra(EXTRA_RECIPE_ID, -1);
        } else {
            Log.d(TAG, "invalid start");
        }

        if (recipeId != -1){
            initFragments();
        } else {
            Log.d(TAG, "invalid recipe id");
        }

    }

    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Create corresponding fragments to this recipe
        RecipeDetailFragment recipeDetailFragment = RecipeDetailFragment.newInstance(recipeId);
        RecipeStepListFragment recipeStepListFragment = RecipeStepListFragment.newInstance(recipeId);
        // fill into containers
        fragmentManager.beginTransaction()
                .replace(R.id.container_recipe_detail, recipeDetailFragment)
                .replace(R.id.container_step_list, recipeStepListFragment)
                .commit();
    }
}