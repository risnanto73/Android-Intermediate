package com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit

import com.tiorisnanto.storyapp_risnanto73.data.remote.response.AllStoriesResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ApiResponse
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<ApiResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") pass: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun addStories(
        @Header("Authorization") token: String,
        @Part("description") des: String,
        @Part file: MultipartBody.Part
    ): Call<ApiResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<AllStoriesResponse>
}