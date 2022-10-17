package com.tiorisnanto.storyapp_risnanto73.activity.details

import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoryItem

class DetailsViewModel: ViewModel() {
    lateinit var storyItem: ListStoryItem

    fun setDetailStory(story: ListStoryItem) : ListStoryItem{
        storyItem = story
        return storyItem
    }
}