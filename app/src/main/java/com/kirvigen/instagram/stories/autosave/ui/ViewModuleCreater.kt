package com.kirvigen.instagram.stories.autosave.ui

import com.kirvigen.instagram.stories.autosave.ui.mainScreen.MainViewModel
import com.kirvigen.instagram.stories.autosave.ui.selectUserScreen.SelectedProfilesViewModel
import com.kirvigen.instagram.stories.autosave.ui.viewerStories.ViewerStoriesViewModel
import com.kirvigen.instagram.stories.autosave.ui.viewerStories.storiesFragment.StoriesViewModule
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

object ViewModuleCreater {

    fun create() = module {
        viewModel { MainViewModel(get(), get(), get()) }
        viewModel { (stories: Stories) -> StoriesViewModule(stories, get()) }
        viewModel { (stories: Stories) -> ViewerStoriesViewModel(stories, get()) }
        viewModel { SelectedProfilesViewModel(get()) }
    }
}