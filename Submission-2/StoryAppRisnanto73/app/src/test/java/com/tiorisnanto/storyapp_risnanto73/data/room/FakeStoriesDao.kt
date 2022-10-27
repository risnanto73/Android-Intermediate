package com.tiorisnanto.storyapp_risnanto73.data.room

import androidx.paging.PagingSource
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

class FakeStoriesDao : StoriesDao {

    private var storiesData = mutableListOf<List<ListStoriesItem>>()

    override suspend fun insertStories(storiesEntity: List<ListStoriesItem>) {
        storiesData.add(storiesEntity)
    }

    override fun getStories(): PagingSource<Int, ListStoriesItem> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAll() {
        storiesData.clear()
    }

}
