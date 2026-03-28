package com.rumoroflight.ui.components

data class JournalEntry(
    val id: Int,
    val date: String,
    val content: String,
    val moodEmoji: String // 😊, 😔, 🌿, 🔥, ☁️
)

