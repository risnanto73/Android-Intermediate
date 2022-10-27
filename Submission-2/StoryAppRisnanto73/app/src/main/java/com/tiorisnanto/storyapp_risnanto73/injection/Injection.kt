package com.tiorisnanto.storyapp_risnanto73.injection

import android.content.Context
import com.tiorisnanto.storyapp_risnanto73.data.remote.retrofit.ApiConfig
import com.tiorisnanto.storyapp_risnanto73.data.repo.StoriesRepository
import com.tiorisnanto.storyapp_risnanto73.data.room.StoriesDatabase

object Injection {
    fun provideStoryRepository(context: Context): StoriesRepository {
        val database = StoriesDatabase.getInstance(context)
        val apiService = ApiConfig.getApiService()
        return StoriesRepository(database, apiService)
    }
}