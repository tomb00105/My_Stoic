package com.example.mystoic.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Data access object for querying the database for quotes.
@Dao
interface QuoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertQuote(quoteEntity : QuoteEntity)

    @Query("SELECT * FROM Quotes")
    fun getAllQuotes(): Flow<List<QuoteEntity>>
}