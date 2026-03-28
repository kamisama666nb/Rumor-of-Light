package com.rumoroflight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import com.rumoroflight.ui.theme.RumorOfLightTheme
import com.rumoroflight.viewmodel.TimerViewModel
import com.rumoroflight.ui.components.PomodoroScreen

class MainActivity : ComponentActivity() {
    
    // 实例化 ViewModel，生命周期跟随 Activity
    private val timerViewModel = TimerViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RumorOfLightTheme {
                // 传入 ViewModel 实例
                MainScreen(timerViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: TimerViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "Rumor of Light", 
                        style = MaterialTheme.typography.titleLarge 
                    ) 
                },
                navigationIcon = { 
                    IconButton(onClick = {}) { 
                        Icon(Icons.Default.Menu, contentDescription = "Menu") 
                    } 
                },
                actions = { 
                    IconButton(onClick = {}) { 
                        Icon(Icons.Default.Search, contentDescription = "Search") 
                    } 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )
        },
        bottomBar = { CustomBottomBar() }
    ) { innerPadding ->
        // 使用 Box 容器承载番茄钟界面，并处理系统边距
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            PomodoroScreen(viewModel = viewModel)
        }
    }
}

@Composable
fun CustomBottomBar() {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 0.dp,
        modifier = Modifier.clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
    ) {
        // 底部导航项示例（Profile 处于选中状态，匹配你的 HTML 设计）
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.List, contentDescription = "Library") },
            label = { Text("Library", style = MaterialTheme.typography.labelSmall) }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Edit, contentDescription = "Journal") },
            label = { Text("Journal", style = MaterialTheme.typography.labelSmall) }
        )
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile", style = MaterialTheme.typography.labelSmall) }
        )
    }
}
