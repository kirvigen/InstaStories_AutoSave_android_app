package com.kirvigen.instagram.stories.autosave.user

import android.content.Context
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

interface SessionInteractor {

    fun saveAuthCookies(cookie: String)
    fun saveAuthHeaders(headers: Map<String, String>)
    fun getCurrentProfile(): Profile?
    fun isAllPermissionsGranted(): Boolean
    fun checkPermission(context: Context)
    fun isAuth(): Boolean

    suspend fun requestPermission(permissionId: String): Boolean
    suspend fun loadCurrentProfile(): Profile?
}