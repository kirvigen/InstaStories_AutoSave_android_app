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

    private val headers: MutableMap<String, String> = mutableMapOf(
        "user-agent" to USER_AGENT_DEFAULT,
        "cookie" to getInstagramCookies()
    )

    override fun getInstagramCookies(): String =
        CookieManager.getInstance()?.getCookie("https://www.instagram.com/") ?: ""

    override suspend fun getCurrentProfile(): Profile? {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest("https://www.instagram.com/", headers)
        ).toStrResponse() ?: ""

        val jsonString = response.split("window._sharedData =")[1].split(";</script>")[0]
        val profileData = JSONObject(jsonString)
            .getJSONObject("config")
            .getJSONObject("viewer")
        val photo = profileData.getString("profile_pic_url_hd")
        val username = profileData.getString("username")
        val description = profileData.getString("biography")
        val id = profileData.getString("id")
        return Profile(id, photo, username, description)
    }

    override suspend fun getStories(userId: String): List<String> {
        TODO("todo")
    }

    override suspend fun getFollowers(): List<Profile> {
        TODO("todo")
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