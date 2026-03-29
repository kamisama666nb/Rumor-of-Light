package com.rumoroflight.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * Pomodoro数据仓库
 * 
 * 功能:
 *  - 统计数据持久化
 *  - 用户设置存储
 *  - 历史记录管理
 */
class PomodoroRepository(private val context: Context) {
    
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "pomodoro_prefs")
    private val gson = Gson()
    
    // === 设置键 ===
    companion object {
        val FOCUS_DURATION = intPreferencesKey("focus_duration")
        val BREAK_DURATION = intPreferencesKey("break_duration")
        val LONG_BREAK_DURATION = intPreferencesKey("long_break_duration")
        val DAILY_GOAL = intPreferencesKey("daily_goal")
        val AMBIENT_SOUND = stringPreferencesKey("ambient_sound")
        val AMBIENT_VOLUME = floatPreferencesKey("ambient_volume")
        
        val TOTAL_COMPLETED = intPreferencesKey("total_completed")
        val TOTAL_FOCUS_MINUTES = intPreferencesKey("total_focus_minutes")
        val CURRENT_STREAK = intPreferencesKey("current_streak")
        val HISTORY_DATA = stringPreferencesKey("history_data")
        val LAST_USED_DATE = stringPreferencesKey("last_used_date")
    }
    
    // === 设置数据 ===
    data class PomodoroSettings(
        val focusDuration: Int = 25,        // 分钟
        val breakDuration: Int = 5,         // 分钟
        val longBreakDuration: Int = 15,    // 分钟
        val dailyGoal: Int = 8,             // 个番茄钟
        val ambientSound: String = "none",  // none/rain/cafe/forest
        val ambientVolume: Float = 0.5f     // 0.0 - 1.0
    )
    
    // === 统计数据 ===
    data class PomodoroStats(
        val totalCompleted: Int = 0,
        val totalFocusMinutes: Int = 0,
        val currentStreak: Int = 0,
        val todayCompleted: Int = 0,
        val history: Map<String, Int> = emptyMap() // 日期 -> 完成数
    )
    
    // === 历史记录项 ===
    data class HistoryEntry(
        val date: String,
        val completed: Int
    )
    
    // === 读取设置 ===
    val settingsFlow: Flow<PomodoroSettings> = context.dataStore.data.map { prefs ->
        PomodoroSettings(
            focusDuration = prefs[FOCUS_DURATION] ?: 25,
            breakDuration = prefs[BREAK_DURATION] ?: 5,
            longBreakDuration = prefs[LONG_BREAK_DURATION] ?: 15,
            dailyGoal = prefs[DAILY_GOAL] ?: 8,
            ambientSound = prefs[AMBIENT_SOUND] ?: "none",
            ambientVolume = prefs[AMBIENT_VOLUME] ?: 0.5f
        )
    }
    
    // === 读取统计 ===
    val statsFlow: Flow<PomodoroStats> = context.dataStore.data.map { prefs ->
        val historyJson = prefs[HISTORY_DATA] ?: "{}"
        val history: Map<String, Int> = gson.fromJson(
            historyJson,
            object : TypeToken<Map<String, Int>>() {}.type
        ) ?: emptyMap()
        
        val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
        val lastUsedDate = prefs[LAST_USED_DATE] ?: today
        
        // 检查是否是新的一天
        val todayCompleted = if (lastUsedDate == today) {
            history[today] ?: 0
        } else {
            0
        }
        
        PomodoroStats(
            totalCompleted = prefs[TOTAL_COMPLETED] ?: 0,
            totalFocusMinutes = prefs[TOTAL_FOCUS_MINUTES] ?: 0,
            currentStreak = prefs[CURRENT_STREAK] ?: 0,
            todayCompleted = todayCompleted,
            history = history
        )
    }
    
    // === 保存设置 ===
    suspend fun saveSettings(settings: PomodoroSettings) {
        context.dataStore.edit { prefs ->
            prefs[FOCUS_DURATION] = settings.focusDuration
            prefs[BREAK_DURATION] = settings.breakDuration
            prefs[LONG_BREAK_DURATION] = settings.longBreakDuration
            prefs[DAILY_GOAL] = settings.dailyGoal
            prefs[AMBIENT_SOUND] = settings.ambientSound
            prefs[AMBIENT_VOLUME] = settings.ambientVolume
        }
    }
    
    // === 记录完成一个番茄钟 ===
    suspend fun recordCompletion(focusMinutes: Int) {
        context.dataStore.edit { prefs ->
            val today = LocalDate.now().format(DateTimeFormatter.ISO_DATE)
            val lastUsedDate = prefs[LAST_USED_DATE] ?: today
            
            // 读取历史数据
            val historyJson = prefs[HISTORY_DATA] ?: "{}"
            val history: MutableMap<String, Int> = gson.fromJson(
                historyJson,
                object : TypeToken<MutableMap<String, Int>>() {}.type
            ) ?: mutableMapOf()
            
            // 更新今日数据
            history[today] = (history[today] ?: 0) + 1
            
            // 保存历史
            prefs[HISTORY_DATA] = gson.toJson(history)
            prefs[LAST_USED_DATE] = today
            
            // 更新总计
            prefs[TOTAL_COMPLETED] = (prefs[TOTAL_COMPLETED] ?: 0) + 1
            prefs[TOTAL_FOCUS_MINUTES] = (prefs[TOTAL_FOCUS_MINUTES] ?: 0) + focusMinutes
            
            // 更新连击
            if (lastUsedDate == today) {
                prefs[CURRENT_STREAK] = (prefs[CURRENT_STREAK] ?: 0) + 1
            } else {
                prefs[CURRENT_STREAK] = 1
            }
        }
    }
    
    // === 获取最近7天数据 ===
    suspend fun getWeeklyData(): List<HistoryEntry> {
        val history = mutableListOf<HistoryEntry>()
        val today = LocalDate.now()
        
        context.dataStore.data.map { prefs ->
            val historyJson = prefs[HISTORY_DATA] ?: "{}"
            val historyMap: Map<String, Int> = gson.fromJson(
                historyJson,
                object : TypeToken<Map<String, Int>>() {}.type
            ) ?: emptyMap()
            
            for (i in 6 downTo 0) {
                val date = today.minusDays(i.toLong())
                val dateStr = date.format(DateTimeFormatter.ISO_DATE)
                history.add(
                    HistoryEntry(
                        date = dateStr,
                        completed = historyMap[dateStr] ?: 0
                    )
                )
            }
        }.collect { }
        
        return history
    }
}
