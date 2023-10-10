package com.momento.mystoic.data

import kotlinx.coroutines.flow.Flow

interface DailyQuoteRepository {
    fun getDailyQuoteEntityStream(): Flow<QuoteEntity>

    fun getDailyQuoteDateStream(): Flow<String>

    suspend fun saveNewDailyQuote(quoteEntity: QuoteEntity)

    suspend fun dataStoreEmpty(): Boolean
}

class OfflineDailyQuoteRepository(private val dailyQuoteDataStore : DailyQuoteDataStore) : DailyQuoteRepository {
    override fun getDailyQuoteEntityStream(): Flow<QuoteEntity> =
        dailyQuoteDataStore.getDailyQuote()

    override fun getDailyQuoteDateStream(): Flow<String> =
        dailyQuoteDataStore.getDailyQuoteDate()

    override suspend fun saveNewDailyQuote(quoteEntity: QuoteEntity) =
        dailyQuoteDataStore.saveDailyQuote(quoteEntity)

    override suspend fun dataStoreEmpty() =
        dailyQuoteDataStore.dataStoreEmpty()
}