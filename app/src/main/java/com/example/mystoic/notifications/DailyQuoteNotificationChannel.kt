package com.example.mystoic.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.core.app.ComponentActivity
import com.example.mystoic.R

object DailyQuoteNotificationChannel {
    private val channelId = Resources.getSystem().getString(R.string.quote_channel_id)
    private val channelName = Resources.getSystem().getString(R.string.channel_name)
    private val channelDescription = Resources.getSystem().getString(R.string.channel_description)
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