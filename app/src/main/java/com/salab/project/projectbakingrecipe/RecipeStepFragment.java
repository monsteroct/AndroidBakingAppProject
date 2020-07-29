package com.salab.project.projectbakingrecipe;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeStepBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepFragment extends Fragment {

    private static final String ARG_RECIPE_ID = "recipe_argument";
    private static final String ARG_STEP_ID = "step_argument";
    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    private int mRecipeId;
    private int mStepId;
    private FragmentRecipeStepBinding mBinding;
    private boolean mIsLandscapeMode;

    public RecipeStepFragment() {
        // Required empty public constructor
    }


    public static RecipeStepFragment newInstance(int recipeId, int stepId) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_ID, stepId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
            mStepId = getArguments().getInt(ARG_STEP_ID);
        }
        Log.d(TAG, "Detail Recipe Step started, with recipe/step Id = " + mRecipeId + ", " + mStepId);
        // check orientation of device
        mIsLandscapeMode = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = FragmentRecipeStepBinding.inflate(inflater, container, false);

        // Hide system UIs and action bar
        if (mIsLandscapeMode) setFullScreen();


        return mBinding.getRoot();
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