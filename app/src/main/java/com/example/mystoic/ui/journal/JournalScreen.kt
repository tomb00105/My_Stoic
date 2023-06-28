package com.example.mystoic.ui.journal

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.ui.AppViewModelProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalScreen(
    navigateToHome: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: JournalScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    Text("Journal")
}