package com.example.mystoic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Setup for quotes database
@Database(entities = [QuoteEntity::class], version = 1, exportSchema = false)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao() : QuoteDao

    // Make quote database available either by getting the current database or creating a new
    // one from the data if it does not yet exist
    companion object {
        @Volatile
        private var Instance : QuoteDatabase? = null

        fun getDatabase(context: Context): QuoteDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    QuoteDatabase::class.java,
                    "quote_database"
                )
                    .createFromAsset("database/quote_database.db")
                    .build()
                    .also {
                        Instance = it
                    }
            }
        }
    }
}