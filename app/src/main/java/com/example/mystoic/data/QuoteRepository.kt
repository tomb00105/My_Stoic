package com.example.mystoic.data

import kotlinx.coroutines.flow.Flow

interface QuoteRepository {

    fun getAllQuotesStream(): Flow<List<QuoteEntity>>

    fun getRandomQuoteStream(): Flow<QuoteEntity>
}

class OfflineQuoteRepository(private val quoteDao: QuoteDao) : QuoteRepository {
    override fun getAllQuotesStream(): Flow<List<QuoteEntity>> = quoteDao.getAllQuotes()

    override fun getRandomQuoteStream(): Flow<QuoteEntity> = quoteDao.getRandomQuote()


}