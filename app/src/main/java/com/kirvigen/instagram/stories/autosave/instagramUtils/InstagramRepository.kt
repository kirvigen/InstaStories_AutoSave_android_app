package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.MyResult

interface InstagramRepository {

    fun getStories(userId: String)

    fun getInstagramCookies(): String

    fun getFollowers(): List<Profile>

    fun getCurrentProfile(): Profile

    suspend fun getUserId(nickname: String): MyResult
}