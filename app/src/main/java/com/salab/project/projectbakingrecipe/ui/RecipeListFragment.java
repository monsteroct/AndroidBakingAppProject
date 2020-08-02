package com.salab.project.projectbakingrecipe.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory;
import androidx.recyclerview.widget.GridLayoutManager;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.adapter.RecipeListAdapter;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeListBinding;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeListViewModel;

import static com.salab.project.projectbakingrecipe.ui.RecipeActivity.EXTRA_RECIPE_ID;

/**
 * Fragment to display list of recipes
 */
public class RecipeListFragment extends Fragment implements RecipeListAdapter.RecipeItemClickListener{

    private static final String TAG = RecipeListFragment.class.getSimpleName();
    public static int mNumSpan = 1;

    private FragmentRecipeListBinding mBiding;
    private RecipeListAdapter mAdapter;

    public RecipeListFragment() {
        // Required empty public constructor
        Log.d(TAG, "New " + TAG + " instance is created.");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout with ViewBiding
        mBiding = FragmentRecipeListBinding.inflate(inflater, container, false);
        return mBiding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initRecyclerView();

        // init ViewModel
        AndroidViewModelFactory factory = new AndroidViewModelFactory(getActivity().getApplication());
        RecipeListViewModel viewModel = new ViewModelProvider(getActivity(), factory).get(RecipeListViewModel.class);
        viewModel.getmRecipeList().observe(getViewLifecycleOwner(), recipes -> {
            // update adapter
            Log.d(TAG, "RecipeList Updated");
            mAdapter.setmRecipeList(recipes);
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mBiding = null;
    }

    public void initRecyclerView(){
        // show loading progress bar
        showProgressBar(true);

        calculateNumSpan();

        GridLayoutManager manager = new GridLayoutManager(getContext(), mNumSpan);
        mBiding.rvRecipeList.setLayoutManager(manager);

        mBiding.rvRecipeList.setHasFixedSize(true);

        mAdapter = new RecipeListAdapter(null, this);
        mBiding.rvRecipeList.setAdapter(mAdapter);

        // hide loading progress bar
        showProgressBar(false);
    }

    private void calculateNumSpan() {
        // phone landscape or tablet expand to 3 span of grid view
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int widthPixels = displayMetrics.widthPixels;
        int dpi = displayMetrics.densityDpi;
        int widthDp = widthPixels * 160 / dpi;
        if (widthDp >= 600){
            mNumSpan = 3;
        } else {
            mNumSpan = 1;
        }
    }

    private void showProgressBar(boolean isShowing) {
        if (isShowing) {
            mBiding.rvRecipeList.setVisibility(View.INVISIBLE);
            mBiding.progressRecipeList.setVisibility(View.VISIBLE);
        } else {
            mBiding.rvRecipeList.setVisibility(View.VISIBLE);
            mBiding.progressRecipeList.setVisibility(View.GONE);
        }
    }


    @Override
    public void onRecipeClick(int recipeId) {
        Intent recipeIntent = new Intent(getContext(), RecipeActivity.class);
        recipeIntent.putExtra(EXTRA_RECIPE_ID, recipeId);
        startActivity(recipeIntent);
    }
}