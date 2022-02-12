package com.kirvigen.instagram.stories.autosave.instagramUtils

import androidx.lifecycle.LiveData
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.MyResult

interface InstagramRepository {

    fun getStoriesUser(userId: Long): LiveData<List<Stories>>

    fun getInstagramCookies(): String

    fun saveInstagramCookies(cookies: String)

    fun deleteProfile(profileId: Long)

    fun saveInstagramHeaders(headers: Map<String, String>)

    fun getProfiles(): LiveData<List<Profile>>

    fun getStories(): LiveData<List<Stories>>

    fun saveProfiles(profiles: List<Profile>)

    suspend fun getProfilesSync(): List<Profile>

    suspend fun searchProfile(searchText: String): List<Profile>

    suspend fun loadActualStories(userId: Long): List<Stories>

    suspend fun updateStoriesLocalUrl(storiesId: Long, localUrl: String)

    suspend fun getStories(profileId: Long): List<Stories>

    suspend fun getProfile(stories: Stories): Profile?

    suspend fun getProfile(profileId: Long): Profile?

    suspend fun getProfile(nickname: String): Profile?

    suspend fun getCurrentProfile(): Profile?
}