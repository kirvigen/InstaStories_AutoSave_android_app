package com.kirvigen.instagram.stories.autosave.ui.viewerStatistics

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepositoryImpl
import com.kirvigen.instagram.stories.autosave.ui.viewerStatistics.adapter.data.StatisticsProfileItem
import com.kirvigen.instagram.stories.autosave.utils.combineWith
import kotlinx.coroutines.launch
import java.io.File
import kotlin.math.roundToInt

class ViewerStatisticViewModel(
    private val instagramInteractor: InstagramInteractor,
    private val instagramRepository: InstagramRepository
) : ViewModel() {

    private val allProfiles = instagramRepository.getProfiles()
    private val allStories = instagramRepository.getStories()

    init {
    }

    val mainList = allProfiles.combineWith(allStories) { profiles, listStories ->
        if (profiles == null || listStories == null) return@combineWith emptyList()
        var videoSize = 0L
        var photoSize = 0L

        val statisticsProfiles = profiles.map { profile ->
            val storiesProfile = listStories.filter { it.userId == profile.id }
            var size: Long = 0
            storiesProfile.forEach { stories ->
                val file = File(stories.localUri)
                val fileSize = file.length()
                size += fileSize
                videoSize += if (stories.isVideo) fileSize else 0
                photoSize += if (!stories.isVideo) fileSize else 0
            }
            StatisticsProfileItem(
                profileId = profile.id,
                nickname = profile.nickname,
                photo = profile.photo,
                size = size,
                sizeName = getNameSize(size)
            )
        }.sortedBy { it.size }.reversed()

        val commonSize = videoSize + photoSize
        mainInfo.postValue(
            StatisticsInfo(
                commonSize = getNameSize(commonSize),
                videoSize = getNameSize(videoSize),
                photoSize = getNameSize(photoSize),
                videoPercent = (videoSize / commonSize.toFloat() * 100).toInt(),
                photoPercent = (photoSize / commonSize.toFloat() * 100).toInt()
            )
        )
        return@combineWith statisticsProfiles
    }

    val mainInfo: MutableLiveData<StatisticsInfo> = MutableLiveData()

    fun goToProfileInstagram(profileId: Long, context: Context) {
        viewModelScope.launch {
            val url = InstagramRepositoryImpl.INSTAGRAM_MAIN_URL + instagramRepository.getProfile(profileId)?.nickname
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(url)
            context.startActivity(i)
        }
    }

    fun deleteProfile(profileId: Long) {
        viewModelScope.launch {
            instagramInteractor.deleteUserData(profileId)
        }
    }


    private fun getNameSize(size: Long): String {
        val kilobytes = size / 1024f
        val megabytes = kilobytes / 1024f
        val gigabytes = megabytes / 1024f
        return when {
            kilobytes < 100 -> "${roundingOneDigit(kilobytes)} KB"
            megabytes < 100 -> "${roundingOneDigit(megabytes)} MB"
            gigabytes < 100 -> "${roundingOneDigit(gigabytes)} GB"
            else -> "${roundingOneDigit(kilobytes)} KB"
        }
    }

    private fun roundingOneDigit(value: Float): Float {
        val value10 = (value * 10).roundToInt()
        return value10 / 10f
    }
}