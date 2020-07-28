package com.salab.project.projectbakingrecipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeDetailBinding;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeStepListBinding;

/**
 * Display list of recipe steps
 */
public class RecipeStepListFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_argument";

    private String mRecipeId;
    private FragmentRecipeStepListBinding mBinding;


    public RecipeStepListFragment() {
        // Required empty public constructor
    }

    public static RecipeStepListFragment newInstance(String recipeId) {
        // use factory patten to create fragment instance of that recipe
        RecipeStepListFragment fragment = new RecipeStepListFragment();
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
        mBinding = FragmentRecipeStepListBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
    }
}