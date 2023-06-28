package com.example.mystoic.data

import android.content.Context

interface AppContainer {
    val quoteDatabaseRepository: QuoteDatabaseRepository
    val dailyQuoteDataStore: DailyQuoteDataStore
    val dailyQuoteRepository: DailyQuoteRepository
}

class AppDataContainer(private val context: Context) : AppContainer {

    override val quoteDatabaseRepository: QuoteDatabaseRepository by lazy {
        OfflineQuoteDatabaseRepository(
            QuoteDatabase.createOrPassDatabase(context).quoteDao(),
        )
    }

    override val dailyQuoteDataStore: DailyQuoteDataStore by lazy {
        DailyQuoteDataStore(context)
    }

    override val dailyQuoteRepository: DailyQuoteRepository by lazy {
        OfflineDailyQuoteRepository(
            dailyQuoteDataStore
        )
    }

}