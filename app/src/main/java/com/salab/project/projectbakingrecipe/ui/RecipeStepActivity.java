package com.salab.project.projectbakingrecipe.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.salab.project.projectbakingrecipe.R;

public class RecipeStepActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "selected_recipe_id";
    public static final String EXTRA_STEP_ID = "selected_step_id";

    private static final String TAG = RecipeStepActivity.class.getSimpleName();
    private int mRecipeId = -1;
    private int mStepId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        Intent receivedIntent = getIntent();
        if (receivedIntent != null) {
            // get recipe id to display recipe detail
            if (receivedIntent.hasExtra(EXTRA_RECIPE_ID)) {
                mRecipeId = receivedIntent.getIntExtra(EXTRA_RECIPE_ID, -1);
            }
            if (receivedIntent.hasExtra(EXTRA_STEP_ID)) {
                mStepId = receivedIntent.getIntExtra(EXTRA_STEP_ID, -1);
            }

            if (mRecipeId != -1 && mStepId != -1) {
                initFragments();
            } else {
                Log.d(TAG, "invalid recipe and step id");
            }

        }
    }

    private void initFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Create corresponding fragments to this recipe
        RecipeStepFragment recipeStepFragment = RecipeStepFragment.newInstance(mRecipeId, mStepId);
        // fill into containers
        fragmentManager.beginTransaction()
                .replace(R.id.container_step_detail, recipeStepFragment)
                .commit();
    }
}