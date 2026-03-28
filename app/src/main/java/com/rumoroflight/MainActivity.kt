package com.rumoroflight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
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

@Composable
fun MainScreen() {
    // 初始界面显示新名称
    Text("Rumor of Light 已启动")
}
