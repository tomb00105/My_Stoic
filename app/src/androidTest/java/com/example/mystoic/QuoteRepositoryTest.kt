package com.example.mystoic

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.mystoic.data.OfflineQuoteRepository
import com.example.mystoic.data.QuoteDatabase
import com.example.mystoic.data.QuoteRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest

import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

class QuoteRepositoryTest {
    private val numberOfQuotes = 1774
    private lateinit var quoteRepository: QuoteRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        quoteRepository = OfflineQuoteRepository(QuoteDatabase.createOrPassDatabase(context).quoteDao())
    }

    @Test
    fun getAllQuotes_returnsAllQuotes() {
        val quotesFlow = quoteRepository.getAllQuotesStream()
        runTest {
            val quotes = quotesFlow.first()
            assertTrue("Quotes: ${quotes.size.toString()}", quotes.size == numberOfQuotes)
        }
    }

    @Test
    fun getRandomQuote_returnsRandomQuote() {
        runTest {
            val quoteOne = quoteRepository.getRandomQuoteStream().first()
            val quoteTwo = quoteRepository.getRandomQuoteStream().first()
            assertNotEquals("$quoteOne equal to $quoteTwo", quoteOne, quoteTwo)
        }
    }
}