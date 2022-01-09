package com.kirvigen.instagram.stories.autosave.activity.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.databinding.ActivityMainBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.MyResult
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val instagramInteractor: InstagramInteractor by inject()
    private val mainViewModel: MainViewModel by viewModel()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        mainViewModel.loadProfile()
        mainViewModel.currentProfile.observe(this, { profile ->
            setCurrentProfile(profile)
        })
    }

    private fun setCurrentProfile(profile: Profile) {
        binding?.profileImage?.loadImage(profile.photo)

        binding?.profileTitle?.text = profile.name
    }

    override fun onResume() {
        super.onResume()
        mainViewModel.onResume()
    }
}