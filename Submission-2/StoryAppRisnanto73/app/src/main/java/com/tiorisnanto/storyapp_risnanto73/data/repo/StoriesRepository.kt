package com.tiorisnanto.storyapp_risnanto73.data.repo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tiorisnanto.storyapp_risnanto73.data.remote.mediator.StoriesRemoteMediator
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ApiResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.LoginResult
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiService
import com.tiorisnanto.storyapp_risnanto73.data.resultresponse.ResultResponse
import com.tiorisnanto.storyapp_risnanto73.data.room.StoriesDatabase
import com.tiorisnanto.storyapp_risnanto73.utils.wrapEspressoIdlingResource
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoriesRepository (
    private val storiesDatabase: StoriesDatabase,
    private val apiService: ApiService,
) {

    fun register(name: String, email: String, pass: String): LiveData<ResultResponse<ApiResponse>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = apiService.register(name, email, pass)
                if (!response.error) {
                    emit(ResultResponse.Success(response))
                } else {
                    Log.e(TAG, "Register Failed: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register Exceptions: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun login(email: String, pass: String): LiveData<ResultResponse<LoginResult>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = apiService.login(email, pass)
                if (!response.error) {
                    emit(ResultResponse.Success(response.loginResult))
                } else {
                    Log.e(TAG, "Register Failed: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }
            } catch (e: Exception) {
                Log.e(TAG, "Register Exceptions: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun getStoriesMap(token: String): LiveData<ResultResponse<List<ListStoriesItem>>> =
        liveData {
            emit(ResultResponse.Loading)
            try {
                val response = apiService.getAllStoriesLocation("Bearer $token")
                if (!response.error) {
                    emit(ResultResponse.Success(response.listStories))
                } else {
                    Log.e(TAG, "GetStoriesMap Fail: ${response.message}")
                    emit(ResultResponse.Error(response.message))
                }

            } catch (e: Exception) {
                Log.e(TAG, "GetStoriesMap Exception: ${e.message.toString()} ")
                emit(ResultResponse.Error(e.message.toString()))
            }
        }

    fun postStories(
        token: String,
        description: RequestBody,
        imageMultipart: MultipartBody.Part,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): LiveData<ResultResponse<ApiResponse>> = liveData {
        emit(ResultResponse.Loading)
        try {
            val response = apiService.addStories("Bearer $token", description, imageMultipart, lat, lon)
            if (!response.error) {
                emit(ResultResponse.Success(response))
            } else {
                Log.e(TAG, "PostStories Fail: ${response.message}")
                emit(ResultResponse.Error(response.message))
            }
        } catch (e: Exception) {
            Log.e(TAG, "PostStories Exception: ${e.message.toString()} ")
            emit(ResultResponse.Error(e.message.toString()))
        }
    }

    fun getPagingStories(token: String): Flow<PagingData<ListStoriesItem>> {
        wrapEspressoIdlingResource {
            @OptIn(ExperimentalPagingApi::class)
            return Pager(
                config = PagingConfig(
                    pageSize = 5
                ),
                remoteMediator = StoriesRemoteMediator(storiesDatabase, apiService, token),
                pagingSourceFactory = {
                    storiesDatabase.storiesDao().getStories()
                }
            ).flow
        }
    }

    companion object {
        private const val TAG = "StoriesRepository"
    }
}