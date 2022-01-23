package com.kirvigen.instagram.stories.autosave.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import java.lang.reflect.Type
import kotlin.math.roundToInt

fun getStringFromMapString(mapStr: String, key: String): String {
    return mapStr.split("$key=")[1].split(";")[0].trim()
}

val Int.dpToPx: Int
    get() = (this * (Resources.getSystem().displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT)).roundToInt()

fun <T> valueOrNull(callback: () -> T?): T? {
    return try {
        callback()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun <T, K, R> LiveData<T>.combineWith(
    liveData: LiveData<K>,
    block: (T?, K?) -> R
): LiveData<R> {
    val result = MediatorLiveData<R>()
    result.addSource(this) {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(block(this@combineWith.value, liveData.value))
        }
    }
    result.addSource(liveData) {
        CoroutineScope(Dispatchers.IO).launch {
            result.postValue(block(this@combineWith.value, liveData.value))
        }
    }
    return result
}

inline fun <reified T> String?.toObject(): T? {
    val typeOfMap: Type = object : TypeToken<T>() {}.type
    return try {
        Gson().fromJson(this ?: "", typeOfMap)
    } catch (e: Exception) {
        null
    }
}

fun Any?.toJson(): String {
    return Gson().toJson(this ?: return "")
}
