package com.kirvigen.instagram.stories.autosave.utils

import android.annotation.SuppressLint
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@SuppressLint("CheckResult")
fun ImageView?.loadImage(url: String, crossFade: Boolean = true) {
    Glide.with(this ?: return)
        .load(url)
        .apply {
            if (crossFade)
                transition(DrawableTransitionOptions.withCrossFade())
        }
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