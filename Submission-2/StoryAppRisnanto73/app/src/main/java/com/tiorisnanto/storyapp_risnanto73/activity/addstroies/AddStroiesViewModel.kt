package com.tiorisnanto.storyapp_risnanto73.activity.addstroies

import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddStroiesViewModel(private val storiesRepository: StoriesRepository) : ViewModel() {
    fun postStories(
        token: String,
        description: RequestBody,
        imageMultipart: MultipartBody.Part,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ) = storiesRepository.postStories(token, description, imageMultipart, lat, lon)
}