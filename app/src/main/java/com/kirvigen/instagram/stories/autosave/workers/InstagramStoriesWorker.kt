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
import com.kirvigen.instagram.stories.autosave.utils.NotificationUtils
import com.kirvigen.instagram.stories.autosave.utils.loadBitmap
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class InstagramStoriesWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val instagramInteractor by inject(InstagramInteractor::class.java)

    override suspend fun doWork(): Result {
        NotificationUtils.createNotification(applicationContext, "Стартуем загружать истории")
        val listStories = instagramInteractor.loadStoriesForAllProfile(true)
        if (listStories.isEmpty()) {
            NotificationUtils.createNotification(applicationContext, "Новых историй не обнаружено")
            return Result.success()
        }

        val text = "Загружены истории: ${listStories.size}"
        val stories = listStories.first()

        return suspendCoroutine { ret ->
            applicationContext.loadBitmap(stories.localUri) { bitmap ->
                if (bitmap != null) {
                    NotificationUtils.createNotificationImage(applicationContext, bitmap, text)
                } else {
                    NotificationUtils.createNotification(applicationContext, text)
                }
                ret.resume(Result.success())
            }
        }
    }

    companion object {
        private const val TAG_WORKER = "instagram_worker"

        fun planningWorkers(context: Context) {
            val myConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val refreshCpnWork =
                PeriodicWorkRequest.Builder(InstagramStoriesWorker::class.java, 60, TimeUnit.MINUTES, 50, TimeUnit.MINUTES)
                    .setConstraints(myConstraints)
                    .addTag(TAG_WORKER)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG_WORKER, ExistingPeriodicWorkPolicy.KEEP, refreshCpnWork
            )
        }
    }
}