package com.kirvigen.instagram.stories.autosave.instagramUtils.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

@Dao
interface StoriesDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listStories: List<Stories>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Stories)

    @Query("DELETE FROM `stories` WHERE id = :storiesId")
    suspend fun deleteStories(storiesId: Long)

    @Query("UPDATE `stories` SET `localUri` = :localUrl WHERE id = :storiesId")
    suspend fun updateStoriesLocalUri(storiesId: Long, localUrl: String)

    @Query("SELECT * FROM stories WHERE userId = :profileId ORDER BY `date` DESC")
    fun getStoriesProfile(profileId: Long): LiveData<List<Stories>>

    @Query("SELECT * FROM stories WHERE userId = :profileId ORDER BY `date` DESC")
    fun getStoriesProfileSync(profileId: Long): List<Stories>

    @Query("SELECT * FROM stories ORDER BY `date` DESC")
    fun getAllStories(): LiveData<List<Stories>>

}