package com.tiorisnanto.storyapp_risnanto73.activity.register

import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository

class RegisterViewModel(val storiesRepository: StoriesRepository) : ViewModel() {

    fun register(name: String, email: String, pass: String) =
        storiesRepository.register(name,email, pass)

}