package com.example.mystoic.notifications

import android.annotation.SuppressLint
import android.app.Application
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.mystoic.MainActivity
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.R
import com.example.mystoic.data.DailyQuoteRepository
import com.example.mystoic.data.QuoteDatabaseRepository
import com.example.mystoic.data.QuoteEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class DailyQuoteAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, content: Intent) {
        Log.d("ALARM_RECEIVED", "Alarm was received!")
        val application = context.applicationContext as Application
        val appContainer = (application as MyStoicApplication).container
        val quoteDatabaseRepository = appContainer.quoteDatabaseRepository
        val dailyQuoteRepository = appContainer.dailyQuoteRepository

        CoroutineScope(Dispatchers.IO).launch {
            val dailyQuote : QuoteEntity
            if (dailyQuoteDateIsToday(dailyQuoteRepository)) {
                dailyQuote = dailyQuoteRepository.getDailyQuoteEntityStream().first()
            }
            else {
                dailyQuote = getRandomQuote(quoteDatabaseRepository)
                saveNewDailyQuote(dailyQuoteRepository, dailyQuote)
            }
            val notificationBuilder = buildDailyQuoteNotification(context, dailyQuote)
            pushDailyQuoteNotification(context, notificationBuilder)
            val alarm = AlarmUtils(context)
            alarm.initRepeatingAlarm(DailyQuoteNotificationChannel.dailyQuoteRequestCode)
        }
    }


    private fun buildDailyQuoteNotification(context: Context, dailyQuote: QuoteEntity) : NotificationCompat.Builder {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        return NotificationCompat.Builder(context, "Daily Quote Channel ID")
            .setSmallIcon(R.drawable.ic_launcher_foreground_stoic)
            .setContentTitle(context.getString(R.string.daily_quote))
            .setContentText("${dailyQuote.text}\n- ${dailyQuote.author}")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
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

    private suspend fun saveNewDailyQuote(dailyQuoteRepository: DailyQuoteRepository, quoteEntity: QuoteEntity) {

        dailyQuoteRepository.saveNewDailyQuote(quoteEntity)
    }

    private suspend fun dailyQuoteDateIsToday(dailyQuoteRepository: DailyQuoteRepository) : Boolean {
        val dailyQuoteDate = dailyQuoteRepository.getDailyQuoteDateStream().first()

        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val currentDate = dateFormat.format(calendar.time)

        return dailyQuoteDate == currentDate
    }
}