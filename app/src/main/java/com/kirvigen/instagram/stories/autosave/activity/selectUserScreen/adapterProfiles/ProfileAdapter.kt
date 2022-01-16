package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.viewHolders.ProfileViewHolder
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.AsyncListDiffer

class ProfileAdapter(
    private val selectedProfilerChanged: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), ProfileSelector {
    private val profiles = AsyncListDiffer(this, DIFF_CALLBACK)
    private val selectedProfile: MutableList<Profile> = mutableListOf()

    fun setData(profiles: List<Profile>) {
        val mutableList = profiles.toMutableList() as MutableList<Any>
        mutableList.addAll(mutableList.lastIndex + 1, selectedProfile)
        this.profiles.submitList(mutableList.distinctBy { if (it is Profile) it.id else -1 })
    }

    fun getSelectedProfiles(): List<Profile> = selectedProfile.toList()

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        (holder as ProfileViewHolder).clearAnimation()
    }

    override fun onFailedToRecycleView(holder: RecyclerView.ViewHolder): Boolean {
        return true
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemBinding = ItemProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ProfileViewHolder(itemBinding, this)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val profile = profiles.currentList[position]
        (holder as? ProfileViewHolder)?.bind((profile as? Profile) ?: return, profile in selectedProfile)
    }

    override fun getItemCount(): Int = profiles.currentList.size

    override fun onUnselect(profile: Profile) {
        selectedProfile.remove(profile)
        selectedProfilerChanged.invoke()
    }

    override fun onSelect(profile: Profile) {
        selectedProfile.add(profile)
        selectedProfilerChanged.invoke()
    }
}

private val DIFF_CALLBACK =
    object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(objOld: Any, objNew: Any): Boolean {
            return objOld == objNew
        }

        override fun areContentsTheSame(objOld: Any, objNew: Any): Boolean {
            return if (objOld is Profile && objNew is Profile) {
                objOld.name == objNew.name
            } else {
                false
            }
        }
    }