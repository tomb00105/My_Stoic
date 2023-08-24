package com.example.mystoic.notifications

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mystoic.MainActivity
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.R
import com.example.mystoic.data.QuoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JournalReminderAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, content: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val notificationBuilder = buildJournalReminder(context)
            pushJournalNotification(context, notificationBuilder)
            val alarm = AlarmUtils(context)
            alarm.initRepeatingAlarm(JournalNotificationChannel.journalRequestCode)
        }
    }

    private fun buildJournalReminder(context: Context) : NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, "Journal Channel ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground_stoic)
            .setContentTitle(context.getString(R.string.stoic_journal))
            .setContentText(context.getString(R.string.journal_notification_content))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
    }

    @SuppressLint("MissingPermission")
    private fun pushJournalNotification(context: Context, notificationBuilder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            notify(0, notificationBuilder.build())
        }
    }
}