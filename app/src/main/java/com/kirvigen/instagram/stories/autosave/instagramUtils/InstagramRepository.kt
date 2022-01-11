package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.MyResult

interface InstagramRepository {

    suspend fun getStories(userId: Long): List<Stories>

    fun getInstagramCookies(): String

    fun saveAuthHeaders(headers: Map<String,String>)

    suspend fun getFollowers(): List<Profile>

    suspend fun getCurrentProfile(refresh: Boolean = false): Profile?

    suspend fun wipeCurrentProfile()

    suspend fun getUserId(nickname: String): MyResult
}