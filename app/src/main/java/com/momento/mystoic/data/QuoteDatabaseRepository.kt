package com.momento.mystoic.data

import kotlinx.coroutines.flow.Flow

interface QuoteDatabaseRepository {

    fun getAllQuotesStream(): Flow<List<QuoteEntity>>

    fun getRandomQuoteStream(): Flow<QuoteEntity>

    fun getAllFavouritesStream(): Flow<List<QuoteEntity>>

    fun getAllFavouritesIdStream(): Flow<List<Int>>

    fun getAllJournalEntriesStream(): Flow<List<JournalEntity>>

    fun getSingleJournalTextStream(date: String): Flow<String>

    suspend fun insertFavourite(favouriteEntity: FavouriteEntity)

    suspend fun insertJournalEntry(journalEntity: JournalEntity)

    suspend fun deleteFavourite(favouriteEntity: FavouriteEntity)

    suspend fun deleteJournalEntry(journalEntity: JournalEntity)

    suspend fun updateJournalEntry(journalEntity: JournalEntity)
}

class OfflineQuoteDatabaseRepository(private val quoteDao: QuoteDao) : QuoteDatabaseRepository {
    override fun getAllQuotesStream(): Flow<List<QuoteEntity>> =
        quoteDao.getAllQuotes()

    override fun getRandomQuoteStream(): Flow<QuoteEntity> =
        quoteDao.getRandomQuote()

    override fun getAllFavouritesStream(): Flow<List<QuoteEntity>> =
        quoteDao.getAllFavourites()

    override fun getAllFavouritesIdStream(): Flow<List<Int>> =
        quoteDao.getAllFavouritesId()

    override fun getAllJournalEntriesStream(): Flow<List<JournalEntity>> =
        quoteDao.getAllJournalEntries()

    override fun getSingleJournalTextStream(date: String): Flow<String> =
        quoteDao.getSingleJournalEntry(date)

    override suspend fun insertFavourite(favouriteEntity: FavouriteEntity) =
        quoteDao.insertFavourite(favouriteEntity)

    override suspend fun insertJournalEntry(journalEntity: JournalEntity) =
        quoteDao.insertJournalEntry(journalEntity)

    override suspend fun updateJournalEntry(journalEntity: JournalEntity) =
        quoteDao.updateJournalEntry(journalEntity)

    override suspend fun deleteFavourite(favouriteEntity: FavouriteEntity) =
        quoteDao.deleteFavourite(favouriteEntity)

    override suspend fun deleteJournalEntry(journalEntity: JournalEntity) =
        quoteDao.deleteJournalEntry(journalEntity)
}