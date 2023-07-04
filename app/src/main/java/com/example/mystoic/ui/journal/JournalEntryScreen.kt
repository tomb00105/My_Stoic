package com.example.mystoic.ui.journal

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.ui.AppViewModelProvider

@Composable
fun JournalEntryScreen(
    entryDate: String?,
    modifier: Modifier = Modifier,
    viewModel: JournalEntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val journalText = viewModel.entryUiState.text

    Column {
        Text(text = entryDate ?: "INVALID DATE")
        TextField(value = journalText, onValueChange = { viewModel.updateUiState(it) })
    }
}