package com.kirvigen.instagram.stories.autosave.utils

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
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

fun getNameFile(urlFile: String): String {
    return valueOrNull { urlFile.split("?")[0].split("/")[urlFile.split("/").size - 1] } ?: ""
}

fun copyFile(inputPath: String, outputPath: String, fileName: String) {
    try {
        val dir = File(outputPath)
        if (!dir.exists()) {
            dir.mkdirs()
        }

        val inputStream = FileInputStream(inputPath)
        val outputStream = FileOutputStream(outputPath + fileName)
        val buffer = ByteArray(1024)
        var read: Int
        while (inputStream.read(buffer).also { read = it } != -1) {
            outputStream.write(buffer, 0, read)
        }
        inputStream.close()

        // write the output file
        outputStream.flush()
        outputStream.close()
    } catch (fnfe1: FileNotFoundException) {
        Log.e("moveFile", fnfe1.toString())
    } catch (e: Exception) {
        Log.e("moveFile", e.toString())
    }
}
