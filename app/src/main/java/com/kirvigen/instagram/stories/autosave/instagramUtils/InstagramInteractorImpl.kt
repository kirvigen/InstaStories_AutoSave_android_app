package com.kirvigen.instagram.stories.autosave.instagramUtils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Profile
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InstagramInteractorImpl(
    private val context: Context,
    private val instagramRepository: InstagramRepository
) : InstagramInteractor, CoroutineScope {

    private val downloadManager = ThinDownloadManager()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.IO

    override fun savedSelectedProfile(list: List<Profile>) {
        instagramRepository.saveProfiles(list)
    }

    override suspend fun loadStoriesForAllProfile(allUpdate: Boolean): List<Stories> = withContext(Dispatchers.IO) {
        val storiesLoaded = mutableListOf<Stories>()
        instagramRepository.getProfilesSync().forEach { profile ->
            val oldStories = instagramRepository.getStories(profile.id).toMutableList()
            if (System.currentTimeMillis() - profile.lastUpdate > 60 * 60 * 1000) {
                val loadedStories = instagramRepository.loadActualStories(profile.id)
                oldStories.addAll(loadedStories)
            }

            val storiesNotLoad = oldStories.distinctBy { it.id }.filter { it.localUri == "" }
            CoroutineScope(Dispatchers.IO).launch {
                storiesNotLoad.forEach { stories ->
                    Log.e(TAG, "start download ${stories.id}")
                    val pathDownloaded = downloadFile(stories.sourceMedia)
                    pathDownloaded?.let { localUrl ->
                        storiesLoaded.add(stories)
                        instagramRepository.updateStoriesLocalUrl(stories.id, localUrl)
                    }

                    val status = if (pathDownloaded == null) "failed" else "success"

                    Log.e(TAG, "$status download ${stories.id}")
                }
            }
        }
        return@withContext storiesLoaded
    }

    private suspend fun downloadFile(url: String): String? = suspendCoroutine { ret ->
        val name = url.split("?")[0].split("/")[url.split("/").size - 1]
        val destinationUri = Uri.parse(context.externalCacheDir.toString() + "/$name")
        val request = DownloadRequest(Uri.parse(url))
            .setRetryPolicy(DefaultRetryPolicy())
            .setDestinationURI(destinationUri)
            .setPriority(DownloadRequest.Priority.HIGH)
            .setStatusListener(object : DownloadStatusListenerV1 {
                override fun onProgress(
                    downloadRequest: DownloadRequest?,
                    totalBytes: Long,
                    downloadedBytes: Long,
                    progress: Int
                ) {
                }

                override fun onDownloadComplete(downloadRequest: DownloadRequest?) {
                    ret.resume(downloadRequest?.destinationURI.toString())
                }

                override fun onDownloadFailed(
                    downloadRequest: DownloadRequest?,
                    errorCode: Int,
                    errorMessage: String?
                ) {
                    ret.resume(null)
                }
            })
        downloadManager.add(request)
    }

    companion object {
        private const val TAG = "InstagramInteractorImpl"
    }
}