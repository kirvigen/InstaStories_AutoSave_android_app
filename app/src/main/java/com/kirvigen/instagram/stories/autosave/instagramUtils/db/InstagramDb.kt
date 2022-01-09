package com.kirvigen.instagram.stories.autosave.instagramUtils.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

@Database(
    entities = [Profile::class, Stories::class],
    version = 1,
    exportSchema = false
)
abstract class InstagramDb : RoomDatabase() {
    abstract fun ProfileDao(): ProfileDao
    abstract fun StoriesDao(): StoriesDao
}