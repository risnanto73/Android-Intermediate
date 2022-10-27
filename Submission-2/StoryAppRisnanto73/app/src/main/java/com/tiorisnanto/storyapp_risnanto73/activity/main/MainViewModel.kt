package com.tiorisnanto.storyapp_risnanto73.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class MainViewModel(private val pref: UserPreferences) : ViewModel()  {

    fun getUser(): Flow<UserModel> {
        return pref.getUser()
    }

    fun logout() {
        viewModelScope.launch {
            pref.logout()
        }
    }
}