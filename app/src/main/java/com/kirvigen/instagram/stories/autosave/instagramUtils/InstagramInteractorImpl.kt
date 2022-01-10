package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.content.Context
import android.content.Intent
import com.kirvigen.instagram.stories.autosave.activity.instagramAuth.WebInstaAuthActivity

class InstagramInteractorImpl(
    private val instagramRepository: InstagramRepository,
    private val context: Context
) : InstagramInteractor {

    override fun isAuthInstagram(): Boolean = instagramRepository.getInstagramCookies() != ""

    override fun saveAuthHeaders(headers: Map<String, String>) {

    }

    override fun checkAndOpenAuthInstagram() {
//        if (!isAuthInstagram()) {
            val intent = Intent(context, WebInstaAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
//        }
    }
}
