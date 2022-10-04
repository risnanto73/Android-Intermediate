package com.tiorisnanto.storyapp_risnanto73.activity.Stories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.AllStoriesResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoryItem
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiConfig
import com.tiorisnanto.storyapp_risnanto73.helper.Events
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListStoriesViewModel: ViewModel() {

    companion object {
        private const val TAG = "ListStoryViewModel"
    }

    private val _itemStory = MutableLiveData<List<ListStoryItem>>()
    val itemStory: LiveData<List<ListStoryItem>> = _itemStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isHaveData = MutableLiveData<Boolean>()
    val isHaveData: LiveData<Boolean> = _isHaveData

    private val _snackBarText = MutableLiveData<Events<String>>()
    val snackBarText: LiveData<Events<String>> = _snackBarText

    fun showListStory(token: String) {
        _isLoading.value = true
        _isHaveData.value = true
        val client = ApiConfig
            .getApiService()
            .getAllStories("Bearer $token")

        client.enqueue(object : Callback<AllStoriesResponse> {
            override fun onResponse(
                call: Call<AllStoriesResponse>,
                response: Response<AllStoriesResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (!responseBody.error) {
                            _itemStory.value = response.body()?.listStory
                            _isHaveData.value = responseBody.message == "Stories fetched successfully"
                        }
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                    _snackBarText.value = Events(response.message())
                }
            }

            override fun onFailure(call: Call<AllStoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
                _snackBarText.value = Events(t.message.toString())
            }
        })
    }

}