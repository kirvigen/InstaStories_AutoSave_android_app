package com.kirvigen.instagram.stories.autosave.ui.welcomeScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.kirvigen.instagram.stories.autosave.ui.instagramAuth.AuthInstagramResultCallback
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.MainActivity
import com.kirvigen.instagram.stories.autosave.databinding.ActivityWelcomeBinding
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.utils.animateAlpha
import org.koin.android.ext.android.inject

class WelcomeActivity : AppCompatActivity() {

    private val sessionInteractor: SessionInteractor by inject()
    private var binding: ActivityWelcomeBinding? = null

    private val authInstResult = registerForActivityResult(AuthInstagramResultCallback()) {
       goToMainActivity()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcomeBinding.inflate(layoutInflater)

        if (sessionInteractor.isAuth()) {
            goToMainActivity()
        }

        setContentView(binding?.root)

        binding?.authBtn?.setOnClickListener {
            authInstResult.launch("")
        }

        binding?.mainContainer?.animateAlpha(1f)
    }

    private fun goToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}