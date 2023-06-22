package com.example.mystoic.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.compose.runtime.collectAsState
import androidx.core.app.ActivityCompat
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.mystoic.R
import com.example.mystoic.data.QuoteRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import java.util.Calendar


class NotificationAlarmManager(
    private val context: Context,
    private val quoteRepository: QuoteRepository,
    ) {
    suspend fun setAlarm() {

        val receiver = ComponentName(context, BootReceiver::class.java)

        context.packageManager.setComponentEnabledSetting(
            receiver,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )

        // Move execution to an I/O thread
        withContext(Dispatchers.IO) {
            // Create AlarmManager
            val alarmManager: AlarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            // Get Flow of a random quote from database
            val randomQuoteFlow = quoteRepository.getRandomQuoteStream()

            // Get quote from flow
            val randomQuote = randomQuoteFlow.first()
            
            quoteRepository.latestQuoteText = randomQuote.text
            quoteRepository.latestQuoteAuthor = randomQuote.author
            

            // Create intent for eventual notification using the quote
            val intent = Intent(context, AlarmReceiver::class.java)
                .putExtra(context.getString(R.string.daily_quote_intent_text_key), randomQuote.text)
                .putExtra(context.getString(R.string.daily_quote_intent_author_key), randomQuote.author)


            // Create the PendingIntent for the alarm
            val alarmIntent: PendingIntent = intent.let {
                PendingIntent.getBroadcast(context, 0, it, PendingIntent.FLAG_MUTABLE)
            }

            // Set calender for alarm to start at approximately 8:00 a.m.
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 8)
            }

            // Create alarm and set ir to repeat every day
            alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
            )
        }
    }
}