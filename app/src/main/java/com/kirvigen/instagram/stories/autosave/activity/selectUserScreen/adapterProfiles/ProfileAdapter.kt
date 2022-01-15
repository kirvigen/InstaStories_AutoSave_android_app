package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.viewHolders.ProfileViewHolder
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.AsyncListDiffer

class ProfileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val profiles = AsyncListDiffer(this, DIFF_CALLBACK)
    private val selectedProfile: MutableList<Profile> = mutableListOf()

    fun setData(profiles: List<Profile>) {
        val mutableList = profiles.toMutableList()
        mutableList.addAll(mutableList.lastIndex + 1, selectedProfile)
        this.profiles.submitList(mutableList.distinct())
    }

    fun getSelectedProfiles(): List<Profile> = selectedProfile

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as ProfileViewHolder).clearAnimation()
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(selectedProfile, itemBinding)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val profile = profiles.currentList[position]
        (holder as? ProfileViewHolder)?.bind(profile, profile in selectedProfile)
    }

    override fun getItemCount(): Int = profiles.currentList.size
}

private val DIFF_CALLBACK: DiffUtil.ItemCallback<Profile> =
    object : DiffUtil.ItemCallback<Profile>() {
        override fun areItemsTheSame(oldProfile: Profile, newProfile: Profile): Boolean {
            return oldProfile.id == newProfile.id
        }

        override fun areContentsTheSame(oldProfile: Profile, newProfile: Profile): Boolean {
            return oldProfile.name == newProfile.name
        }
    }