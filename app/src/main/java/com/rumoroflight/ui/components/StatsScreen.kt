@file:OptIn(ExperimentalMaterial3Api::class)

package com.rumoroflight.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumoroflight.data.PomodoroRepository
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * 统计屏幕 - BottomSheet
 */
@Composable
fun StatsSheet(
    stats: PomodoroRepository.PomodoroStats,
    dailyGoal: Int,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        containerColor = MaterialTheme.colorScheme.surface
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
                    text = "Your Progress",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Filled.Close, "Close")
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === 今日目标进度 ===
            TodayGoalCard(
                completed = stats.todayCompleted,
                goal = dailyGoal
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === 本周趋势 ===
            WeeklyChart(history = stats.history)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // === 总体统计卡片 ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    icon = Icons.Filled.CheckCircle,
                    value = "${stats.totalCompleted}",
                    label = "Total",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.primary
                )
                
                StatCard(
                    icon = Icons.Filled.Timer,
                    value = "${stats.totalFocusMinutes / 60}h",
                    label = "Hours",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.secondary
                )
                
                StatCard(
                    icon = Icons.Filled.LocalFireDepartment,
                    value = "${stats.currentStreak}",
                    label = "Streak",
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

// === 今日目标卡片 ===
@Composable
fun TodayGoalCard(completed: Int, goal: Int) {
    val progress = (completed.toFloat() / goal.coerceAtLeast(1)).coerceIn(0f, 1f)
    
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Today's Goal",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 1.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "$completed / $goal",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = progress,
                        modifier = Modifier.size(72.dp),
                        strokeWidth = 8.dp,
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
            
            if (completed >= goal) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "🎉 Goal achieved! Great work!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// === 本周图表 ===
@Composable
fun WeeklyChart(history: Map<String, Int>) {
    val maxValue = history.values.maxOrNull()?.toFloat() ?: 1f
    
    // 获取最近7天
    val today = java.time.LocalDate.now()
    val weekData = (6 downTo 0).map { daysAgo ->
        val date = today.minusDays(daysAgo.toLong())
        val dateStr = date.format(java.time.format.DateTimeFormatter.ISO_DATE)
        dateStr to (history[dateStr] ?: 0)
    }
    
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "This Week",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weekData.forEach { (dateStr, value) ->
                    val dayLabel = try {
                        java.time.LocalDate.parse(dateStr)
                            .dayOfWeek.toString().substring(0, 3)
                    } catch (e: Exception) {
                        "?"
                    }
                    
                    val height = if (maxValue > 0) {
                        (value.toFloat() / maxValue * 120).dp
                    } else {
                        0.dp
                    }
                    
                    BarItem(
                        label = dayLabel,
                        value = value,
                        height = height,
                        color = if (value > 0) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.surfaceVariant
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun BarItem(
    label: String,
    value: Int,
    height: androidx.compose.ui.unit.Dp,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        if (value > 0) {
            Text(
                text = "$value",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(height.coerceAtLeast(4.dp))
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                .background(color)
        )
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

// === 统计卡片 ===
@Composable
fun StatCard(
    icon: ImageVector,
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    color: Color
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        tonalElevation = 1.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 0.8.sp
            )
        }
    }
}
