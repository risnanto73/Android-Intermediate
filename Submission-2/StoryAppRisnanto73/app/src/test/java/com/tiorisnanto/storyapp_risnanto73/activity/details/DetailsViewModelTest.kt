package com.tiorisnanto.storyapp_risnanto73.activity.details

import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import org.junit.Assert
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class DetailsViewModelTest{
    @Mock
    private lateinit var detailsViewModel: DetailsViewModel
    private val dummyStories = DataDummy.singleDummy()

    @Test
    fun `when setDetailStories() is called Should Success`() {
        val expectedStories = dummyStories
        Mockito.`when`(detailsViewModel.setDetailStories(dummyStories)).thenReturn(expectedStories)

        val actualStories = detailsViewModel.setDetailStories(
            ListStoriesItem(
                "Photo URL Stories 1",
                "2022-10-27T22:22:22Z",
                "Stories 1",
                "Stories Description",
                "1",
                7.3,
                7.3
            )
        )

        Assert.assertNotNull(actualStories)
        Assert.assertEquals(actualStories, expectedStories)
    }
}