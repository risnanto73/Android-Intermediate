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
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.contrib.RecyclerViewActions.scrollTo
import androidx.test.espresso.intent.Intents.intended
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.tiorisnanto.storyapp_risnanto73.JsonConverters
import com.tiorisnanto.storyapp_risnanto73.R
import com.tiorisnanto.storyapp_risnanto73.activity.details.DetailsActivity
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiConfig
import com.tiorisnanto.storyapp_risnanto73.utils.EspressoIdlingResource
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@MediumTest
class ListStoriesActivityTest {
    private lateinit var scenario: ActivityScenario<ListStoriesActivity>
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val user = UserModel(
        name = "Risnanto73",
        email = "tiolast63@gmail.com",
        password = "Porenjer7129_",
        userId = "1",
        token = "lorem",
        true
    )

    private val mockWebServer = MockWebServer()

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun getStories_Success() {
        val intent = Intent(context, ListStoriesActivity::class.java)
        intent.putExtra(ListStoriesActivity.EXTRA_USER, user)
        scenario = launchActivity(intent)

        onView(withId(R.id.rv_story)).perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.iv_story)).check(matches(isDisplayed()))

    }

    @Test
    fun getStories_Error() {
        val intent = Intent(context, ListStoriesActivity::class.java)
        intent.putExtra(ListStoriesActivity.EXTRA_USER, user)
        scenario = launchActivity(intent)

        val mockResponse = MockResponse()
            .setResponseCode(500) // error
        mockWebServer.enqueue(mockResponse)

        onView(withId(R.id.rv_story))
            .check(matches(isDisplayed()))
        onView(withText(R.string.something_wrong))
            .check(matches(isDisplayed()))
    }
}