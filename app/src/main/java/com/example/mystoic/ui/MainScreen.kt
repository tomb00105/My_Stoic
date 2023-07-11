package com.example.mystoic.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.mystoic.MyStoicBottomNavigation
import com.example.mystoic.navigation.MainNavScreens
import com.example.mystoic.navigation.MainNavHost

/*@Composable
fun MainScreen(
    windowSizeClass: WindowSizeClass,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val bottomNavigationItems = listOf(
        MainNavScreens.Home,
        MainNavScreens.Journal
    )
    Scaffold(
        bottomBar = {
            MyStoicBottomNavigation(navController, bottomNavigationItems)
        }
    ) { innerPadding ->
        MainNavHost(navController, modifier = Modifier.padding(innerPadding))
    }
}*/

