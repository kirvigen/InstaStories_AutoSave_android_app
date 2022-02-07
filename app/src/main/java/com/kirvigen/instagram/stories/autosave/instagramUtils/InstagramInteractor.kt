package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

interface InstagramInteractor {

    suspend fun loadStoriesForAllProfile(allUpdate: Boolean = false): List<Stories>

    fun savedSelectedProfile(list: List<Profile>)

}