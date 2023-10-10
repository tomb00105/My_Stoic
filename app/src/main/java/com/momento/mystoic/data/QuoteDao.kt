package com.momento.mystoic.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quoteEntity : QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(favouriteEntity: FavouriteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(journalEntity: JournalEntity)

    @Delete
    suspend fun deleteFavourite(favouriteEntity: FavouriteEntity)

    @Delete
    suspend fun deleteJournalEntry(journalEntity: JournalEntity)

    @Update
    suspend fun updateJournalEntry(journalEntity: JournalEntity)

    @Query("SELECT * FROM Quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM Quotes ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuote(): Flow<QuoteEntity>

    @Query("SELECT * FROM Quotes JOIN Favourites ON Quotes.id = Favourites.id")
    fun getAllFavourites(): Flow<List<QuoteEntity>>

    @Query("SELECT id FROM Favourites")
    fun getAllFavouritesId(): Flow<List<Int>>

    @Query("SELECT * FROM Journal ORDER BY date DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntity>>

    @Query("SELECT text FROM JOURNAL WHERE :date == date")
    fun getSingleJournalEntry(date: String): Flow<String>
}