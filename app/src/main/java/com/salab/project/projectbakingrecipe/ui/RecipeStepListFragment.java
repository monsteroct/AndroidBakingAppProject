package com.salab.project.projectbakingrecipe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.models.RecipeStep;
import com.salab.project.projectbakingrecipe.adapter.RecipeStepListAdapter;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeStepListBinding;

import java.util.ArrayList;
import java.util.List;

import static com.salab.project.projectbakingrecipe.ui.RecipeStepActivity.EXTRA_RECIPE_ID;
import static com.salab.project.projectbakingrecipe.ui.RecipeStepActivity.EXTRA_STEP_ID;

/**
 * Display list of recipe steps
 */
public class RecipeStepListFragment extends Fragment implements RecipeStepListAdapter.RecipeStepOnClickListener {

    private static final String ARG_RECIPE_ID = "recipe_argument";

    private int mRecipeId;
    private FragmentRecipeStepListBinding mBinding;
    private RecipeStepListAdapter mAdapter;


    public RecipeStepListFragment() {
        // Required empty public constructor
    }

    public static RecipeStepListFragment newInstance(int recipeId) {
        // use factory patten to create fragment instance of that recipe
        RecipeStepListFragment fragment = new RecipeStepListFragment();
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
        mBinding = FragmentRecipeStepListBinding.inflate(inflater, container, false);

        initRecyclerView();

        return mBinding.getRoot();
    }

    private void initRecyclerView() {
        mBinding.rvRecipeStepList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new RecipeStepListAdapter(getContext(), getDummyDataSet(), this);

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

    @Override
    public void onRecipeStepClick(int stepId) {
        Intent recipeStepIntent = new Intent(getContext(),RecipeStepActivity.class);
        // RecipeStep needs two argument to determine
        recipeStepIntent.putExtra(EXTRA_RECIPE_ID, mRecipeId);
        recipeStepIntent.putExtra(EXTRA_STEP_ID, stepId);
        startActivity(recipeStepIntent);
    }
}