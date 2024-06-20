package com.example.whattodo.domain.notificationScheduler

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy.REPLACE
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.whattodo.domain.models.task.item.TaskItem
import com.example.whattodo.utils.extensions.getSecondsFromEpoch
import com.example.whattodo.utils.notifications.NotificationWorker
import com.example.whattodo.utils.notifications.NotificationWorker.Companion.NOTIFICATION_ID
import com.example.whattodo.utils.notifications.NotificationWorker.Companion.NOTIFICATION_MESSAGE
import com.example.whattodo.utils.notifications.NotificationWorker.Companion.NOTIFICATION_TITLE
import com.example.whattodo.utils.notifications.NotificationWorker.Companion.NOTIFICATION_WORK
import timber.log.Timber
import java.time.Instant
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class NotificationSchedulerImpl(private val context: Context) : NotificationScheduler {

    override suspend fun scheduleNotification(taskItems: List<TaskItem>) {
        if (!checkNotificationPermission()) {
            Timber.d("Notification Permissions are not granted!!!")
            return
        }
        if (taskItems.isEmpty()) return
        taskItems.forEach { item ->
            val data = Data.Builder()
                .putLong(NOTIFICATION_ID, item.id)
                .putString(NOTIFICATION_TITLE, item.title)
                .putString(NOTIFICATION_MESSAGE, item.content)
                .build()

            // -1 hour because notifications should appear 1h before task ends
            scheduleNotificationWithWorker(
                data = data,
                delaySeconds = getDelayInSeconds(item.validUntil),
                id = item.id,
            )
        }
    }

    override suspend fun removeScheduledNotification(taskItems: List<TaskItem>) {
        if (taskItems.isEmpty()) return
        val workManager = WorkManager.getInstance(context)
        taskItems.forEach { item ->
            val uniqueName = "${NOTIFICATION_WORK}_${item.id}"
            workManager.cancelUniqueWork(uniqueName)
            Timber.d("Canceled schedule notification with name: $uniqueName")
        }
    }

    override fun cancelAllScheduledNotification() {
        WorkManager.getInstance(context).cancelAllWork()
        Timber.d("Canceled all scheduled notifications!")
    }

    private fun getDelayInSeconds(endTime: LocalDateTime): Long {
        val endTimeSeconds = endTime.minusHours(1).getSecondsFromEpoch()
        val nowSeconds = Instant.now().epochSecond
        val hourInSeconds = 3600
        val diff = endTimeSeconds - nowSeconds
        val delay = if (diff >= hourInSeconds) {
            diff
        } else {
            0
        }
        Timber.d("Notification delay: $delay seconds")
        return delay
    }

    private fun scheduleNotificationWithWorker(
        delaySeconds: Long,
        data: Data,
        id: Long,
    ) {
        val notificationWork = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
            .setInitialDelay(delaySeconds, TimeUnit.SECONDS).setInputData(data).build()

        val instanceWorkManager = WorkManager.getInstance(context)
        val uniqueName = "${NOTIFICATION_WORK}_${id}"
        instanceWorkManager.beginUniqueWork(
            uniqueName,
            REPLACE,
            notificationWork
        )
            .enqueue()
        Timber.d("Scheduled Worker with name: $uniqueName")
    }

    private fun checkNotificationPermission(): Boolean {
        return if (VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            NotificationManagerCompat.from(context).areNotificationsEnabled()
        }
    }
}