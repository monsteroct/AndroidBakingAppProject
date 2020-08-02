package com.salab.project.projectbakingrecipe.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.Nullable;

import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.repository.RecipeRepository;

public class ShoppingListUpdateService extends IntentService {

    public static final String TAG = ShoppingListUpdateService.class.getSimpleName();

    public static final String ACTION_CHANGE_RECIPE = "change_recipe";
    public static final String ACTION_CONTENT_UPDATE = "content_update";
    public static final String ACTION_INGREDIENT_CHECKED = "ingredient_checked";
    public static final String WIDGET_RECIPE_SHAREDPREFERENCE = "WidgetRecipe";
    public static final String KEY_WIDGET_RECIPE_ID = "widget_recipe_id";
    public static final String WIDGET_SHOPPING_LIST_SERVICE = "WidgetShoppingListService";

    public ShoppingListUpdateService() {
        super(WIDGET_SHOPPING_LIST_SERVICE);
        Log.d(TAG, "new Instance created");
    }

    public static void startChangeRecipeService(Context context, int recipeId){
        // gateway allows programmatically trigger change recipe procedure
        Intent intent = new Intent(context, ShoppingListUpdateService.class);
        intent.setAction(ACTION_CHANGE_RECIPE);
        intent.putExtra(KEY_WIDGET_RECIPE_ID, recipeId);
        context.startService(intent);
    }

    public static void startContentUpdateService(Context context){
        // gateway allows programmatically trigger update widget content procedure
        Intent intent = new Intent(context, ShoppingListUpdateService.class);
        intent.setAction(ACTION_CONTENT_UPDATE);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        // accept intents from widget or app itself and redirect to proper functions
        if (intent == null || intent.getAction() == null){return;}

        String intentAction = intent.getAction();
        Log.d(TAG, "accept Intent with action: " + intentAction);

        switch (intentAction){
            case ACTION_CHANGE_RECIPE:
                // show the ingredient list on widget
                if (intent.hasExtra(KEY_WIDGET_RECIPE_ID)){
                    saveRecipeChoice(intent.getIntExtra(KEY_WIDGET_RECIPE_ID,0));
                }
                break;
            case ACTION_CONTENT_UPDATE:
                // update widget data is requested (other than list)
                updateWidgetContent();
                break;
        }
    }

    private void updateWidgetContent() {
        // retrieve saved recipe id and query corresponding data.
        // then trigger AppWidgetProvider to assemble RemoteViews and update widgets
        RecipeRepository repository = new RecipeRepository(this);

        Recipe selectedRecipe = repository.getRecipeByIdRaw(readRecipeChoice());

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ShoppingListWidgetProvider.class));

        ShoppingListWidgetProvider.updateAppWidgets(this, appWidgetManager, appWidgetIds, selectedRecipe);
        Log.d(TAG, "updating widget with recipe Id: " + selectedRecipe.getId());
    }

    private void saveRecipeChoice(int recipeId) {
        //save choice in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences(WIDGET_RECIPE_SHAREDPREFERENCE, MODE_PRIVATE).edit();
        editor.putInt(KEY_WIDGET_RECIPE_ID, recipeId);
        editor.apply();
        Log.d(TAG, "save recipe id: " + recipeId + "into SharedPreferences");
    }

    private int readRecipeChoice() {
        //save choice in SharedPreferences
        return getSharedPreferences(WIDGET_RECIPE_SHAREDPREFERENCE, MODE_PRIVATE)
                .getInt(KEY_WIDGET_RECIPE_ID, -1);
    }


}
