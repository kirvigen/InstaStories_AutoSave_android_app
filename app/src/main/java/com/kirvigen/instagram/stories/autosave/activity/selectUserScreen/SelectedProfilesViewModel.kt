package com.kirvigen.instagram.stories.autosave.activity.selectUserScreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SelectedProfilesViewModel(
    private val instagramRepository: InstagramRepository
) : ViewModel() {

    val profilesSearch = MutableLiveData<List<Profile>>()
    private var jobSearch: Job? = null

    fun search(searchText: String) {
        jobSearch?.cancel()
        if (searchText.isEmpty()) {
            profilesSearch.postValue(emptyList())
        } else {
            jobSearch = viewModelScope.launch {
                delay(DELAY_SEARCH)
                profilesSearch.postValue(instagramRepository.searchProfile(searchText))
            }
            jobSearch?.start()
        }
    }

    companion object {
        private val DELAY_SEARCH = 400L
    }
}