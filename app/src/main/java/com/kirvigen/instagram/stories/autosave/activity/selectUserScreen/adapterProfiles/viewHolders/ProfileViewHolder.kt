package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.viewHolders

import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.loadImage

class ProfileViewHolder(
    private val selectedList: MutableList<Profile>,
    private val binding: ItemProfileBinding
) : RecyclerView.ViewHolder(binding.root) {

    private var selectedLocal: Boolean = false

    fun bind(profile: Profile, isSelected: Boolean) {
        selectedLocal = isSelected
        binding.profileImage.loadImage(profile.photo)
        binding.name.text = profile.name
        binding.nickname.text = profile.nickname

        binding.container.setOnClickListener {
            if (selectedLocal) {
                selectedList.remove(profile)
                setUnselectState()
            } else {
                selectedList.add(profile)
                setSelectState()
            }
            selectedLocal = !selectedLocal
        }

        binding.bgInstagram.isVisible = false

        if (selectedLocal) {
            setSelectState()
        } else {
            setUnselectState()
        }
    }

    fun clearAnimation() {
        binding.profileImage.clearAnimation()
    }

    private fun setSelectState() {
        binding.bgInstagram.isVisible = true
        binding.profileImage
            .animate()
            .scaleX(SCALE_SELECTED)
            .scaleY(SCALE_SELECTED)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun setUnselectState() {
        binding.profileImage
            .animate()
            .scaleX(1f)
            .scaleY(1f)
            .withEndAction {
                binding.bgInstagram.isVisible = false
            }
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    companion object {
        private const val SCALE_SELECTED = 0.8f
    }
}