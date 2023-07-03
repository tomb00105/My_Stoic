package com.example.mystoic.ui.home

import android.icu.util.Calendar
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
    }

    val homeScreenUiState: Flow<HomeScreenUiState> =
        dailyQuoteRepository.getDailyQuoteEntityStream().map {
            HomeScreenUiState(it.text, it.author)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeScreenUiState()
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

data class DailyQuoteDateUiState(
    val dailyQuoteDate: String = "",
    val currentDate: String = "",
)