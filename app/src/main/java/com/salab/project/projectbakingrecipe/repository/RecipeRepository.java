package com.salab.project.projectbakingrecipe.repository;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.salab.project.projectbakingrecipe.ExecutorStore;
import com.salab.project.projectbakingrecipe.database.AppDatabase;
import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.models.RecipeStep;
import com.salab.project.projectbakingrecipe.utils.NetWorkUtil;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Repository interacting with view models to fetch data
 */
public class RecipeRepository {

    private static final String TAG = RecipeRepository.class.getSimpleName();
    private AppDatabase mAppDatabase;
    private ExecutorStore mExecutorStore;

    public RecipeRepository(Context context) {
        mAppDatabase = AppDatabase.getsInstance(context);
        mExecutorStore = ExecutorStore.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        updateFromWebIfEmpty();
        return mAppDatabase.recipeDao().queryAllRecipes();
    }

    public LiveData<Recipe> getRecipeById(int recipeId){
        updateFromWebIfEmpty();
        return mAppDatabase.recipeDao().queryRecipeById(recipeId);
    }

    public Recipe getRecipeByIdRaw(int recipeId){
        // widget request raw data instead of wrapped in LiveData
        return mAppDatabase.recipeDao().queryRecipeByIdRaw(recipeId);
    }

    private void updateFromWebIfEmpty(){
        // retrieve data from web service only when table is empty
        // both db query and network connection need to be run on background thread
        mExecutorStore.getNetworkIO().execute(() -> {
            if (mAppDatabase.recipeDao().getRowCount() == 0){
                fetchDataFromWeb();
            }
        });
    }

    public void fetchDataFromWeb(){
        Call<List<Recipe>> call = NetWorkUtil.createBakingDateService().getRecipes();
        Response<List<Recipe>> response = null;
        try {
            response = call.execute();
        } catch (IOException e){
            Log.d(TAG, "network fetch failed: " + e.getMessage());
        }
        if (response != null && response.isSuccessful() && response.body() != null) {
            // insert data into local storage (single source of truth principle)
            List<Recipe> recipeList = response.body();
            updateRecipeDatabase(recipeList);
            Log.d(TAG, "local database update completed");
        }
    }

    private void updateRecipeDatabase(List<Recipe> recipeList){
        mAppDatabase.recipeDao().insertRecipes(recipeList);
    }

}
