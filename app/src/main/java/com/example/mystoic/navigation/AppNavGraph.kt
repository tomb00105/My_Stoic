package com.example.mystoic.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.mystoic.R
import com.example.mystoic.ui.favourites.FavouritesScreen
import com.example.mystoic.ui.home.HomeScreen
import com.example.mystoic.ui.journal.JournalEntryScreen
import com.example.mystoic.ui.journal.JournalScreen

sealed class MainNavScreens(
    val route: String,
    @StringRes val resourceId: Int,
    val icon: ImageVector,
) {
    object Home : MainNavScreens("Home", R.string.home_screen_route, Icons.Filled.Home)
    object Journal : MainNavScreens("Journal", R.string.journal_screen_route, Icons.Filled.Edit)
    object  Favourites: MainNavScreens("Favourites", R.string.favourites_screen_route, Icons.Filled.Star)
}

sealed class SubScreen(
    val route: String,
    @StringRes val resourceId: Int
) {
    object Entry: SubScreen("journalEntry", R.string.entry_screen_route)
}

@Composable
fun MainNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = MainNavScreens.Home.route,
        modifier = modifier,
    ) {
        composable(route = MainNavScreens.Home.route) {
            HomeScreen()
        }
        composable(route = MainNavScreens.Journal.route) {
            JournalScreen(
                navigateToEntry = { navController.navigate("${SubScreen.Entry.route}/$it")}
            )
        }
        composable(route = MainNavScreens.Favourites.route) {
            FavouritesScreen()
        }
        composable(
            route = "${SubScreen.Entry.route}/{entryDate}",
            arguments = listOf(
                navArgument("entryDate") {
                    type = NavType.StringType
            })
        ) { backStackEntry ->
            JournalEntryScreen(
                entryDate = backStackEntry.arguments?.getString("entryDate"),
                navController = navController
            )
        }
    }
}

/*
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
}*/
