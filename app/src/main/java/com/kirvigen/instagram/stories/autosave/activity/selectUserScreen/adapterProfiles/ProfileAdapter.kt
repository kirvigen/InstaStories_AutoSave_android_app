package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.viewHolders.ProfileViewHolder
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

class ProfileAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var profiles: List<Profile> = emptyList()
    private val selectedProfile: MutableList<Profile> = mutableListOf()

    fun setData(profiles: List<Profile>) {
        this.profiles = profiles
        notifyDataSetChanged()
    }

    fun getSelectedProfiles(): List<Profile> = profiles.filter { it in selectedProfile }

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
        val profile = profiles[position]
        (holder as? ProfileViewHolder)?.bind(profile, profile in selectedProfile)
    }

    override fun getItemCount(): Int = profiles.size
}