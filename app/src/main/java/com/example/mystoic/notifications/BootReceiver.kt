package com.example.mystoic.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.mystoic.R
import com.example.mystoic.data.OfflineQuoteRepository
import com.example.mystoic.data.QuoteDatabase
import com.example.mystoic.data.QuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Used to restart alarm when the device is restarted
object BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Repeating Daily Quote Notification Alarm needs to set again on every reboot.
        setAlarmOnReboot(context, intent)
    }

    private fun setAlarmOnReboot(context: Context, intent: Intent) {
        if (bootHasJustCompleted(context, intent)) {
            val quoteRepository = getQuoteRepository(context)
            val alarmTimeHMS = intArrayOf(8, 0, 0)

            CoroutineScope(Dispatchers.IO).launch {
                AlarmSetter.setDailyQuoteAlarm(context, quoteRepository, alarmTimeHMS)
            }
        }
    }

    private fun bootHasJustCompleted(context: Context, intent : Intent): Boolean {
        return intent.action == context.getString(R.string.boot_completed)
    }

    private fun getQuoteRepository(context: Context): QuoteRepository {
        return OfflineQuoteRepository(QuoteDatabase.createOrPassDatabase(context).quoteDao())
    }

}