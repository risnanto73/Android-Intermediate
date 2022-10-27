package com.tiorisnanto.storyapp_risnanto73.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.storyapp_risnanto73.activity.main.MainViewModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences

class ViewModelUserFactory(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}