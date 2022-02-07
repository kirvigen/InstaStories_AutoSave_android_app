package com.kirvigen.instagram.stories.autosave.workers

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramRepository
import com.kirvigen.instagram.stories.autosave.instagramUtils.data.Stories
import com.kirvigen.instagram.stories.autosave.utils.NotificationUtils
import com.kirvigen.instagram.stories.autosave.utils.loadBitmap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit

class CheckerWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val instagramInteractor by inject(InstagramInteractor::class.java)
    private val instagramRepository by inject(InstagramRepository::class.java)

    override suspend fun doWork(): Result {
        val listStories = instagramInteractor.loadStoriesForAllProfile(true)
        if (listStories.isEmpty()) return Result.success()

        CoroutineScope(Dispatchers.IO).launch {
            val profiles = instagramRepository.getProfilesSync()
            val data = listStories
                .groupBy { it.userId }
                .map { entry ->
                    Pair(profiles.find { it.id == entry.key }?.name.toString(), entry.value.size)
                }
            var text = ""
            data.forEach { pair ->
                text += if (text.isEmpty()) {
                    "${pair.first} (${pair.second})"
                } else {
                    ", ${pair.first} (${pair.second})"
                }
            }
            text = "Загружены истории по профилям: $text"
            val stories = listStories.first()
            applicationContext.loadBitmap(stories.localUri) {
                NotificationUtils.createNotificationImage(applicationContext, it, text)
            }
        }

        return Result.success()
    }

    companion object {
        private const val TAG_WORKER = "instagram_worker"

        fun planningWorkers(context: Context) {
            val myConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val refreshCpnWork =
                PeriodicWorkRequest.Builder(CheckerWorker::class.java, 60, TimeUnit.MINUTES, 50, TimeUnit.MINUTES)
                    .setConstraints(myConstraints)
                    .addTag(TAG_WORKER)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG_WORKER, ExistingPeriodicWorkPolicy.KEEP, refreshCpnWork
            )
        }
    }
}