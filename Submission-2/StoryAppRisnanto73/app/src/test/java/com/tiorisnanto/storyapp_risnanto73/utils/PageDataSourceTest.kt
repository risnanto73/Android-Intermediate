package com.tiorisnanto.storyapp_risnanto73.utils

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

class PageDataSourceTest :
    PagingSource<Int, LiveData<List<ListStoriesItem>>>() {

    companion object {
        fun snapshot(items: List<ListStoriesItem>): PagingData<ListStoriesItem> {
            return PagingData.from(items)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, LiveData<List<ListStoriesItem>>>): Int {
        return 0
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, LiveData<List<ListStoriesItem>>> {
        return LoadResult.Page(emptyList(), 0, 1)
    }

}