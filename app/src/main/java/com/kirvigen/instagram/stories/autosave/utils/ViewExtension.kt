package com.kirvigen.instagram.stories.autosave.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide

@SuppressLint("CheckResult")
fun ImageView?.loadImage(url: String) {
    Glide.with(this ?: return)
        .load(url)
        .into(this)
}

fun View?.animateAlpha(alpha: Float, endCallback: (() -> Unit)? = null) {
    this?.animate()
        ?.alpha(alpha)
        ?.setInterpolator(DecelerateInterpolator())
        ?.withEndAction {
            endCallback?.invoke()
        }
        ?.start()
}