package com.momento.mystoic.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.ComponentActivity

object JournalNotificationChannel {
    private const val channelId = "Journal Channel ID"
    private const val channelName = "Journal Reminder"
    private const val channelDescription = "Notification to write in your journal at 8pm every day."
    private const val importance = NotificationManager.IMPORTANCE_DEFAULT

    fun createNotificationChannel(context: Context) {
        if (apiLevelAtOrAbove26()) {
            val journalChannel = NotificationChannel(
                channelId,
                channelName,
                importance,
            )

            journalChannel.apply { description =
                channelDescription
            }

            val notificationManager = getNotificationManager(context)

            notificationManager.createNotificationChannel(journalChannel)
        }
    }

    private fun apiLevelAtOrAbove26() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    private fun getNotificationManager(context: Context) : NotificationManager {
        return context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
    }
}