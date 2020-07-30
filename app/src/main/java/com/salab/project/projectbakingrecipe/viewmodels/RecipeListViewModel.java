package com.salab.project.projectbakingrecipe.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.salab.project.projectbakingrecipe.Model.Recipe;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

    private LiveData<List<Recipe>> mRecipeList;

    public RecipeListViewModel(){
        // initialize data
        mRecipeList = new MutableLiveData<>();
    }


    public LiveData<List<Recipe>> getmRecipeList() {
        return mRecipeList;
    }
}
