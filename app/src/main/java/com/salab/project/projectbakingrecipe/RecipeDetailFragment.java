package com.salab.project.projectbakingrecipe;

import android.app.IntentService;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.salab.project.projectbakingrecipe.Model.Ingredient;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeDetailBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Display recipe details including image, name, servings and ingredients
 */
public class RecipeDetailFragment extends Fragment {


    private static final String ARG_RECIPE_ID = "recipe_argument";

    private String mRecipeId;
    private FragmentRecipeDetailBinding mBinding;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String recipeId) {
        // use factory patten to create fragment instance of that recipe
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getString(ARG_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRecipeDetailBinding.inflate(inflater, container, false);

        // init ListView
        IngredientListAdapter mAdapter = new IngredientListAdapter(getDummyDataSet());
        mBinding.lvDetailIngredientList.setAdapter(mAdapter);
        //TODO : fix the list view is scrollable

        return mBinding.getRoot();
    }

    private List<Ingredient> getDummyDataSet() {

        List<Ingredient> ingredientList = new ArrayList<>();

        Ingredient ingredientDummy = new Ingredient();
        ingredientDummy.setIngredient("Graham Cracker crumbs");
        ingredientDummy.setQuantity(2);
        ingredientDummy.setMeasure("Cup");
        ingredientList.add(ingredientDummy);

        Ingredient ingredientDummy2 = new Ingredient();
        ingredientDummy2.setIngredient("unsalted butter, melted");
        ingredientDummy2.setQuantity(6);
        ingredientDummy2.setMeasure("TBLSP");
        ingredientList.add(ingredientDummy2);

        return ingredientList;
    }

    /**
     * Simple BaseAdapter is used to back the ingredients ListView
     */
    class IngredientListAdapter extends BaseAdapter{

        private List<Ingredient> mIngredientList;

        public IngredientListAdapter(List<Ingredient> IngredientList) {
            mIngredientList = IngredientList;
        }

        @Override
        public int getCount() {
            if (mIngredientList == null) {
                return 0;
            } else {
                return mIngredientList.size();
            }
        }

        @Override
        public Ingredient getItem(int position) {
            return mIngredientList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                //inflate list item view once
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.ingredient_item, parent, false);
            }

            TextView ingredientNameTextView = convertView.findViewById(R.id.tv_item_ingredient_name);
            TextView ingredientQuantityTextView = convertView.findViewById(R.id.tv_item_ingredient_quantity);
            TextView ingredientMeasureTextView = convertView.findViewById(R.id.tv_item_ingredient_measure);

            ingredientNameTextView.setText(getItem(position).getIngredient());
            //getQuantity() returns integer
            ingredientQuantityTextView.setText(String.valueOf(getItem(position).getQuantity()));
            ingredientMeasureTextView.setText(getItem(position).getMeasure());

            return convertView;
        }
    }
}