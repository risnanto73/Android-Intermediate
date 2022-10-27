package com.tiorisnanto.storyapp_risnanto73.activity.maps

import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository

class MapsViewModel (private val storiesRepository: StoriesRepository) : ViewModel() {

    fun getStories(token: String) = storiesRepository.getStoriesMap(token)

}