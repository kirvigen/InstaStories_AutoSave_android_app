package com.kirvigen.instagram.stories.autosave.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

@SuppressLint("CheckResult")
fun ImageView?.loadImage(url: String, centerCrop: Boolean = true) {
    val builder = Glide.with(this ?: return)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
    if (centerCrop)
        builder.centerCrop()
    builder.into(this)
}