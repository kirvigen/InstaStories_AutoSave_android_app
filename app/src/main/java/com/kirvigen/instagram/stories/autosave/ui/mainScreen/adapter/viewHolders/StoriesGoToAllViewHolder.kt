package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemViewAllStoriesBinding
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.GoToProfile
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.ProfileViewerActivity
import com.kirvigen.instagram.stories.autosave.utils.setThrottleOnClickListener

class StoriesGoToAllViewHolder(
    private val binding: ItemViewAllStoriesBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: GoToProfile) {
        binding.container.setThrottleOnClickListener {
            ProfileViewerActivity.openProfile(item.profile.id, it.context)
        }
    }
}