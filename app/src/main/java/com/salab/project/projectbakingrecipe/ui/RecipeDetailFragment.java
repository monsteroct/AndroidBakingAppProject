package com.salab.project.projectbakingrecipe.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.models.Ingredient;
import com.salab.project.projectbakingrecipe.models.RecipeStep;
import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.adapter.RecipeStepListAdapter;
import com.salab.project.projectbakingrecipe.adapter.RecipeStepListAdapter.RecipeStepOnClickListener;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeDetailBinding;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModel;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModelFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Display recipe details including image, name, servings, ingredients and steps
 */
public class RecipeDetailFragment extends Fragment implements RecipeStepListAdapter.RecipeStepOnClickListener{

    private static final String ARG_RECIPE_ID = "recipe_argument";
    private static final String TAG = RecipeDetailFragment.class.getSimpleName();

    private FragmentRecipeDetailBinding mBinding;
    private RecipeStepListAdapter mAdapter;
    RecipeDetailSharedViewModel mViewModel;

    public RecipeDetailFragment() {
        // Required empty public constructor
        Log.d(TAG, "New " + TAG + " instance is created. (no args)");
    }

    public static RecipeDetailFragment newInstance(int recipeId) {
        // use factory patten to create fragment instance of that recipe
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        fragment.setArguments(args);
        Log.d(TAG, "New " + TAG + " instance is created.");
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        initRecyclerView();

        if (getArguments() != null) {
            int RecipeId = getArguments().getInt(ARG_RECIPE_ID);
            // init ViewModel
            RecipeDetailSharedViewModelFactory factory = new RecipeDetailSharedViewModelFactory(getActivity().getApplication(), RecipeId);
            mViewModel = new ViewModelProvider(getActivity(), factory).get(RecipeDetailSharedViewModel.class);

            mViewModel.getmSelectedRecipe().observe(getViewLifecycleOwner(), recipe -> {
                //update UI
                mBinding.tvDetailRecipeName.setText(recipe.getName());
                fillIngredientListToTextView(recipe.getIngredients());
                mAdapter.setmRecipeStepList(recipe.getSteps());

                Log.d(TAG, "Observe selected Recipe changed");
            });
        }

    }

    private void fillIngredientListToTextView(List<Ingredient> ingredientList) {
        // flatten Ingredient list to String for simplicity
        mBinding.tvDetailIngredientList.setText("");
        String ingredientDesc;
        for (int i = 0; i < ingredientList.size(); i++) {
            Ingredient ingredient = ingredientList.get(i);
            ingredientDesc = getString(R.string.msg_ingredient_desc,
                    ingredient.getQuantity(), ingredient.getMeasure(), ingredient.getIngredient());
            mBinding.tvDetailIngredientList.append(ingredientDesc + "\n");
        }
    }

    private void initRecyclerView() {
        mBinding.rvRecipeStepList.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new RecipeStepListAdapter(getContext(), null, this);

        //scrolling all activity layout, so turn of individual scrolling
        mBinding.rvRecipeStepList.setNestedScrollingEnabled(false);

        mBinding.rvRecipeStepList.setHasFixedSize(true);
        mBinding.rvRecipeStepList.setAdapter(mAdapter);
    }

    @Override
    public void onRecipeStepClick(int stepId) {
        // fragment notifies the ViewModel to update selected RecipeStep LiveData
        // host activity switch the page
        mViewModel.getRecipeStepById(stepId);
    }
}