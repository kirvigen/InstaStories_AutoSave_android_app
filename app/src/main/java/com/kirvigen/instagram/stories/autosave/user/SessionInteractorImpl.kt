package com.kirvigen.instagram.stories.autosave.user

import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.toJson
import com.kirvigen.instagram.stories.autosave.utils.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class SessionInteractorImpl(
    private val sharedPreferences: SharedPreferences,
    private val instagramRepository: InstagramRepository,
    private val context: Context
) : SessionInteractor {

    override fun isAuth(): Boolean = getCurrentProfile() != null

    override fun saveAuthCookies(cookie: String) {
        instagramRepository.saveInstagramCookies(cookie)
    }

    override fun saveAuthHeaders(headers: Map<String, String>) {
        instagramRepository.saveInstagramHeaders(headers)
    }

    override fun getCurrentProfile(): Profile? = sharedPreferences.getString(CURRENT_PROFILE_KEY, "")?.toObject()

    override suspend fun loadCurrentProfile(): Profile? {
        val profile = instagramRepository.getCurrentProfile() ?: return null
        sharedPreferences.edit().putString(CURRENT_PROFILE_KEY, profile.toJson()).apply()
        return profile
    }

    override fun isAllPermissionsGranted(): Boolean {
        ARRAY_PERMISSIONS.forEach {
            if (ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
                return false
        }
        return true
    }

    override suspend fun requestPermission(permissionId: String): Boolean = suspendCoroutine { ret ->
        Dexter.withContext(context).withPermission(permissionId)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    ret.resume(true)
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    ret.resume(false)
                }

                override fun onPermissionRationaleShouldBeShown(p0: PermissionRequest?, p1: PermissionToken?) {
                    ret.resume(false)
                }
            }).check()
    }

    override fun checkPermission(context: Context) {
        if (!isAllPermissionsGranted()) {
            CoroutineScope(Dispatchers.Main).launch {
                val result = requestPermissions()
                if (!result) {
                    showDialogPermissionsDenied(context)
                }
            }
        }
    }

    private fun showDialogPermissionsDenied(context: Context) {
        MaterialDialog(context).show {
            title(R.string.dialog_permissions_denied_head)
            message(R.string.dialog_permissions_denied_body)
            cancelable(false)
            positiveButton(R.string.retry) {
                checkPermission(context)
                it.dismiss()
            }
        }
    }

    companion object {
        private val ARRAY_PERMISSIONS = mutableListOf(
            android.Manifest.permission.INTERNET
        ).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                add(android.Manifest.permission.FOREGROUND_SERVICE)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                add(android.Manifest.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS)
            }
        }
        private const val CURRENT_PROFILE_KEY = "current_profile_key"
    }
}

enum class PermissionRequest()
