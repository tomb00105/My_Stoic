package com.example.mystoic.ui.home

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.twotone.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.R
import com.example.mystoic.ui.AppViewModelProvider
import com.example.mystoic.ui.permission.NotificationsRequest
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsState(initial = HomeScreenUiState())
    val dailyQuoteDateUiState by viewModel.dailyQuoteDateUiState.collectAsState(initial = DailyQuoteDateUiState())
    val favouritesUiState by viewModel.favouritesUiState.collectAsState(initial = FavouritesUiState())
    val dailyQuoteIsFavourite = remember { mutableStateOf(favouritesUiState.favourites.contains(homeScreenUiState.id)) }
    dailyQuoteIsFavourite.value = favouritesUiState.favourites.contains(homeScreenUiState.id)

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

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.android_logo),
                            contentDescription = "test",
                            modifier = Modifier.size(120.dp)
                        )
                        Text(
                            text = homeScreenUiState.dailyQuoteAuthor,
                            textAlign = TextAlign.Center,
                        )
                        Text(
                            text = homeScreenUiState.dailyQuoteText,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp)
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
                            quoteText = homeScreenUiState.dailyQuoteText,
                            quoteAuthor = homeScreenUiState.dailyQuoteAuthor,
                            isFavourite = dailyQuoteIsFavourite.value,
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = randomQuote.text,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(4.dp)
                    )
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
}

@Composable
fun SharingBar(
    quoteText: String,
    quoteAuthor: String,
    isFavourite: Boolean,
    viewModel: HomeScreenViewModel,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current

    Row(modifier = modifier) {
        IconButton(onClick = {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(
                    Intent.EXTRA_TEXT,
                    "$quoteText\n\n-$quoteAuthor")
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(context, shareIntent, null)
        }) {
            Image(imageVector = Icons.Filled.Share, contentDescription = "Share quote")
        }
        IconButton(
            onClick = {
                if (isFavourite) {
                    viewModel.deleteFavourite(true)
                } else {
                    viewModel.saveFavourite(true)
                }
            }
        ) {
            if (isFavourite) {
                Image(imageVector = Icons.Filled.Star, contentDescription = "Favourite")
            } else {
                Image(imageVector = Icons.TwoTone.Star, contentDescription = "Add to favourites")
            }
        }
    }
}