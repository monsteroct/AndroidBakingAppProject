package com.salab.project.projectbakingrecipe.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class RecipeDetailSharedViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private int mRecipeId;

    public RecipeDetailSharedViewModelFactory(int recipeId){
        mRecipeId = recipeId;
    }

    @NonNull
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        return (T) new RecipeDetailSharedViewModel(mRecipeId);
    }
}
