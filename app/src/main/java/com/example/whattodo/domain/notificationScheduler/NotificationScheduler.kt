package com.example.whattodo.domain.notificationScheduler

import com.example.whattodo.domain.models.task.item.TaskItem

interface NotificationScheduler {
    suspend fun scheduleNotification(taskItems: List<TaskItem>)
    suspend fun removeScheduledNotification(taskItems: List<TaskItem>)
    fun cancelAllScheduledNotification()
}