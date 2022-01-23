package com.kirvigen.instagram.stories.autosave.ui.mainScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.GoToAllItem
import com.kirvigen.instagram.stories.autosave.utils.getStringFromMapString
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import com.kirvigen.instagram.stories.autosave.ui.mainScreen.adapter.data.ProfileWithStoriesItem
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.utils.combineWith
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay

class MainViewModel(
    private val instagramRepository: InstagramRepository,
    private val instagramInteractor: InstagramInteractor,
    private val sessionInteractor: SessionInteractor
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var job: Job? = null
    private val allProfiles = instagramRepository.getProfiles()
    private val allStories = instagramRepository.getStories()

    val refreshingData = MutableLiveData(false)
    val currentProfile = MutableLiveData<Profile>(sessionInteractor.getCurrentProfile())

    val mainList = allProfiles.combineWith(allStories) { profiles, stories ->
        return@combineWith mutableListOf<ProfileWithStoriesItem>().apply {
            profiles?.reversed()?.forEach { profile ->
                val filteredStories = stories?.filter { it.userId == profile.id } ?: emptyList()
                val storiesMapped: List<Any> = if (filteredStories.size < COUNT_VISIBLE_STORIES) {
                    filteredStories
                } else {
                    (filteredStories.subList(0, 5).toMutableList() as? MutableList<Any>)
                        ?.apply {
                            add(GoToAllItem())
                        }?.toList() ?: emptyList()
                }
                add(
                    ProfileWithStoriesItem(
                        id = profile.id,
                        name = profile.nickname,
                        photo = profile.photo,
                        storiesList = storiesMapped
                    )
                )
            }
        }
    }

    init {
        CoroutineScope(Dispatchers.Main).launch {
            instagramRepository.loadActualStories(instagramRepository.getProfile("kir_vigen")?.id ?: return@launch)
        }
        loadCurrentProfile()
        refreshData()
    }

    suspend fun getStoriesData(): LiveData<List<Stories>> =
        instagramRepository.getStoriesUser(instagramRepository.getProfile("kir_vigen")?.id ?: 0)

    fun setSelectedUser(profiles: List<Profile>) {
        launch {
            instagramInteractor.savedSelectedProfile(profiles)
            refreshData(true)
        }
    }

    fun refreshData(isUiRefresh: Boolean = false) {
        refreshingData.postValue(isUiRefresh)
        job?.cancel()
        job = launch {
            instagramInteractor.loadStoriesForAllProfile()
            refreshingData.postValue(false)
        }
        job?.start()

        launch {
            delay(5000)
            refreshingData.postValue(false)
        }
    }

    private fun loadCurrentProfile() {
        launch {
            val profile = sessionInteractor.loadCurrentProfile() ?: return@launch
            currentProfile.postValue(profile)
        }
    }

    private fun getCurrentUserId(): Long {
        return getStringFromMapString(instagramRepository.getInstagramCookies(), "ds_user_id").toLong()
    }

    companion object {
        private const val COUNT_VISIBLE_STORIES = 5
    }
}