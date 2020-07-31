package com.salab.project.projectbakingrecipe.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.models.RecipeStep;
import com.salab.project.projectbakingrecipe.repository.RecipeRepository;

import java.util.List;

/**
 * Shared ViewModel holds selected recipe and its list of ingredients and steps for
 * RecipeDetailFragment, RecipeStepFragment
 */
public class RecipeDetailSharedViewModel extends AndroidViewModel {

    private static final String TAG = RecipeDetailSharedViewModel.class.getSimpleName();
    private RecipeRepository mRepository;

    private int mSelectedRecipeId;
    private int mSelectedRecipeStepIndex;

    private LiveData<Recipe> mSelectedRecipe;
    private MutableLiveData<RecipeStep> mSelectedRecipeStep;


    public RecipeDetailSharedViewModel(Application application, int recipeId){
        super(application);
        mRepository = new RecipeRepository(application.getApplicationContext());

        mSelectedRecipeId = recipeId;
        mSelectedRecipe = mRepository.getRecipeById(mSelectedRecipeId);
        // TODO : fix the potential issue when there is no steps
        mSelectedRecipeStepIndex = 0;
        mSelectedRecipeStep = new MutableLiveData<RecipeStep>();
    }

    public LiveData<Recipe> getmSelectedRecipe() {
        return mSelectedRecipe;
    }

    public void getRecipeStepById(int StepId){
        List<RecipeStep> recipeStepList = mSelectedRecipe.getValue().getSteps();
        RecipeStep recipeStep;
        for (int i = 0; i < recipeStepList.size(); i++){
            recipeStep = recipeStepList.get(i);
            if (recipeStep.getId() == StepId){
                mSelectedRecipeStepIndex = i;
                mSelectedRecipeStep.setValue(recipeStep);
                Log.d(TAG, "Update RecipeStep index to " + mSelectedRecipeStepIndex);
            }
        }
    }

    public void getNextRecipeStep(){
        List<RecipeStep> recipeStepList = mSelectedRecipe.getValue().getSteps();
        if (mSelectedRecipeStepIndex < recipeStepList.size() - 1){
            mSelectedRecipeStepIndex += 1;
            mSelectedRecipeStep.setValue(recipeStepList.get(mSelectedRecipeStepIndex));
            Log.d(TAG, "Update RecipeStep index to " + mSelectedRecipeStepIndex);
        }
    }

    public void getPreviousRecipeStep(){
        List<RecipeStep> recipeStepList = mSelectedRecipe.getValue().getSteps();
        if (mSelectedRecipeStepIndex > 0){
            mSelectedRecipeStepIndex -= 1;
            mSelectedRecipeStep.setValue(recipeStepList.get(mSelectedRecipeStepIndex));
            Log.d(TAG, "Update RecipeStep index to " + mSelectedRecipeStepIndex);
        }
    }

    public MutableLiveData<RecipeStep> getmSelectedRecipeStep() {
        return mSelectedRecipeStep;
    }
}
