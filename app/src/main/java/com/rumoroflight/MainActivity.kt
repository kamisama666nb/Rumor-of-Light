package com.rumoroflight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rumoroflight.ui.theme.RumorOfLightTheme
import com.rumoroflight.viewmodel.TimerViewModel
import com.rumoroflight.ui.components.PomodoroScreen
import com.rumoroflight.ui.components.JournalScreen

class MainActivity : ComponentActivity() {
    private val timerViewModel = TimerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RumorOfLightTheme {
                MainScreen(timerViewModel)
            }
        }
    }
}

enum class Screen {
    Library,
    Journal,
    Focus,
    Music,
    Profile
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TimerViewModel) {
    var currentScreen by remember { mutableStateOf(Screen.Focus) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Rumor of Light",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Menu, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(Icons.Outlined.Search, contentDescription = null)
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(currentScreen) {
                currentScreen = it
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Crossfade(targetState = currentScreen, label = "") { screen ->
                when (screen) {
                    Screen.Library -> LibraryPlaceholder()
                    Screen.Journal -> JournalScreen()
                    Screen.Focus -> PomodoroScreen(viewModel)
                    Screen.Music -> MusicPlaceholder()
                    Screen.Profile -> ProfilePlaceholder()
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        NavigationBar(
            containerColor = MaterialTheme.colorScheme.surface,
            modifier = Modifier.height(80.dp)
        ) {
            BottomNavItem(
                currentScreen == Screen.Library,
                { onScreenSelected(Screen.Library) },
                Icons.Outlined.AutoStories,
                "LIBRARY"
            )
            BottomNavItem(
                currentScreen == Screen.Journal,
                { onScreenSelected(Screen.Journal) },
                Icons.Outlined.EditNote,
                "JOURNAL"
            )
            BottomNavItem(
                currentScreen == Screen.Focus,
                { onScreenSelected(Screen.Focus) },
                Icons.Outlined.Timer,
                "FOCUS",
                isPrimary = true
            )
            BottomNavItem(
                currentScreen == Screen.Music,
                { onScreenSelected(Screen.Music) },
                Icons.Outlined.LibraryMusic,
                "MUSIC"
            )
            BottomNavItem(
                currentScreen == Screen.Profile,
                { onScreenSelected(Screen.Profile) },
                Icons.Outlined.Fingerprint,
                "PROFILE"
            )
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    label: String,
    isPrimary: Boolean = false
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                icon,
                contentDescription = label,
                modifier = Modifier.size(if (isPrimary) 26.dp else 24.dp)
            )
        },
        label = {
            Text(
                label,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium
            )
        }
    )
}

@Composable
fun LibraryPlaceholder() {
    PlaceholderScreen(
        icon = Icons.Outlined.AutoStories,
        title = "Knowledge Library",
        desc = "Coming in Phase 2\nRSS • Notes"
    )
}

@Composable
fun MusicPlaceholder() {
    PlaceholderScreen(
        icon = Icons.Outlined.LibraryMusic,
        title = "The Sonic Aura",
        desc = "Coming in Phase 3\nMusic Integration"
    )
}

@Composable
fun ProfilePlaceholder() {
    PlaceholderScreen(
        icon = Icons.Outlined.Fingerprint,
        title = "Digital Identity",
        desc = "Settings • AI Config"
    )
}

@Composable
fun PlaceholderScreen(
    icon: ImageVector,
    title: String,
    desc: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(64.dp))
            Spacer(Modifier.height(16.dp))
            Text(title, style = MaterialTheme.typography.titleLarge)
            Spacer(Modifier.height(8.dp))
            Text(desc, textAlign = TextAlign.Center)
        }
    }
}
