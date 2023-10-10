package com.momento.mystoic.ui.journal

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momento.mystoic.data.JournalEntity
import com.momento.mystoic.data.QuoteDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class JournalScreenViewModel(
    private val quoteDatabaseRepository: QuoteDatabaseRepository,
) : ViewModel() {

    init {
        viewModelScope.launch {
            val latestEntryDate =
                quoteDatabaseRepository.getSingleJournalTextStream(getCurrentDateAsString()).first()
            if (latestEntryDate.isNullOrBlank()) {
                saveNewJournalEntry()
            }
        }
    }

    val journalScreenUiState: Flow<JournalScreenUiState> =
        quoteDatabaseRepository.getAllJournalEntriesStream().map {
            JournalScreenUiState(journalEntries = it)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = JournalScreenUiState()
            )

    private fun saveNewJournalEntry() {
        val newJournalEntry = JournalEntity(getCurrentDateAsString(), "Test")
        viewModelScope.launch {
            quoteDatabaseRepository.insertJournalEntry(newJournalEntry)
        }
    }

    fun deleteJournalEntry(date: String, text: String) {
        val entryToDelete = JournalEntity(date, text)
        viewModelScope.launch {
            quoteDatabaseRepository.deleteJournalEntry(entryToDelete)
        }
    }
    private fun getCurrentDateAsString(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}

data class JournalScreenUiState (
    val journalEntries: List<JournalEntity> = emptyList(),
)