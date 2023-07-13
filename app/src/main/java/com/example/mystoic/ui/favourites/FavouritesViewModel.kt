package com.example.mystoic.ui.favourites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.FavouriteEntity
import com.example.mystoic.data.QuoteDatabaseRepository
import com.example.mystoic.data.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavouritesViewModel(
    private val quoteDatabaseRepository: QuoteDatabaseRepository
) : ViewModel() {

    val favouritesUiState: Flow<FavouritesUiState> =
        quoteDatabaseRepository.getAllFavouritesStream().map {
            FavouritesUiState(favourites = it)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = FavouritesUiState()
            )

    fun deleteFavourite(quoteEntity: QuoteEntity) {
        val favouriteToDelete = FavouriteEntity(quoteEntity.id)
        viewModelScope.launch {
            quoteDatabaseRepository.deleteFavourite(favouriteToDelete)
        }
    }
}

data class FavouritesUiState (
    val favourites: List<QuoteEntity> = emptyList()
)