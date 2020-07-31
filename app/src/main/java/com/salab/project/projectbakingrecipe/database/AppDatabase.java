package com.salab.project.projectbakingrecipe.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.models.RecipeStep;

@Database(entities = {Recipe.class}, version = 1, exportSchema = false)
@TypeConverters(RecipeTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    //singleton pattern
    private static final Object LOCK = new Object();
    private static final String databaseName = "app_database";
    private static AppDatabase sInstance;

    public static AppDatabase getsInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context, AppDatabase.class, databaseName).build();
            }
        }
        return sInstance;
    }

    // create abstract Dao methods
    public abstract RecipeDao recipeDao();

}
