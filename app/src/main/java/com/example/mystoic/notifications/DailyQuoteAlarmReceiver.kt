package com.example.mystoic.notifications

import android.annotation.SuppressLint
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.R
import com.example.mystoic.data.QuoteDatabaseRepository
import com.example.mystoic.data.QuoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

class DailyQuoteAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, content: Intent) {
        Log.d("ALARM_RECEIVED", "Alarm was received!")
        val application = context.applicationContext as Application
        val appContainer = (application as MyStoicApplication).container
        val quoteDatabaseRepository = appContainer.quoteDatabaseRepository

        CoroutineScope(Dispatchers.IO).launch {
            val randomQuote = getRandomQuote(quoteDatabaseRepository)
            val notificationBuilder = buildDailyQuoteNotification(context, randomQuote)
            saveNewDailyQuote(context, randomQuote)
            pushDailyQuoteNotification(context, notificationBuilder)
        }
    }


    private fun buildDailyQuoteNotification(context: Context, dailyQuote: QuoteEntity) : NotificationCompat.Builder {

        return NotificationCompat.Builder(context, "Daily Quote Channel ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground_stoic)
            .setContentTitle(context.getString(R.string.daily_quote))
            .setContentText("${dailyQuote.text}\n- ${dailyQuote.author}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }
    private suspend fun getRandomQuote(quoteDatabaseRepository: QuoteDatabaseRepository) : QuoteEntity {
        val quoteFlow = quoteDatabaseRepository.getRandomQuoteStream()
        return quoteFlow.first()
    }

    @SuppressLint("MissingPermission")
    private fun pushDailyQuoteNotification(context: Context, notificationBuilder: NotificationCompat.Builder) {
        with(NotificationManagerCompat.from(context)) {
            notify(0, notificationBuilder.build())
        }
    }

    private suspend fun saveNewDailyQuote(context: Context, quoteEntity: QuoteEntity) {
        val application = context.applicationContext as Application
        val appContainer = (application as MyStoicApplication).container
        val dailyQuoteRepository = appContainer.dailyQuoteRepository

        dailyQuoteRepository.saveNewDailyQuote(quoteEntity)
    }
}