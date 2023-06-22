package com.example.mystoic.ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun MyStoicApp(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = HomeScreenViewModel.factory),
) {
    val allQuotes by viewModel.getAllQuotes().collectAsState(emptyList())
    Text(text = "Quotes in database: ${allQuotes.size.toString()}")
    HomeScreen()
}