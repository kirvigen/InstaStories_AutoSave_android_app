package com.kirvigen.instagram.stories.autosave.base

import android.content.Intent
import android.view.ContextMenu
import android.view.View
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.ViewerStatisticsActivity

class MenuSessionCreator() : View.OnCreateContextMenuListener {

    override fun onCreateContextMenu(menu: ContextMenu?, v: View?, menuInfo: ContextMenu.ContextMenuInfo?) {
        menu?.add(R.string.statistics)?.setOnMenuItemClickListener {
            v ?: return@setOnMenuItemClickListener true
            val intent = Intent(v.context, ViewerStatisticsActivity::class.java)
            v.context.startActivity(intent)
            return@setOnMenuItemClickListener true
        }
    }
}