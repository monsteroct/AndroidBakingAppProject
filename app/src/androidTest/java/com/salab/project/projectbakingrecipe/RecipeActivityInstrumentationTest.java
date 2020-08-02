package com.salab.project.projectbakingrecipe;

import android.content.Intent;
import android.util.Log;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.salab.project.projectbakingrecipe.ui.RecipeActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.ThreadLocalRandom;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.salab.project.projectbakingrecipe.ui.RecipeActivity.EXTRA_RECIPE_ID;
import static org.hamcrest.Matchers.not;

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
    public void setup(){
        // reference https://stackoverflow.com/questions/42272683/get-recyclerview-total-item-count-in-espresso?rq=1
        RecyclerView rv = mActivityRule.getActivity().findViewById(R.id.rv_recipe_step_list);
        mRecyclerViewItemCount = rv.getAdapter().getItemCount();
        Log.d("TEST", mRecyclerViewItemCount+"");
    }

    @Test
    public void testTraceButtonIsClickable(){
        // test last item visible
        onView(withId(R.id.menu_item_recipe_trace_button)).check(matches(isClickable()));
    }


    @Test
    public void testLastItem_ShowDetailStepPage(){
        // test last item visible
        onView(withId(R.id.rv_recipe_step_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.pv_step_video)).check(matches(isDisplayed()));
    }

    @Test
    public void testFullScreen() {
        // test last item visible
        onView(withId(R.id.rv_recipe_step_list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        onView(withId(R.id.exo_fullscreen_button)).perform(click());
        // description is not visible
        onView(withId(R.id.tv_step_detail_desc)).check(doesNotExist());

        onView(withId(R.id.exo_fullscreen_button)).perform(click());
        onView(withId(R.id.tv_step_detail_desc)).check(matches(isDisplayed()));
    }

}
