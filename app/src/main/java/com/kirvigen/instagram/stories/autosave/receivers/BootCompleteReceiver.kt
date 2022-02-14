package com.kirvigen.instagram.stories.autosave.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.kirvigen.instagram.stories.autosave.utils.NotificationUtils
import com.kirvigen.instagram.stories.autosave.workers.InstagramStoriesWorker

class BootCompleteReceiver: BroadcastReceiver()  {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.e("INSTA SAVER", "SETUP WORKERS")
            context?.let {
                InstagramStoriesWorker.planningWorkers(it)
                NotificationUtils.createNotification(it,"Перезапускаем worker")
            }
        }
    }

}