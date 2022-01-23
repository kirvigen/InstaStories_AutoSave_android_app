package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

interface InstagramInteractor {

    suspend fun loadStoriesForAllProfile(allUpdate: Boolean = false)

    fun savedSelectedProfile(list: List<Profile>)

}