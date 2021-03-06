package com.salab.project.projectbakingrecipe.widget;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.repository.RecipeRepository;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService.KEY_CLICKED_INGREDIENT_INDEX;
import static com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService.KEY_WIDGET_TRACING_RECIPE_ID;
import static com.salab.project.projectbakingrecipe.widget.ShoppingListUpdateService.WIDGET_RECIPE_SHARED_PREFERENCE;

public class ShoppingListRemoteViewsServices extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ShoppingListRemoteViewsServicesFactory(this.getApplicationContext());
    }
}

class ShoppingListRemoteViewsServicesFactory implements RemoteViewsService.RemoteViewsFactory{

    private static final String TAG = ShoppingListRemoteViewsServicesFactory.class.getSimpleName();

    private Context mContext;
    private List<Ingredient> mIngredientList;


    public ShoppingListRemoteViewsServicesFactory(Context context){
        // RemoteViews creation needs context as parameter
        mContext = context;
        Log.d(TAG, "new RemoteViewsFactory is created");
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        // whenever it is notified that the dataset is changed -> query again
        RecipeRepository repository = new RecipeRepository(mContext);
        int recipeId = mContext.getSharedPreferences(WIDGET_RECIPE_SHARED_PREFERENCE, MODE_PRIVATE).getInt(KEY_WIDGET_TRACING_RECIPE_ID, -1);
        if (recipeId != -1){
            Recipe recipe = repository.getRecipeByIdRaw(recipeId);
            mIngredientList = recipe.getIngredients();
        } else {
            // has no favorite recipe been set
            mIngredientList = null;
        }
        Log.d(TAG, "on data set change called. Current recipeId is: " + recipeId);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientList == null){
            return 0;
        } else {
            return mIngredientList.size();
        }

    }

    @Override
    public RemoteViews getViewAt(int position) {

        Ingredient selectedIngredient = mIngredientList.get(position);

        RemoteViews view = new RemoteViews(mContext.getPackageName(), R.layout.shopping_item);
        view.setTextViewText(R.id.tv_widget_ingredient_name, selectedIngredient.getIngredient());
        view.setTextViewText(R.id.tv_widget_ingredient_qty, String.valueOf(selectedIngredient.getQuantity()));
        view.setTextViewText(R.id.tv_widget_ingredient_meas, selectedIngredient.getMeasure());

        // change checkbox image depends on isPurchased flag
        if (selectedIngredient.isPurchased()){
            view.setImageViewResource(R.id.iv_widget_ingredient_checkbox, R.drawable.ic_baseline_check_box_24);
        } else {
            view.setImageViewResource(R.id.iv_widget_ingredient_checkbox, R.drawable.ic_baseline_check_box_outline_blank_24);
        }

        Intent fillIntent = new Intent();
        fillIntent.putExtra(KEY_CLICKED_INGREDIENT_INDEX, position);
        view.setOnClickFillInIntent(R.id.wrapper_widget_ingredient_list, fillIntent);
        return view;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        // at least 1 type, otherwise will always show loading
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
