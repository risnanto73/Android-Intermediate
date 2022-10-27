package com.tiorisnanto.storyapp_risnanto73.activity.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.storyapp_risnanto73.activity.addstroies.AddStroiesViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.login.LoginViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.main.MainViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.maps.MapsViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.register.RegisterViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.stories.ListStoriesViewModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.injection.Injection

class ViewModelFactory private constructor(
    private val storyRepository: StoriesRepository
) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListStoriesViewModel::class.java) -> {
                ListStoriesViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStroiesViewModel::class.java) -> {
                AddStroiesViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(storyRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(Injection.provideStoryRepository(context))
            }.also { instance = it }
    }
}