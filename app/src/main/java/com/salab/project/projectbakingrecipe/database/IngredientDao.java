package com.salab.project.projectbakingrecipe.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.salab.project.projectbakingrecipe.models.Ingredient;

import java.util.List;
@Dao
public interface IngredientDao {

    @Query("SELECT * FROM ingredient WHERE recipeId = :recipeId")
    LiveData<List<Ingredient>> queryIngredientsById(int recipeId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertIngredients(Ingredient... ingredient);

}
