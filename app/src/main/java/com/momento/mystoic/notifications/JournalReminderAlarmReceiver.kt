package com.momento.mystoic.notifications

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.momento.mystoic.MainActivity
import com.momento.mystoic.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JournalReminderAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, content: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            val notificationBuilder = buildJournalReminder(context)
            pushJournalNotification(context, notificationBuilder)
            val alarm = AlarmUtils(context)
            alarm.initRepeatingAlarm(AlarmRequestCode.JOURNAL)
        }
    }

    private fun buildJournalReminder(context: Context) : NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, AlarmRequestCode.JOURNAL.requestCode, intent, PendingIntent.FLAG_IMMUTABLE)

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
            notify(AlarmRequestCode.JOURNAL.requestCode, notificationBuilder.build())
        }
    }
}