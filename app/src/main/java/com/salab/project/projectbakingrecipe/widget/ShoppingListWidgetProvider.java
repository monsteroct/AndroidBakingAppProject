package com.salab.project.projectbakingrecipe.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.ui.RecipeActivity;
import com.salab.project.projectbakingrecipe.ui.RecipeListActivity;

import static com.salab.project.projectbakingrecipe.ui.RecipeActivity.EXTRA_RECIPE_ID;
import static com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService.ACTION_INGREDIENT_CHECKED;

/**
 * Implementation of App Widget functionality.
 */
public class ShoppingListWidgetProvider extends AppWidgetProvider {

    private static final String TAG = ShoppingListWidgetProvider.class.getSimpleName();
    public static final int RECIPE_DETAIL_REQUEST_CODE = 0;
    public static final int RECIPE_LIST_REQUEST_CODE = 1;
    public static final int ITEM_CHECK_REQUEST_CODE = 2;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe selectedRecipe) {

        boolean hasSelectedRecipe = selectedRecipe == null;

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.shopping_list);
        // Setup list view adapter
        views.setRemoteAdapter(R.id.lv_widget_ingredient_list, new Intent(context, ShoppingListRemoteViewsServices.class));
        views.setPendingIntentTemplate(R.id.lv_widget_ingredient_list, getItemCheckPendingIntent(context));
        views.setEmptyView(R.id.lv_widget_ingredient_list, R.id.tv_widget_empty_view);

        if (!hasSelectedRecipe) {
            views.setTextViewText(R.id.tv_widget_recipe_name, selectedRecipe.getName());
            views.setTextViewText(R.id.tv_widget_recipe_servings, String.valueOf(selectedRecipe.getServings()));

            // On recipe title click, redirect to the recipe page
            views.setOnClickPendingIntent(R.id.tv_widget_recipe_name, getRecipeDetailPendingIntent(context, selectedRecipe));

        } else {
            // override title with empty string when there's no favorite recipe
            views.setTextViewText(R.id.tv_widget_recipe_name, "");
            views.setTextViewText(R.id.tv_widget_recipe_servings, "");
            // when there's no favorite recipe. show alternative view which redirecting to recipe list paget
            views.setOnClickPendingIntent(R.id.tv_widget_empty_view, getRecipeListPendingIntent(context));
        }

        appWidgetManager.updateAppWidget(appWidgetId, views);
        Log.d(TAG, "widget no: " + appWidgetId + " updated");
    }

    static void updateAppWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, Recipe selectedRecipe) {
        for(int appWidgetId : appWidgetIds){
            updateAppWidget(context, appWidgetManager, appWidgetId, selectedRecipe);
        }
    }

    private static PendingIntent getRecipeListPendingIntent(Context context) {
        Intent recipeListIntent = new Intent(context, RecipeListActivity.class);
        return PendingIntent.getActivity(context,
                RECIPE_LIST_REQUEST_CODE,
                recipeListIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getItemCheckPendingIntent(Context context) {
        Intent itemCheckIntent = new Intent(context, ShoppingListUpdateService.class);
        itemCheckIntent.setAction(ACTION_INGREDIENT_CHECKED);
        return PendingIntent.getService(context,
                ITEM_CHECK_REQUEST_CODE,
                itemCheckIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static PendingIntent getRecipeDetailPendingIntent(Context context, Recipe selectedRecipe) {
        Intent recipeDetailIntent = new Intent(context, RecipeActivity.class);
        recipeDetailIntent.putExtra(EXTRA_RECIPE_ID, selectedRecipe.getId());
        return PendingIntent.getActivity(context,
                RECIPE_DETAIL_REQUEST_CODE,
                recipeDetailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        ShoppingListUpdateService.startContentUpdateService(context);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

