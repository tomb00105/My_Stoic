package com.example.mystoic.data

import android.content.Context

interface AppContainer {
    val quoteRepository : QuoteRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val quoteRepository: QuoteRepository by lazy {
        OfflineQuoteRepository(QuoteDatabase.getDatabase(context).quoteDao())
    }
}