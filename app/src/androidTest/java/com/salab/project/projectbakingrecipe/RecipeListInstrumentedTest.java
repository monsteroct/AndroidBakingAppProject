package com.salab.project.projectbakingrecipe;


import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.salab.project.projectbakingrecipe.ui.RecipeListActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withChild;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;


@RunWith(AndroidJUnit4.class)
public class RecipeListInstrumentedTest {

    private static final String targetRecipeName = "Yellow Cake";

    private int mRecyclerViewItemCount;

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityRule = new ActivityTestRule<>(RecipeListActivity.class);


    @Before
    public void setup(){
        // reference https://stackoverflow.com/questions/42272683/get-recyclerview-total-item-count-in-espresso?rq=1
        RecyclerView rv = mActivityRule.getActivity().findViewById(R.id.rv_recipe_list);
        mRecyclerViewItemCount = rv.getAdapter().getItemCount();
    }


    @Test
    public void ClickLastRecipeInList_RecipeDetailContentIsVisible() throws InterruptedException {
        // pause 1s wait background thread done. Alternative to IdlingResources
        Thread.sleep(1000);
        onView(withId(R.id.rv_recipe_list))
                .perform((RecyclerViewActions.actionOnItemAtPosition(mRecyclerViewItemCount - 1, click())));
        onView(withId(R.id.tv_detail_recipe_name)).check(matches(isDisplayed()));
        onView(withId(R.id.tv_detail_ingredient_list)).check(matches(isDisplayed()));
    }


    @Test
    public void ClickTargetNameRecipeInList_DisplayRightRecipe() throws InterruptedException {
        // pause 1s wait background thread done.
        Thread.sleep(1000);
        onView(withId(R.id.rv_recipe_list))
                .perform(RecyclerViewActions.actionOnItem(hasDescendant(withText(targetRecipeName)), click()));

        onView(withId(R.id.tv_detail_recipe_name)).check(matches(withText(targetRecipeName)));
    }

}
