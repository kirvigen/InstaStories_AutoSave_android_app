package com.kirvigen.instagram.stories.autosave.utils

import androidx.recyclerview.widget.RecyclerView

class AdapterAnyActionObserver(private val callback: (RecyclerView.AdapterDataObserver) -> Unit) :
    RecyclerView.AdapterDataObserver() {

    override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
        super.onItemRangeChanged(positionStart, itemCount)
        callback.invoke(this)
    }

    override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
        super.onItemRangeInserted(positionStart, itemCount)
        callback.invoke(this)
    }

    override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
        super.onItemRangeRemoved(positionStart, itemCount)
        callback.invoke(this)
    }

    override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
        super.onItemRangeMoved(fromPosition, toPosition, itemCount)
        callback.invoke(this)
    }

    override fun onStateRestorationPolicyChanged() {
        super.onStateRestorationPolicyChanged()
        callback.invoke(this)
    }
}