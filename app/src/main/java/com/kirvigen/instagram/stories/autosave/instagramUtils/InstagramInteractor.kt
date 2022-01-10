package com.kirvigen.instagram.stories.autosave.instagramUtils

interface InstagramInteractor {

    fun saveAuthHeaders(headers: Map<String,String>)

    fun isAuthInstagram(): Boolean

    fun checkAndOpenAuthInstagram()
}