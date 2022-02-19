package com.kirvigen.instagram.stories.autosave.base

import android.view.ContextMenu
import android.view.View
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.ProfileViewerActivity

class MenuProfileCreator(
    private val profileName: String,
    private val profileId: Long,
    private val menuProfileCallbacks: MenuProfileCallbacks,
    private val isOpenProfileEnable: Boolean = true
) : View.OnCreateContextMenuListener {

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.setHeaderTitle(profileName)
        if (isOpenProfileEnable) {
            menu?.add(R.string.open_profile)?.setOnMenuItemClickListener {
                ProfileViewerActivity.openProfile(profileId, v?.context ?: return@setOnMenuItemClickListener true)
                return@setOnMenuItemClickListener true
            }
        }
        menu?.add(R.string.go_to_instagram)?.setOnMenuItemClickListener {
            menuProfileCallbacks.onOpenProfileListener(profileId)
            return@setOnMenuItemClickListener true
        }
        menu?.add(R.string.delete)?.setOnMenuItemClickListener {
            menuProfileCallbacks.onDeleteProfileListener(profileId)
            return@setOnMenuItemClickListener true
        }
    }

    interface MenuProfileCallbacks {
        fun onOpenProfileListener(profileId: Long)
        fun onDeleteProfileListener(profileId: Long)
    }
}