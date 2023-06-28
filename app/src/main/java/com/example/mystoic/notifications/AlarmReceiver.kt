package com.example.mystoic.notifications

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mystoic.R

object AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, content: Intent) {
        AlarmSetter.ensureBootReceiverEnabled(context)

        val notificationBuilder = buildDailyQuoteNotification(context, content)

        pushDailyQuoteNotification(context, notificationBuilder)
    }

    private fun buildDailyQuoteNotification(context: Context, content: Intent) : NotificationCompat.Builder {
        val text = content.getStringExtra(context.getString(R.string.daily_quote_intent_text_key))
        val author = content.getStringExtra(context.getString(R.string.daily_quote_intent_author_key))
        return NotificationCompat.Builder(context, "Daily Quote Channel ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground_stoic)
            .setContentTitle(context.getString(R.string.daily_quote))
            .setContentText("$text\n- $author")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    @SuppressLint("MissingPermission")
    private fun pushDailyQuoteNotification(context: Context, notificationBuilder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            notify(0, notificationBuilder.build())
        }
    }
}