package com.kirvigen.instagram.stories.autosave.ui.mainScreen

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.databinding.ActivityMainBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.AdapterListProfilesWithStories
import com.kirvigen.instagram.stories.autosave.ui.selectUserScreen.SelectedProfilesResultCallback
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import kotlinx.android.synthetic.main.activity_main.refreshLayout
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private val sessionInteractor: SessionInteractor by inject()
    private var binding: ActivityMainBinding? = null
    private val mainAdapter: AdapterListProfilesWithStories = AdapterListProfilesWithStories()
    private val searchOtherProfiles = registerForActivityResult(SelectedProfilesResultCallback()) { profile ->
        profile?.let { mainViewModel.setSelectedUser(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

//        if (binding?.storiesList?.itemDecorationCount == 0) {
//            binding?.storiesList?.addItemDecoration(
//                GridSpacingItemDecoration(
//                    3,
//                    2.dpToPx,
//                    emptyList(),
//                    includeEdge = false
//                )
//            )
//        }
//        binding?.storiesList?.layoutManager = GridLayoutManager(this, 3)

        sessionInteractor.checkPermission(this)

        binding?.storiesList?.adapter = mainAdapter

        binding?.addUserButton?.setOnClickListener {
            searchOtherProfiles.launch("")
        }
        binding?.refreshLayout?.setOnRefreshListener {
            mainViewModel.refreshData(true)
        }
        binding?.refreshLayout?.setColorSchemeColors(
            toColor(R.color.insta_color_1),
            toColor(R.color.insta_color_2),
            toColor(R.color.insta_color_3),
            toColor(R.color.insta_color_4),
            toColor(R.color.insta_color_5)
        )
        mainViewModel.currentProfile.observe(this, { profile ->
            setCurrentProfile(profile)
        })
        mainViewModel.mainList.observe(this@MainActivity, { items ->
            mainAdapter.submitData(items)
        })
        mainViewModel.refreshingData.observe(this, { isRefresh ->
            binding?.refreshLayout?.isRefreshing = isRefresh
        })
    }

    private fun setCurrentProfile(profile: Profile) {
        binding?.profileImage?.loadImage(profile.photo)
        binding?.profileTitle?.text = profile.name
    }

    private fun toColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(this, colorRes)
    }
}