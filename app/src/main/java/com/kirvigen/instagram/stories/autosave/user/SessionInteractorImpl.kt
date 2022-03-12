package com.kirvigen.instagram.stories.autosave.user

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.POWER_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import android.util.Log
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
    private val instagramRepository: InstagramRepository
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

    @SuppressLint("BatteryLife")
    override fun checkPermission(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return
        val packageName: String = context.packageName
        val pm = context.getSystemService(POWER_SERVICE) as PowerManager?
        if (pm?.isIgnoringBatteryOptimizations(packageName) == false
            || !checkWriteExternalStorage(context)
        ) {
            showDialogBatteryPermissionsDenied(context) {
                val intent = Intent()
                intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
                intent.data = Uri.parse("package:$packageName")
                context.startActivity(intent)
                CoroutineScope(Dispatchers.Main).launch {
                    if (!getPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        checkPermission(context)
                    }
                }
            }
        }
    }

    override suspend fun checkPermissionFileWrite(context: Context): Boolean = suspendCoroutine { ret ->
        if (checkWriteExternalStorage(context)) {
            ret.resume(true)
            return@suspendCoroutine
        }
        showDialogFilePermissionsDenied(context) {
            if (!it) {
                ret.resume(false)
                return@showDialogFilePermissionsDenied
            }
            CoroutineScope(Dispatchers.Main).launch {
                ret.resume(getPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE))
            }
        }
    }

    private suspend fun getPermission(context: Context, permission: String): Boolean = suspendCoroutine { ret ->
        Dexter.withContext(context)
            .withPermission(permission)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                    Log.e(this::class.simpleName, "Granted permission $permission")
                    ret.resume(true)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    Log.e(this::class.simpleName, "Denied permission $permission")
                    ret.resume(false)
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    Log.e(this::class.simpleName, "RationaleShouldBeShown permission $permission")
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    private fun showDialogFilePermissionsDenied(context: Context, onClick: (Boolean) -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_permissions_denied_head)
            message(R.string.dialog_permissions_file_write_body)
            cancelable(false)
            positiveButton(R.string.btn_title_battery_permission_dialog) {
                onClick.invoke(true)
                it.dismiss()
            }
            negativeButton(R.string.cancel) {
                onClick.invoke(false)
            }
        }
    }

    private fun showDialogBatteryPermissionsDenied(context: Context, onClick: () -> Unit) {
        MaterialDialog(context).show {
            title(R.string.dialog_permissions_denied_head)
            message(R.string.dialog_permissions_denied_body)
            cancelable(false)
            positiveButton(R.string.btn_title_battery_permission_dialog) {
                onClick.invoke()
                it.dismiss()
            }
        }
    }

    companion object {
        private const val CURRENT_PROFILE_KEY = "current_profile_key"

        fun checkWriteExternalStorage(context: Context): Boolean =
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
    }
}