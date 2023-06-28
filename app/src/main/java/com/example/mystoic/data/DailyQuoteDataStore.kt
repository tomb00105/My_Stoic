package com.example.mystoic.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dailyQuote")

class DailyQuoteDataStore(private val context: Context) {

    companion object {
        val QUOTE_ID = intPreferencesKey("QUOTE_ID")
        val QUOTE_TEXT = stringPreferencesKey("QUOTE_EXT")
        val QUOTE_AUTHOR = stringPreferencesKey("QUOTE_AUTHOR")
    }

    suspend fun saveToDataStore(quoteEntity: QuoteEntity) {
        context.dataStore.edit {
            it[QUOTE_ID] = quoteEntity.id
            it[QUOTE_TEXT] = quoteEntity.text
            it[QUOTE_AUTHOR] = quoteEntity.author
        }
    }

    fun getFromDataStore() = context.dataStore.data.map {
        QuoteEntity(
            id = it[QUOTE_ID] ?: -1,
            text = it[QUOTE_TEXT] ?: "",
            author = it[QUOTE_AUTHOR] ?: ""
        )
    }

    suspend fun clearDataStore() {
        context.dataStore.edit {
            it.clear()
        }
    }

    suspend fun dataStoreEmpty(): Boolean {
        val dailyQuoteFlow = getFromDataStore()
        return coroutineScope {
            val dailyQuote = dailyQuoteFlow.first()
            val isEmpty = async { (dailyQuote.id == -1) }
            isEmpty.await()
        }
    }
}