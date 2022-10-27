package com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit

import com.tiorisnanto.storyapp_risnanto73.DataDummy
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.AllStoriesResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ApiResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {

    private val dummyStoriesResponse = DataDummy.generateDummyStoriesResponse()
    private val dummyApiResponseSuccess = DataDummy.generateDummyApiResponseSuccess()
    private val dummyLoginResponseSuccess =  DataDummy.generateDummyLoginResponseSuccess()

    override suspend fun register(name: String, email: String, pass: String): ApiResponse {
        return dummyApiResponseSuccess
    }

    override suspend fun login(email: String, pass: String): LoginResponse {
        return dummyLoginResponseSuccess
    }

    override suspend fun addStories(
        token: String,
        description: RequestBody,
        file: MultipartBody.Part,
        latitude: RequestBody?,
        longitude: RequestBody?
    ): ApiResponse {
        return dummyApiResponseSuccess
    }

    override suspend fun getAllStories(token: String, page: Int, size: Int): AllStoriesResponse {
        return dummyStoriesResponse
    }

    override suspend fun getAllStoriesLocation(token: String): AllStoriesResponse {
        return dummyStoriesResponse
    }

}
