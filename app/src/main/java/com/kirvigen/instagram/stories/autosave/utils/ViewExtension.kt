package com.kirvigen.instagram.stories.autosave.utils

import android.annotation.SuppressLint
import android.widget.ImageView
import com.bumptech.glide.Glide

@SuppressLint("CheckResult")
fun ImageView?.loadImage(url: String) {
    Glide.with(this ?: return)
        .load(url)
        .into(this)
}