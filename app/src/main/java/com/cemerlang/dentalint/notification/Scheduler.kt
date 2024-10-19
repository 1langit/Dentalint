package com.cemerlang.dentalint.notification

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

class Scheduler(private val context: Context) {

    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    @SuppressLint("MissingPermission")
    fun schedule(item: AlarmItem) {

        Log.d("debug", "2")

        val intent = Intent(context, Receiver::class.java).apply {
            putExtra("EXTRA_MESSAGE", "${item.title}:${item.content}")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val currentTime = LocalDateTime.now(ZoneId.systemDefault())
            val time = if (item.time.isBefore(currentTime)) {
                item.time.plusDays(1)
            } else {
                item.time
            }

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                time.atZone(ZoneId.systemDefault()).toEpochSecond() * 1000,
                AlarmManager.INTERVAL_DAY,
                PendingIntent.getBroadcast(
                    context,
                    item.hashCode(),
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            )
        }
    }

    fun cancel(item: AlarmItem) {
        alarmManager.cancel(
            PendingIntent.getBroadcast(
                context,
                item.hashCode(),
                Intent(context, Receiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        )
    }
}