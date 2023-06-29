package com.example.mystoic.notifications

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import java.util.Calendar

class AlarmUtils(val context: Context) {
    private var alarmMgr: AlarmManager? = null
    private var alarmIntent: PendingIntent
    private val dayInMillis = 86400000L

    init {
        alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmIntent = Intent(context, DailyQuoteAlarmReceiver::class.java).let { mIntent ->
            // if you want more than one notification use different requestCode
            // every notification need different requestCode
            PendingIntent.getBroadcast(context, 100, mIntent, PendingIntent.FLAG_MUTABLE)
        }
    }

    @SuppressLint("MissingPermission")
    fun initRepeatingAlarm(){
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
            set(Calendar.HOUR_OF_DAY, 8)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }

        val alarmTime = calendar.timeInMillis + dayInMillis

        alarmMgr?.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            alarmTime,
            alarmIntent
        )
    }

}
