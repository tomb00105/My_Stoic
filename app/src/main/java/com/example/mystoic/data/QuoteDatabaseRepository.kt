package com.example.mystoic.data

import kotlinx.coroutines.flow.Flow

interface QuoteDatabaseRepository {

    fun getAllQuotesStream(): Flow<List<QuoteEntity>>

    fun getRandomQuoteStream(): Flow<QuoteEntity>

    fun getAllFavouritesStream(id: Int): Flow<List<QuoteEntity>>

    fun getAllJournalEntriesStream(): Flow<List<JournalEntity>>

    fun getSingleJournalEntryStream(date: String): Flow<JournalEntity>

    suspend fun insertFavourite(quoteEntity: QuoteEntity)

    suspend fun insertJournalEntry(journalEntity: JournalEntity)

    suspend fun deleteFavourite(quoteEntity: QuoteEntity)

    suspend fun deleteJournalEntry(journalEntity: JournalEntity)

    suspend fun updateJournalEntry(journalEntity: JournalEntity)
}

class OfflineQuoteDatabaseRepository(private val quoteDao: QuoteDao) : QuoteDatabaseRepository {
    override fun getAllQuotesStream(): Flow<List<QuoteEntity>> =
        quoteDao.getAllQuotes()

    override fun getRandomQuoteStream(): Flow<QuoteEntity> =
        quoteDao.getRandomQuote()

    override fun getAllFavouritesStream(id: Int): Flow<List<QuoteEntity>> =
        quoteDao.getAllFavourites(id)

    override fun getAllJournalEntriesStream(): Flow<List<JournalEntity>> =
        quoteDao.getAllJournalEntries()

    override fun getSingleJournalEntryStream(date: String): Flow<JournalEntity> =
        quoteDao.getSingleJournalEntry(date)

    override suspend fun insertFavourite(quoteEntity: QuoteEntity) =
        quoteDao.insertFavourite(quoteEntity)

    override suspend fun insertJournalEntry(journalEntity: JournalEntity) =
        quoteDao.insertJournalEntry(journalEntity)

    override suspend fun updateJournalEntry(journalEntity: JournalEntity) =
        quoteDao.updateJournalEntry(journalEntity)

    override suspend fun deleteFavourite(quoteEntity: QuoteEntity) =
        quoteDao.deleteFavourite(quoteEntity)

    override suspend fun deleteJournalEntry(journalEntity: JournalEntity) =
        quoteDao.deleteJournalEntry(journalEntity)
}