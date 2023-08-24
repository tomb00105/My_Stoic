package com.example.mystoic.ui.favourites

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.data.QuoteEntity
import com.example.mystoic.ui.AppViewModelProvider
import com.example.mystoic.ui.journal.JournalScreenViewModel

@Composable
fun FavouritesScreen(
    modifier: Modifier = Modifier,
    viewModel: FavouritesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val favouritesUiState = viewModel.favouritesUiState.collectAsState(initial = FavouritesUiState())
    val favourites = favouritesUiState.value.favourites

    LazyColumn(modifier = modifier) {
        if (favourites.isNotEmpty()) {
            items(favourites) { favourite ->
                ListItem(
                    headlineContent = { Text(favourite.author) },
                    supportingContent = { Text(favourite.text) },
                    trailingContent = {
                        IconButton(
                            onClick = {
                                viewModel.deleteFavourite(
                                    QuoteEntity(favourite.id, favourite.text, favourite.author)
                                )
                            }
                        ) {
                            Image(imageVector = Icons.Filled.Delete, contentDescription = "Remove from favourites")
                        }
                    }
                )
            }
        }
    }
}