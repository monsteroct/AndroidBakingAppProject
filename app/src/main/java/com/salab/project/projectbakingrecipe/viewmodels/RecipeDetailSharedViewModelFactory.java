package com.salab.project.projectbakingrecipe.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RecipeDetailSharedViewModelFactory extends ViewModelProvider.AndroidViewModelFactory {

    private Application mApplication;
    private int mRecipeId;

    public RecipeDetailSharedViewModelFactory(Application application, int recipeId){
        super(application);
        mApplication = application;
        mRecipeId = recipeId;
    }

    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new RecipeDetailSharedViewModel(mApplication, mRecipeId);
    }
}
