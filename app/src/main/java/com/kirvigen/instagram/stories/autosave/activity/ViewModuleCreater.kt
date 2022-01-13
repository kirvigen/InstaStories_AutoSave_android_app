package com.kirvigen.instagram.stories.autosave.activity

import com.kirvigen.instagram.stories.autosave.activity.mainScreen.MainViewModel
import com.kirvigen.instagram.stories.autosave.activity.viewerStories.ViewerStoriesActivity
import com.kirvigen.instagram.stories.autosave.activity.viewerStories.ViewerStoriesViewModel
import com.kirvigen.instagram.stories.autosave.activity.viewerStories.storiesFragment.StoriesViewModule
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModuleCreater {

    fun create() = module {
        viewModel { MainViewModel(get(), get()) }
        viewModel { (stories: Stories) -> StoriesViewModule(stories, get()) }
        viewModel { (stories: Stories) -> ViewerStoriesViewModel(stories, get()) }
    }
}