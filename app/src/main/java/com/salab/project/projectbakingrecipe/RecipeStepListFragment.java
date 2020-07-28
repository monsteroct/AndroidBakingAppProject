package com.salab.project.projectbakingrecipe;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.Model.Recipe;
import com.salab.project.projectbakingrecipe.Model.RecipeStep;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeDetailBinding;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeStepListBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Display list of recipe steps
 */
public class RecipeStepListFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_argument";

    private String mRecipeId;
    private FragmentRecipeStepListBinding mBinding;
    private RecipeStepListAdapter mAdapter;


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

        initRecyclerView();

        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mBinding.rvRecipeStepList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecipeStepListAdapter(getContext(), getDummyDataSet());

        //scrolling all activity layout, so turn of individual scrolling
        mBinding.rvRecipeStepList.setNestedScrollingEnabled(false);

        mBinding.rvRecipeStepList.setHasFixedSize(true);
        mBinding.rvRecipeStepList.setAdapter(mAdapter);
    }

    private List<RecipeStep> getDummyDataSet() {
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