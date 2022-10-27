package com.tiorisnanto.storyapp_risnanto73.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.tiorisnanto.storyapp_risnanto73.data.remote.response.ListStoriesItem

@Database(
    entities = [ListStoriesItem::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoriesDatabase : RoomDatabase() {
    abstract fun storiesDao(): StoriesDao
    abstract fun remoteKeysDao(): RemoteKeysDao

    companion object {
        @Volatile
        private var INSTANCE: StoriesDatabase? = null

        @JvmStatic
        fun getInstance(context: Context): StoriesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    StoriesDatabase::class.java, "stories.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}