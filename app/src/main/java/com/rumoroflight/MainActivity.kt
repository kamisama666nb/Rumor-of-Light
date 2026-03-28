package com.rumoroflight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.rumoroflight.ui.theme.RumorOfLightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RumorOfLightTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Rumor of Light", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = { IconButton(onClick = {}) { Icon(Icons.Default.Menu, null) } },
                actions = { IconButton(onClick = {}) { Icon(Icons.Default.Search, null) } },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f)
                )
            )
        },
        bottomBar = { CustomBottomBar() }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(32.dp))
            [span_13](start_span)Text("Studio Settings", style = MaterialTheme.typography.displayLarge)[span_13](end_span)
            Text(
                "Refine your intellectual environment.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            [span_14](start_span))
            
            Spacer(Modifier.height(40.dp))
            
            // 示例：AI Configuration 卡片[span_14](end_span)
            SettingsCard(title = "AI Configuration") {
                Column(Modifier.padding(20.dp)) {
                    Text("SELECT INTELLIGENCE MODEL", style = MaterialTheme.typography.labelSmall)
                    Row(Modifier.padding(top = 12.dp)) {
                        Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)) {
                            Text("OpenAI")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SettingsCard(title: String, content: @Composable () -> Unit) {
    Column(Modifier.padding(vertical = 16.dp)) {
        [span_15](start_span)Text(title, style = MaterialTheme.typography.titleMedium)[span_15](end_span)
        Spacer(Modifier.height(12.dp))
        Box(Modifier.clip(RoundedCornerShape(16.dp)).background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))) {
            content()
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
        // 简化的导航项
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Person, null) },
            label = { Text("Profile", style = MaterialTheme.typography.labelSmall) }
        )
    }
}
