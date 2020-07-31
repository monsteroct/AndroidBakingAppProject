package com.salab.project.projectbakingrecipe.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModel;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModelFactory;

public class RecipeActivity extends AppCompatActivity  {

    public static final String EXTRA_RECIPE_ID = "selected_recipe_id";
    private static final String TAG = RecipeActivity.class.getSimpleName();

    private int mRecipeId = -1;
    private boolean mTwoPane;

    private FragmentManager mFragmentManager = getSupportFragmentManager();
    // single Fragment instance for each class, relying on ViewModel to update data inside
    private RecipeDetailFragment recipeDetailFragment;
    private RecipeStepFragment recipeStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);

        if (findViewById(R.id.container_recipe_step_detail) != null){
            mTwoPane = true;
            Log.d(TAG, "Recipe detail page in two pane mode");
        }

        Intent receivedIntent = getIntent();
        if (receivedIntent != null && receivedIntent.hasExtra(EXTRA_RECIPE_ID)){
            // get recipe id to display recipe detail
            mRecipeId = receivedIntent.getIntExtra(EXTRA_RECIPE_ID, -1);
        } else {
            Log.d(TAG, "invalid start");
        }

        if (mRecipeId != -1){
            // only create new instances of fragments when fresh activity created (not configuration change)
            initFragments();
        }
        RecipeDetailSharedViewModelFactory factory = new RecipeDetailSharedViewModelFactory(this.getApplication(), mRecipeId);
        RecipeDetailSharedViewModel viewModel = new ViewModelProvider(this, factory).get(RecipeDetailSharedViewModel.class);

        viewModel.getmSelectedRecipeStep().observe(this, recipeStep -> {
            // get notifies when recipeStep changed (= StepClick -> change recipe -> activity do action)
            Log.d(TAG, "Observer recipe step changed");
            int containerId;

            if (mTwoPane){
                containerId = R.id.container_recipe_step_detail;
            } else {
                containerId = R.id.container_recipe_detail;
            }

            mFragmentManager.beginTransaction()
                    .replace(containerId, recipeStepFragment)
                    .commit();
        });
    }

    private void initFragments() {
        // Create corresponding fragments to this recipe
        recipeDetailFragment = RecipeDetailFragment.newInstance(mRecipeId);
        recipeStepFragment = RecipeStepFragment.newInstance(mRecipeId, 1);

        // one pane mode all share the same container
        mFragmentManager.beginTransaction()
                .add(R.id.container_recipe_detail, recipeDetailFragment)
                .commit();

        if (mTwoPane) {
            // two pane mode has another container.
            mFragmentManager.beginTransaction()
                    .add(R.id.container_recipe_step_detail, recipeStepFragment)
                    .commit();
        }

    }
}