package com.example.mystoic.ui.journal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.ui.AppViewModelProvider

@Composable
fun JournalEntryScreen(
    entryDate: String?,
    modifier: Modifier = Modifier,
    viewModel: JournalEntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val journalEntryUiState = viewModel.journalEntryUiState.collectAsState()

    Column {
        Text(text = entryDate ?: "INVALID DATE")
        Text(text = journalEntryUiState.value.entry?.text ?: "")
    }
}