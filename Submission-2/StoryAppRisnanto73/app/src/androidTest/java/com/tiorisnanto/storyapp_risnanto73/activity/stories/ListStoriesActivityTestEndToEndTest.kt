package com.tiorisnanto.storyapp_risnanto73.activity.stories

import android.content.Context
import android.content.Intent
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.longClick
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.tiorisnanto.storyapp_risnanto73.MainActivity
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.utils.EspressoIdlingResource
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.addstroies.AddStroiesActivity
import com.tiorisnanto.storyapp_risnanto73.activity.details.DetailsActivity
import com.tiorisnanto.storyapp_risnanto73.activity.maps.MapsActivity

@RunWith(AndroidJUnit4::class)
@LargeTest
class ListStoriesActivityTestEndToEndTest {
    private val user = UserModel(
        name = "Risnanto73",
        email = "tiolast63@gmail.com",
        password = "Porenjer7129_",
        userId = "1",
        token = "lorem",
        true
    )
    private lateinit var scenario: ActivityScenario<MainActivity>
    private val context: Context = ApplicationProvider.getApplicationContext()

    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loadListStories() {
        val intent = Intent(context, ListStoriesActivity::class.java)
        intent.putExtra(ListStoriesActivity.EXTRA_USER, user)
        scenario = launchActivity(intent)


        onView(withId(R.id.rv_story)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.scrollToPosition<RecyclerView.ViewHolder>(
                10
            )
        )
        onView(withId(R.id.swipe_refresh)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.iv_add_story)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.iv_maps)).check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Intents.release()
    }

    @Test
    fun loadDetailStories() {
        val intent = Intent(context, ListStoriesActivity::class.java)
        intent.putExtra(ListStoriesActivity.EXTRA_USER, user)
        scenario = launchActivity(intent)

        Intents.init()
        onView(withId(R.id.rv_story)).perform(
            RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                0,
                ViewActions.click()
            )
        )
        Intents.intended(hasComponent(DetailsActivity::class.java.name))
        onView(withId(R.id.iv_story))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_description))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.iv_story))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.tv_name))
            .check(matches(ViewMatchers.isDisplayed()))
    }

    @Test
    fun loadStoriesMap() {
        val intent = Intent(context, ListStoriesActivity::class.java)
        intent.putExtra(ListStoriesActivity.EXTRA_USER, user)
        scenario = launchActivity(intent)

        Intents.init()
        onView(withId(R.id.iv_maps)).perform(longClick())
        intended(hasComponent(MapsActivity::class.java.name))
    }

    @Test
    fun loadAddStories() {
        val intent = Intent(context, ListStoriesActivity::class.java)
        intent.putExtra(ListStoriesActivity.EXTRA_USER, user)
        scenario = launchActivity(intent)

        onView(withId(R.id.iv_add_story)).perform(longClick())
        onView(withId(R.id.btn_gallery))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.btn_camera_x))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.et_description))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.switchCompact))
            .check(matches(ViewMatchers.isDisplayed()))
        onView(withId(R.id.btn_upload))
            .check(matches(ViewMatchers.isDisplayed()))
    }
}