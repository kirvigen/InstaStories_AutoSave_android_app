package com.kirvigen.instagram.stories.autosave.instagramUtils

import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class InstagramInteractorImpl(private val instagramRepository: InstagramRepository) : InstagramInteractor,
    CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun savedSelectedProfile(list: List<Profile>) {
        instagramRepository.saveProfiles(list)
    }

    override suspend fun loadStoriesForAllProfile(allUpdate: Boolean) {
        coroutineScope {
            instagramRepository.getProfilesSync().forEach { profile ->
                if (allUpdate) {
                    instagramRepository.loadActualStories(profile.id)
                } else {
                    if (System.currentTimeMillis() - profile.lastUpdate > 60 * 60 * 1000) {
                        instagramRepository.loadActualStories(profile.id)
                    }
                }
            }
        }
    }
}