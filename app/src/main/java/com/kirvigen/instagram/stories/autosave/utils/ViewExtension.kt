package com.kirvigen.instagram.stories.autosave.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
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

fun Activity.setTransparentStatusBar() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = Color.argb(0, 0, 0, 0)
}