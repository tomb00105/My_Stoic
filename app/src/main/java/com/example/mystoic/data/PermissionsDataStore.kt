package com.example.mystoic.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

val Context.permissionsDataStore: DataStore<Preferences> by preferencesDataStore(name = "permissions")

class PermissionsDataStore(private val context: Context) {
    companion object {
        val NOTIFICATIONS_DECLINED = booleanPreferencesKey("NOTIFICATIONS")
    }

    fun saveToDataStore(notificationsDeclined: Boolean) {
        runBlocking { context.permissionsDataStore.edit {
            it[NOTIFICATIONS_DECLINED] = notificationsDeclined
        } }
    }

    fun getFromDataStore() = context.permissionsDataStore.data.map {
        val notificationsDeclined = it[NOTIFICATIONS_DECLINED] ?: false
        notificationsDeclined
    }
}

