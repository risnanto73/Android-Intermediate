package com.tiorisnanto.storyapp_risnanto73.activity.login

import androidx.lifecycle.*
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository

class LoginViewModel(private val storiesRepository: StoriesRepository): ViewModel()  {

    fun login(email: String, pass: String) =
        storiesRepository.login(email, pass)

}