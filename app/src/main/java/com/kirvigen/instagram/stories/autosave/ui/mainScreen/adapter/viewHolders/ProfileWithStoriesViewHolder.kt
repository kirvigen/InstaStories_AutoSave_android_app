package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileWithStoriesBinding
import com.kirvigen.instagram.stories.autosave.base.MenuProfileCreator
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.StoriesInCardAdapter
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.ProfileWithStoriesItem
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.ViewerProfileActivity
import com.kirvigen.instagram.stories.autosave.utils.MarginItemDecoration
import com.kirvigen.instagram.stories.autosave.utils.MarginParamsDp
import com.kirvigen.instagram.stories.autosave.utils.loadImage
import com.kirvigen.instagram.stories.autosave.utils.setThrottleOnClickListener

class ProfileWithStoriesViewHolder(
    private val binding: ItemProfileWithStoriesBinding,
    private val menuProfileCallbacks: MenuProfileCreator.MenuProfileCallbacks
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ProfileWithStoriesItem) {
        binding.profileImage.loadImage(item.photo)
        binding.profileTitle.text = item.name
        binding.emptyStories.isVisible = item.storiesList.isEmpty()
        binding.recyclerStories.adapter = StoriesInCardAdapter(item.storiesList)

        binding.container.setOnCreateContextMenuListener(
            MenuProfileCreator(item.name, item.id, menuProfileCallbacks)
        )

        binding.container.setThrottleOnClickListener {
            ViewerProfileActivity.openProfile(item.id, it.context)
        }

        while (binding.recyclerStories.itemDecorationCount != 0) {
            binding.recyclerStories.removeItemDecorationAt(0)
        }

        binding.recyclerStories.addItemDecoration(
            MarginItemDecoration(
                marginParams = MarginParamsDp(left = 16, right = 2),
                position = 0
            )
        )

        binding.recyclerStories.addItemDecoration(
            MarginItemDecoration(
                marginParams = MarginParamsDp(right = 2, left = 2),
                positionDisable = arrayOf(0, item.storiesList.lastIndex)
            )
        )


    }
}