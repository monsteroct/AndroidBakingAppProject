package com.salab.project.projectbakingrecipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.Model.Ingredient;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeDetailBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Display recipe details including image, name, servings and ingredients
 */
public class RecipeDetailFragment extends Fragment {


    private static final String ARG_RECIPE_ID = "recipe_argument";

    private int mRecipeId;
    private FragmentRecipeDetailBinding mBinding;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(int recipeId) {
        // use factory patten to create fragment instance of that recipe
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRecipeDetailBinding.inflate(inflater, container, false);

        fillIngredientListToTextView();

        return mBinding.getRoot();
    }

    private void fillIngredientListToTextView() {
        mBinding.tvDetailIngredientList.setText("");
        List<Ingredient> ingredientList = getDummyDataSet();
        String ingredientDesc;
        for (int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = ingredientList.get(i);
            ingredientDesc = getString(R.string.msg_ingredient_desc, ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient());
            mBinding.tvDetailIngredientList.append(ingredientDesc + "\n");
        }
        Log.d("TEST", mBinding.tvDetailIngredientList.toString());
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
}