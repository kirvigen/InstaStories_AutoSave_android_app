package com.kirvigen.instagram.stories.autosave.utils

import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.kirich1409.androidnotificationdsl.NotificationPriority
import com.kirich1409.androidnotificationdsl.notification
import com.kirvigen.instagram.stories.autosave.R

class NotificationUtils {

    companion object {
        const val CHANNEL_DEFAULT_ID = "test_1"

        fun createNotificationImage(
            context: Context,
            bitmap: Bitmap,
            text: String,
            notificationId: Int = 1000,
            title: String = "Автоматическое уведомление"
        ) {
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            val notification = notification(context, CHANNEL_DEFAULT_ID, smallIcon = R.drawable.ic_stat_name) {
                contentTitle = title
                contentText = text
                priority = NotificationPriority.DEFAULT
                style = NotificationCompat.BigPictureStyle().bigPicture(bitmap)
            }

            notificationManager.notify(notificationId, notification)
        }

        fun createNotification(
            context: Context,
            text: String,
            notificationId: Int = 1000,
            title: String = "Автоматическое уведомление"
        ) {
            val notificationManager = ContextCompat.getSystemService(
                context,
                NotificationManager::class.java
            ) as NotificationManager

            val notification = notification(context, CHANNEL_DEFAULT_ID, smallIcon = R.drawable.ic_stat_name) {
                contentTitle = title
                contentText = text
                priority = NotificationPriority.DEFAULT
            }

            notificationManager.notify(notificationId, notification)
        }
    }
}