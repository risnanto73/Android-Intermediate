package com.tiorisnanto.storyapp_risnanto73.data.repo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.MainCoroutineRules
import com.tiorisnanto.storyapp_risnanto73.adapter.StoriesAdapter
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiService
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.FakeApiService
import com.tiorisnanto.storyapp_risnanto73.data.room.FakeStoriesDao
import com.tiorisnanto.storyapp_risnanto73.data.room.StoriesDao
import com.tiorisnanto.storyapp_risnanto73.utils.PageDataSourceTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoriesRepositoryTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var storiesRepository: StoriesRepository
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var storiesDao: StoriesDao
    private var dummmyName = "Risnanto"
    private var dummyEmail = "Risnanto73@gmail.com"
    private var dummyPass = "Porenjer71298"
    private var dummyMultipart = DataDummy.generateDummyMultipartFile()
    private var dummyDescription = DataDummy.generateDummyRequestBody()
    private var dummyLatitude = DataDummy.generateDummyRequestBody()
    private var dummyLongitude = DataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storiesDao = FakeStoriesDao()
    }

    @Test
    fun `when login() is called Should  Not Null`() = runTest {
        val expectedResponse = DataDummy.generateDummyLoginResponseSuccess()
        val actualResponse = apiService.login(dummyEmail, dummyPass)
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `when register() is called Should  Not Null`() = runTest {
        val expectedResponse = DataDummy.generateDummyApiResponseSuccess()
        val actualResponse = apiService.register(dummmyName, dummyEmail, dummyPass)
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @Test
    fun `when getStoriesMap() is called Should Not Null`() = runTest {
        val expectedStories = DataDummy.generateDummyStoriesResponse()
        val actualStories = apiService.getAllStoriesLocation("token")
        assertNotNull(actualStories)
        assertEquals(expectedStories.listStories.size, actualStories.listStories.size)
    }

    @Test
    fun `when postStories() is called Should Not Null`() = runTest {
        val expectedResponse = DataDummy.generateDummyApiResponseSuccess()
        val actualResponse = apiService.addStories("token", dummyDescription, dummyMultipart, dummyLatitude, dummyLongitude)
        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when getPagingStories() is called Should Not Null`() = runTest {
        val mainCoroutineRule = MainCoroutineRules()

        val dummyStories = DataDummy.generateDummyListStories()
        val data = PageDataSourceTest.snapshot(dummyStories)

        val expectedResult = flowOf(data)
        Mockito.`when`(storiesRepository.getPagingStories("token")).thenReturn(expectedResult)

        storiesRepository.getPagingStories("token").collect {
            val differ = AsyncPagingDataDiffer(
                StoriesAdapter.DIFF_CALLBACK,
                listUpdateCallback,
                mainCoroutineRule.dispatcher,
                mainCoroutineRule.dispatcher
            )

            differ.submitData(it)
            assertNotNull(differ.snapshot())
            assertEquals(
                DataDummy.generateDummyStoriesResponse().listStories.size,
                differ.snapshot().size
            )
        }
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}