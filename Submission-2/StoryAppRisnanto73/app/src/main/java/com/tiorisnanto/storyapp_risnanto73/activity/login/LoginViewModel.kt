package com.tiorisnanto.storyapp_risnanto73.activity.login

import android.util.Log
import androidx.lifecycle.*
import com.tiorisnanto.storyapp_risnanto73.data.model.UserModel
import com.tiorisnanto.storyapp_risnanto73.data.model.UserPreferences
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.LoginResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiConfig
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.helper.Helper
import kotlinx.coroutines.launch
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val storiesRepository: StoriesRepository): ViewModel()  {

    fun login(email: String, pass: String) =
        storiesRepository.login(email, pass)

}