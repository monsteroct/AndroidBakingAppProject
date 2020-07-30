package com.salab.project.projectbakingrecipe;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.Model.Ingredient;
import com.salab.project.projectbakingrecipe.Model.RecipeStep;
import com.salab.project.projectbakingrecipe.RecipeStepListAdapter.RecipeStepOnClickListener;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeDetailBinding;

import java.util.ArrayList;
import java.util.List;

import static com.salab.project.projectbakingrecipe.RecipeStepActivity.EXTRA_RECIPE_ID;
import static com.salab.project.projectbakingrecipe.RecipeStepActivity.EXTRA_STEP_ID;

/**
 * Display recipe details including image, name, servings and ingredients
 */
public class RecipeDetailFragment extends Fragment{


    private static final String ARG_RECIPE_ID = "recipe_argument";
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();
    
    private int mRecipeId;
    private FragmentRecipeDetailBinding mBinding;
    private RecipeStepListAdapter mAdapter;

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
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // populate the UI
        fillIngredientListToTextView();
        initRecyclerView();
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

    private void initRecyclerView() {
        mBinding.rvRecipeStepList.setLayoutManager(new LinearLayoutManager(getContext()));

        // make host activity listen to the step list click event, and do corresponding actions
        RecipeStepOnClickListener stepClickListener = null;
        try{
            stepClickListener = (RecipeStepOnClickListener) getActivity();
        } catch (ClassCastException e){
            Log.d(TAG, "Host Activity does not implement Adapter's onClick listener interface");
        }

        mAdapter = new RecipeStepListAdapter(getContext(), getDummyStepDataSet(), stepClickListener);

        //scrolling all activity layout, so turn of individual scrolling
        mBinding.rvRecipeStepList.setNestedScrollingEnabled(false);

        mBinding.rvRecipeStepList.setHasFixedSize(true);
        mBinding.rvRecipeStepList.setAdapter(mAdapter);
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


    private List<RecipeStep> getDummyStepDataSet() {
        //create dummy recipe list, only for build purpose
        List<RecipeStep> recipeStepList = new ArrayList<>();

        RecipeStep dummyStep = new RecipeStep();
        dummyStep.setId(0);
        dummyStep.setShortDescription("Recipe Introduction");
        recipeStepList.add(dummyStep);

        RecipeStep dummyStep2 = new RecipeStep();
        dummyStep2.setId(1);
        dummyStep2.setShortDescription("Starting prep");
        recipeStepList.add(dummyStep2);

        return recipeStepList;
    }
    

}