package com.kirvigen.instagram.stories.autosave.instagramUtils.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

@Dao
interface ProfileDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(listProfiles: List<Profile>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(profile: Profile)

    @Query("SELECT * FROM `profile` WHERE isCorrect = 1")
    suspend fun getCorrectProfile()

    @Query("SELECT * FROM `profile`")
    suspend fun getTopGames(): List<Profile>
}