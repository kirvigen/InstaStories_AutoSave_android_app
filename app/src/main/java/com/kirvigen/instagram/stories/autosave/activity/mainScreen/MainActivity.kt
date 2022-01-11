package com.kirvigen.instagram.stories.autosave.activity.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.kirvigen.instagram.stories.autosave.activity.mainScreen.adapter.StoriesAdapter
import com.kirvigen.instagram.stories.autosave.databinding.ActivityMainBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.GridSpacingItemDecoration
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import com.kirvigen.instagram.stories.autosave.utils.dpToPx
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (binding?.storiesList?.itemDecorationCount == 0) {
            binding?.storiesList?.addItemDecoration(GridSpacingItemDecoration(3, 2.dpToPx, emptyList()))
        }
        binding?.storiesList?.layoutManager = GridLayoutManager(this, 3)



        mainViewModel.loadProfile()
        mainViewModel.currentProfile.observe(this, { profile ->
            setCurrentProfile(profile)
        })
        mainViewModel.storiesData.observe(this, { storiesData ->
            setStoriesData(storiesData)
        })
    }

    private fun setStoriesData(storiesData: List<Stories>) {
        binding?.storiesList?.adapter = StoriesAdapter(storiesData)
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