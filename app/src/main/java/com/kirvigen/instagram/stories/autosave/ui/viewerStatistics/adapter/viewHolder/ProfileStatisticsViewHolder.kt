package com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter.viewHolder

import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.base.MenuProfileCreator
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileStatisticsBinding
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.ViewerProfileActivity
import com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter.data.StatisticsProfileItem
import com.kirvigen.instagram.stories.autosave.utils.loadImage

class ProfileStatisticsViewHolder(
    private val binding: ItemProfileStatisticsBinding,
    private val menuProfileCallbacks: MenuProfileCreator.MenuProfileCallbacks
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(statisticsProfileItem: StatisticsProfileItem) {
        binding.profileTitle.text = statisticsProfileItem.nickname
        binding.profileImage.loadImage(statisticsProfileItem.photo)
        binding.textSize.text = statisticsProfileItem.sizeName
        binding.more.setOnCreateContextMenuListener(
            MenuProfileCreator(
                profileId = statisticsProfileItem.profileId,
                profileName = statisticsProfileItem.nickname,
                menuProfileCallbacks = menuProfileCallbacks
            )
        )
        binding.more.setOnClickListener {
            it.showContextMenu()
        }
        binding.mainContainer.setOnClickListener {
            ViewerProfileActivity.openProfile(statisticsProfileItem.profileId, it.context)
        }
    }
}