package com.kirvigen.instagram.stories.autosave.instagramUtils

interface InstagramInteractor {

    fun isAuthInstagram(): Boolean

    fun checkAndOpenAuthInstagram()
}