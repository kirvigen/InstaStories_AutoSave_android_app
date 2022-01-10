package com.kirvigen.instagram.stories.autosave.activity.mainScreen.adapter.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.loadImage

class StoriesViewHolder(private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(stories: Stories) {
        binding.imageView.loadImage(stories.preview)
    }
}