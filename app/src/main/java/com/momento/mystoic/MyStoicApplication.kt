package com.momento.mystoic

import android.app.Application
import com.momento.mystoic.data.AppContainer
import com.momento.mystoic.data.AppDataContainer
import com.momento.mystoic.notifications.DailyQuoteNotificationChannel
import com.momento.mystoic.notifications.JournalNotificationChannel

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