package com.kirvigen.instagram.stories.autosave.instagramUtils.db

import androidx.room.Dao
import androidx.room.Delete
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

    @Query("SELECT * FROM `profile` WHERE isCurrentProfile = 1")
    suspend fun getCorrectProfile(): Profile?

    @Query("SELECT * FROM `profile` WHERE isCurrentProfile = 0")
    suspend fun getProfiles(): List<Profile>

    @Query("SELECT * FROM `profile` WHERE id = :profileId")
    suspend fun getProfile(profileId: Long): Profile?

    @Query("SELECT * FROM `profile` WHERE nickname = :nickname")
    suspend fun getProfile(nickname: String): Profile?
}