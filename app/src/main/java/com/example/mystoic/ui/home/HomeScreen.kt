package com.example.mystoic.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
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
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(text = homeScreenUiState.dailyQuoteText)
                        Button(onClick = {
                            val alarmUtils = AlarmUtils(context.applicationContext)
                            alarmUtils.initRepeatingAlarm()
                        }
                        ) {
                            Text(text = "**Set Alarm**")
                        }
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
                    .padding(8.dp)
            ) {

            }
        }
    }
}