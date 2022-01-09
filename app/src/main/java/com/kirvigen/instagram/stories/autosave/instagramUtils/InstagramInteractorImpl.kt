package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.content.Context
import android.content.Intent
import android.util.Log
import com.kirvigen.instagram.stories.autosave.activity.instagramAuth.WebInstaAuthActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InstagramInteractorImpl(
    private val instagramRepository: InstagramRepository,
    private val context: Context
) : InstagramInteractor {

    override fun isAuthInstagram(): Boolean = instagramRepository.getInstagramCookies() != ""

    override fun checkAndOpenAuthInstagram() {
        if (!isAuthInstagram()) {
            val intent = Intent(context, WebInstaAuthActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)
        } else {
            CoroutineScope(Dispatchers.IO).launch {
                Log.e("dDDD", instagramRepository.getCurrentProfile().toString())
            }
        }
    }
}