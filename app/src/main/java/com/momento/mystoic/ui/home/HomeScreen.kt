package com.momento.mystoic.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.momento.mystoic.R
import com.momento.mystoic.navigation.MainNavScreens
import com.momento.mystoic.ui.AppViewModelProvider
import com.momento.mystoic.ui.SharingBar
import com.momento.mystoic.ui.permission.NotificationsRequest
import com.momento.mystoic.ui.utils.MyStoicContentType
import com.momento.mystoic.ui.utils.MyStoicNavigationType

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    navigationType: MyStoicNavigationType,
    contentType: MyStoicContentType,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val navItems = listOf(
        MainNavScreens.Home,
        MainNavScreens.Journal,
        MainNavScreens.Favourites
    )
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsState(initial = HomeScreenUiState())
    val dailyQuoteDateUiState by viewModel.dailyQuoteDateUiState.collectAsState(initial = DailyQuoteDateUiState())
    val favouritesUiState by viewModel.favouritesUiState.collectAsState(initial = FavouritesUiState())
    val dailyQuoteIsFavourite = remember { mutableStateOf(favouritesUiState.favourites.contains(homeScreenUiState.id)) }
    dailyQuoteIsFavourite.value = favouritesUiState.favourites.contains(homeScreenUiState.id)
    val randomQuoteIsFavourite = remember { mutableStateOf(favouritesUiState.favourites.contains(viewModel.currentRandomQuote.value.id)) }
    randomQuoteIsFavourite.value = favouritesUiState.favourites.contains(viewModel.currentRandomQuote.value.id)

    val randomQuote = viewModel.currentRandomQuote.value

    val context = LocalContext.current
    val notificationsPermissionState = rememberPermissionState(
        Manifest.permission.POST_NOTIFICATIONS
    )
    val requestButtonIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    requestButtonIntent.data = Uri.parse("package:" + context.packageName)

    if (dailyQuoteDateUiState.dailyQuoteDate != dailyQuoteDateUiState.currentDate) {
        viewModel.setNewDailyQuote()
    }

    NotificationsRequest()

    when (navigationType) {
        MyStoicNavigationType.PERMANENT_NAVIGATION_DRAWER -> {
            HomeScreenContent(
                viewModel = viewModel,
                homeScreenUiState = homeScreenUiState,
                notificationsPermissionState = notificationsPermissionState,
                requestButtonIntent = requestButtonIntent,
                dailyQuoteIsFavourite = dailyQuoteIsFavourite,
                randomQuoteIsFavourite = randomQuoteIsFavourite,
                navigationType = navigationType,
                modifier = modifier
            )
        }
        MyStoicNavigationType.NAVIGATION_RAIL -> {
            HomeScreenContent(
                viewModel = viewModel,
                homeScreenUiState = homeScreenUiState,
                notificationsPermissionState = notificationsPermissionState,
                requestButtonIntent = requestButtonIntent,
                dailyQuoteIsFavourite = dailyQuoteIsFavourite,
                randomQuoteIsFavourite = randomQuoteIsFavourite,
                navigationType = navigationType,
                modifier = modifier
            )
        }
        else -> {
            HomeScreenContent(
                viewModel = viewModel,
                homeScreenUiState = homeScreenUiState,
                notificationsPermissionState = notificationsPermissionState,
                requestButtonIntent = requestButtonIntent,
                dailyQuoteIsFavourite = dailyQuoteIsFavourite,

                randomQuoteIsFavourite = randomQuoteIsFavourite,
                navigationType = navigationType,
                modifier = modifier
            )
        }
    }


}


