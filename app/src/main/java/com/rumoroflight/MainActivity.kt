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
import androidx.compose.material.icons.filled.*
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

// === 导航枚举 ===
enum class Screen {
    Library,    // 知识库 (Phase 2)
    Journal,    // 日记本
    Focus,      // 番茄钟
    Music,      // 音乐 (Phase 3)
    Profile     // 个人设置
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
                        text = "Rumor of Light",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Medium
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { /* 菜单抽屉 */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* 搜索功能 */ }) {
                        Icon(
                            imageVector = Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f)
                )
            )
        },
        bottomBar = { 
            BottomNavigationBar(currentScreen) { selected -> 
                currentScreen = selected 
            } 
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(
                targetState = currentScreen,
                label = "ScreenTransition"
            ) { screen ->
                when (screen) {
                    Screen.Library -> LibraryPlaceholder()
                    Screen.Journal -> JournalScreen()
                    Screen.Focus -> PomodoroScreen(viewModel = viewModel)
                    Screen.Music -> MusicPlaceholder()
                    Screen.Profile -> ProfilePlaceholder()
                }
            }
        }
    }
}

// === 底部导航栏 - KnowledgeVault 风格 ===
@Composable
fun BottomNavigationBar(
    currentScreen: Screen,
    onScreenSelected: (Screen) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 8.dp,
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                spotColor = Color.Black.copy(alpha = 0.03f)
            ),
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        tonalElevation = 3.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            modifier = Modifier
                .height(80.dp)
                .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
        ) {
            // Library
            BottomNavItem(
                selected = currentScreen == Screen.Library,
                onClick = { onScreenSelected(Screen.Library) },
                icon = Icons.Outlined.AutoStories,
                selectedIcon = Icons.Filled.AutoStories,
                label = "LIBRARY"
            )
            
            // Journal
            BottomNavItem(
                selected = currentScreen == Screen.Journal,
                onClick = { onScreenSelected(Screen.Journal) },
                icon = Icons.Outlined.EditNote,
                selectedIcon = Icons.Filled.EditNote,
                label = "JOURNAL"
            )
            
            // Focus (Center - Primary Action)
            BottomNavItem(
                selected = currentScreen == Screen.Focus,
                onClick = { onScreenSelected(Screen.Focus) },
                icon = Icons.Outlined.Timer,
                selectedIcon = Icons.Filled.Timer,
                label = "FOCUS",
                isPrimary = true
            )
            
            // Music
            BottomNavItem(
                selected = currentScreen == Screen.Music,
                onClick = { onScreenSelected(Screen.Music) },
                icon = Icons.Outlined.LibraryMusic,
                selectedIcon = Icons.Filled.LibraryMusic,
                label = "MUSIC"
            )
            
            // Profile
            BottomNavItem(
                selected = currentScreen == Screen.Profile,
                onClick = { onScreenSelected(Screen.Profile) },
                icon = Icons.Outlined.Fingerprint,
                selectedIcon = Icons.Filled.Fingerprint,
                label = "PROFILE"
            )
        }
    }
}

@Composable
fun RowScope.BottomNavItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: ImageVector,
    selectedIcon: ImageVector,
    label: String,
    isPrimary: Boolean = false
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = {
            Icon(
                imageVector = if (selected) selectedIcon else icon,
                contentDescription = label,
                modifier = Modifier.size(if (isPrimary) 26.dp else 24.dp)
            )
        },
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing
            )
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
        )
    )
}

// === 占位屏幕 (Phase 2/3 待实现) ===
@Composable
fun LibraryPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.AutoStories,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
            )
            Text(
                text = "Knowledge Library",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Coming in Phase 2\nRSS Feeds • Web Clippings • Notes",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun MusicPlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.LibraryMusic,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)
            )
            Text(
                text = "The Sonic Aura",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Coming in Phase 3\nNetease Cloud Music Integration",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ProfilePlaceholder() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Fingerprint,
                contentDescription = null,
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.secondary.copy(alpha = 0.6f)
            )
            Text(
                text = "Digital Identity",
                style = MaterialTheme.typography.displayMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Settings • Integrations • AI Config",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}
