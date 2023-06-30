package com.example.mystoic.navigation

import android.Manifest
import android.app.Application
import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mystoic.MyStoicApplication
import com.example.mystoic.R
import com.example.mystoic.data.PermissionsDataStore
import com.example.mystoic.ui.MainScreen
import com.example.mystoic.ui.home.HomeScreen
import com.example.mystoic.ui.journal.JournalScreen
import com.example.mystoic.ui.permission.RequestPermissions
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

sealed class TopLevelScreens(val route: String, @StringRes val resourceId: Int) {
    object Main: TopLevelScreens("Main", R.string.main_screen_route)
    object Permissions: TopLevelScreens("Permission", R.string.permission_screen_route)
}

sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : BottomNavigationScreens("Home", R.string.home_screen_route, Icons.Filled.Home)
    object Journal : BottomNavigationScreens("Journal", R.string.journal_screen_route, Icons.Filled.Edit)
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavigationScreens.Home.route,
        modifier = modifier,
    ) {
        composable(route = BottomNavigationScreens.Home.route) {
            HomeScreen()
        }
        composable(route = BottomNavigationScreens.Journal.route) {
            JournalScreen(
                navigateToHome = {},
            )
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun TopLevelNavHost(
    navController: NavHostController,
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
) {
    val notificationsPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
    val context = LocalContext.current
    val application = context.applicationContext as Application
    val dataStore = (application as MyStoicApplication).container.permissionsDataStore
    val permissionDeclinedState = dataStore.getFromDataStore().collectAsState(initial = true)

    val startScreen = getStartScreen(notificationsPermissionState, dataStore, permissionDeclinedState)

    NavHost(
        navController = navController,
        startDestination =  startScreen,
        modifier = modifier,
    ) {

        composable(route = TopLevelScreens.Main.route) {
            MainScreen(
                windowSizeClass = windowSizeClass
            )
        }
        composable(route = TopLevelScreens.Permissions.route) {
            RequestPermissions()
        }
    }
}


@OptIn(ExperimentalPermissionsApi::class)
fun getStartScreen(notificationsPermissionState: PermissionState, dataStore: PermissionsDataStore, permissionDeclinedState: State<Boolean>) : String {
        if (notificationsPermissionState.status.isGranted) {
            dataStore.saveToDataStore(false)
            return TopLevelScreens.Main.route
        } else if (!permissionDeclinedState.value) {
            return TopLevelScreens.Permissions.route
        } else {
            return TopLevelScreens.Main.route
        }
}