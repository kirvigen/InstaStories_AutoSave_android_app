package com.kirvigen.instagram.stories.autosave.ui.mainScreen

import android.view.ContextMenu
import android.view.View
import com.kirvigen.instagram.stories.autosave.R

class MenuProfileCreator(
    private val profileId: Long,
    private val menuProfileCallbacks: MenuProfileCallbacks
): View.OnCreateContextMenuListener {

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
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