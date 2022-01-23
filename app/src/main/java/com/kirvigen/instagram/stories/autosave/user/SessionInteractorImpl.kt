package com.kirvigen.instagram.stories.autosave.user

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import com.afollestad.materialdialogs.MaterialDialog
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.toJson
import com.kirvigen.instagram.stories.autosave.utils.toObject

import android.content.Context.POWER_SERVICE

import android.os.PowerManager

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.annotation.RequiresApi

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

    override fun checkPermission(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissionsBattery(context)
        }
    }

    @SuppressLint("BatteryLife")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun requestPermissionsBattery(context: Context) {
        val packageName: String = context.packageName
        val pm = context.getSystemService(POWER_SERVICE) as PowerManager?
        if (pm?.isIgnoringBatteryOptimizations(packageName) == false) {
            showDialogBatteryPermissionsDenied(context) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                context.startActivity(intent)
            }
        }
    }

    private fun showDialogBatteryPermissionsDenied(context: Context, onClick: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_permissions_denied_head)
            message(R.string.dialog_permissions_battery_denied_body)
            cancelable(false)
            positiveButton(R.string.btn_title_battery_permission_dialog) {
                onClick.invoke()
                it.dismiss()
            }
        }
    }

    companion object {
        private const val CURRENT_PROFILE_KEY = "current_profile_key"
    }
}