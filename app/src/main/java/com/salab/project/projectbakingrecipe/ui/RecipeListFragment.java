package com.salab.project.projectbakingrecipe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.models.Recipe;
import com.salab.project.projectbakingrecipe.adapter.RecipeListAdapter;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeListBinding;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeListViewModel;

import java.util.ArrayList;
import java.util.List;

import static com.salab.project.projectbakingrecipe.ui.RecipeActivity.EXTRA_RECIPE_ID;

/**
 * Fragment to display list of recipes
 */
public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeItemClickListener{

    private static final String TAG = RecipeListFragment.class.getSimpleName();

    FragmentRecipeListBinding mBiding;
    RecipeListAdapter mAdapter;

    public RecipeListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout with ViewBiding
        mBiding = FragmentRecipeListBinding.inflate(inflater, container, false);

        initRecyclerView();

        return mBiding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        AndroidViewModelFactory factory = new AndroidViewModelFactory(getActivity().getApplication());
        RecipeListViewModel viewModel = new ViewModelProvider(getActivity(), factory).get(RecipeListViewModel.class);
        viewModel.getmRecipeList().observe(getViewLifecycleOwner(), recipes -> {
            // update adapter
            Log.d(TAG, "RecipeList Updated");
            mAdapter.setmRecipeList(recipes);
        });

    }

    public void initRecyclerView(){
        // show loading progress bar
        showProgressBar(true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mBiding.rvRecipeList.setLayoutManager(manager);

        mBiding.rvRecipeList.setHasFixedSize(true);

        mAdapter = new RecipeListAdapter(null, this);
        mBiding.rvRecipeList.setAdapter(mAdapter);

        // hide loading progress bar
        showProgressBar(false);
    }

    private void showProgressBar(boolean isShowing) {
        if (isShowing) {
            mBiding.rvRecipeList.setVisibility(View.INVISIBLE);
            mBiding.progressRecipeList.setVisibility(View.VISIBLE);
        } else {
            mBiding.rvRecipeList.setVisibility(View.VISIBLE);
            mBiding.progressRecipeList.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onRecipeClick(int recipeId) {
        Intent recipeIntent = new Intent(getContext(), RecipeActivity.class);
        recipeIntent.putExtra(EXTRA_RECIPE_ID, recipeId);
        startActivity(recipeIntent);
    }
}