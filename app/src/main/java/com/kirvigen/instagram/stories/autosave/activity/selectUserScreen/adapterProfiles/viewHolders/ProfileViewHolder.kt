package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.viewHolders

import android.view.animation.DecelerateInterpolator
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.activity.selectUserScreen.adapterProfiles.ProfileSelector
import com.kirvigen.instagram.stories.autosave.databinding.ItemProfileBinding
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.animateScale
import com.kirvigen.instagram.stories.autosave.utils.loadImage

class ProfileViewHolder(
    private val binding: ItemProfileBinding,
    private val selector: ProfileSelector
) : RecyclerView.ViewHolder(binding.root) {

    private var selectedLocal: Boolean = false

    fun bind(profile: Profile, isSelected: Boolean) {
        selectedLocal = isSelected
        binding.profileImage.loadImage(profile.photo)
        binding.name.text = profile.name
        binding.nickname.text = profile.nickname

        binding.container.setOnClickListener {
            if (selectedLocal) {
                selector.onUnselect(profile)
                setUnselectState()
            } else {
                selector.onSelect(profile)
                setSelectState()
            }
            selectedLocal = !selectedLocal
        }

        binding.bgInstagram.isVisible = false

        if (selectedLocal) {
            setSelectState(false)
        } else {
            setUnselectState(false)
        }
    }

    fun clearAnimation() {
        binding.profileImage.clearAnimation()
        binding.bgScaled.clearAnimation()
    }

    private fun setSelectState(animated: Boolean = true) {
        binding.bgInstagram.isVisible = true
        binding.profileImage
            .animate()
            .scaleX(SCALE_SELECTED_IMAGE)
            .scaleY(SCALE_SELECTED_IMAGE)
            .setInterpolator(DecelerateInterpolator())
            .apply {
                duration = if (!animated)
                    0
                else {
                    200
                }
            }
            .start()
        binding.bgScaled.animateScale(1f, animated) {
            binding.bgChanged.setImageResource(R.drawable.gradient_instagram)
            binding.bgScaled.animateScale(SCALE_BORDER, animated)
        }
    }

    private fun setUnselectState(animated: Boolean = true) {
        binding.profileImage
            .animate()
            .scaleX(1f)
            .scaleY(1f)
            .withEndAction {
                binding.bgInstagram.isVisible = false
            }
            .setInterpolator(DecelerateInterpolator())
            .apply {
                duration = if (!animated)
                    0
                else {
                    200
                }
            }
            .start()
        binding.bgScaled.animateScale(1f, animated) {
            binding.bgChanged.setImageResource(R.color.gray_border)
            binding.bgScaled.animateScale(SCALE_BORDER, animated)
        }

    }

    companion object {
        private const val SCALE_BORDER = 0.97f
        private const val SCALE_SELECTED_IMAGE = 0.85f
    }
}