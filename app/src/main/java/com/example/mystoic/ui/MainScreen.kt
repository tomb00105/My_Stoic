package com.example.mystoic.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mystoic.MyStoicBottomNavigation
import com.example.mystoic.navigation.AppNavHost
import com.example.mystoic.navigation.BottomNavigationScreens
import com.example.mystoic.ui.home.HomeScreenViewModel
import com.example.mystoic.ui.permission.RequestPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val bottomNavigationItems = listOf(
        BottomNavigationScreens.Home,
        BottomNavigationScreens.Journal
    )

    val notificationsPermissionState = rememberPermissionState(
        android.Manifest.permission.POST_NOTIFICATIONS
    )

    Box(modifier = modifier.fillMaxSize()) {
        if (notificationsPermissionState.status.isGranted) {
            Scaffold(
                bottomBar = {
                    MyStoicBottomNavigation(navController, bottomNavigationItems)
                }
            ) { innerPadding ->
                AppNavHost(navController, modifier = Modifier.padding(innerPadding))
            }
        } else {
            RequestPermissions(notificationsPermissionState)
        }
    }

}

