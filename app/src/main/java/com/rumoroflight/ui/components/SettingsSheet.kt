@file:OptIn(ExperimentalMaterial3Api::class)

package com.rumoroflight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumoroflight.data.PomodoroRepository

/**
 * 设置面板 - BottomSheet
 * 
 * 可配置:
 *  - 工作时长
 *  - 休息时长
 *  - 长休息时长
 *  - 每日目标
 *  - 白噪音类型
 *  - 音量
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsSheet(
    settings: PomodoroRepository.PomodoroSettings,
    onDismiss: () -> Unit,
    onSave: (PomodoroRepository.PomodoroSettings) -> Unit
) {
    var focusDuration by remember { mutableStateOf(settings.focusDuration) }
    var breakDuration by remember { mutableStateOf(settings.breakDuration) }
    var longBreakDuration by remember { mutableStateOf(settings.longBreakDuration) }
    var dailyGoal by remember { mutableStateOf(settings.dailyGoal) }
    var ambientSound by remember { mutableStateOf(settings.ambientSound) }
    var ambientVolume by remember { mutableStateOf(settings.ambientVolume) }
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 3.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 32.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // === 标题栏 ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Timer Settings",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, "Close")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === 时长设置 ===
            SettingSection(title = "Duration") {
                DurationPicker(
                    label = "Focus",
                    value = focusDuration,
                    onValueChange = { focusDuration = it },
                    range = 5..60,
                    icon = Icons.Filled.Timer
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                DurationPicker(
                    label = "Short Break",
                    value = breakDuration,
                    onValueChange = { breakDuration = it },
                    range = 3..15,
                    icon = Icons.Filled.FreeBreakfast
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                DurationPicker(
                    label = "Long Break",
                    value = longBreakDuration,
                    onValueChange = { longBreakDuration = it },
                    range = 10..30,
                    icon = Icons.Filled.Weekend
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === 每日目标 ===
            SettingSection(title = "Daily Goal") {
                DurationPicker(
                    label = "Pomodoros per day",
                    value = dailyGoal,
                    onValueChange = { dailyGoal = it },
                    range = 1..16,
                    icon = Icons.Filled.Flag,
                    suffix = "🍅"
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === 白噪音设置 ===
            SettingSection(title = "Ambient Sound") {
                AmbientSoundSelector(
                    selected = ambientSound,
                    onSelect = { ambientSound = it }
                )
                
                if (ambientSound != "none") {
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    VolumeSlider(
                        volume = ambientVolume,
                        onVolumeChange = { ambientVolume = it }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // === 保存按钮 ===
            Button(
                onClick = {
                    onSave(
                        PomodoroRepository.PomodoroSettings(
                            focusDuration = focusDuration,
                            breakDuration = breakDuration,
                            longBreakDuration = longBreakDuration,
                            dailyGoal = dailyGoal,
                            ambientSound = ambientSound,
                            ambientVolume = ambientVolume
                        )
                    )
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "SAVE SETTINGS",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
            }
        }
    }
}

// === 设置分组 ===
@Composable
fun SettingSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(12.dp))
        content()
    }
}

// === 时长选择器 ===
@Composable
fun DurationPicker(
    label: String,
    value: Int,
    onValueChange: (Int) -> Unit,
    range: IntRange,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    suffix: String = "min"
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(
                    onClick = { if (value > range.first) onValueChange(value - 1) },
                    enabled = value > range.first
                ) {
                    Icon(Icons.Filled.Remove, "Decrease")
                }
                
                Text(
                    text = "$value $suffix",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.widthIn(min = 70.dp)
                )
                
                IconButton(
                    onClick = { if (value < range.last) onValueChange(value + 1) },
                    enabled = value < range.last
                ) {
                    Icon(Icons.Filled.Add, "Increase")
                }
            }
        }
    }
}

// === 白噪音选择器 ===
@Composable
fun AmbientSoundSelector(
    selected: String,
    onSelect: (String) -> Unit
) {
    val sounds = listOf(
        "none" to "None",
        "rain" to "Rain",
        "cafe" to "Café",
        "forest" to "Forest"
    )
    
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        sounds.forEach { (key, label) ->
            FilterChip(
                selected = selected == key,
                onClick = { onSelect(key) },
                label = {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selected == key) FontWeight.Bold else FontWeight.Medium
                    )
                },
                leadingIcon = if (selected == key) {
                    {
                        Icon(
                            Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else null,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }
    }
}

// === 音量滑块 ===
@Composable
fun VolumeSlider(
    volume: Float,
    onVolumeChange: (Float) -> Unit
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.VolumeDown,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            Slider(
                value = volume,
                onValueChange = onVolumeChange,
                valueRange = 0f..1f,
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp)
            )
            
            Icon(
                imageVector = Icons.Filled.VolumeUp,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = "Volume: ${(volume * 100).toInt()}%",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
    }
}
