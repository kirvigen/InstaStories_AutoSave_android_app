package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.util.Log
import android.webkit.CookieManager
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.utils.BadResponseException
import com.kirvigen.instagram.stories.autosave.utils.Constans.Companion.USER_AGENT_DEFAULT
import com.kirvigen.instagram.stories.autosave.utils.MyResult
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import com.kirvigen.instagram.stories.autosave.utils.toStrResponse
import org.json.JSONObject

class InstagramRepositoryImpl(
    private val okHttpClientCoroutine: OkHttpClientCoroutine
) : InstagramRepository {

    private val instaCookies: String
        get() = CookieManager.getInstance()?.getCookie("https://www.instagram.com/") ?: ""

    private val headers: MutableMap<String, String> = mutableMapOf(
        "user-agent" to USER_AGENT_DEFAULT,
        "cookie" to instaCookies
    )

    override fun getInstagramCookies(): String = instaCookies

    override fun getCurrentProfile(): Profile {
        TODO("Not yet implemented")
    }

    override fun getStories(userId: String) {
    }

    override fun getFollowers(): List<Profile> {
        TODO("блин капец потом")
    }

    override suspend fun getUserId(nickname: String): MyResult {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest("https://www.instagram.com/${nickname}/", headers)
        ).toStrResponse() ?: return MyResult.Error("internetError", BadResponseException())

        val jsonString = response.split("window._sharedData =")[1].split(";</script>")[0]
        val userId = JSONObject(jsonString)
            .getJSONObject("entry_data")
            .getJSONArray("ProfilePage")
            .getJSONObject(0)
            .getString("logging_page_id")
            .split("_")[1]

        return MyResult.Success(userId)
    }
}