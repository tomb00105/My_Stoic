package com.momento.mystoic.ui.favourites

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.momento.mystoic.ui.AppViewModelProvider
import com.momento.mystoic.ui.SharingBar

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
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = favourite.author,
                            fontWeight = FontWeight.Bold
                        )
                        Text(text = favourite.text)
                        SharingBar(
                            quoteText = favourite.text,
                            quoteAuthor = favourite.author,
                            isDailyQuote = false,
                            isFavourite = true,
                            homeScreenViewModel = null,
                            favouritesViewModel = viewModel,
                            favouritesQuoteEntity = favourite
                        )
                    }
                }
            }
        }
    }
}