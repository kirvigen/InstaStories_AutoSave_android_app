package com.kirvigen.instagram.stories.autosave.utils

import android.util.Log
import com.kirvigen.instagram.stories.autosave.utils.Constans.Companion.USER_AGENT_DEFAULT
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class OkHttpClientCoroutine {

    val okHttpclient = OkHttpClient()

    companion object {
        fun buildGetRequest(url: String, headers: Map<String, String>? = null): Request {
            return Request.Builder()
                .url(url)
                .apply {
                    headers?.forEach {
                        addHeader(it.key, it.value)
                    }
                }
                .build()
        }
    }

    suspend fun newCall(request: Request): MyResult {
        return suspendCoroutine { ret ->
            okHttpclient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                    ret.resume(MyResult.Error(e.message.toString()))
                }

                override fun onResponse(call: Call, response: Response) {
                    try {
//                        Log.e("Cookie (${request.url})", request.headers["cookie"] ?: "")
//                        Log.e("Response Headers (${request.url})", response.headers.toString())
                        ret.resume(MyResult.Success(response.body?.string().toString()))
                    } catch (e: Exception) {
                        ret.resume(MyResult.Error(e.message.toString()))
                    }
                }
            })
        }
    }
}

fun MyResult.toStrResponse(): String? {
    return when (this) {
        is MyResult.Error -> {
            Log.e(this.javaClass.simpleName, "Response Error ${this.message}")
            null
        }
        is MyResult.Success -> {
            this.data
        }
    }
}

sealed class MyResult {
    class Success(val data: String) : MyResult()
    class Error(val message: String, exception: Exception? = null) : MyResult()
}

class BadResponseException : Exception()