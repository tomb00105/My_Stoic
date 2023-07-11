package com.example.mystoic.ui.journal

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.ui.AppViewModelProvider
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    navigateToEntry: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JournalScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val journalScreenUiState = viewModel.journalScreenUiState.collectAsState(initial = JournalScreenUiState())
    val journalEntries = journalScreenUiState.value.journalEntries

    LazyColumn() {
        if (journalEntries.isNotEmpty()) {
            items(journalScreenUiState.value.journalEntries) { entry ->
                ListItem(
                    headlineContent = { Text(entry.date) },
                    supportingContent = { Text(entry.text ?: "")},
                    modifier = Modifier.clickable {
                        navigateToEntry(entry.date)
                    },
                )
            }
        }
    }
}