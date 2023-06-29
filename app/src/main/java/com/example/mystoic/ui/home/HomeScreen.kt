package com.example.mystoic.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mystoic.notifications.AlarmUtils
import com.example.mystoic.ui.AppViewModelProvider

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeScreenUiState by viewModel.homeScreenUiState.collectAsState(initial = HomeScreenUiState())
    val context = LocalContext.current
    Column(modifier = modifier.fillMaxSize()) {
        Text(text = homeScreenUiState.dailyQuoteText)
        Button(onClick = {
            val alarmUtils = AlarmUtils(context.applicationContext)
            alarmUtils.initRepeatingAlarm()
        }
            ) {

        }
    }
}