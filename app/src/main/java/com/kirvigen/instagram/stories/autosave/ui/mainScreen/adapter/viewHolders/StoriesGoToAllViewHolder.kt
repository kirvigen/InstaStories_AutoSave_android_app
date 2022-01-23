package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders

import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemViewAllStoriesBinding
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.GoToAllItem

class StoriesGoToAllViewHolder(
    private val binding: ItemViewAllStoriesBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item:GoToAllItem) {
        binding.container.setOnClickListener {
            TODO("go to profile")
        }
    }

}