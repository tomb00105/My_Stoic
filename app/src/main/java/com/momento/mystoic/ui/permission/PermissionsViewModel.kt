package com.momento.mystoic.ui.permission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.momento.mystoic.data.PermissionsDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class PermissionsViewModel(private val permissionsDataStore: PermissionsDataStore) : ViewModel() {
    val permissionsDeniedUiState: Flow<PermissionsDeniedUiState> =
        permissionsDataStore.getFromDataStore().map {
        PermissionsDeniedUiState(it)
    }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000L),
                initialValue = PermissionsDeniedUiState()
            )

    fun saveToDataStore(isDeclined: Boolean) {
        permissionsDataStore.saveToDataStore(isDeclined)
    }
}

data class PermissionsDeniedUiState(
    val permissionDenied: Boolean = true
)