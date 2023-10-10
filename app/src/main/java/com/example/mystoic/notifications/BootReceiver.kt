package com.example.mystoic.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mystoic.R

// Used to restart alarm when the device is restarted
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Repeating Daily Quote Notification Alarm needs to set again on every reboot.
        if (bootHasJustCompleted(context, intent)) {
            val alarm = AlarmUtils(context)
            alarm.initRepeatingAlarm(AlarmRequestCode.DAILY_QUOTE)
            alarm.initRepeatingAlarm(AlarmRequestCode.JOURNAL)
        }
    }

    /*private fun setAlarmOnReboot(context: Context) {
        val alarmTimeHMS = intArrayOf(8, 0, 0)
        AlarmSetter.setDailyQuoteAlarm(context, alarmTimeHMS)
    }*/

    private fun bootHasJustCompleted(context: Context, intent : Intent): Boolean {
        return intent.action == context.getString(R.string.boot_completed)
    }
}