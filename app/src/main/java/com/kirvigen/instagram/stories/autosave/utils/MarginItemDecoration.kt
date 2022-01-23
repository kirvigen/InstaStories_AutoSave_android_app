package com.kirvigen.instagram.stories.autosave.utils

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

const val defaultMargin = -99999

data class MarginParamsDp(
    val top: Int = defaultMargin,
    val bottom: Int = defaultMargin,
    val left: Int = defaultMargin,
    val right: Int = defaultMargin
) {
    companion object {
        fun top(dp: Int) = MarginParamsDp(top = dp)
    }
}

class MarginItemDecoration(
    private val marginParams: MarginParamsDp,
    private val viewType: Int = 0,
    private val position: Int = -1,
    private val positionDisable: Array<Int> = arrayOf()
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        val adapter = parent.adapter
        val itemViewType = adapter?.getItemViewType(itemPosition)
        if (itemViewType != viewType || itemPosition in positionDisable) return

        if (position != -1) {
            if (position == itemPosition)
                addMargin(outRect)
        } else {
            addMargin(outRect)
        }
    }

    private fun addMargin(outRect: Rect) {
        if (marginParams.bottom != defaultMargin)
            outRect.bottom = marginParams.bottom.dpToPx

        if (marginParams.top != defaultMargin)
            outRect.top = marginParams.top.dpToPx

        if (marginParams.left != defaultMargin)
            outRect.left = marginParams.left.dpToPx

        if (marginParams.right != defaultMargin)
            outRect.right = marginParams.right.dpToPx
    }
}