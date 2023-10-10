package com.example.mystoic.ui.journal

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                Card(
                    colors = CardDefaults.cardColors(
                        MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 4.dp
                    ),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(100.dp)
                        .clickable {
                            navigateToEntry(entry.date)
                        }
                ) {
                    val inputDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    val parsedDate = inputDateFormat.parse(entry.date)
                    val outputDateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale.getDefault())
                    val formattedEntryDate = outputDateFormat.format(parsedDate!!)
                    Text(
                        text = formattedEntryDate,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = entry.text!!,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(
                            top = 0.dp,
                            bottom = 16.dp,
                            start = 16.dp,
                            end = 16.dp
                        )
                    )
                }
            }
        }
    }
}