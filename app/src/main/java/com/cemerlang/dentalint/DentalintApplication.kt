package com.cemerlang.dentalint

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.cemerlang.dentalint.analytics.AnalyticsHelper
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DentalintApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AnalyticsHelper.initialize(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "notes",
                "Daily Notes",
                NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
}