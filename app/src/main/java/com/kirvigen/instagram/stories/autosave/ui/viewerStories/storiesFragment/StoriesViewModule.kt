package com.kirvigen.instagram.stories.autosave.ui.viewerStories.storiesFragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import kotlinx.coroutines.launch
import java.io.File
import java.net.URLConnection

class StoriesViewModule(stories: Stories, instagramRepository: InstagramRepository) : ViewModel() {

    val profile: MutableLiveData<Profile> = MutableLiveData()

    init {
        viewModelScope.launch {
            profile.postValue(instagramRepository.getProfile(stories))
        }
    }

    fun shareStory(stories: Stories, context: Context) {
        val file = File(stories.localUri)
        if (file.exists()) {
            shareFile(file, context)
        }
    }

    private fun shareFile(file: File, context: Context) {
        val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        context.startActivity(Intent.createChooser(shareIntent, "Choose an app"))
    }
}