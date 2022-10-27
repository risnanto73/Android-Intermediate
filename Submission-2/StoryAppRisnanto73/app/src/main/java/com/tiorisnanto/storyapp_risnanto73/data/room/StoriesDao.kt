package com.tiorisnanto.storyapp_risnanto73.data.room

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

@Dao
interface StoriesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStories(storiesEntity: List<ListStoriesItem>)

    @Query("SELECT * FROM story")
    fun getStories(): PagingSource<Int, ListStoriesItem>

    @Query("DELETE FROM story")
    suspend fun deleteAll()
}