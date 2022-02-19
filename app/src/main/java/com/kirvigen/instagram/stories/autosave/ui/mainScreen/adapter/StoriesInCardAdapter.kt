package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.base.BaseViewHolder
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesBinding
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesLoadingBinding
import com.kirvigen.instagram.stories.autosave.databinding.ItemViewAllStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.GoToProfile
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.LoadingStoriesItem
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders.StoriesGoToAllViewHolder
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders.StoriesProfileViewHolder

class StoriesInCardAdapter(private val items: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val itemBinding = ItemViewAllStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StoriesGoToAllViewHolder(itemBinding)
            }
            2 -> {
                val itemBinding = ItemStoriesLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                BaseViewHolder(itemBinding.root)
            }
            else -> {
                val itemBinding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                StoriesProfileViewHolder(itemBinding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? StoriesProfileViewHolder)?.bind((items[position] as? Stories) ?: return)
        (holder as? StoriesGoToAllViewHolder)?.bind((items[position] as? GoToProfile) ?: return)
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is GoToProfile -> 1
            is LoadingStoriesItem -> 2
            else -> 0
        }
    }
}