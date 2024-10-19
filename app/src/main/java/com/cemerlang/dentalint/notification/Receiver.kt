package com.cemerlang.dentalint.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.getSystemService
import com.cemerlang.dentalint.R

class Receiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        Log.d("debug", "3")

        val message = intent!!.getStringExtra("EXTRA_MESSAGE") ?: return
        showNotification(context!!, message)
    }

    fun showNotification(context: Context, message: String) {

        Log.d("debug", "4")

        val notification = NotificationCompat.Builder(context, "notes")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(message.split(":")[0])
            .setContentText(message.split(":")[1])
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_MAX)

        val notificationManager = context.getSystemService<NotificationManager>()
        notificationManager!!.notify(1, notification.build())
    }
}