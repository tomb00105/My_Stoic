package com.example.mystoic

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.mystoic.data.OfflineQuoteDatabaseRepository
import com.example.mystoic.data.QuoteDatabase
import com.example.mystoic.data.QuoteDatabaseRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class QuoteDatabaseRepositoryTest {
    private val numberOfQuotes = 1774
    private lateinit var quoteDatabaseRepository: QuoteDatabaseRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        quoteDatabaseRepository = OfflineQuoteDatabaseRepository(QuoteDatabase.createOrPassDatabase(context).quoteDao())
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @Test
    fun getAllQuotes_returnsAllQuotes() {
        val quotesFlow = quoteDatabaseRepository.getAllQuotesStream()
        runTest {
            val quotes = quotesFlow.first()
            assertTrue("Quotes: ${quotes.size}", quotes.size == numberOfQuotes)
        }
    }

    @kotlinx.coroutines.ExperimentalCoroutinesApi
    @Test
    fun getRandomQuote_returnsRandomQuote() {
        runTest {
            val quoteOne = quoteDatabaseRepository.getRandomQuoteStream().first()
            val quoteTwo = quoteDatabaseRepository.getRandomQuoteStream().first()
            assertNotEquals("$quoteOne equal to $quoteTwo", quoteOne, quoteTwo)
        }
    }
}