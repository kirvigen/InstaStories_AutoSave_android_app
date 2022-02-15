package com.kirvigen.instagram.stories.autosave.user

import android.content.Context
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

interface SessionInteractor {

    fun saveAuthCookies(cookie: String)
    fun saveAuthHeaders(headers: Map<String, String>)
    fun getCurrentProfile(): Profile?
    fun checkPermissionBattery(context: Context)
    fun isAuth(): Boolean

    suspend fun checkPermissionFileWrite(context: Context): Boolean
    suspend fun loadCurrentProfile(): Profile?
}