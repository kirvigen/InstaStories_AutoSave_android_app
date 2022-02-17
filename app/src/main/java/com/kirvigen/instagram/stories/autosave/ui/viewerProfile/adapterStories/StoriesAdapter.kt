package com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesMatchParentBinding
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories.data.StoriesViewData
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories.viewHolders.StoriesViewHolder

class StoriesAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = AsyncListDiffer(this, DIFF_CALLBACK_STORIES)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemStoriesMatchParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? StoriesViewHolder)?.bind(items.currentList[position])
    }

    override fun getItemCount(): Int = items.currentList.size

    fun submitStories(listStories: List<StoriesViewData>) {
        items.submitList(listStories)
    }
}

private val DIFF_CALLBACK_STORIES =
    object : DiffUtil.ItemCallback<StoriesViewData>() {
        override fun areItemsTheSame(objOld: StoriesViewData, objNew: StoriesViewData): Boolean {
            return objOld.stories.id == objNew.stories.id
        }

        override fun areContentsTheSame(objOld: StoriesViewData, objNew: StoriesViewData): Boolean {
            return true
        }
    }