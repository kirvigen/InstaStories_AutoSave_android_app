package com.kirvigen.instagram.stories.autosave.activity.instagramAuth

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kirvigen.instagram.stories.autosave.databinding.ActivityWebInstaAuthBinding
import android.webkit.WebSettings
import android.widget.Toast
import androidx.core.view.isVisible
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.utils.Constans
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class WebInstaAuthActivity : AppCompatActivity() {

    private val loginUrl = "https://www.instagram.com/login"
    private val instagramInteractor: InstagramInteractor by inject()

    private var binding: ActivityWebInstaAuthBinding? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebInstaAuthBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val webSettings: WebSettings = binding?.webContainer?.settings ?: return
        webSettings.javaScriptEnabled = true
        binding?.webContainer?.loadUrl(loginUrl)

        binding?.webContainer?.webViewClient = InstagramWebClient { cookies ->
            successAuth(cookies)
        }

        binding?.backBtn?.setOnClickListener {
            onBackPressed()
        }
    }

    private fun successAuth(cookies: String) {


    }
}