package com.example.mystoic.data

import kotlinx.coroutines.flow.Flow

interface QuoteRepository {
    var latestQuoteText : String?
    var latestQuoteAuthor : String?

    fun getAllQuotesStream(): Flow<List<QuoteEntity>>

    fun getRandomQuoteStream(): Flow<QuoteEntity>
}

class OfflineQuoteRepository(private val quoteDao: QuoteDao) : QuoteRepository {
    override var latestQuoteAuthor: String? = null
    override var latestQuoteText: String? = null

    override fun getAllQuotesStream(): Flow<List<QuoteEntity>> = quoteDao.getAllQuotes()

    override fun getRandomQuoteStream(): Flow<QuoteEntity> = quoteDao.getRandomQuote()
}