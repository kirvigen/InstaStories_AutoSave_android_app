package com.kirvigen.instagram.stories.autosave.instagramUtils.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

@Dao
interface StoriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listProfiles: List<Stories>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Stories)

    @Query("SELECT * FROM stories WHERE userId = :profileId")
    suspend fun getStories(profileId: String)
}