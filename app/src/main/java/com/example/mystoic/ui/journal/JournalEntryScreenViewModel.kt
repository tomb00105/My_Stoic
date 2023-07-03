package com.example.mystoic.ui.journal

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.JournalEntity
import com.example.mystoic.data.QuoteDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class JournalEntryScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val quoteDatabaseRepository: QuoteDatabaseRepository,
) : ViewModel() {

    private val entryDate: String = checkNotNull(savedStateHandle["entryDate"])

    val journalEntryUiState: StateFlow<JournalEntryUiState> =
        quoteDatabaseRepository.getSingleJournalEntryStream(entryDate).map {
            JournalEntryUiState(it)
        }
            .filterNotNull()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = JournalEntryUiState()
            )

    fun updateJournal(date: String, text: String) {
        val updatedEntry = JournalEntity(date, text)
        viewModelScope.launch {
            quoteDatabaseRepository.updateJournalEntry(updatedEntry)
        }
    }
}

data class JournalEntryUiState (
    val entry: JournalEntity? = null
)