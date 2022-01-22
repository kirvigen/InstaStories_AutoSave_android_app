package com.kirvigen.instagram.stories.autosave.ui.viewerStories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories

class ViewerStoriesViewModel(stories: Stories, instagramRepository: InstagramRepository): ViewModel() {

    val storiesData: LiveData<List<Stories>> = instagramRepository.getStoriesUser(stories.userId)

}