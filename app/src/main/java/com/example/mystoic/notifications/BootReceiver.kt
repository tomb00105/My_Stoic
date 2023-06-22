package com.example.mystoic.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.datastore.dataStoreFile
import com.example.mystoic.R
import com.example.mystoic.data.AppContainer
import com.example.mystoic.data.OfflineQuoteRepository
import com.example.mystoic.data.QuoteDatabase
import com.example.mystoic.data.QuoteRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Calendar

// Used to restart alarm when the device is restarted
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == "android.intent.action.BOOT_COMPLETED") {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            CoroutineScope(Dispatchers.IO).launch {
                val quoteRepository: QuoteRepository =
                    OfflineQuoteRepository(QuoteDatabase.getDatabase(context).quoteDao())

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
}