package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders

import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.ui.viewerStories.ViewerStoriesActivity
import com.kirvigen.instagram.stories.autosave.utils.loadImage

class StoriesProfileViewHolder(private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(stories: Stories) {
        binding.videoIcon.isVisible = stories.isVideo
        Glide.with(binding.imageView)
            .load(stories.preview)
            .centerCrop()
            .into(binding.imageView)
        binding.imageView.setOnClickListener {
            ViewerStoriesActivity.startView(stories, it.context)
        }
    }
}