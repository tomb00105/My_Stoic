package com.example.mystoic.ui.journal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.JournalEntity
import com.example.mystoic.data.QuoteDatabaseRepository
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class JournalEntryScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val quoteDatabaseRepository: QuoteDatabaseRepository,
) : ViewModel() {

    private val entryDate: String = checkNotNull(savedStateHandle["entryDate"])
    init {
        viewModelScope.launch {
            entryUiState = EntryUiState(quoteDatabaseRepository.getSingleJournalTextStream(entryDate)
                .filterNotNull()
                .first())
        }
    }

    var entryUiState by mutableStateOf(EntryUiState())
        private set


    fun updateUiState(text: String) {
        entryUiState =
            EntryUiState(text)
    }


    fun updateJournal() {
        viewModelScope.launch {
            val updatedEntry = JournalEntity(entryDate, entryUiState.text)
            quoteDatabaseRepository.updateJournalEntry(updatedEntry)
        }
    }
}

data class EntryUiState (
    val text: String = ""
)