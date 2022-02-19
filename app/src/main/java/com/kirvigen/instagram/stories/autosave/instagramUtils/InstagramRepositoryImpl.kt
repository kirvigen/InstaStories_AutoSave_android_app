package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.ProfileDao
import com.kirvigen.instagram.stories.autosave.instagramUtils.db.StoriesDao
import com.kirvigen.instagram.stories.autosave.utils.OkHttpClientCoroutine
import com.kirvigen.instagram.stories.autosave.utils.toStrResponse
import com.kirvigen.instagram.stories.autosave.utils.valueOrNull
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
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

    override fun saveInstagramCookies(cookies: String) {
        sharedPreferences.edit().putString(SAVED_COOKIES_KEY, cookies).apply()
    }

    override fun deleteProfile(profileId: Long) {
        launch { profileDao.deleteProfile(profileId) }
    }

    override fun getInstagramCookies(): String = sharedPreferences.getString(SAVED_COOKIES_KEY, "") ?: ""

    override suspend fun getCurrentProfile(): Profile? {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest(INSTAGRAM_MAIN_URL, getInstagramHeaders())
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
            return@valueOrNull Profile(
                id,
                photo,
                username,
                description,
                username,
                System.currentTimeMillis(),
                isCurrentProfile = true
            )
        }

        return profile
    }

    override fun getStoriesUser(userId: Long): LiveData<List<Stories>> = storiesDao.getStoriesProfile(userId)

    override suspend fun getStoriesProfile(profileId: Long): List<Stories> = storiesDao.getStoriesProfileSync(profileId)

    override suspend fun deleteStories(storiesId: Long) = storiesDao.deleteStories(storiesId)

    override suspend fun updateStoriesLocalUrl(storiesId: Long, localUrl: String) {
        storiesDao.updateStoriesLocalUri(storiesId, localUrl)
    }

    override suspend fun loadActualStories(userId: Long): List<Stories> {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest(
                "https://i.instagram.com/api/v1/feed/user/$userId/story/",
                getInstagramHeaders()
            )
        ).toStrResponse() ?: return emptyList()
        val stories = parseStories(response, userId)

        coroutineScope {
            async(Dispatchers.IO) {
                storiesDao.insert(stories)
                profileDao.profileSetLastUpdate(userId)
            }
        }
        return stories
    }

    override suspend fun searchProfile(searchText: String): List<Profile> {
        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest(
                "https://www.instagram.com/web/search/topsearch/?context=blended&query=${searchText}&rank_token=&include_reel=true",
                getInstagramHeaders()
            )
        ).toStrResponse() ?: return emptyList()

        val result = valueOrNull {
            val list = mutableListOf<Profile>()
            val arrayObj = JSONObject(response).getJSONArray("users")
            for (i in 0 until arrayObj.length()) {
                val item = arrayObj.getJSONObject(i).getJSONObject("user")
                val id = item.getLong("pk")
                val name = item.getString("full_name")
                val description = item.optString("biography") ?: ""
                val photo = item.getString("profile_pic_url")
                val username = item.getString("username")
                list.add(Profile(id, photo, name, description, username, System.currentTimeMillis()))
            }
            list
        }


        return result ?: emptyList()
    }

    override suspend fun getProfile(stories: Stories): Profile? = profileDao.getProfile(stories.userId)

    override suspend fun getProfile(profileId: Long): Profile? = profileDao.getProfile(profileId)

    override suspend fun getProfile(nickname: String): Profile? {
        val savedProfile = profileDao.getProfile(nickname)
        if (savedProfile != null) return savedProfile

        val response = okHttpClientCoroutine.newCall(
            OkHttpClientCoroutine.buildGetRequest("https://www.instagram.com/${nickname}/", getInstagramHeaders())
        ).toStrResponse() ?: return null

        val profile = valueOrNull {
            val jsonString = response.split("window._sharedData =")[1].split(";</script>")[0]
            val mainObj = JSONObject(jsonString)
                .getJSONObject("entry_data")
                .getJSONArray("ProfilePage")
                .getJSONObject(0)

            val userId = mainObj.getString("logging_page_id")
                .split("_")[1].toLong()

            val userObj = mainObj.getJSONObject("graphql").getJSONObject("user")

            val name = userObj.getString("full_name")
            val description = userObj.optString("biography") ?: ""
            val photo = userObj.getString("profile_pic_url")

            return@valueOrNull Profile(userId, photo, name, description, nickname, System.currentTimeMillis())

        }

        profile?.let { profileDao.insert(profile) }

        return profile
    }

    override fun saveInstagramHeaders(headers: Map<String, String>) {
        val headersJson = Gson().toJson(headers)
        sharedPreferences.edit().putString(SAVED_HEADERS_KEY, headersJson).apply()
    }

    override fun getProfiles(): LiveData<List<Profile>> = profileDao.getAllProfiles()

    override fun getStories(): LiveData<List<Stories>> = storiesDao.getAllStories()

    override suspend fun saveProfiles(profiles: List<Profile>) {
        profileDao.insert(profiles.map { it.copy(insertTime = System.currentTimeMillis()) })
    }

    override suspend fun getProfilesSync(): List<Profile> = profileDao.getProfilesSync()

    override suspend fun getStoriesSync(): List<Stories> = storiesDao.getAllStoriesSync()

    private fun getInstagramHeaders(): Map<String, String> {
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
                    val contents = item.getJSONObject("image_versions2")
                        .getJSONArray("candidates")
                    val firstUrl = contents.getJSONObject(0).getString("url")
                    if (firstUrl.contains(".webp")) {
                        contents.getJSONObject(1).getString("url")
                    } else {
                        firstUrl
                    }

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
        const val INSTAGRAM_MAIN_URL = "https://www.instagram.com/"
        private const val SAVED_COOKIES_KEY = "saved_cookies_key"
        private const val SAVED_HEADERS_KEY = "saved_headers_key"
    }
}