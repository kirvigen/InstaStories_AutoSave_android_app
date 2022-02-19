package com.kirvigen.instagram.stories.autosave.ui.viewerProfile

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.ui.viewerProfile.adapterStories.data.StoriesViewData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ViewerProfileViewModel(
    private val profileId: Long,
    private val instagramRepository: InstagramRepository,
    private val instagramInteractor: InstagramInteractor
) : ViewModel() {

    val storiesData: MutableLiveData<List<StoriesViewData>> = MutableLiveData(listOf())
    val profileData: MutableLiveData<Profile> = MutableLiveData()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val profile = instagramRepository.getProfile(profileId) ?: return@launch
            profileData.postValue(profile)
            val storiesViewDataList = mutableListOf<StoriesViewData>()
            instagramRepository.getStoriesProfile(profile.id).forEach { stories ->
                val simpleDateFormat = SimpleDateFormat("d MMM", Locale.getDefault())
                val data = simpleDateFormat.format(Date(stories.date * 1000))
                val day = data.split(" ")[0]
                val month = data.split(" ")[1]
                val storiesViewData = StoriesViewData(day, month, stories)
                storiesViewDataList.add(storiesViewData)
            }
            storiesData.postValue(storiesViewDataList)
        }
    }

    fun goToProfileInstagram(profileId: Long, context: Context) {
        viewModelScope.launch {
            val url = InstagramRepositoryImpl.INSTAGRAM_MAIN_URL + instagramRepository.getProfile(profileId)?.nickname
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
    }

    suspend fun deleteProfile(profileId: Long) {
        instagramInteractor.deleteUserData(profileId)
    }
}