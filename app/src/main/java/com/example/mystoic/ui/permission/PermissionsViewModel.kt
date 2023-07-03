package com.example.mystoic.ui.permission

import androidx.lifecycle.ViewModel
import com.example.mystoic.data.PermissionsDataStore

class PermissionsViewModel(private val permissionsDataStore: PermissionsDataStore) : ViewModel() {
    fun saveToDataStore(isDeclined: Boolean) {
        permissionsDataStore.saveToDataStore(isDeclined)
    }
}