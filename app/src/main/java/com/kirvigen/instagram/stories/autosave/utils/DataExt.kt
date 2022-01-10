package com.kirvigen.instagram.stories.autosave.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import kotlin.math.roundToInt

fun getStringFromMapString(mapStr: String, key: String): String {
    return mapStr.split("$key=")[1].split(";")[0].trim()
}

val Int.dpToPx: Int
    get() = (this * (Resources.getSystem().displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()
