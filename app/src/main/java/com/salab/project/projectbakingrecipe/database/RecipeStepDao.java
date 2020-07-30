package com.salab.project.projectbakingrecipe.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.salab.project.projectbakingrecipe.models.RecipeStep;

import java.util.List;
@Dao
public interface RecipeStepDao {

    @Query("SELECT * FROM recipe_step WHERE recipeId = :recipeId")
    LiveData<List<RecipeStep>> queryRecipeStepsById(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipeSteps(RecipeStep... recipeStep);

}
