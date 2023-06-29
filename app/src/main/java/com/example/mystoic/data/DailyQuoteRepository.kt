package com.example.mystoic.data

import kotlinx.coroutines.flow.Flow

interface DailyQuoteRepository {
    fun getCurrentDailyQuoteEntityStream(): Flow<QuoteEntity>

    suspend fun saveNewDailyQuote(quoteEntity: QuoteEntity)

    suspend fun dataStoreEmpty(): Boolean
}

class OfflineDailyQuoteRepository(private val dailyQuoteDataStore : DailyQuoteDataStore) : DailyQuoteRepository {
    override fun getCurrentDailyQuoteEntityStream(): Flow<QuoteEntity> =
        dailyQuoteDataStore.getFromDataStore()

    override suspend fun saveNewDailyQuote(quoteEntity: QuoteEntity) =
        dailyQuoteDataStore.saveToDataStore(quoteEntity)

    override suspend fun dataStoreEmpty() =
        dailyQuoteDataStore.dataStoreEmpty()
}