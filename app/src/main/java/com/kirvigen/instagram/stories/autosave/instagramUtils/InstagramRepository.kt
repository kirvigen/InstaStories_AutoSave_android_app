package com.kirvigen.instagram.stories.autosave.instagramUtils

import androidx.lifecycle.LiveData
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.MyResult

interface InstagramRepository {

    fun getStoriesUser(userId: Long): LiveData<List<Stories>>

    fun getInstagramCookies(): String

    fun saveInstagramCookies(cookies: String)

    fun saveInstagramHeaders(headers: Map<String, String>)

    suspend fun searchProfile(searchText: String): List<Profile>

    suspend fun loadActualStories(userId: Long): List<Stories>

    suspend fun getProfile(stories: Stories): Profile?

    suspend fun getCurrentProfile(): Profile?

    suspend fun getProfile(nickname: String): Profile?
}