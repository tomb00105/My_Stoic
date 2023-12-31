package com.momento.mystoic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.momento.compose.MyStoicTheme
import com.momento.mystoic.notifications.AlarmRequestCode
import com.momento.mystoic.notifications.AlarmUtils

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            MyStoicTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val alarm = AlarmUtils(this)
                    alarm.initRepeatingAlarm(AlarmRequestCode.DAILY_QUOTE)
                    alarm.initRepeatingAlarm(AlarmRequestCode.JOURNAL)
                    MyStoicApp(windowSizeClass = windowSizeClass)
                }
            }
        }
    }
}
