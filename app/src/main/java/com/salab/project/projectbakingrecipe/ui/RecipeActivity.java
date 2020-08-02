package com.salab.project.projectbakingrecipe.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModel;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModelFactory;
import com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService;

import static com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService.KEY_WIDGET_TRACING_RECIPE_ID;
import static com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService.WIDGET_RECIPE_SHARED_PREFERENCE;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);

        // change icon if the recipe is the traced one
        int saveRecipeId = getSharedPreferences(WIDGET_RECIPE_SHARED_PREFERENCE, MODE_PRIVATE).getInt(KEY_WIDGET_TRACING_RECIPE_ID, -1);
        if (saveRecipeId != -1 && saveRecipeId == mRecipeId){
            MenuItem item = menu.findItem(R.id.menu_item_recipe_trace_button);
            item.setIcon(R.drawable.ic_baseline_turned_in_24);
            item.setChecked(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_item_recipe_trace_button){
            if (item.isChecked()){
                item.setIcon(R.drawable.ic_baseline_turned_in_not_24);
                item.setChecked(false);
                // ask service to update status in shared preference. Only one recipe allowed -> un-tracing current one means saving nothing
                ShoppingListUpdateService.startChangeRecipeService(this, -1);
            } else {
                item.setIcon(R.drawable.ic_baseline_turned_in_24);
                item.setChecked(true);
                ShoppingListUpdateService.startChangeRecipeService(this, mRecipeId);
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}