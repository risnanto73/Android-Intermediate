package com.tiorisnanto.storyapp_risnanto73.activity.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.getOrAwaitValue
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest{
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var mapsViewModel: MapsViewModel
    private val dummyMaps = DataDummy.generateDummyMapsEntity()

    @Before
    fun setUp() {
        mapsViewModel = MapsViewModel(storiesRepository)
    }

    @Test
    fun `when getStories() is Called Should Not Null and Return Success`() {
        val expectedStories = MutableLiveData<ResultResponse<List<ListStoriesItem>>>()
        expectedStories.value = ResultResponse.Success(dummyMaps)
        Mockito.`when`(mapsViewModel.getStories("Token")).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getStories("Token").getOrAwaitValue()

        Mockito.verify(storiesRepository).getStoriesMap("Token")
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is ResultResponse.Success)
        Assert.assertEquals(dummyMaps.size, (actualStories as ResultResponse.Success).data.size)
    }

    @Test
    fun `when Network Error Should Return Error`() {
        val expectedStories = MutableLiveData<ResultResponse<List<ListStoriesItem>>>()
        expectedStories.value = ResultResponse.Error("Error")
        Mockito.`when`(mapsViewModel.getStories("Token")).thenReturn(expectedStories)

        val actualStories = mapsViewModel.getStories("Token").getOrAwaitValue()

        Mockito.verify(storiesRepository).getStoriesMap("Token")
        Assert.assertNotNull(actualStories)
        Assert.assertTrue(actualStories is ResultResponse.Error)
    }
}