package com.salab.project.projectbakingrecipe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.models.RecipeStep;

@Database(entities = {Ingredient.class, Recipe.class, RecipeStep.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    //singleton pattern
    private static final Object LOCK = new Object();
    private static final String databaseName = "app_database";
    private static AppDatabase sInstance;

    public AppDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context, AppDatabase.class, databaseName).build();
            }
        }
        return sInstance;
    }

    // create abstract Dao methods
    public abstract IngredientDao ingredientDao();
    public abstract RecipeDao recipeDao();
    public abstract RecipeStepDao recipeStepDao();

}
