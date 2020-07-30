package com.salab.project.projectbakingrecipe.viewmodels;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.models.RecipeStep;

import java.util.List;

public class RecipeDetailSharedViewModel extends ViewModel {

    private MutableLiveData<Integer> mRecipeId = new MutableLiveData<>();
    private MutableLiveData<Integer> mRecipeStepId = new MutableLiveData<>();

    private LiveData<Recipe> mSelectedRecipe =
            Transformations.switchMap(mRecipeId, new Function<Integer, LiveData<Recipe>>() {
                @Override
                public LiveData<Recipe> apply(Integer input) {
                    return null;
                }
            });

        private LiveData<List<Ingredient>> mIngredientList =
                Transformations.switchMap(mRecipeId, new Function<Integer, LiveData<List<Ingredient>>>() {
                    @Override
                    public LiveData<List<Ingredient>> apply(Integer input) {
                        return null;
                    }
                });

        private LiveData<List<RecipeStep>> mRecipeStepList =
                Transformations.switchMap(mRecipeId, new Function<Integer, LiveData<List<RecipeStep>>>() {

                    @Override
                    public LiveData<List<RecipeStep>> apply(Integer input) {
                        return null;
                    }
                });;

        public RecipeDetailSharedViewModel(int recipeId){
            mRecipeId.setValue(recipeId);
            mRecipeStepId.setValue(1);
        }

        public MutableLiveData<Integer> getmRecipeId() {
            return mRecipeId;
        }

        public void setmRecipeId(MutableLiveData<Integer> mRecipeId) {
            this.mRecipeId = mRecipeId;
        }

        public MutableLiveData<Integer> getmRecipeStepId() {
            return mRecipeStepId;
        }

        public void setmRecipeStepId(MutableLiveData<Integer> mRecipeStepId) {
            this.mRecipeStepId = mRecipeStepId;
        }
    }
