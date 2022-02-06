package com.kirvigen.instagram.stories.autosave.workers

import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.kirich1409.androidnotificationdsl.NotificationPriority
import com.kirich1409.androidnotificationdsl.contentText
import com.kirich1409.androidnotificationdsl.contentTitle
import com.kirich1409.androidnotificationdsl.notification
import com.kirvigen.instagram.stories.autosave.R
import com.kirvigen.instagram.stories.autosave.instagramUtils.InstagramInteractor
import com.kirvigen.instagram.stories.autosave.utils.MyResult
import com.thin.downloadmanager.DefaultRetryPolicy
import com.thin.downloadmanager.DownloadRequest
import com.thin.downloadmanager.DownloadStatusListenerV1
import com.thin.downloadmanager.ThinDownloadManager
import org.koin.java.KoinJavaComponent.inject
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class CheckerWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val instagramInteractor by inject(InstagramInteractor::class.java)

    override suspend fun doWork(): Result {
        createNotification(applicationContext, "Стартуем загружать истории")
        instagramInteractor.loadStoriesForAllProfile(true)
        createNotification(applicationContext, "Закончили загружать истории")
        return Result.success()
    }

    companion object {
        private const val TAG_WORKER = "instagram_worker"

        private fun createNotification(context: Context, text: String, notificationId: Int = 1000) {
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            val notification = notification(context, "test_1", smallIcon = R.drawable.ic_stat_name) {
                contentTitle = "Автоматическое уведомление"
                contentText = text
                priority = NotificationPriority.DEFAULT
            }

            notificationManager.notify(notificationId, notification)
        }

        fun planningWorkers(context: Context) {
            val myConstraints = Constraints.Builder()
                .apply {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        setRequiresDeviceIdle(true)
//                    }
                }
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val refreshCpnWork =
                PeriodicWorkRequest.Builder(CheckerWorker::class.java, 15, TimeUnit.MINUTES, 5, TimeUnit.MINUTES)
                    .setConstraints(myConstraints)
                    .addTag(TAG_WORKER)
                    .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                TAG_WORKER, ExistingPeriodicWorkPolicy.KEEP, refreshCpnWork
            )
        }
    }
}