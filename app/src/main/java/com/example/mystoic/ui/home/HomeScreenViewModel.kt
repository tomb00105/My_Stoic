package com.example.mystoic.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.DailyQuoteRepository
import com.example.mystoic.data.QuoteDatabaseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

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

    }
    val homeScreenUiState: Flow<HomeScreenUiState> =
        dailyQuoteRepository.getCurrentDailyQuoteEntityStream().map {
            HomeScreenUiState(it.text, it.author)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeScreenUiState()
            )

    private suspend fun isDailyQuoteEmpty(): Boolean {
        val isEmpty = dailyQuoteRepository.dataStoreEmpty()
        return isEmpty
    }

    private fun setNewDailyQuote() {
        viewModelScope.launch {
            val newDailyQuote = quoteDatabaseRepository.getRandomQuoteStream().first()
            dailyQuoteRepository.saveNewDailyQuote(newDailyQuote)
        }
    }
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

data class HomeScreenUiState(
    val dailyQuoteText: String = "",
    val dailyQuoteAuthor: String = "",
)