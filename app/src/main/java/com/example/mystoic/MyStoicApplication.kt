package com.example.mystoic

import android.app.Application
import com.example.mystoic.data.AppContainer
import com.example.mystoic.data.AppDataContainer

class MyStoicApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}