package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.content.SharedPreferences
import android.webkit.CookieManager
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.ProfileDao
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.StoriesDao
import com.kirvigen.instagram.stories.autosave.utils.BadResponseException
import com.kirvigen.instagram.stories.autosave.utils.MyResult
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import com.kirvigen.instagram.stories.autosave.utils.getStringFromMapString
import com.kirvigen.instagram.stories.autosave.utils.toStrResponse
import com.kirvigen.instagram.stories.autosave.utils.valueOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.coroutines.CoroutineContext

class InstagramRepositoryImpl(
    private val okHttpClientCoroutine: OkHttpClientCoroutine,
    private val profileDao: ProfileDao,
    private val storiesDao: StoriesDao,
    private val sharedPreferences: SharedPreferences
) : InstagramRepository, CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun getInstagramCookies(): String {
        val manager = CookieManager.getInstance()
        manager?.flush()
        return manager?.getCookie(INSTAGRAM_MAIN_URL) ?: ""
    }

    override suspend fun getCurrentProfile(): Profile? {
        val savedProfile = profileDao.getCorrectProfile()
        if (savedProfile != null) return savedProfile

        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest(INSTAGRAM_MAIN_URL, getLocalHeaders())
        ).toStrResponse() ?: ""

        val profile = valueOrNull {
            val jsonString = response.split("window._sharedData =")[1].split(";</script>")[0]
            val profileData = JSONObject(jsonString)
                .getJSONObject("config")
                .getJSONObject("viewer")
            val photo = profileData.getString("profile_pic_url_hd")
            val username = profileData.getString("username")
            val description = profileData.getString("biography")
            val id = profileData.getLong("id")
            return@valueOrNull Profile(id, photo, username, description, true)
        }

        coroutineScope { async(Dispatchers.IO) { profileDao.insert(profile ?: return@async) } }

        return profile
    }

    override suspend fun wipeCurrentProfile() {
        saveAuthHeaders(mapOf())
        CookieManager.getInstance()?.setCookie(INSTAGRAM_MAIN_URL, "")
        CookieManager.getInstance()?.flush()

        profileDao.deleteProfile(profileDao.getCorrectProfile() ?: return)
    }

    override suspend fun getStories(userId: Long): List<Stories> {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest(
                "https://i.instagram.com/api/v1/feed/user/$userId/story/",
                getLocalHeaders()
            )
        ).toStrResponse() ?: return emptyList()
        val stories = parseStories(response, userId)

        coroutineScope { async(Dispatchers.IO) { storiesDao.insert(stories) } }

        return stories
    }

    override suspend fun getFollowers(): List<Profile> {
        TODO("todo")
    }

    override fun getUserId(nickname: String): String {
        return getStringFromMapString(getInstagramCookies(), "ds_user_id")
    }

    override fun saveAuthHeaders(headers: Map<String, String>) {
        val headersJson = Gson().toJson(headers)
        sharedPreferences.edit().putString(SAVED_HEADERS_KEY, headersJson).apply()
    }

    private fun getLocalHeaders(): Map<String, String> {
        val typeOfMap: Type = object : TypeToken<Map<String, String>>() {}.type
        val headersString = sharedPreferences.getString(SAVED_HEADERS_KEY, "") ?: ""
        return try {
            val headers: MutableMap<String, String> = Gson().fromJson(headersString, typeOfMap)

            headers.apply {
                put("cookie", getInstagramCookies())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            mapOf()
        }
    }

    private fun parseStories(jsonString: String, userId: Long): List<Stories> {
        val result = mutableListOf<Stories>()

        try {
            val jsonData = JSONObject(jsonString).getJSONObject("reel").getJSONArray("items")
            for (i in 0 until jsonData.length()) {
                val item = jsonData.getJSONObject(i)
                val data = item.getLong("taken_at")
                val media = item.getJSONObject("image_versions2")
                    .getJSONArray("candidates")
                    .getJSONObject(2)
                    .getString("url")
                val idStories = item.getString("id").split("_")[0].toLong()
                val isVideo = item.optJSONArray("video_versions") != null
                val sourceMedia = if (isVideo) {
                    item.getJSONArray("video_versions")
                        .getJSONObject(0).getString("url")
                } else {
                    item.getJSONObject("image_versions2")
                        .getJSONArray("candidates")
                        .getJSONObject(0)
                        .getString("url")
                }

                result.add(
                    Stories(
                        id = idStories,
                        sourceMedia = sourceMedia,
                        isVideo = isVideo,
                        userId = userId,
                        date = data,
                        preview = media
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return result
    }

    companion object {
        private const val INSTAGRAM_MAIN_URL = "https://www.instagram.com/"
        private const val SAVED_HEADERS_KEY = "saved_headers_key"
    }
}