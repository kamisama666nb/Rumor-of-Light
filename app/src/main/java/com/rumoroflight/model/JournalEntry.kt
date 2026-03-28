package com.rumoroflight.model

data class JournalEntry(
    val id: Int,
    val date: String,
    val content: String,
    val moodEmoji: String // 😊, 😔, 🌿, 🔥, ☁️
)

