package com.kirvigen.instagram.stories.autosave.ui.viewerProfile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.base.MenuProfileCreator
import com.kirvigen.instagram.stories.autosave.databinding.ActivityProfileViewerBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories.StoriesAdapter
import com.kirvigen.instagram.stories.autosave.utils.GridSpacingItemDecoration
import com.kirvigen.instagram.stories.autosave.utils.WrapContentGridLayoutManager
import com.kirvigen.instagram.stories.autosave.utils.dpToPx
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import com.kirvigen.instagram.stories.autosave.utils.setThrottleOnClickListener
import kotlinx.coroutines.launch
import org.koin.android.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ProfileViewerActivity : AppCompatActivity(), MenuProfileCreator.MenuProfileCallbacks {

    private val profileId by lazy { intent.getLongExtra(PROFILE_EXTRA, -1) }
    private val viewModel: ViewerProfileViewModel by viewModel { parametersOf(profileId) }
    private var binding: ActivityProfileViewerBinding? = null
    private val adapterStories = StoriesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileViewerBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.back?.setThrottleOnClickListener {
            onBackPressed()
        }

        binding?.recyclerStories?.addItemDecoration(
            GridSpacingItemDecoration(
                COUNT_GRID_COLUMN,
                PADDING_STORIES.dpToPx,
                emptyList(),
                includeEdge = false
            )
        )
        binding?.recyclerStories?.layoutManager = WrapContentGridLayoutManager(this, COUNT_GRID_COLUMN)
        binding?.recyclerStories?.adapter = adapterStories

        viewModel.storiesData.observe(this) { listStories ->
            if (listStories.isNotEmpty()) {
                val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                val dateStart = simpleDateFormat.format(Date(listStories.first().stories.date * 1000))
                val dateEnd = simpleDateFormat.format(Date(listStories.last().stories.date * 1000))
                val countInfoStories =
                    resources.getQuantityString(R.plurals.story_count, listStories.size, listStories.size)
                binding?.countInfo?.text = countInfoStories
                binding?.dateInfo?.text = getString(R.string.date_stories_border, dateEnd, dateStart)
            }
            adapterStories.submitStories(listStories)
        }
        viewModel.profileData.observe(this) { profile ->
            binding?.profileImage?.loadImage(profile?.photo ?: "")
            binding?.profileTitle?.text = profile?.nickname ?: ""

            binding?.more?.setThrottleOnClickListener {
                it.showContextMenu()
            }
            binding?.more?.setOnCreateContextMenuListener(
                MenuProfileCreator(profile.nickname, profile.id, this, false)
            )
        }
    }

    override fun onOpenProfileListener(profileId: Long) {
        viewModel.goToProfileInstagram(profileId, this)
    }

    override fun onDeleteProfileListener(profileId: Long) {
        lifecycleScope.launch {
            viewModel.deleteProfile(profileId)
            finish()
        }
    }

    companion object {
        private const val COUNT_GRID_COLUMN = 3
        private const val PADDING_STORIES = 2
        private const val PROFILE_EXTRA = "profile"

        fun openProfile(profileId: Long, context: Context) {
            val intent = Intent(context, ProfileViewerActivity::class.java)
            intent.putExtra(PROFILE_EXTRA, profileId)
            context.startActivity(intent)
        }
    }
}