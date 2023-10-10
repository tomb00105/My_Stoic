package com.example.mystoic

import android.app.Application
import com.example.mystoic.data.AppContainer
import com.example.mystoic.data.AppDataContainer
import com.example.mystoic.notifications.DailyQuoteNotificationChannel
import com.example.mystoic.notifications.JournalNotificationChannel

class MyStoicApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = initializeContainer()
        DailyQuoteNotificationChannel.createNotificationChannel(this)
        JournalNotificationChannel.createNotificationChannel(this)
    }

    fun initializeContainer() : AppContainer {
        return AppDataContainer(this)
    }
}