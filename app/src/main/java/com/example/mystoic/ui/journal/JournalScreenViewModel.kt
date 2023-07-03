package com.example.mystoic.ui.journal

import android.icu.util.Calendar
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.JournalEntity
import com.example.mystoic.data.QuoteDatabaseRepository
import com.example.mystoic.ui.home.HomeScreenUiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class JournalScreenViewModel(
    private val quoteDatabaseRepository: QuoteDatabaseRepository,
) : ViewModel() {

    val journalScreenUiState: Flow<JournalScreenUiState> =
        quoteDatabaseRepository.getAllJournalEntriesStream().map {
            JournalScreenUiState(journalEntries = it)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = JournalScreenUiState()
            )

    fun saveNewJournalEntry() {
        val newJournalEntry = JournalEntity(getCurrentDateAsString(), "")
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
    fun getCurrentDateAsString(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}

data class JournalScreenUiState (
    val journalEntries: List<JournalEntity> = emptyList(),
)