package com.kirvigen.instagram.stories.autosave.activity.instagramAuth

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

class AuthInstagramResultCallback : ActivityResultContract<String?, Profile?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, WebInstaAuthActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Profile? {
        if (resultCode != Activity.RESULT_OK) return null
        intent ?: return null
        return intent.getParcelableExtra(KEY_RESULT_INSTAGRAM_RESULT)
    }

    companion object {
        const val KEY_RESULT_INSTAGRAM_RESULT = "instagram_auth_result"
    }
}