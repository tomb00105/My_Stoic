package com.example.mystoic.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.data.QuoteDao
import com.example.mystoic.data.QuoteEntity
import kotlinx.coroutines.flow.Flow

class HomeScreenViewModel(private val quoteDao: QuoteDao): ViewModel() {

    fun getAllQuotes(): Flow<List<QuoteEntity>> = quoteDao.getAllQuotes()

    companion object {
        val factory : ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoicApplication)
                HomeScreenViewModel(application.database.quoteDao())
            }
        }
    }
}