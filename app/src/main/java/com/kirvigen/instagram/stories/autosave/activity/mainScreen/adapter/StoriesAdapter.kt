package com.kirvigen.instagram.stories.autosave.activity.mainScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.activity.mainScreen.adapter.viewHolders.StoriesViewHolder
import com.kirvigen.instagram.stories.autosave.databinding.ItemStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

class StoriesAdapter(private val storiesData: List<Stories>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? StoriesViewHolder)?.bind(storiesData[position])
    }

    override fun getItemCount(): Int = storiesData.size
}