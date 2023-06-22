package com.example.mystoic

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.mystoic.data.AppContainer
import com.example.mystoic.data.AppDataContainer
import com.example.mystoic.data.QuoteDatabase

class MyStoicApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }

    val database : QuoteDatabase by lazy { QuoteDatabase.getDatabase(this)}
}