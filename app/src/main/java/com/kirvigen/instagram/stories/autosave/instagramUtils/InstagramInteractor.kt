package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

interface InstagramInteractor {

    fun isAuthInstagram(): Boolean

    suspend fun logout()

    suspend fun loadCurrentUser(): Profile?

    fun checkAndOpenAuthInstagram()
}