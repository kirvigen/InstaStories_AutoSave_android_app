package com.kirvigen.instagram.stories.autosave.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

@SuppressLint("CheckResult")
fun ImageView?.loadImage(url: String, crossFade: Boolean = true) {
    if (crossFade) this?.alpha = 0f
    Glide.with(this ?: return)
        .load(url)
        .listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: Target<Drawable?>?,
                isFirstResource: Boolean
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable?,
                model: Any?,
                target: Target<Drawable?>?,
                dataSource: DataSource?,
                isFirstResource: Boolean
            ): Boolean {
                if (crossFade) {
                    this@loadImage.animateAlpha(1f)
                }

                return false
            }
        })
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

fun View?.animateScale(scale: Float, animated: Boolean = true, endCallback: (() -> Unit)? = null) {
    this?.animate()
        ?.scaleY(scale)
        ?.scaleX(scale)
        ?.withEndAction {
            endCallback?.invoke()
        }?.apply {
            duration = if (!animated)
                0
            else {
                150
            }
        }
        ?.start()
}

fun Activity.setTransparentStatusBar() {
    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    window.statusBarColor = Color.argb(0, 0, 0, 0)
}

fun View?.hideKeyboard() {
    val imm = this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    imm?.hideSoftInputFromWindow(this?.windowToken ?: return, 0)
}

fun View?.showKeyboard() {
    val inputMethodManager = this?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
    inputMethodManager?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
}

fun Context?.loadBitmap(url: String, callback: (Bitmap?) -> Unit) {
    Glide.with(this ?: return)
        .asBitmap()
        .load(url)
        .into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                callback(resource)
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                callback(null)
            }
        });
}

private const val CLICK_DELAY_MILLIS = 1000L

fun View?.setThrottleOnClickListener(callback: (view: View) -> Unit) {
    var lastClickTime = 0L

    this?.setOnClickListener {
        val currentTimeMillis = System.currentTimeMillis()

        if (currentTimeMillis - lastClickTime > CLICK_DELAY_MILLIS) {
            lastClickTime = currentTimeMillis
            callback.invoke(it)
        }
    }
}