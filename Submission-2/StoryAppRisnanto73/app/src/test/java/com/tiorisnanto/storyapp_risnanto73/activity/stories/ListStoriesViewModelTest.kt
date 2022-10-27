package com.tiorisnanto.storyapp_risnanto73.activity.stories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.ListUpdateCallback
import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.MainCoroutineRules
import com.tiorisnanto.storyapp_risnanto73.adapter.StoriesAdapter
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import com.tiorisnanto.storyapp_risnanto73.getOrAwaitValue
import com.tiorisnanto.storyapp_risnanto73.utils.PageDataSourceTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ListStoriesViewModelTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRules = MainCoroutineRules()

    @Mock
    private lateinit var listStoriesViewModel: ListStoriesViewModel

    @Test
    fun `when Get Stories Should Not Null`() = runTest {
        val dummyStories = DataDummy.generateDummyListStories()
        val data = PageDataSourceTest.snapshot(dummyStories)
        val stories = MutableLiveData<PagingData<ListStoriesItem>>()
        stories.value = data

        Mockito.`when`(listStoriesViewModel.getStories("token")).thenReturn(stories)
        val actualStories = listStoriesViewModel.getStories("token").getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoriesAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainCoroutineRules.dispatcher,
            workerDispatcher = mainCoroutineRules.dispatcher,
        )

        differ.submitData(actualStories)

        advanceUntilIdle()
        Mockito.verify(listStoriesViewModel).getStories("token")
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
        assertEquals(dummyStories[0].name, differ.snapshot()[0]?.name)
    }
}

val noopListUpdateCallback = object : ListUpdateCallback {
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
}