package com.example.whattodo.utils.notifications

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.whattodo.MainActivity
import com.example.whattodo.MyApplication.Companion.CHANNEL_ID
import com.example.whattodo.R

//https://medium.com/@ifr0z/workmanager-notification-date-and-time-pickers-aad1d938b0a3

class NotificationWorker(
    context: Context,
    params: WorkerParameters,
) : Worker(context, params) {
    override fun doWork(): Result {
        val id = inputData.getLong(NOTIFICATION_ID, -1)

        if (id == -1L) return Result.failure()

        val title = inputData.getString(NOTIFICATION_TITLE)
        val message = inputData.getString(NOTIFICATION_MESSAGE)

        sendNotification(id, title, message)

        return Result.success()
    }

    private fun sendNotification(
        id: Long,
        title: String?,
        message: String?,
    ) {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(NOTIFICATION_ID, id)

        val notificationManager =
            applicationContext.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val pendingIntent = getActivity(applicationContext, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background) //todo replace with icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_EVENT)
                .setVibrate(longArrayOf(100, 100))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        notificationManager.notify(id.toInt(), notificationBuilder.build())
    }

    companion object {
        const val NOTIFICATION_MESSAGE = "WTD_notification_message"
        const val NOTIFICATION_TITLE = "WTD_notification_title"
        const val NOTIFICATION_ID = "WTD_notification_id"
        const val NOTIFICATION_WORK = "WTD_notification_work"
    }
}