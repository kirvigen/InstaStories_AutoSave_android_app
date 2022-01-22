package com.kirvigen.instagram.stories.autosave.ui.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.getStringFromMapString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val instagramRepository: InstagramRepository
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    val currentProfile = MutableLiveData<Profile>()

    suspend fun getStoriesData(): LiveData<List<Stories>> =
        instagramRepository.getStoriesUser(instagramRepository.getProfile("kir_vigen")?.id ?: 0)

    init {
        CoroutineScope(Dispatchers.Main).launch {
            instagramRepository.loadActualStories(instagramRepository.getProfile("kir_vigen")?.id ?: return@launch)
        }
    }

    fun loadProfile() {
        launch {
            try {
                val profile = instagramRepository.getCurrentProfile() ?: return@launch
                currentProfile.postValue(profile)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getCurrentUserId(): Long {
        return getStringFromMapString(instagramRepository.getInstagramCookies(), "ds_user_id").toLong()
    }
}