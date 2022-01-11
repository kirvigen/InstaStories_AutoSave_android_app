package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.content.Context
import android.content.Intent
import com.kirvigen.instagram.stories.autosave.activity.instagramAuth.WebInstaAuthActivity

class InstagramInteractorImpl(
    private val instagramRepository: InstagramRepository,
    private val context: Context
) {

    fun isAuthInstagram(): Boolean = instagramRepository.getInstagramCookies() != ""

    fun checkAndOpenAuthInstagram() {
        if (!isAuthInstagram()) {
            val intent = Intent(context, WebInstaAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        }
    }
}
