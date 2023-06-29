package com.example.mystoic

import android.Manifest
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun RequestPermissions() {
    val notificationsPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    if (notificationsPermissionState.status.isGranted) {
        Text("Notification permission Granted")
    } else {
        Column {
            val textToShow = if (notificationsPermissionState.status.shouldShowRationale) {
                // If the user has denied the permission but the rationale can be shown,
                // then gently explain why the app requires this permission
                "Notifications are required to send you daily quotes and journal reminders. Please grant the permission."
            } else {
                // If it's the first time the user lands on this feature, or the user
                // doesn't want to be asked again for this permission, explain that the
                // permission is required
                "Notifications are required to send you daily quotes and journal reminders. " +
                        "Please grant the permission"
            }
            Text(textToShow)
            Button(onClick = { notificationsPermissionState.launchPermissionRequest() }) {
                Text("Request permission")
            }
        }
    }

}
