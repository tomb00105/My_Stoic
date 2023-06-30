package com.example.mystoic.ui.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mystoic.data.PermissionsDataStore
import com.example.mystoic.ui.home.HomeScreenUiState
import com.example.mystoic.ui.home.HomeScreenViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PermissionsViewModel(private val permissionsDataStore: PermissionsDataStore) : ViewModel() {
    fun saveToDataStore(isDeclined: Boolean) {
        permissionsDataStore.saveToDataStore(isDeclined)
    }
}