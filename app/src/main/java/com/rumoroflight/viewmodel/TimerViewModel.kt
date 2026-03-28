package com.rumoroflight.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*

class TimerViewModel : ViewModel() {
    var timeLeft by mutableLongStateOf(25 * 60 * 1000L) // 初始25分钟
    var isRunning by mutableStateOf(false)
    var totalCompleted by mutableIntStateOf(0)
    
    private var timerJob: Job? = null

    fun toggleTimer() {
        if (isRunning) {
            timerJob?.cancel()
        } else {
            timerJob = viewModelScope.launch {
                while (timeLeft > 0) {
                    delay(1000)
                    timeLeft -= 1000
                }
                onTimerFinished()
            }
        }
        isRunning = !isRunning
    }

    private fun onTimerFinished() {
        isRunning = false
        totalCompleted++
        timeLeft = 25 * 60 * 1000L // 重置
    }

    fun resetTimer() {
        timerJob?.cancel()
        isRunning = false
        timeLeft = 25 * 60 * 1000L
    }
}
