package com.kirvigen.instagram.stories.autosave.activity.mainScreen

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.CookieManager
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.utils.MyResult
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val instagramRepositoryImpl = InstagramRepositoryImpl(CookieManager.getInstance(), OkHttpClientCoroutine())
        CoroutineScope(Dispatchers.Main).launch {
            val result = instagramRepositoryImpl.getUserId("superhitriylis")
            when (result) {
                is MyResult.Success -> Log.e("RESULT TEST", result.data)
            }
        }
    }
}