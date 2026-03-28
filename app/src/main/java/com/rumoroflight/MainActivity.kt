package com.rumoroflight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.rumoroflight.ui.theme.RumorOfLightTheme

// 关键：根据你截图里的真实路径进行 Import
import com.rumoroflight.viewmodel.TimerViewModel
import com.rumoroflight.ui.components.PomodoroScreen
import com.rumoroflight.ui.components.JournalScreen

class MainActivity : ComponentActivity() {
    private val timerViewModel = TimerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RumorOfLightTheme {
                MainScreen(timerViewModel)
            }
        }
    }
}

enum class Screen { Pomodoro, Journal, Profile }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TimerViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.Pomodoro) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Rumor of Light", style = MaterialTheme.typography.titleLarge) }
            )
        },
        bottomBar = { 
            CustomBottomBar(currentScreen) { selected -> currentScreen = selected } 
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            Crossfade(targetState = currentScreen, label = "ScreenTransition") { screen ->
                when (screen) {
                    Screen.Pomodoro -> PomodoroScreen(viewModel = viewModel)
                    Screen.Journal -> JournalScreen()
                    Screen.Profile -> Text("个人设置界面", modifier = Modifier.padding(24.dp))
                }
            }
        }
    }
}

@Composable
fun CustomBottomBar(currentScreen: Screen, onScreenSelected: (Screen) -> Unit) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
    ) {
        NavigationBarItem(
            selected = currentScreen == Screen.Pomodoro,
            onClick = { onScreenSelected(Screen.Pomodoro) },
            icon = { Icon(Icons.Default.Timer, "Timer") },
            label = { Text("Focus") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Journal,
            onClick = { onScreenSelected(Screen.Journal) },
            icon = { Icon(Icons.Default.Edit, "Journal") },
            label = { Text("Journal") }
        )
        NavigationBarItem(
            selected = currentScreen == Screen.Profile,
            onClick = { onScreenSelected(Screen.Profile) },
            icon = { Icon(Icons.Default.Person, "Profile") },
            label = { Text("Profile") }
        )
    }
}
