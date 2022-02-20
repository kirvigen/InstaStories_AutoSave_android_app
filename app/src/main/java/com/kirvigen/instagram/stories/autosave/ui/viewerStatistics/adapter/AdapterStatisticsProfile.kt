package com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.base.MenuProfileCreator
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileStatisticsBinding
import com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter.data.StatisticsProfileItem
import com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter.viewHolder.ProfileStatisticsViewHolder

class AdapterStatisticsProfile(
    private val menuProfileCallbacks: MenuProfileCreator.MenuProfileCallbacks
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemProfileStatisticsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileStatisticsViewHolder(itemBinding, menuProfileCallbacks)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as? ProfileStatisticsViewHolder)?.bind(items.currentList[position])
    }

    override fun getItemCount(): Int = items.currentList.size

    fun submitList(list: List<StatisticsProfileItem>) {
        items.submitList(list)
    }
}

private val DIFF_CALLBACK =
    object : DiffUtil.ItemCallback<StatisticsProfileItem>() {
        override fun areItemsTheSame(objOld: StatisticsProfileItem, objNew: StatisticsProfileItem): Boolean {
            return objOld.profileId == objNew.profileId
        }

        override fun areContentsTheSame(objOld: StatisticsProfileItem, objNew: StatisticsProfileItem): Boolean {
            return objOld.size == objOld.size
        }
    }