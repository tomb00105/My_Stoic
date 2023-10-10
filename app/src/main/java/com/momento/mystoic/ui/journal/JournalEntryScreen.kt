package com.momento.mystoic.ui.journal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.momento.mystoic.ui.AppViewModelProvider

@Composable
fun JournalEntryScreen(
    entryDate: String?,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: JournalEntryScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val journalText = viewModel.entryUiState.text
    val showSaveDialog = viewModel.showSaveDialog.collectAsState()

    Scaffold(
        topBar = {
            JournalEntryTopBar(
                navController,
                viewModel
            ) },
    ) { innerPadding ->
        if (showSaveDialog.value) {
            AskToSave(viewModel = viewModel, navController = navController)
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    MaterialTheme.colorScheme.primaryContainer
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 4.dp
                ),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                LazyColumn() {
                    item {
                        Box(modifier = Modifier.safeDrawingPadding()) {
                            OutlinedTextField(
                                value = journalText,
                                onValueChange = { viewModel.updateUiState(it) },
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                                    .background(color = MaterialTheme.colorScheme.surface),
                                minLines = 15,
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryTopBar(
    navController: NavHostController,
    viewModel: JournalEntryScreenViewModel,
    modifier: Modifier = Modifier
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    TopAppBar(
        title = {
            Text(
                "Journal Date: ${viewModel.formattedEntryDate}",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                if (viewModel.entryUiState.text != viewModel.savedText) {
                    viewModel.setShowSaveDialog(true)
                } else {
                    navController.popBackStack()
                }
            }
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = "Back to journal"
                )
            }
        },
        actions = {
            // RowScope here, so these icons will be placed horizontally
            IconButton(onClick = { saveJournalEntry(viewModel) }) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = "Localized description"
                )
            }
        },
        scrollBehavior = scrollBehavior
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JournalEntryBottomNavigation(
    navController: NavHostController,
    viewModel: JournalEntryScreenViewModel,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = Modifier,
        containerColor = MaterialTheme.colorScheme.primaryContainer,
    ) {
        NavigationBarItem(
            icon = { Icon(Icons.Filled.ArrowBack, contentDescription = "back") },
            label = { Text("Back") },
            selected = false,
            onClick = {
                if (viewModel.entryUiState.text != viewModel.savedText) {
                    viewModel.setShowSaveDialog(true)
                } else {
                    navController.popBackStack()
                }
            }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Filled.Done, contentDescription = "Save") },
            label = { Text("Save") },
            selected = false,
            onClick = {
                saveJournalEntry(viewModel)
            }
        )
    }
}

@Composable
fun AskToSave(
    viewModel: JournalEntryScreenViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val showSaveDialog = viewModel.showSaveDialog.collectAsState()

    if (showSaveDialog.value) {
        Dialog(onDismissRequest = {
            viewModel.setShowSaveDialog(false)
        }) {
            Card() {
                Column(
                    modifier = Modifier.padding(8.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "If you exit without saving you will lose any unsaved changes. " +
                                "Would you like to save this entry?",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(4.dp)
                    )
                    Row {
                        Button(
                            onClick = {
                            saveJournalEntry(viewModel = viewModel)
                            viewModel.setShowSaveDialog(false)
                            navController.popBackStack()
                        },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(text = "Yes")
                        }
                        Button(
                            onClick = {
                            viewModel.setShowSaveDialog(false)
                            navController.popBackStack()
                        },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("No")
                        }
                    }
                }
            }
        }
    }
}

fun saveJournalEntry(
    viewModel: JournalEntryScreenViewModel,
    modifier: Modifier = Modifier
) {
    viewModel.updateJournal()
}