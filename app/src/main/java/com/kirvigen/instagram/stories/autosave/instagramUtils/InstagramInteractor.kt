package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

interface InstagramInteractor {

    suspend fun loadStoriesForAllProfile()

    fun savedSelectedProfile(list: List<Profile>)

}