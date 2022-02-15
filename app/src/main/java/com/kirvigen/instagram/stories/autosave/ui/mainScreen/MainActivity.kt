package com.kirvigen.instagram.stories.autosave.ui.mainScreen

import android.os.Bundle
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.databinding.ActivityMainBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.AdapterListProfilesWithStories
import com.kirvigen.instagram.stories.autosave.ui.selectUserScreen.SelectedProfilesResultCallback
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.utils.AdapterAnyActionObserver
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(), MenuProfileCreator.MenuProfileCallbacks {

    private val mainViewModel: MainViewModel by viewModel()
    private val sessionInteractor: SessionInteractor by inject()
    private var binding: ActivityMainBinding? = null
    private val mainAdapter: AdapterListProfilesWithStories = AdapterListProfilesWithStories(this)
    private val searchOtherProfiles = registerForActivityResult(SelectedProfilesResultCallback()) { profiles ->
        profiles?.let {
            mainViewModel.setSelectedUser(it)
            mainAdapter.registerAdapterDataObserver(AdapterAnyActionObserver { adapterObserver ->
                binding?.storiesList?.scrollToPosition(0)
                mainAdapter.unregisterAdapterDataObserver(adapterObserver)
            })
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        sessionInteractor.checkPermissionBattery(this)

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
        mainViewModel.currentProfile.observe(this) { profile ->
            setCurrentProfile(profile)
        }
        mainViewModel.mainList.observe(this@MainActivity) { items ->
            mainAdapter.submitData(items)
        }
        mainViewModel.refreshingData.observe(this) { isRefresh ->
            binding?.refreshLayout?.isRefreshing = isRefresh
        }
    }

    override fun onOpenProfileListener(profileId: Long) {
        mainViewModel.goToProfileInstagram(profileId, this)
    }

    override fun onDeleteProfileListener(profileId: Long) {
        mainViewModel.deleteProfile(profileId)
    }

    private fun setCurrentProfile(profile: Profile) {
        binding?.profileImage?.loadImage(profile.photo)
        binding?.profileTitle?.text = profile.name
    }

    private fun toColor(@ColorRes colorRes: Int): Int {
        return ContextCompat.getColor(this, colorRes)
    }
}