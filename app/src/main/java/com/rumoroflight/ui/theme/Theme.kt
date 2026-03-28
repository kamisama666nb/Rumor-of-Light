package com.rumoroflight.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    [span_7](start_span)primary = Color(0xFF466270),           // primary[span_7](end_span)
    onPrimary = Color(0xFFF0F9FF),
    [span_8](start_span)surface = Color(0xFFFAF9F5),           // background[span_8](end_span)
    onSurface = Color(0xFF2F342E),
    [span_9](start_span)surfaceVariant = Color(0xFFEDEEE8),    // surface-container[span_9](end_span)
    onSurfaceVariant = Color(0xFF5C605A),
    outline = Color(0xFF787C75)
)

@Composable
fun RumorOfLightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    [span_10](start_span)// 暂时强制使用亮色模式以匹配你的设计稿[span_10](end_span)
    val colorScheme = LightColorScheme 
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
