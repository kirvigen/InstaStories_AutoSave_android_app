package com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories.viewHolders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.ui.viewerStories.ViewerStoriesActivity
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesBinding
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesMatchParentBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories.data.StoriesViewData
import com.kirvigen.instagram.stories.autosave.utils.loadImage

class StoriesViewHolder(private val binding: ItemStoriesMatchParentBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(storiesViewData: StoriesViewData) {
        val stories = storiesViewData.stories
        binding.day.text = storiesViewData.day
        binding.month.text = storiesViewData.monthName
        binding.videoIcon.isVisible = stories.isVideo
        binding.imageView.loadImage(stories.previewUrl)
        binding.imageView.setOnClickListener {
            ViewerStoriesActivity.startView(stories, it.context)
        }
    }
}