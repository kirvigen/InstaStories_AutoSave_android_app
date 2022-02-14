package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

interface InstagramInteractor {

    fun deleteUserData(profileId: Long)

    suspend fun loadStoriesForAllProfile(allUpdate: Boolean = false): List<Stories>

    suspend fun savedSelectedProfile(list: List<Profile>)

}