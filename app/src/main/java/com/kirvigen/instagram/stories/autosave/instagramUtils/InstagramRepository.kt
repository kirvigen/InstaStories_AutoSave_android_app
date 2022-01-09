package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.MyResult

interface InstagramRepository {

    fun getStories(userId: String)

    fun getFollowers(): List<Profile>

    suspend fun getUserId(nickname:String): MyResult

}