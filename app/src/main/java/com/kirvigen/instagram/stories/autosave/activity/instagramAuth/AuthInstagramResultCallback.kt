package com.kirvigen.instagram.stories.autosave.activity.instagramAuth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

class AuthInstagramResultCallback : ActivityResultContract<String, String?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, WebInstaAuthActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) return null
        intent ?: return null
        return intent.getStringExtra(KEY_RESULT_INSTAGRAM_COOKIES)
    }

    companion object {
        const val KEY_RESULT_INSTAGRAM_COOKIES = "instagram_cookies_key"
    }
}