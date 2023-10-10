package com.momento.mystoic.ui

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.momento.mystoic.MyStoicApplication
import com.momento.mystoic.ui.favourites.FavouritesViewModel
import com.momento.mystoic.ui.home.HomeScreenViewModel
import com.momento.mystoic.ui.journal.JournalEntryScreenViewModel
import com.momento.mystoic.ui.journal.JournalScreenViewModel
import com.momento.mystoic.ui.permission.PermissionsViewModel

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
        initializer {
            FavouritesViewModel(
                myStoicApplication().container.quoteDatabaseRepository
            )
        }
    }
}

fun CreationExtras.myStoicApplication(): MyStoicApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MyStoicApplication)