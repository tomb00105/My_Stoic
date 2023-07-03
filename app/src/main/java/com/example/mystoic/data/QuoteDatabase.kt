package com.example.mystoic.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [QuoteEntity::class, FavouriteEntity::class, JournalEntity::class],
    version = 1,
    exportSchema = true
)
abstract class QuoteDatabase : RoomDatabase() {
    abstract fun quoteDao() : QuoteDao

    // Make quote database available either by getting the current database or creating a new
    // one from the data if it does not yet exist
    companion object {
        @Volatile
        private var QuoteDatabaseInstance : QuoteDatabase? = null

        fun createOrPassDatabase(context: Context): QuoteDatabase {
            return QuoteDatabaseInstance ?: synchronized(this) {
                Room.databaseBuilder(
                    context,
                    QuoteDatabase::class.java,
                    "quotes_database"
                )
                    .fallbackToDestructiveMigration()
                    .createFromAsset("database/quotes_database.db")
                    .build()
                    .also {
                        QuoteDatabaseInstance = it
                    }
            }
        }
    }
}