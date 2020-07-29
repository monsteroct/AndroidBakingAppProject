package com.salab.project.projectbakingrecipe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RecipeStepActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "selected_recipe_id";
    public static final String EXTRA_STEP_ID = "selected_step_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);
    }
}