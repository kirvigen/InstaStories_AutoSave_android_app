package com.kirvigen.instagram.stories.autosave.activity.viewerStories.storiesFragment

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import kotlinx.coroutines.launch

class StoriesViewModule(stories:Stories, instagramRepository: InstagramRepository):ViewModel() {

    val profile: MutableLiveData<Profile> = MutableLiveData()

    init {
        viewModelScope.launch {
            profile.postValue(instagramRepository.getProfile(stories))
        }
    }

}