package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext

class InstagramInteractorImpl(private val instagramRepository: InstagramRepository) : InstagramInteractor,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun savedSelectedProfile(list: List<Profile>) {
        instagramRepository.saveProfiles(list)
    }

    override suspend fun loadStoriesForAllProfile() {
        coroutineScope {
            instagramRepository.getProfilesSync().forEach { profile ->
                async { instagramRepository.loadActualStories(profile.id) }
            }
        }
    }
}