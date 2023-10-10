package com.example.mystoic.ui.permission

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mystoic.ui.AppViewModelProvider
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun RequestPermissions(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: PermissionsViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val openDialog = remember { mutableStateOf(false) }
    val openAlertDialog = remember { mutableStateOf(false) }

    val notificationsPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )

    val context = LocalContext.current

    val requestButtonIntent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
    requestButtonIntent.data = Uri.parse("package:" + context.packageName)

    if (openAlertDialog.value) {
        AlertDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openAlertDialog.value = false
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(text = "Are you sure you want to deny this permission?\n\nYou will not be" +
                            " able to receive daily quote notifications.\n")
                    Row {
                        Button(onClick = {
                            viewModel.saveToDataStore(true)
                            openAlertDialog.value = false
                            openDialog.value = false
                        },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Yes")
                        }
                        Button(onClick = {
                            openAlertDialog.value = false
                        },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            }
        }
    }


    if (openDialog.value)
    {
        Dialog(onDismissRequest = { openDialog.value = false}) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize()
            ) {
                Card(modifier = Modifier.padding(16.dp)) {
                    Column(
                        modifier = Modifier.padding(4.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val textToShow =
                            if (notificationsPermissionState.status.shouldShowRationale) {
                                // If the user has denied the permission but the rationale can be shown,
                                // then gently explain why the app requires this permission
                                "Notifications are required to send you daily quotes and journal reminders. Please grant" +
                                        " the permission."
                            } else {
                                // If it's the first time the user lands on this feature, or the user
                                // doesn't want to be asked again for this permission, explain that the
                                // permission is required
                                "Notifications are required to send you daily quotes and journal reminders. Please allow" +
                                        " this permission via app settings."
                            }
                        Text(
                            textToShow,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(8.dp)
                        )
                        Row {
                            Button(
                                onClick = {
                                    Log.d("PERMISSION_REQUEST", "Permission requested")
                                    if (notificationsPermissionState.status.shouldShowRationale) {
                                        notificationsPermissionState.launchPermissionRequest()
                                    } else {
                                        context.startActivity(requestButtonIntent)
                                    }
                                    openDialog.value = false
                                },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text("Allow")
                            }
                            Button(
                                onClick = {
                                    Log.d("PERMISSION_REQUEST", "Permission denied")
                                    openAlertDialog.value = true
                                    // navController.navigate(TopLevelScreens.Main.route)
                                },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text("Deny")
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun NotificationsRequest(
    modifier: Modifier = Modifier,
    viewModel: PermissionsViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val permissionsDeniedUiState = viewModel.permissionsDeniedUiState.collectAsState(initial = PermissionsDeniedUiState())
    val permissionDenied = permissionsDeniedUiState.value.permissionDenied

    val permissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
    val context = LocalContext.current
    val requestButtonIntent = Intent(ACTION_APPLICATION_DETAILS_SETTINGS)
    requestButtonIntent.data = Uri.parse("package:" + context.packageName)

    val openDialog = remember { mutableStateOf(false) }

    if (!permissionState.status.isGranted && !permissionDenied) {
        Dialog(
            onDismissRequest = {}
        ) {
            Card(modifier = modifier.padding(16.dp)) {
                Column(
                    modifier = Modifier,
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val textToShow =
                        if (permissionState.status.shouldShowRationale) {
                            // If the user has denied the permission but the rationale can be shown,
                            // then gently explain why the app requires this permission
                            "Notifications are required to send you daily quotes and journal reminders.\n\n Please grant" +
                                    " the permission."
                        } else {
                            // If it's the first time the user lands on this feature, or the user
                            // doesn't want to be asked again for this permission, explain that the
                            // permission is required
                            "Notifications are required to send you daily quotes and journal reminders.\n\n Please allow" +
                                    " via app settings."
                        }
                    Text(
                        textToShow,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(8.dp)
                    )
                    Row {
                        Button(
                            onClick = {
                                if (permissionState.status.shouldShowRationale) {
                                    permissionState.launchPermissionRequest()
                                } else {
                                    context.startActivity(requestButtonIntent)
                                }
                            },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Allow")
                        }
                        Button(
                            onClick = { openDialog.value = true },
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text("Deny")
                        }
                    }
                }
            }
        }
        if (openDialog.value) {
            Dialog(onDismissRequest = { openDialog.value = false }) {
                Card(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = "Are you sure you want to deny this permission?\n\nYou will not be" +
                                " able to receive daily quote notifications.\n")
                        Row {
                            Button(onClick = {
                                viewModel.saveToDataStore(true)
                                openDialog.value = false
                            },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text("Yes")
                            }
                            Button(onClick = {
                                openDialog.value = false
                            },
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Text("Cancel")
                            }
                        }
                    }
                }
            }
        }
    }
}