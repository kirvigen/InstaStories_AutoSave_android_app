package com.kirvigen.instagram.stories.autosave.ui.viewerStories.storiesFragment

import android.content.Context
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.core.content.FileProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.user.SessionInteractor
import com.kirvigen.instagram.stories.autosave.utils.copyFile
import com.kirvigen.instagram.stories.autosave.utils.getNameFile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class StoriesViewModule(
    stories: Stories,
    instagramRepository: InstagramRepository,
    private val sessionInteractor: SessionInteractor
) : ViewModel() {

    val profile: MutableLiveData<Profile> = MutableLiveData()
    val savedLoader: MutableLiveData<Boolean> = MutableLiveData(false)

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

    fun saveToGallery(stories: Stories, context: Context) {
        viewModelScope.launch {
            val result = sessionInteractor.checkPermissionFileWrite(context)
            if (!result || stories.localUri.isEmpty()) {
                showToast(context, R.string.error_save_to_gallary)
                return@launch
            }

            val fileName = getNameFile(stories.localUri)
            val outputPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).path + "/${context.getString(R.string.app_name)}/"
            withContext(Dispatchers.IO) {
                copyFile(stories.localUri, outputPath, fileName)
            }
            addFileTiGalleryEvent(outputPath + fileName, context)
            showToast(context, R.string.saved)
        }
    }

    private fun showToast(context: Context, @StringRes resString: Int) {
        Toast.makeText(context, context.getString(resString), Toast.LENGTH_SHORT).show()
    }

    private fun shareFile(file: File, context: Context) {
        val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
        context.startActivity(Intent.createChooser(shareIntent, ""))
    }

    private fun addFileTiGalleryEvent(outputPath: String, context: Context) {
        MediaScannerConnection.scanFile(
            context, arrayOf(File(outputPath).toString()),
            null, null
        )
    }
}