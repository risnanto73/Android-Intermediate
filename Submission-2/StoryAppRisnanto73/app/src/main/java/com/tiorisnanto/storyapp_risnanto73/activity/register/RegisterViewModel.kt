package com.tiorisnanto.storyapp_risnanto73.activity.register

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ApiResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiConfig
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.helper.Helper
import org.json.JSONObject
import org.json.JSONTokener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterViewModel(val storiesRepository: StoriesRepository) : ViewModel() {

    fun register(name: String, email: String, pass: String) =
        storiesRepository.register(name,email, pass)

}