@Composable
private fun ReplyNavigationRail(
    navController: NavHostController,
    navItems: List<MainNavScreens>,
    modifier: Modifier = Modifier
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    NavigationRail(modifier = modifier) {
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
}

@Composable
fun NavDrawerContent(
    navController: NavHostController,
    navItems: List<MainNavScreens>,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier,
    ) {
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
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreenContent(
    viewModel: HomeScreenViewModel,
    homeScreenUiState: HomeScreenUiState,
    notificationsPermissionState: PermissionState,
    requestButtonIntent: Intent,
    dailyQuoteIsFavourite: MutableState<Boolean>,
    randomQuoteIsFavourite: MutableState<Boolean>,
    navigationType: MyStoicNavigationType,
    modifier: Modifier
) {
    val columnPadding = if (navigationType == MyStoicNavigationType.BOTTOM_NAVIGATION) {
        0.dp
    } else {
        24.dp
    }
    LazyColumn(
        modifier = modifier.padding(columnPadding),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                QuoteCard(
                    viewModel = viewModel,
                    homeScreenUiState = homeScreenUiState,
                    notificationsPermissionState = notificationsPermissionState,
                    requestButtonIntent = requestButtonIntent,
                    quoteIsFavourite = dailyQuoteIsFavourite,
                    isDailyQuote = true,
                    navigationType = navigationType,
                    modifier = Modifier,
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                QuoteCard(
                    viewModel = viewModel,
                    homeScreenUiState = homeScreenUiState,
                    notificationsPermissionState = notificationsPermissionState,
                    requestButtonIntent = requestButtonIntent,
                    quoteIsFavourite = randomQuoteIsFavourite,
                    isDailyQuote = false,
                    navigationType = navigationType,
                    modifier = Modifier,
                )
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun QuoteCard(
    viewModel: HomeScreenViewModel,
    homeScreenUiState: HomeScreenUiState,
    notificationsPermissionState: PermissionState,
    requestButtonIntent: Intent,
    quoteIsFavourite: MutableState<Boolean>,
    isDailyQuote: Boolean,
    navigationType: MyStoicNavigationType,
    modifier: Modifier
) {
    val context = LocalContext.current
    val quoteText = if (isDailyQuote) {
        homeScreenUiState.dailyQuoteText
    } else {
        viewModel.currentRandomQuote.value.text
    }
    val quoteAuthor = if (isDailyQuote) {
        homeScreenUiState.dailyQuoteAuthor
    } else {
        viewModel.currentRandomQuote.value.author
    }

    Card(
        colors = CardDefaults.cardColors(
            MaterialTheme.colorScheme.primaryContainer
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        modifier = modifier
            .defaultMinSize(minHeight = 300.dp)
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                ) {
                    val authorImage = if (quoteAuthor == "Cleanthes") {
                        R.drawable.cleanthes
                    } else if (quoteAuthor == "Epictetus") {
                        R.drawable.epictetus
                    } else if (quoteAuthor.contains("Marcus")) {
                        R.drawable.marcus
                    } else {
                        R.drawable.seneca
                    }
                    Image(
                        painter = painterResource(authorImage),
                        contentDescription = "test",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(shape = CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = quoteAuthor,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                }
                    val textPadding = if (navigationType == MyStoicNavigationType.BOTTOM_NAVIGATION) {
                        0.dp
                    } else {
                        24.dp
                    }
                Text(
                    text = quoteText,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp, start = textPadding, end = textPadding)
                )
                if (!notificationsPermissionState.status.isGranted) {
                    Button(onClick = {
                        if (notificationsPermissionState.status.shouldShowRationale) {
                            notificationsPermissionState.launchPermissionRequest()
                        } else {
                            context.startActivity(requestButtonIntent)
                        }
                    }
                    ) {
                        Text(text = "Allow Notifications")
                    }
                }
                SharingBar(
                    quoteText = quoteText,
                    quoteAuthor = quoteAuthor,
                    isDailyQuote = isDailyQuote,
                    isFavourite = quoteIsFavourite.value,
                    homeScreenViewModel = viewModel,
                    favouritesViewModel = null,
                    favouritesQuoteEntity = null,
                    modifier = Modifier.padding(top = 16.dp, start = textPadding, end = textPadding)
                )
                if (!isDailyQuote) {
                    Button(
                        onClick = {
                            viewModel.setNewRandomQuote()
                        },
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text(text = "Get new random quote")
                    }
                }
            }

    }
}