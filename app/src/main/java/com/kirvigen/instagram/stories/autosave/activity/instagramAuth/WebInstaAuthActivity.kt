package com.kirvigen.instagram.stories.autosave.activity.instagramAuth

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import android.webkit.WebSettings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.activity.instagramAuth.AuthInstagramResultCallback.Companion.KEY_RESULT_INSTAGRAM_RESULT
import com.kirvigen.instagram.stories.autosave.databinding.ActivityWebInstaAuthBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import com.kirvigen.instagram.stories.autosave.utils.dpToPx
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import com.kirvigen.instagram.stories.autosave.utils.valueOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import kotlin.coroutines.CoroutineContext

class WebInstaAuthActivity : AppCompatActivity(), CoroutineScope {

    private val loginUrl = "https://www.instagram.com/login"
    private val instagramInteractor: InstagramInteractor by inject()
    private var binding: ActivityWebInstaAuthBinding? = null

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebInstaAuthBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val webSettings: WebSettings = binding?.webContainer?.settings ?: return
        webSettings.javaScriptEnabled = true
        binding?.webContainer?.loadUrl(loginUrl)

        binding?.webContainer?.webViewClient = InstagramWebClient { cookies ->
            launch { successAuth(cookies) }
        }
    }

    private suspend fun successAuth(cookies: String) {
        binding?.webContainer?.animateAlpha(0f)

        val profile = instagramInteractor.loadCurrentUser()
        profile?.let {
            binding?.profileImage?.loadImage(profile.photo)
            binding?.nickname?.text = profile.name

            binding?.webViewContainer?.apply {
                animate()
                    .alpha(0f)
                    .translationY(50.dpToPx.toFloat())
                    .setInterpolator(DecelerateInterpolator())
                    .start()
            }

            binding?.headTitle?.animateAlpha(0f) {
                binding?.headTitle?.setText(R.string.auth_success)
                binding?.headTitle?.animateAlpha(1f)
            }

            delay(1000L)

            val intent = Intent().apply {
                putExtra(KEY_RESULT_INSTAGRAM_RESULT, profile)
            }

            setResult(RESULT_OK, intent)
        }

        if (profile == null) {
            valueOrNull { Toast.makeText(this, getString(R.string.error_auth), Toast.LENGTH_LONG).show() }
            setResult(RESULT_CANCELED)
        }
    }
}