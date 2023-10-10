package com.momento.mystoic.ui.home

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momento.mystoic.data.DailyQuoteRepository
import com.momento.mystoic.data.FavouriteEntity
import com.momento.mystoic.data.QuoteDatabaseRepository
import com.momento.mystoic.data.QuoteEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class HomeScreenViewModel(
    private val quoteDatabaseRepository: QuoteDatabaseRepository,
    private val dailyQuoteRepository: DailyQuoteRepository,
): ViewModel() {
    init {
        viewModelScope.launch {
            if (isDailyQuoteEmpty()) {
                Log.d("IS_QUOTE_EMPTY", "Log was empty in viewModel")
                setNewDailyQuote()
            }
        }
        setNewRandomQuote()
    }

    val currentRandomQuote = mutableStateOf(QuoteEntity(-1, "", ""))

    val homeScreenUiState: Flow<HomeScreenUiState> =
        dailyQuoteRepository.getDailyQuoteEntityStream().map {
            HomeScreenUiState(it.id, it.text, it.author)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeScreenUiState()
            )

    val favouritesUiState: Flow<FavouritesUiState> =
        quoteDatabaseRepository.getAllFavouritesIdStream().map {
            FavouritesUiState(it)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = FavouritesUiState()
            )

    val dailyQuoteDateUiState: Flow<DailyQuoteDateUiState> =
        dailyQuoteRepository.getDailyQuoteDateStream().map {
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val currentDate = dateFormat.format(calendar.time)
            DailyQuoteDateUiState(dailyQuoteDate = it, currentDate = currentDate)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = DailyQuoteDateUiState()
            )

    private suspend fun isDailyQuoteEmpty(): Boolean {
        return dailyQuoteRepository.dataStoreEmpty()
    }

    fun setNewDailyQuote() {
        viewModelScope.launch {
            val newDailyQuote =
                quoteDatabaseRepository.getRandomQuoteStream().first()
            dailyQuoteRepository.saveNewDailyQuote(newDailyQuote)
        }
    }

    fun setNewRandomQuote() {
        viewModelScope.launch {
            val newRandomQuote =
                quoteDatabaseRepository.getRandomQuoteStream().first()
            currentRandomQuote.value = newRandomQuote
        }
    }

    fun saveFavourite(isDailyQuote: Boolean) {
        var newFavourite: FavouriteEntity
        viewModelScope.launch {
            newFavourite = if (isDailyQuote) {
                FavouriteEntity(homeScreenUiState.first().id)
            } else {
                FavouriteEntity(currentRandomQuote.value.id)
            }
            quoteDatabaseRepository.insertFavourite(newFavourite)
        }
    }

    fun deleteFavourite(isDailyQuote: Boolean) {
        var favouriteToDelete: FavouriteEntity
        viewModelScope.launch {
            favouriteToDelete = if (isDailyQuote) {
                FavouriteEntity(homeScreenUiState.first().id)
            } else {
                FavouriteEntity(currentRandomQuote.value.id)
            }
            quoteDatabaseRepository.deleteFavourite(favouriteToDelete)
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeScreenUiState(
    val id: Int = -1,
    val dailyQuoteText: String = "",
    val dailyQuoteAuthor: String = "",
)

data class FavouritesUiState (
    val favourites: List<Int> = emptyList()
)

data class DailyQuoteDateUiState (
    val dailyQuoteDate: String = "",
    val currentDate: String = "",
)