package com.example.mystoic.data

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.dailyQuoteDataStore: DataStore<Preferences> by preferencesDataStore(name = "dailyQuote")

class DailyQuoteDataStore(private val context: Context) {

    companion object {
        val QUOTE_ID = intPreferencesKey("QUOTE_ID")
        val QUOTE_TEXT = stringPreferencesKey("QUOTE_EXT")
        val QUOTE_AUTHOR = stringPreferencesKey("QUOTE_AUTHOR")
    }

    fun saveToDataStore(quoteEntity: QuoteEntity) {
        runBlocking { context.dailyQuoteDataStore.edit {
            it[QUOTE_ID] = quoteEntity.id
            it[QUOTE_TEXT] = quoteEntity.text
            it[QUOTE_AUTHOR] = quoteEntity.author
        } }

    }

    fun getFromDataStore() = context.dailyQuoteDataStore.data.map {
        Log.d("IS_QUOTE_EMPTY", "id on get: ${it[QUOTE_ID]}")
        QuoteEntity(
            id = it[QUOTE_ID] ?: -1,
            text = it[QUOTE_TEXT] ?: "",
            author = it[QUOTE_AUTHOR] ?: ""
        )
    }

    suspend fun dataStoreEmpty(): Boolean {
        val dailyQuoteFlow = getFromDataStore()
        return coroutineScope {
            val dailyQuote = dailyQuoteFlow.first()
            Log.d("IS_QUOTE_EMPTY", "${dailyQuote.id}")
            val isEmpty = async { (dailyQuote.id == -1 || dailyQuote.id == null) }
            isEmpty.await()
        }
    }
}