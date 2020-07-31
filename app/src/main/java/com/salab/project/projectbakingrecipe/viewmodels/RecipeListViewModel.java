package com.salab.project.projectbakingrecipe.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salab.project.projectbakingrecipe.database.AppDatabase;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.repository.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends AndroidViewModel {

    private LiveData<List<Recipe>> mRecipeList;
    private RecipeRepository mRepository;

    public RecipeListViewModel(Application application){
        super(application);
        // initialize data
        mRepository = new RecipeRepository(application);
        mRecipeList = mRepository.getRecipes();
    }

    public LiveData<List<Recipe>> getmRecipeList() {
        return mRecipeList;
    }
}
