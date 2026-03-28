package com.rumoroflight.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun JournalScreen() {
    val entries = listOf(
        JournalEntry(1, "2026.03.28", "Rumor of Light 编译环境终于跑通了。", "🌿"),
        JournalEntry(2, "2026.03.27", "在手机上坚持开发，这就是光的力量。", "😊")
    )

    LazyColumn(
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("努力日记", style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(8.dp))
            Text("记录每一刻的微光。", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
        }
        
        items(entries) { entry ->
            JournalCard(entry)
        }
    }
}

@Composable
fun JournalCard(entry: JournalEntry) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                Text(entry.date, style = MaterialTheme.typography.labelSmall)
                Text(entry.moodEmoji)
            }
            Spacer(Modifier.height(12.dp))
            Text(entry.content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
