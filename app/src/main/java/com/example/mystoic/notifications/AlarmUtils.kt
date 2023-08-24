package com.example.mystoic.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.mystoic.notifications.DailyQuoteNotificationChannel.dailyQuoteRequestCode
import com.example.mystoic.notifications.JournalNotificationChannel.journalRequestCode
import java.util.Calendar

class AlarmUtils(val context: Context) {
    private var alarmMgr: AlarmManager? = null
    private var dailyQuoteAlarmIntent: PendingIntent
    private var journalAlarmIntent: PendingIntent
    private val dayInMillis = 86400000L

    init {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        dailyQuoteAlarmIntent = Intent(context, DailyQuoteAlarmReceiver::class.java).let { mIntent ->
            // if you want more than one notification use different requestCode
            // every notification need different requestCode
            PendingIntent.getBroadcast(context, dailyQuoteRequestCode, mIntent, PendingIntent.FLAG_MUTABLE)
        }
        journalAlarmIntent = Intent(context, JournalReminderAlarmReceiver::class.java).let { mIntent ->
            PendingIntent.getBroadcast(context, journalRequestCode, mIntent, PendingIntent.FLAG_MUTABLE)
        }
    }

    @SuppressLint("MissingPermission")
    fun initRepeatingAlarm(requestCode: Int) {
        val bootReceiver = ComponentName(context, BootReceiver::class.java)
        if (context.packageManager.getComponentEnabledSetting(bootReceiver) !=
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED
        ) {
            context.packageManager.setComponentEnabledSetting(
                bootReceiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP
            )
        }

        val calendar: Calendar = Calendar.getInstance().apply {
            if (requestCode == dailyQuoteRequestCode)
            {
                set(Calendar.HOUR_OF_DAY, 8)

            }
            else {
                set(Calendar.HOUR_OF_DAY, 20)
            }
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val alarmTime = calendar.timeInMillis + dayInMillis

        alarmMgr?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            dailyQuoteAlarmIntent
        )
    }
}
