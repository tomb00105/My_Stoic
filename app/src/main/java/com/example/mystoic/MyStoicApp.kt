package com.example.mystoic

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.mystoic.navigation.MainNavHost
import com.example.mystoic.navigation.MainNavScreens
import com.example.mystoic.ui.utils.MyStoicContentType
import com.example.mystoic.ui.utils.MyStoicNavigationType
import com.google.accompanist.permissions.ExperimentalPermissionsApi

@Composable
fun MyStoicApp(
    windowSizeClass: WindowSizeClass,
    navController: NavHostController = rememberNavController(),
) {
    val navigationType: MyStoicNavigationType
    val contentType: MyStoicContentType
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            navigationType = MyStoicNavigationType.BOTTOM_NAVIGATION
            contentType = MyStoicContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium -> {
            navigationType = MyStoicNavigationType.NAVIGATION_RAIL
            contentType = MyStoicContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded -> {
            navigationType = MyStoicNavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType = MyStoicContentType.LIST_AND_DETAIL
        }
        else -> {
            navigationType = MyStoicNavigationType.BOTTOM_NAVIGATION
            contentType = MyStoicContentType.LIST_ONLY
        }
    }

    val navItems = listOf(
        MainNavScreens.Home,
        MainNavScreens.Journal,
        MainNavScreens.Favourites
    )

    when (navigationType) {
        MyStoicNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            MyStoicNavDrawer(
                navController = navController,
                navItems = navItems,
                navigationType = navigationType,
                contentType = contentType,
            )
        }
        MyStoicNavigationType.NAVIGATION_RAIL -> {
            MyStoicNavRail(
                navController = navController,
                navItems = navItems,
                navigationType = navigationType,
                contentType = contentType,
            )
        }
        else -> {
            MyStoicBottomNavigation(
                navController = navController,
                navItems = navItems,
                navigationType = navigationType,
                contentType = contentType
            )
        }
    }
}

@Composable
private fun MyStoicNavDrawer(
    navController: NavHostController,
    navItems: List<MainNavScreens>,
    navigationType: MyStoicNavigationType,
    contentType: MyStoicContentType,
    modifier: Modifier = Modifier
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(
                modifier = Modifier.width(240.dp)
            ) {
                val backStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = backStackEntry?.destination
                navItems.forEach { screen ->
                    NavigationDrawerItem(
                        icon = { Icon(screen.icon, contentDescription = screen.route) },
                        label = { Text(stringResource(id = screen.resourceId)) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }

    ) {
        MainNavHost(navController = navController, navigationType = navigationType, contentType = contentType)
    }
}

@Composable
private fun MyStoicNavRail(
    navController: NavHostController,
    navItems: List<MainNavScreens>,
    navigationType: MyStoicNavigationType,
    contentType: MyStoicContentType,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    Row(modifier = modifier) {
        NavigationRail(modifier = Modifier) {
            navItems.forEach { screen ->
                NavigationRailItem(
                    icon = { Icon(screen.icon, contentDescription = screen.route) },
                    label = { Text(stringResource(id = screen.resourceId)) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
            MainNavHost(
                navController = navController,
                navigationType = navigationType,
                contentType = contentType,
            )

    }

}

@Composable
fun MyStoicBottomNavigation(
    navController: NavHostController,
    navItems: List<MainNavScreens>,
    navigationType: MyStoicNavigationType,
    contentType: MyStoicContentType,
    modifier: Modifier = Modifier
) {
    Scaffold(
        bottomBar = {
            NavigationBar(
                //containerColor = MaterialTheme.colorScheme.primaryContainer,
                modifier = modifier,
                content = {
                    val backStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = backStackEntry?.destination

                    navItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.route) },
                            label = { Text(stringResource(id = screen.resourceId)) },
                            selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                            onClick = {
                                navController.navigate(screen.route) {
                                    // Pop up to the start destination of the graph to
                                    // avoid building up a large stack of destinations
                                    // on the back stack as users select items
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    // Avoid multiple copies of the same destination when
                                    // reselecting the same item
                                    launchSingleTop = true
                                    // Restore state when reselecting a previously selected item
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            )
        }) { innerPadding ->
            MainNavHost(
                navController = navController,
                navigationType = navigationType,
                contentType = contentType,
                modifier = Modifier.padding(innerPadding))

    }
}