package com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileWithStoriesBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.base.MenuProfileCreator
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.ProfileWithStoriesItem
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.viewHolders.ProfileWithStoriesViewHolder

class AdapterListProfilesWithStories(
    private val menuProfileCallbacks: MenuProfileCreator.MenuProfileCallbacks
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val items = AsyncListDiffer(this, DIFF_CALLBACK)

    fun submitData(list: List<ProfileWithStoriesItem>) {
        items.submitList(list)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemProfileWithStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileWithStoriesViewHolder(itemBinding, menuProfileCallbacks)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ProfileWithStoriesViewHolder)?.bind(items.currentList[position])
    }

    override fun getItemCount(): Int = items.currentList.size
}

private val DIFF_CALLBACK =
    object : DiffUtil.ItemCallback<ProfileWithStoriesItem>() {
        override fun areItemsTheSame(objOld: ProfileWithStoriesItem, objNew: ProfileWithStoriesItem): Boolean {
            return objOld.id == objNew.id
        }

        override fun areContentsTheSame(objOld: ProfileWithStoriesItem, objNew: ProfileWithStoriesItem): Boolean {
            if (objOld.storiesList.size == objNew.storiesList.size) {
                if (objNew.storiesList.isEmpty()) return true

                return (objOld.storiesList.first() as? Stories)?.id == (objNew.storiesList.first() as? Stories)?.id
            }
            return false
        }
    }