package com.salab.project.projectbakingrecipe.database;

import androidx.room.Ignore;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.RecipeStep;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Save list of ingredients and recipe steps as String in JSON format for simplicity
 */
public class RecipeTypeConverter {

    private static Gson gson;

    private static Gson getGsonInstance(){
        //singleton gson instance to be reused
        if (gson == null){
            gson = new Gson();
        }
        return gson;
    }

    @TypeConverter
    public static String ListIngredientToJson(List<Ingredient> ingredientList){
        return ingredientList == null ? null : getGsonInstance().toJson(ingredientList);
    }

    @TypeConverter
    public static List<Ingredient> JsonToListIngredient(String ingredientListJson){
        Type collectionType = new TypeToken<List<Ingredient>>(){}.getType();
        return ingredientListJson == null ? null : getGsonInstance().fromJson(ingredientListJson, collectionType);
    }

    @TypeConverter
    public static String ListStepToJson(List<RecipeStep> stepList){
        return stepList == null ? null : getGsonInstance().toJson(stepList);
    }

    @TypeConverter
    public static List<RecipeStep> JsonToListStep(String stepListJson){
        Type collectionType = new TypeToken<List<RecipeStep>>(){}.getType();
        return stepListJson == null ? null : getGsonInstance().fromJson(stepListJson, collectionType);
    }

}
