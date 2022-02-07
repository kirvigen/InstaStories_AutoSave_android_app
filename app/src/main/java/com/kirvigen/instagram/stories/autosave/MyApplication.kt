package com.kirvigen.instagram.stories.autosave

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import androidx.core.content.ContextCompat
import com.kirvigen.instagram.stories.autosave.ui.ViewModuleCreater
import com.kirvigen.instagram.stories.autosave.workers.CheckerWorker
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        createChannel("test_1", "Статусы задач")

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyApplication)
            modules(
                MainModuleCreater.create(),
                ViewModuleCreater.create()
            )
        }

        CheckerWorker.planningWorkers(applicationContext)
    }

    private fun createChannel(channelId: String, channelName: String) {
        val notificationManager = ContextCompat.getSystemService(
            this,
            NotificationManager::class.java
        ) as NotificationManager

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                enableLights(true)
                enableVibration(true)
                lightColor = Color.GREEN
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}