package com.example.mystoic.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.app.ComponentActivity
import com.example.mystoic.R

object DailyQuoteNotificationChannel {
    private const val channelId = "Daily Quote Channel ID"
    private const val channelName = "Daily Quote"
    private const val channelDescription = "Notification for a random daily quote at around 8am every day."
    private const val importance = NotificationManager.IMPORTANCE_DEFAULT

    fun createNotificationChannel(context: Context) {
        if (apiLevelAtOrAbove26()) {
            val dailyQuoteChannel = NotificationChannel(
                channelId,
                channelName,
                importance,
            )

            dailyQuoteChannel.apply { description = channelDescription }

            val notificationManager = getNotificationManager(context)

            notificationManager.createNotificationChannel(dailyQuoteChannel)
        }
    }

    private fun apiLevelAtOrAbove26() : Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    }

    private fun getNotificationManager(context: Context) : NotificationManager {
        return context.getSystemService(ComponentActivity.NOTIFICATION_SERVICE) as NotificationManager
    }
}