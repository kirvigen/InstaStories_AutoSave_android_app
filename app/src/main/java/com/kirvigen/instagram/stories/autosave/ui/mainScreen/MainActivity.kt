package com.kirvigen.instagram.stories.autosave.ui.mainScreen

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapterStories.StoriesAdapter
import com.kirvigen.instagram.stories.autosave.ui.selectUserScreen.SelectedProfilesResultCallback
import com.kirvigen.instagram.stories.autosave.databinding.ActivityMainBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.utils.GridSpacingItemDecoration
import com.kirvigen.instagram.stories.autosave.utils.dpToPx
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val sessionInteractor: SessionInteractor by inject()
    private var binding: ActivityMainBinding? = null
    private val searchOtherProfiles = registerForActivityResult(SelectedProfilesResultCallback()) {
         Log.e(this.javaClass.simpleName, it.toString())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        if (binding?.storiesList?.itemDecorationCount == 0) {
            binding?.storiesList?.addItemDecoration(
                GridSpacingItemDecoration(
                    3,
                    2.dpToPx,
                    emptyList(),
                    includeEdge = false
                )
            )
        }

        sessionInteractor.checkPermission(this)

        binding?.storiesList?.layoutManager = GridLayoutManager(this, 3)

        mainViewModel.loadProfile()
        mainViewModel.currentProfile.observe(this, { profile ->
            setCurrentProfile(profile)
        })

        lifecycleScope.launch {
            mainViewModel.getStoriesData().observe(this@MainActivity, { storiesData ->
                setStoriesData(storiesData)
            })
        }


    }

    private fun setStoriesData(storiesData: List<Stories>) {
        binding?.storiesList?.adapter = StoriesAdapter(storiesData)
    }

    private fun setCurrentProfile(profile: Profile) {
        binding?.profileImage?.loadImage(profile.photo)
        binding?.profileTitle?.text = profile.name
    }

}