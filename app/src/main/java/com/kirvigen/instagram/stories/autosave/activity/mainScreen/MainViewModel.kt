package com.kirvigen.instagram.stories.autosave.activity.mainScreen

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class MainViewModel(
    private val instagramInteractor: InstagramInteractor,
    private val instagramRepository: InstagramRepository
) : ViewModel(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    val currentProfile = MutableLiveData<Profile>()
    val storiesData = MutableLiveData<List<Stories>>()

    init {
        storiesData.postValue(
            listOf(
                Stories(9,"",false,9,24,"https://scontent-arn2-2.cdninstagram.com/v/t51.2885-15/e35/271558711_660428164969133_6211411710816131189_n.jpg?_nc_ht=scontent-arn2-2.cdninstagram.com&_nc_cat=105&_nc_ohc=Z2F5H628nScAX9wfPNc&edm=ALCvFkgBAAAA&ccb=7-4&ig_cache_key=Mjc0NzkwMzQ1OTc1ODk5MzI0Mg%3D%3D.2-ccb7-4&oh=00_AT_6Q6RuIDVJNneZhkKrgTM3tGAuRWkI4ZAlnZjd9JWhIQ&oe=61DF1C4B&_nc_sid=643ae9"),
                Stories(9,"",false,9,24,"https://scontent-arn2-2.cdninstagram.com/v/t51.2885-15/e35/271558711_660428164969133_6211411710816131189_n.jpg?_nc_ht=scontent-arn2-2.cdninstagram.com&_nc_cat=105&_nc_ohc=Z2F5H628nScAX9wfPNc&edm=ALCvFkgBAAAA&ccb=7-4&ig_cache_key=Mjc0NzkwMzQ1OTc1ODk5MzI0Mg%3D%3D.2-ccb7-4&oh=00_AT_6Q6RuIDVJNneZhkKrgTM3tGAuRWkI4ZAlnZjd9JWhIQ&oe=61DF1C4B&_nc_sid=643ae9"),
                Stories(9,"",false,9,24,"https://scontent-arn2-2.cdninstagram.com/v/t51.2885-15/e35/271558711_660428164969133_6211411710816131189_n.jpg?_nc_ht=scontent-arn2-2.cdninstagram.com&_nc_cat=105&_nc_ohc=Z2F5H628nScAX9wfPNc&edm=ALCvFkgBAAAA&ccb=7-4&ig_cache_key=Mjc0NzkwMzQ1OTc1ODk5MzI0Mg%3D%3D.2-ccb7-4&oh=00_AT_6Q6RuIDVJNneZhkKrgTM3tGAuRWkI4ZAlnZjd9JWhIQ&oe=61DF1C4B&_nc_sid=643ae9"),
                Stories(9,"",false,9,24,"https://scontent-arn2-2.cdninstagram.com/v/t51.2885-15/e35/271558711_660428164969133_6211411710816131189_n.jpg?_nc_ht=scontent-arn2-2.cdninstagram.com&_nc_cat=105&_nc_ohc=Z2F5H628nScAX9wfPNc&edm=ALCvFkgBAAAA&ccb=7-4&ig_cache_key=Mjc0NzkwMzQ1OTc1ODk5MzI0Mg%3D%3D.2-ccb7-4&oh=00_AT_6Q6RuIDVJNneZhkKrgTM3tGAuRWkI4ZAlnZjd9JWhIQ&oe=61DF1C4B&_nc_sid=643ae9"),
            )
        )
    }


    fun loadProfile() {
        launch {
            try {
                val profile = instagramRepository.getCurrentProfile() ?: return@launch
                currentProfile.postValue(profile)
                Log.e("My STORIES", instagramRepository.getStories(profile.id).toString())
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun onResume() {
        instagramInteractor.checkAndOpenAuthInstagram()

        if (instagramInteractor.isAuthInstagram() && currentProfile.value == null) {
            loadProfile()
        }
    }
}