package com.tiorisnanto.storyapp_risnanto73.activity.details

import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

class DetailsViewModel: ViewModel() {
    lateinit var storiesItem: ListStoriesItem

    fun setDetailStories(stories: ListStoriesItem) : ListStoriesItem{
        storiesItem = stories
        return storiesItem
    }
}