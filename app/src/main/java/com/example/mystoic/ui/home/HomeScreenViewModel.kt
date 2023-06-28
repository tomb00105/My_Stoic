package com.example.mystoic.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.data.QuoteEntity
import com.example.mystoic.data.QuoteRepository
import kotlinx.coroutines.flow.Flow

class HomeScreenViewModel(private val quoteRepository: QuoteRepository): ViewModel() {
    fun getAllQuotes(): Flow<List<QuoteEntity>> = quoteRepository.getAllQuotesStream()
}