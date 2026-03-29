package com.rumoroflight.ui.components

import androidx.compose.animation.core.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumoroflight.viewmodel.TimerViewModel
import com.rumoroflight.viewmodel.TimerMode

/**
 * Pomodoro Screen - 终极完整版
 */
@Composable
fun PomodoroScreen(viewModel: TimerViewModel) {
    val totalTime = when (viewModel.currentMode) {
        TimerMode.FOCUS -> viewModel.settings.focusDuration * 60 * 1000L
        TimerMode.BREAK -> viewModel.settings.breakDuration * 60 * 1000L
        TimerMode.LONG_BREAK -> viewModel.settings.longBreakDuration * 60 * 1000L
    }
    
    val animatedProgress by animateFloatAsState(
        targetValue = viewModel.timeLeft.toFloat() / totalTime,
        animationSpec = tween(durationMillis = 300, easing = EaseOutCubic),
        label = "TimerProgress"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = when (viewModel.currentMode) {
            TimerMode.FOCUS -> MaterialTheme.colorScheme.background
            TimerMode.BREAK -> MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.15f)
            TimerMode.LONG_BREAK -> MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.15f)
        },
        animationSpec = tween(600),
        label = "BackgroundColor"
    )
    
    val accentColor by animateColorAsState(
        targetValue = when (viewModel.currentMode) {
            TimerMode.FOCUS -> MaterialTheme.colorScheme.primary
            TimerMode.BREAK -> MaterialTheme.colorScheme.secondary
            TimerMode.LONG_BREAK -> MaterialTheme.colorScheme.tertiary
        },
        animationSpec = tween(600),
        label = "AccentColor"
    )
    
    // === BottomSheets ===
    if (viewModel.showSettings) {
        SettingsSheet(
            settings = viewModel.settings,
            onDismiss = { viewModel.showSettings = false },
            onSave = { viewModel.updateSettings(it) }
        )
    }
    
    if (viewModel.showStats) {
        StatsSheet(
            stats = viewModel.stats,
            dailyGoal = viewModel.settings.dailyGoal,
            onDismiss = { viewModel.showStats = false }
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        backgroundColor,
                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
                    )
                )
            )
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // === 顶部栏 ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.showStats = true }) {
                    Icon(
                        imageVector = Icons.Filled.BarChart,
                        contentDescription = "Statistics",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "THE CURATOR'S DESK",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        letterSpacing = 2.sp
                    )
                    Text(
                        text = when (viewModel.currentMode) {
                            TimerMode.FOCUS -> "Deep Reading Mode"
                            TimerMode.BREAK -> "Mindful Break"
                            TimerMode.LONG_BREAK -> "Long Rest"
                        },
                        style = MaterialTheme.typography.titleLarge,
                        color = accentColor
                    )
                }
                
                IconButton(onClick = { viewModel.showSettings = true }) {
                    Icon(
                        imageVector = Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            // === 模式切换 ===
            Surface(
                modifier = Modifier.clip(RoundedCornerShape(16.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ModeSwitchButton(
                        selected = viewModel.currentMode == TimerMode.FOCUS,
                        onClick = { viewModel.switchMode(TimerMode.FOCUS) },
                        label = "Focus",
                        icon = Icons.Filled.Timer
                    )
                    ModeSwitchButton(
                        selected = viewModel.currentMode == TimerMode.BREAK,
                        onClick = { viewModel.switchMode(TimerMode.BREAK) },
                        label = "Break",
                        icon = Icons.Filled.FreeBreakfast
                    )
                    ModeSwitchButton(
                        selected = viewModel.currentMode == TimerMode.LONG_BREAK,
                        onClick = { viewModel.switchMode(TimerMode.LONG_BREAK) },
                        label = "Long",
                        icon = Icons.Filled.Weekend
                    )
                }
            }

            // === 引言卡片 ===
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                tonalElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = when (viewModel.currentMode) {
                            TimerMode.FOCUS -> "\"The library is inhabited by spirits that come out of the pages at night.\""
                            TimerMode.BREAK -> "\"Rest is not idleness, and to lie sometimes on the grass is not a waste of time.\""
                            TimerMode.LONG_BREAK -> "\"Almost everything will work again if you unplug it for a few minutes.\""
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            lineHeight = 22.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = when (viewModel.currentMode) {
                            TimerMode.FOCUS -> "— Isabel Allende"
                            TimerMode.BREAK -> "— John Lubbock"
                            TimerMode.LONG_BREAK -> "— Anne Lamott"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.weight(0.5f))

            // === 计时器 ===
            FocusTimerCircle(
                progress = animatedProgress,
                timeLeft = viewModel.timeLeft,
                accentColor = accentColor
            )

            Spacer(modifier = Modifier.weight(0.5f))

            // === 控制面板 ===
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 1.dp
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = { viewModel.toggleTimer() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = accentColor,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Icon(
                            imageVector = if (viewModel.isRunning) Icons.Filled.PauseCircle else Icons.Filled.PlayArrow,
                            contentDescription = null,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (viewModel.isRunning) "PAUSE" else "START",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.2.sp
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.resetTimer() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Refresh,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Reset", style = MaterialTheme.typography.labelMedium)
                        }

                        OutlinedButton(
                            onClick = { viewModel.skipSession() },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.SkipNext,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Skip", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }

            // === 统计栏 ===
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(
                    icon = Icons.Filled.CheckCircle,
                    value = "${viewModel.stats.todayCompleted}/${viewModel.settings.dailyGoal}",
                    label = "Today"
                )
                StatItem(
                    icon = Icons.Filled.Timer,
                    value = "${viewModel.stats.totalCompleted}",
                    label = "Total"
                )
                StatItem(
                    icon = Icons.Filled.LocalFireDepartment,
                    value = "${viewModel.stats.currentStreak}",
                    label = "Streak"
                )
            }
        }
    }
}

@Composable
fun ModeSwitchButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .height(40.dp)
            .width(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                Color.Transparent
            },
            contentColor = if (selected) {
                MaterialTheme.colorScheme.onPrimaryContainer
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            }
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = if (selected) 2.dp else 0.dp
        ),
        contentPadding = PaddingValues(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            fontSize = 11.sp
        )
    }
}

@Composable
fun FocusTimerCircle(progress: Float, timeLeft: Long, accentColor: Color) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(240.dp)
    ) {
        val bgColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)

        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 14.dp.toPx()
            val radius = size.minDimension / 2 - strokeWidth / 2

            drawCircle(
                color = bgColor,
                radius = radius,
                style = Stroke(width = strokeWidth)
            )

            drawArc(
                color = accentColor,
                startAngle = -90f,
                sweepAngle = 360f * progress,
                useCenter = false,
                topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                size = Size(size.width - strokeWidth, size.height - strokeWidth),
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            val minutes = (timeLeft / 1000) / 60
            val seconds = (timeLeft / 1000) % 60
            
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = (-1.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            
            Text(
                text = "Focus Session",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                letterSpacing = 1.2.sp
            )
        }
    }
}

@Composable
fun StatItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String,
    label: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(22.dp),
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            letterSpacing = 0.8.sp
        )
    }
}
