package com.example.mystoic.data

import kotlinx.coroutines.flow.Flow

interface QuoteDatabaseRepository {

    fun getAllQuotesStream(): Flow<List<QuoteEntity>>

    fun getRandomQuoteStream(): Flow<QuoteEntity>
}

class OfflineQuoteDatabaseRepository(private val quoteDao: QuoteDao) : QuoteDatabaseRepository {
    override fun getAllQuotesStream(): Flow<List<QuoteEntity>> = quoteDao.getAllQuotes()

    override fun getRandomQuoteStream(): Flow<QuoteEntity> = quoteDao.getRandomQuote()
}