package com.tiorisnanto.storyapp_risnanto73.activity.stories

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository

class ListStoriesViewModel(
    private val storyRepository: StoriesRepository,
) : ViewModel() {

    fun getStories(token: String): LiveData<PagingData<ListStoriesItem>> {
        return storyRepository.getPagingStories(token).cachedIn(viewModelScope).asLiveData()
    }
}