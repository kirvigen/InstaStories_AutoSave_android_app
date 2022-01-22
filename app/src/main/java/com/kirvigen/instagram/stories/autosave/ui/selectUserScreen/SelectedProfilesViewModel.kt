package com.kirvigen.instagram.stories.autosave.ui.selectUserScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SelectedProfilesViewModel(
    private val instagramRepository: InstagramRepository
) : ViewModel(), CoroutineScope {

    val profilesSearch = MutableLiveData<List<Profile>>()
    private var job: Job? = null
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    fun search(searchText: String) {
        job?.cancel()
        if (searchText.isEmpty()) {
            profilesSearch.postValue(emptyList())
        } else {
            job = launch {
                delay(DELAY_SEARCH)
                val profiles = instagramRepository.searchProfile(searchText)
                delay(DELAY_SEARCH)
                profilesSearch.postValue(profiles)
            }
            job?.start()
        }
    }

    companion object {
        private val DELAY_SEARCH = 100L
    }
}