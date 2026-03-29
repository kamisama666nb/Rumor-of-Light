package com.rumoroflight.viewmodel

import android.app.Application
import android.content.Intent
import android.os.CountDownTimer
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rumoroflight.audio.AmbientSoundPlayer
import com.rumoroflight.data.PomodoroRepository
import com.rumoroflight.service.TimerNotificationService
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

/**
 * TimerViewModel - 番茄钟完整版
 */
class TimerViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = PomodoroRepository(application)
    private val soundPlayer = AmbientSoundPlayer(application)
    private val vibrator = application.getSystemService(Vibrator::class.java)
    
    // === 状态 ===
    var isRunning by mutableStateOf(false)
        private set
    var timeLeft by mutableStateOf(25 * 60 * 1000L)
        private set
    var currentMode by mutableStateOf(TimerMode.FOCUS)
        private set
    
    // === 设置 ===
    var settings by mutableStateOf(PomodoroRepository.PomodoroSettings())
        private set
    
    // === 统计 ===
    var stats by mutableStateOf(PomodoroRepository.PomodoroStats())
        private set
    
    // === UI状态 ===
    var showSettings by mutableStateOf(false)
    var showStats by mutableStateOf(false)
    
    // === 内部变量 ===
    private var countDownTimer: CountDownTimer? = null
    private var completedCycles = 0 // 完成的工作周期数(用于长休息判断)
    
    init {
        loadData()
    }
    
    // === 加载数据 ===
    private fun loadData() {
        viewModelScope.launch {
            repository.settingsFlow.collect { newSettings ->
                settings = newSettings
                if (!isRunning) {
                    resetTimerForMode()
                }
                
                // 更新白噪音
                if (isRunning && settings.ambientSound != "none") {
                    soundPlayer.play(settings.ambientSound, settings.ambientVolume)
                }
            }
        }
        
        viewModelScope.launch {
            repository.statsFlow.collect { newStats ->
                stats = newStats
            }
        }
    }
    
    // === 控制方法 ===
    
    fun toggleTimer() {
        if (isRunning) {
            pauseTimer()
        } else {
            startTimer()
        }
    }
    
    fun resetTimer() {
        stopTimer()
        resetTimerForMode()
    }
    
    fun switchMode(mode: TimerMode) {
        stopTimer()
        currentMode = mode
        resetTimerForMode()
    }
    
    fun skipSession() {
        onTimerComplete()
    }
    
    fun updateSettings(newSettings: PomodoroRepository.PomodoroSettings) {
        viewModelScope.launch {
            repository.saveSettings(newSettings)
        }
    }
    
    // === 私有方法 ===
    
    private fun startTimer() {
        isRunning = true
        
        // 启动白噪音
        if (settings.ambientSound != "none") {
            soundPlayer.play(settings.ambientSound, settings.ambientVolume)
        }
        
        // 启动前台服务
        startForegroundService()
        
        countDownTimer = object : CountDownTimer(timeLeft, 100) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeft = millisUntilFinished
                updateNotification()
            }
            
            override fun onFinish() {
                onTimerComplete()
            }
        }.start()
    }
    
    private fun pauseTimer() {
        isRunning = false
        countDownTimer?.cancel()
        soundPlayer.stop()
        updateNotification()
    }
    
    private fun stopTimer() {
        isRunning = false
        countDownTimer?.cancel()
        countDownTimer = null
        soundPlayer.stop()
        stopForegroundService()
    }
    
    private fun onTimerComplete() {
        stopTimer()
        vibrate()
        
        when (currentMode) {
            TimerMode.FOCUS -> {
                completedCycles++
                
                // 记录完成
                viewModelScope.launch {
                    repository.recordCompletion(settings.focusDuration)
                }
                
                // 判断是否进入长休息(每4个周期)
                currentMode = if (completedCycles % 4 == 0) {
                    TimerMode.LONG_BREAK
                } else {
                    TimerMode.BREAK
                }
            }
            TimerMode.BREAK, TimerMode.LONG_BREAK -> {
                currentMode = TimerMode.FOCUS
            }
        }
        
        resetTimerForMode()
        sendCompletionNotification()
    }
    
    private fun resetTimerForMode() {
        timeLeft = when (currentMode) {
            TimerMode.FOCUS -> settings.focusDuration * 60 * 1000L
            TimerMode.BREAK -> settings.breakDuration * 60 * 1000L
            TimerMode.LONG_BREAK -> settings.longBreakDuration * 60 * 1000L
        }
    }
    
    private fun vibrate() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            vibrator.vibrate(
                VibrationEffect.createWaveform(
                    longArrayOf(0, 200, 100, 200, 100, 400),
                    -1
                )
            )
        } else {
            @Suppress("DEPRECATION")
            vibrator.vibrate(longArrayOf(0, 200, 100, 200, 100, 400), -1)
        }
    }
    
    private fun startForegroundService() {
        val intent = Intent(getApplication(), TimerNotificationService::class.java).apply {
            action = TimerNotificationService.ACTION_START
            putExtra(TimerNotificationService.EXTRA_TIME_LEFT, timeLeft)
            putExtra(TimerNotificationService.EXTRA_IS_FOCUS, currentMode == TimerMode.FOCUS)
        }
        ContextCompat.startForegroundService(getApplication(), intent)
    }
    
    private fun updateNotification() {
        val intent = Intent(getApplication(), TimerNotificationService::class.java).apply {
            action = if (isRunning) {
                TimerNotificationService.ACTION_START
            } else {
                TimerNotificationService.ACTION_PAUSE
            }
            putExtra(TimerNotificationService.EXTRA_TIME_LEFT, timeLeft)
            putExtra(TimerNotificationService.EXTRA_IS_FOCUS, currentMode == TimerMode.FOCUS)
        }
        getApplication<Application>().startService(intent)
    }
    
    private fun stopForegroundService() {
        val intent = Intent(getApplication(), TimerNotificationService::class.java).apply {
            action = TimerNotificationService.ACTION_STOP
        }
        getApplication<Application>().startService(intent)
    }
    
    private fun sendCompletionNotification() {
        val intent = Intent(getApplication(), TimerNotificationService::class.java).apply {
            action = TimerNotificationService.ACTION_COMPLETE
            putExtra(TimerNotificationService.EXTRA_IS_FOCUS, currentMode == TimerMode.FOCUS)
        }
        getApplication<Application>().startService(intent)
    }
    
    override fun onCleared() {
        super.onCleared()
        stopTimer()
    }
}

enum class TimerMode {
    FOCUS,
    BREAK,
    LONG_BREAK
}
