package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesBinding
import com.kirvigen.instagram.stories.autosave.databinding.ItemViewAllStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.GoToAllItem
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders.StoriesGoToAllViewHolder
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders.StoriesProfileViewHolder
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapterStories.viewHolders.StoriesViewHolder

class StoriesInCardAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 1) {
            val itemBinding = ItemViewAllStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            StoriesGoToAllViewHolder(itemBinding)
        } else {
            val itemBinding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            StoriesProfileViewHolder(itemBinding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? StoriesProfileViewHolder)?.bind((items[position] as? Stories) ?: return)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        if (items[position] is GoToAllItem)
            return 1
        return 0
    }
}