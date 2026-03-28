package com.rumoroflight.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape // 必须导入这个
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rumoroflight.viewmodel.TimerViewModel

@Composable
fun PomodoroScreen(viewModel: TimerViewModel) {
    // 进度条计算（注意：这里的总时间建议从 viewModel 中动态获取，目前暂定 25 分钟）
    val totalTime = 25 * 60 * 1000f
    val progress by animateFloatAsState(
        targetValue = viewModel.timeLeft.toFloat() / totalTime,
        label = "TimerProgress"
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // 圆形计时器绘制
        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(280.dp)) {
            val colorPrimary = MaterialTheme.colorScheme.primary
            val colorVariant = MaterialTheme.colorScheme.surfaceVariant
            
            Canvas(modifier = Modifier.fillMaxSize()) {
                // 背景圆环
                drawCircle(
                    color = colorVariant, 
                    style = Stroke(width = 8.dp.toPx())
                )
                // 进度圆弧
                drawArc(
                    color = colorPrimary,
                    startAngle = -90f,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
                )
            }
            
            // 时间数字显示
            val minutes = (viewModel.timeLeft / 1000) / 60
            val seconds = (viewModel.timeLeft / 1000) % 60
            Text(
                text = "%02d:%02d".format(minutes, seconds),
                // 使用之前在 Type.kt 定义的 displayLarge 样式
                style = MaterialTheme.typography.displayLarge.copy(fontSize = 54.sp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        // 控制按钮
        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
            Button(
                onClick = { viewModel.toggleTimer() },
                // 修正点：使用 CircleShape 替代不存在的 shapes.full
                shape = CircleShape 
            ) {
                Text(if (viewModel.isRunning) "暂停" else "开始")
            }
            
            OutlinedButton(
                onClick = { viewModel.resetTimer() },
                // 修正点：使用 CircleShape
                shape = CircleShape 
            ) {
                Text("重置")
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        // 今日统计
        Text(
            text = "今日已完成: ${viewModel.totalCompleted} 个番茄钟",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
