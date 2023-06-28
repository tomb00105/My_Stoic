package com.example.mystoic.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.util.Calendar

object AlarmSetter {
    fun setDailyQuoteAlarm(
        context: Context,
        hourMinuteSecond: IntArray
    ) {
        assert(hourMinuteSecond.size == 3)
        ensureBootReceiverEnabled(context)

        val alarmManager: AlarmManager = getAlarmManager(context)
        val intent: Intent = setIntent(context)
        createAndActivateAlarm(context, intent, hourMinuteSecond, alarmManager)
    }

    private fun createAndActivateAlarm(
        context: Context,
        intent: Intent,
        hourMinuteSecond: IntArray,
        alarmManager: AlarmManager,
    ) {
        val alarmBroadcast: PendingIntent = createAlarmBroadcast(
            context,
            intent
        )

        val alarmTime: Long = setAlarmTimeInMillis(
            hourMinuteSecond[0],
            hourMinuteSecond[1],
            hourMinuteSecond[2],
        )

        alarmManager.setInexactRepeating(
            AlarmManager.RTC,
            alarmTime,
            AlarmManager.INTERVAL_DAY,
            alarmBroadcast
        )
    }

    private fun getAlarmManager(context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun setIntent(context: Context): Intent {
        return Intent(context, DailyQuoteAlarmReceiver::class.java)
    }

    private fun createAlarmBroadcast(context: Context, intent: Intent): PendingIntent {
        return intent.let {
            PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_MUTABLE)
        }
    }

    private fun setAlarmTimeInMillis(alarmHour: Int, alarmMinute: Int, alarmSecond: Int): Long {
        val alarmTimeInMillis = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarmHour)
            set(Calendar.MINUTE, alarmMinute)
            set(Calendar.SECOND, alarmSecond)
        }
            .timeInMillis

        return alarmTimeInMillis
    }

    fun ensureBootReceiverEnabled(context: Context) {
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
    }
}