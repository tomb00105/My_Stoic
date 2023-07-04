package com.example.mystoic.data

import android.icu.util.Calendar
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale

@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quoteEntity : QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavourite(quoteEntity: QuoteEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertJournalEntry(journalEntity: JournalEntity)

    @Delete
    suspend fun deleteFavourite(quoteEntity: QuoteEntity)

    @Delete
    suspend fun deleteJournalEntry(journalEntity: JournalEntity)

    @Update
    suspend fun updateJournalEntry(journalEntity: JournalEntity)

    @Query("SELECT * FROM Quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM Quotes ORDER BY RANDOM() LIMIT 1")
    fun getRandomQuote(): Flow<QuoteEntity>

    @Query("SELECT * FROM Quotes WHERE :id IN Favourites")
    fun getAllFavourites(id: Int): Flow<List<QuoteEntity>>

    @Query("SELECT * FROM Journal ORDER BY date DESC")
    fun getAllJournalEntries(): Flow<List<JournalEntity>>

    @Query("SELECT text FROM JOURNAL WHERE :date == date")
    fun getSingleJournalEntry(date: String): Flow<String>
}