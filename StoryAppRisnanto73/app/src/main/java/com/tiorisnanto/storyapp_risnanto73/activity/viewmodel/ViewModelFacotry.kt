package com.tiorisnanto.storyapp_risnanto73.activity.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.tiorisnanto.storyapp_risnanto73.activity.Login.LoginViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.Main.MainViewModel
import com.tiorisnanto.storyapp_risnanto73.activity.Register.RegisterViewModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences

class ViewModelFacotry(private val pref: UserPreferences) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(pref) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(pref) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }
}