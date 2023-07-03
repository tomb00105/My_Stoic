package com.example.mystoic.ui

import android.icu.util.Calendar
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.ui.home.HomeScreenViewModel
import com.example.mystoic.ui.journal.JournalEntryScreenViewModel
import com.example.mystoic.ui.journal.JournalScreenViewModel
import com.example.mystoic.ui.permission.PermissionsViewModel
import java.text.SimpleDateFormat
import java.util.Locale

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            JournalScreenViewModel(
                myStoicApplication().container.quoteDatabaseRepository
            )
        }
        initializer {
            HomeScreenViewModel(
                myStoicApplication().container.quoteDatabaseRepository,
                myStoicApplication().container.dailyQuoteRepository
            )
        }
        initializer {
            PermissionsViewModel(
                myStoicApplication().container.permissionsDataStore
            )
        }
        initializer {
            JournalEntryScreenViewModel(
                this.createSavedStateHandle(),
                myStoicApplication().container.quoteDatabaseRepository
            )
        }
    }
}

fun CreationExtras.myStoicApplication(): MyStoicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoicApplication)