package com.salab.project.projectbakingrecipe.ui;

import android.app.Dialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.databinding.FragmentRecipeStepBinding;
import com.salab.project.projectbakingrecipe.utils.NetWorkUtil;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModel;
import com.salab.project.projectbakingrecipe.viewmodels.RecipeDetailSharedViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecipeStepFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecipeStepFragment extends Fragment {

    private static final String TAG = RecipeStepFragment.class.getSimpleName();

    public static final String ARG_RECIPE_ID = "recipe_argument";
    public static final String ARG_STEP_INDEX_ID = "step_argument";
    public static final String ARG_TWO_PANE = "two_pane";

    private int mRecipeId;
    private int mStepId;
    private boolean mTwoPane;

    private SimpleExoPlayer mExoPlayer;
    private FragmentRecipeStepBinding mBinding;
    private RecipeDetailSharedViewModel mViewModel;
    private DataSource.Factory mMediaSourceFactory;
    private ImageButton mExoFullScreenImageButton;
    private Dialog mFullScreenPlayerDialog;
    private boolean mFullScreenMode = false;

    public RecipeStepFragment() {
        // Required empty public constructor
        Log.d(TAG, "New " + TAG + " instance is created.");
    }


    public static RecipeStepFragment newInstance(int recipeId, int stepIndexId, boolean twoPane) {
        RecipeStepFragment fragment = new RecipeStepFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_RECIPE_ID, recipeId);
        args.putInt(ARG_STEP_INDEX_ID, stepIndexId);
        args.putBoolean(ARG_TWO_PANE, twoPane);
        fragment.setArguments(args);
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
        mBinding = FragmentRecipeStepBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            mRecipeId = getArguments().getInt(ARG_RECIPE_ID);
            mStepId = getArguments().getInt(ARG_STEP_INDEX_ID);
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);
            Log.d(TAG, "Detail Recipe Step started, with recipe/step Id/two pane  = " + mRecipeId + ", " + mStepId + ", " + mTwoPane);

        }
        if (mTwoPane){
            // in two pane mode, floating buttons to switch step are not necessary
            mBinding.fabNextStep.setVisibility(View.GONE);
            mBinding.fabPreviousStep.setVisibility(View.GONE);
        }

        initExoPlayer();
        initFullScreenButton();
        setupPrevNextOnClickListener();
        initViewModel();
    }


    /**
     * The method setup full screen button in exoplayer
     * 1. customized exo PlayerControlView, 2. use a full screen Dialog as container
     * 3. move the PlayerView between original fragment and Dialog to implement full screen functionality
     * reference: https://knowledge.udacity.com/questions/171512, https://geoffledak.com/blog/tag/android/
     */
    private void initFullScreenButton() {
        mFullScreenPlayerDialog = new Dialog(getContext(), android.R.style.Theme_Black_NoTitleBar_Fullscreen){
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                // if onBackPressed event is not properly handled, the player will stuck in dialog
                exitFullScreenMode();
            }
        };
        mExoFullScreenImageButton = mBinding.pvStepVideo.findViewById(R.id.exo_fullscreen_button);
        mExoFullScreenImageButton.setOnClickListener(v -> {
            if (mFullScreenMode){
                // in full screen mode, return to normal
                exitFullScreenMode();
            } else {
                // enter full screen mode
                enterFullScreenMode();
            }
        });
    }

    private void exitFullScreenMode() {
        // remove player from dialog and move it back to the original fragment
        ((ViewGroup) mBinding.pvStepVideo.getParent()).removeView(mBinding.pvStepVideo);
        mBinding.wrapperExoPlayerView.addView(mBinding.pvStepVideo);
        mExoFullScreenImageButton.setImageDrawable(getResources().getDrawable(R.drawable.exo_icon_fullscreen_enter, getContext().getTheme()));

        mFullScreenPlayerDialog.dismiss();
        mFullScreenMode = false;
    }

    private void enterFullScreenMode() {
        // remove player temporarily from original fragment
        ((ViewGroup) mBinding.pvStepVideo.getParent()).removeView(mBinding.pvStepVideo);
        // add the player into full screen dialog, and set size to "MATCH_PARENT"
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        mFullScreenPlayerDialog.addContentView(mBinding.pvStepVideo, params);
        mExoFullScreenImageButton.setImageDrawable(getResources().getDrawable(R.drawable.exo_controls_fullscreen_exit, getContext().getTheme()));
        mFullScreenPlayerDialog.show();

        mFullScreenMode = true;
    }

    private void initViewModel() {
        RecipeDetailSharedViewModelFactory factory = new RecipeDetailSharedViewModelFactory(getActivity().getApplication(), mRecipeId);
        mViewModel = new ViewModelProvider(getActivity(), factory).get(RecipeDetailSharedViewModel.class);
        mViewModel.getmSelectedRecipeStep().observe(getViewLifecycleOwner(), RecipeStep -> {
            mBinding.tvStepDetailDesc.setText(RecipeStep.getDescription());
            // video source priority VideoURL -> ThumbnailURL
            if (RecipeStep.getVideoURL() != null) {
                loadVideoToPlayer(RecipeStep.getVideoURL());
            } else if (RecipeStep.getThumbnailURL() != null) {
                loadVideoToPlayer(RecipeStep.getThumbnailURL());
            } else {
                Log.d(TAG, "No video available for this step");
            }
            Log.d(TAG, "Observe RecipeStep changed");
        });
    }

    private void setupPrevNextOnClickListener() {
        mBinding.fabPreviousStep.setOnClickListener(v -> mViewModel.getPreviousRecipeStep());
        mBinding.fabNextStep.setOnClickListener(v -> mViewModel.getNextRecipeStep());
    }

    private void initExoPlayer() {
        mExoPlayer = new SimpleExoPlayer.Builder(getContext()).build();
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.setRepeatMode(ExoPlayer.REPEAT_MODE_ONE);
        mExoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onPlayerError(ExoPlaybackException error) {
                // check player error
                Log.d(TAG, "player error");
                Toast.makeText(getContext(), getString(R.string.msg_video_loading_error), Toast.LENGTH_LONG).show();
            }
        });

        mBinding.pvStepVideo.setPlayer(mExoPlayer);

        mMediaSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "Baking"));
    }

    private void loadVideoToPlayer(String videoUrl) {
        if (NetWorkUtil.isOnline(getContext())){
            Uri uri = Uri.parse(videoUrl);
            MediaSource video = new ProgressiveMediaSource.Factory(mMediaSourceFactory).createMediaSource(uri);
            mExoPlayer.prepare(video);
        } else {
            Log.d(TAG, "device offline");
            Toast.makeText(getContext(), getString(R.string.msg_no_connection), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mExoPlayer != null) {
            // configuration change may occur before play initialization
            mExoPlayer.stop(true);
            mExoPlayer.release();
            mBinding = null;
        }
    }
}