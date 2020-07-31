package com.salab.project.projectbakingrecipe.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.salab.project.projectbakingrecipe.models.Recipe;

import java.util.List;
@Dao
public interface RecipeDao {

    @Query("SELECT count(*) FROM recipe")
    int getRowCount();

    @Query("SELECT * FROM recipe")
    LiveData<List<Recipe>> queryAllRecipes();

    @Query("SELECT * FROM recipe WHERE id = :recipeId")
    LiveData<Recipe> queryRecipeById(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRecipes(List<Recipe> recipeList);

}
