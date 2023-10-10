package com.example.mystoic.ui.journal

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.JournalEntity
import com.example.mystoic.data.QuoteDatabaseRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class JournalEntryScreenViewModel(
    savedStateHandle: SavedStateHandle,
    private val quoteDatabaseRepository: QuoteDatabaseRepository,
) : ViewModel() {

    private val entryDate: String = checkNotNull(savedStateHandle["entryDate"])
    private val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    private val parsedDate: Date = inputDateFormat.parse(entryDate)!!
    private val outputDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val formattedEntryDate: String = outputDateFormat.format(parsedDate)

    lateinit var savedText: String

    private val showSaveDialogFlow = MutableStateFlow<Boolean>(false)
    val showSaveDialog = showSaveDialogFlow.asStateFlow()

    fun setShowSaveDialog(show: Boolean) {
        showSaveDialogFlow.value = show
    }

    init {
        viewModelScope.launch {
            entryUiState = EntryUiState(quoteDatabaseRepository.getSingleJournalTextStream(entryDate)
                .filterNotNull()
                .first())
            savedText = entryUiState.text
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
            savedText = entryUiState.text
        }
    }
}

data class EntryUiState (
    val text: String = "",
)