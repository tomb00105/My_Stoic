package com.example.mystoic.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import com.example.mystoic.R
import com.example.mystoic.data.QuoteEntity
import com.example.mystoic.data.QuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

object AlarmSetter {
    suspend fun setDailyQuoteAlarm(
        context: Context,
        quoteRepository: QuoteRepository,
        hourMinuteSecond: IntArray
    ) {
        assert(hourMinuteSecond.size == 3)
        ensureBootReceiverEnabled(context)
        val alarmManager: AlarmManager = getAlarmManager(context)

        CoroutineScope(Dispatchers.IO).launch {
            val randomQuote: QuoteEntity = getRandomQuote(quoteRepository)
            val contentForAlarmReceiver: Intent = setNotificationContent(context, randomQuote)
            createAndActivateAlarm(context, contentForAlarmReceiver, hourMinuteSecond, alarmManager)
        }
    }

    private fun createAndActivateAlarm(
        context: Context,
        contentForAlarmReceiver: Intent,
        hourMinuteSecond: IntArray,
        alarmManager: AlarmManager,
    ) {
        val alarmBroadcastWithQuote: PendingIntent = createAlarmBroadcast(
            context,
            contentForAlarmReceiver
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
            alarmBroadcastWithQuote
        )
    }

    private suspend fun getRandomQuote(quoteRepository: QuoteRepository) : QuoteEntity {
        val quoteFlow = quoteRepository.getRandomQuoteStream()
        return quoteFlow.first()
    }

    private fun getAlarmManager(context: Context): AlarmManager {
        return context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    private fun setNotificationContent(context: Context, quote: QuoteEntity): Intent {
        return Intent(context, AlarmReceiver::class.java)
            .putExtra(context.getString(R.string.daily_quote_intent_text_key), quote.text)
            .putExtra(context.getString(R.string.daily_quote_intent_author_key), quote.author)
    }

    private fun createAlarmBroadcast(context: Context, notificationContent: Intent): PendingIntent {
        return notificationContent.let {
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