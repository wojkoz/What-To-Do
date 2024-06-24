package com.example.whattodo.di

import android.content.Context
import com.example.whattodo.domain.notificationScheduler.NotificationScheduler
import com.example.whattodo.domain.notificationScheduler.NotificationSchedulerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object HelperModule {

    @Singleton
    @Provides
    fun provideNotificationScheduler(@ApplicationContext context: Context): NotificationScheduler {
        return NotificationSchedulerImpl(context = context)
    }
}