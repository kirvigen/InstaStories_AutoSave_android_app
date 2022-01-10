package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.webkit.CookieManager
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.ProfileDao
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.StoriesDao
import com.kirvigen.instagram.stories.autosave.utils.BadResponseException
import com.kirvigen.instagram.stories.autosave.utils.Constans.Companion.USER_AGENT_DEFAULT
import com.kirvigen.instagram.stories.autosave.utils.MyResult
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import com.kirvigen.instagram.stories.autosave.utils.toStrResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.json.JSONObject
import kotlin.coroutines.CoroutineContext

class InstagramRepositoryImpl(
    private val okHttpClientCoroutine: OkHttpClientCoroutine,
    private val profileDao: ProfileDao,
    private val storiesDao: StoriesDao
) : InstagramRepository, CoroutineScope {

    private val headers: MutableMap<String, String> = mutableMapOf(
        "user-agent" to USER_AGENT_DEFAULT,
        "cookie" to getInstagramCookies(),
        "sec-ch-us" to "\"Chromium\";v=\"60\", , \";Not A Brand\";v=\"99\""
    )

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun getInstagramCookies(): String =
        CookieManager.getInstance()?.getCookie("https://www.instagram.com/") ?: ""

    override suspend fun getCurrentProfile(): Profile {
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
        val id = profileData.getLong("id")
        val profile = Profile(id, photo, username, description, true)

        coroutineScope { async(Dispatchers.IO) { profileDao.insert(profile) } }

        return profile
    }

    override suspend fun getStories(userId: Long): List<Stories> {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest("https://i.instagram.com/api/v1/feed/user/$userId/story/", headers)
        ).toStrResponse() ?: return emptyList()
        val stories = parseStories(response, userId)

        coroutineScope { async(Dispatchers.IO) { storiesDao.insert(stories) } }

        return stories
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
                val idStories = item.getLong("id")
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
}