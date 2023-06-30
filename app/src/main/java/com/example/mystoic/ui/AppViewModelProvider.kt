package com.example.mystoic.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.ui.home.HomeScreenViewModel
import com.example.mystoic.ui.journal.JournalScreenViewModel
import com.example.mystoic.ui.permission.PermissionsViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            JournalScreenViewModel(myStoicApplication().container.quoteDatabaseRepository)
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
    }
}

fun CreationExtras.myStoicApplication(): MyStoicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoicApplication)