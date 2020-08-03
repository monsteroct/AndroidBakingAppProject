package com.salab.project.projectbakingrecipe;

import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.salab.project.projectbakingrecipe.ui.RecipeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.salab.project.projectbakingrecipe.ui.RecipeActivity.EXTRA_RECIPE_ID;

@RunWith(AndroidJUnit4.class)
public class RecipeActivityInstrumentationTest {

    private static final int targetRecipeId = 3;
    private int mRecyclerViewItemCount;

    @Rule
    public ActivityTestRule<RecipeActivity> mActivityRule = new ActivityTestRule<RecipeActivity>(RecipeActivity.class){
        // if activity is launched with Intent, then needs to provide it with the Intent required
        // reference https://xebia.com/blog/android-intent-extras-espresso-rules/
        @Override
        protected Intent getActivityIntent() {
            Intent recipeIntent = new Intent(ApplicationProvider.getApplicationContext(), RecipeActivity.class);
            recipeIntent.putExtra(EXTRA_RECIPE_ID, targetRecipeId);
            return recipeIntent;
        }
    };

    @Before
    public void setup() throws InterruptedException {
        // reference https://stackoverflow.com/questions/42272683/get-recyclerview-total-item-count-in-espresso?rq=1
        Thread.sleep(1000); // pause 1s for data query
        RecyclerView rv = mActivityRule.getActivity().findViewById(R.id.rv_recipe_step_list);
        mRecyclerViewItemCount = rv.getAdapter().getItemCount();
    }

    @Test
    public void ClickTraceButton_IsClickable(){
        // test last item visible
        onView(withId(R.id.menu_item_recipe_trace_button)).check(matches(isClickable()));
    }


    @Test
    public void ClickLastStepInList_StepDetailContentIsVisible(){
        onView(withId(R.id.rv_recipe_step_list))
                // recycler view is included in scroll view, so need to scroll to it first
                .perform(scrollTo(), RecyclerViewActions.actionOnItemAtPosition(mRecyclerViewItemCount - 1, click()));
        onView(withId(R.id.pv_step_video)).check(matches(isDisplayed()));
    }

    @Test
    public void ClickEXOPlayerFullScreen_PopUpFullScreenDialogAndReturn() {
        onView(withId(R.id.rv_recipe_step_list))
                .perform(scrollTo(), RecyclerViewActions.actionOnItemAtPosition(mRecyclerViewItemCount - 1, click()));
        // click full screen button, redirect to pop up full-screen dialog. Other views are invisible
        onView(withId(R.id.exo_fullscreen_button)).perform(click());
        onView(withId(R.id.tv_step_detail_desc)).check(doesNotExist());
        // click full screen button again to return
        onView(withId(R.id.exo_fullscreen_button)).perform(click());
        onView(withId(R.id.tv_step_detail_desc)).check(matches(isDisplayed()));
    }

}
