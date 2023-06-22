package com.example.mystoic

import android.app.Application
import com.example.mystoic.data.QuoteDatabase

class MyStoicApplication : Application() {
    // Sets up a
    val database : QuoteDatabase by lazy { QuoteDatabase.getDatabase(this)}
}