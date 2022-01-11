package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

interface InstagramRepository {

    suspend fun getStories(userId: Long): List<Stories>

    fun getInstagramCookies(): String

    fun saveAuthHeaders(headers: Map<String,String>)

    suspend fun getFollowers(): List<Profile>

    suspend fun getCurrentProfile(): Profile?

    suspend fun wipeCurrentProfile()

    fun getUserId(nickname: String): String
}