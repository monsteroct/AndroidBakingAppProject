package com.salab.project.projectbakingrecipe.ui;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeStepBinding;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModel;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeListFragment.class.getSimpleName();

    private static final String ARG_RECIPE_ID = "recipe_argument";
    private static final String ARG_STEP_INDEX_ID = "step_argument";

    private int mRecipeId;
    private int mStepId;
    private FragmentRecipeStepBinding mBinding;
    private boolean mIsLandscapeMode;
    RecipeDetailSharedViewModel mViewModel;

    public RecipeStepFragment() {
        // Required empty public constructor
        Log.d(TAG, "New " + TAG + " instance is created.");
    }


    public static RecipeStepFragment newInstance(int recipeId, int stepIndexId) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_INDEX_ID, stepIndexId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check orientation of device
        mIsLandscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRecipeStepBinding.inflate(inflater, container, false);

        // Hide system UIs and action bar
//        if (mIsLandscapeMode) setFullScreen();
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
            mStepId = getArguments().getInt(ARG_STEP_INDEX_ID);
            Log.d(TAG, "Detail Recipe Step started, with recipe/step Id = " + mRecipeId + ", " + mStepId);

            RecipeDetailSharedViewModelFactory factory = new RecipeDetailSharedViewModelFactory(getActivity().getApplication(), mRecipeId);
            mViewModel = new ViewModelProvider(getActivity(), factory).get(RecipeDetailSharedViewModel.class);
            mViewModel.getmSelectedRecipeStep().observe(getViewLifecycleOwner(), RecipeStep -> {
                mBinding.tvStepDetailDesc.setText(RecipeStep.getDescription());
                Log.d(TAG, "Observe RecipeStep changed");
            });

        }

        // previous, next steps button are available in portrait mode
        mBinding.fabPreviousStep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewModel.getPreviousRecipeStep();
            }
        });

        mBinding.fabNextStep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mViewModel.getNextRecipeStep();
            }
        });

    }



    private void setFullScreen() {

        getActivity().getWindow().getDecorView().setSystemUiVisibility(
                //  hide system UI
                  View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                //immersive mode
                | View.SYSTEM_UI_FLAG_IMMERSIVE
                //prevent layout resizing
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        if(getActivity().getActionBar() != null){
            getActivity().getActionBar().hide();
        }
    }
}