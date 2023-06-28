package com.example.mystoic.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.mystoic.R
import com.example.mystoic.ui.home.HomeScreen
import com.example.mystoic.ui.journal.JournalScreen

sealed class BottomNavigationScreens(val route: String, @StringRes val resourceId: Int, val icon: ImageVector) {
    object Home : BottomNavigationScreens("Home", R.string.home_screen_route, Icons.Filled.Home)
    object Journal : BottomNavigationScreens("Journal", R.string.journal_screen_route, Icons.Filled.Edit)
}

@Composable
fun AppNavHost(
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