package com.kirvigen.instagram.stories.autosave.activity.welcomeScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.activity.instagramAuth.AuthInstagramResultCallback
import com.kirvigen.instagram.stories.autosave.activity.mainScreen.MainActivity
import com.kirvigen.instagram.stories.autosave.databinding.ActivityWelcomeBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class WelcomeActivity : AppCompatActivity() {

    private val instagramRepository: InstagramRepository by inject()
    private var binding: ActivityWelcomeBinding? = null

    private val authInstResult = registerForActivityResult(AuthInstagramResultCallback()) {
       goToMainActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        CoroutineScope(Dispatchers.Main).launch {
            if(instagramRepository.getCurrentProfile() != null) {
                goToMainActivity()
            }
        }

        setContentView(binding?.root)

        binding?.authBtn?.setOnClickListener {
            authInstResult.launch("")
        }

        binding?.mainContainer?.animateAlpha(1f)
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}