package com.salab.project.projectbakingrecipe.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.salab.project.projectbakingrecipe.R;
import com.salab.project.projectbakingrecipe.repository.RecipeRepository;

public class RecipeListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
    }
}