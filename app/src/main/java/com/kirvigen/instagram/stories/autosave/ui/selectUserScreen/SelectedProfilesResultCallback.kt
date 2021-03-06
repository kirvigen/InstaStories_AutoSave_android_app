package com.kirvigen.instagram.stories.autosave.ui.selectUserScreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

class SelectedProfilesResultCallback : ActivityResultContract<String?, List<Profile>?>() {
    override fun createIntent(context: Context, input: String?): Intent {
        return Intent(context, SelectedProfilesActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): List<Profile>? {
        if (resultCode != Activity.RESULT_OK) return null
        intent ?: return null
        return intent.getParcelableArrayListExtra(KEY_RESULT_PROFILES_RESULT)
    }

    companion object {
        const val KEY_RESULT_PROFILES_RESULT = "profiles_result_key"
    }
}