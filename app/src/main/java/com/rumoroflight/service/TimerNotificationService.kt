package com.rumoroflight.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.rumoroflight.MainActivity
import com.rumoroflight.R

/**
 * 计时器前台服务
 * 
 * 功能:
 *  - 后台计时器运行
 *  - 通知栏显示倒计时
 *  - 完成提醒
 */
class TimerNotificationService : Service() {
    
    companion object {
        const val CHANNEL_ID = "pomodoro_timer_channel"
        const val NOTIFICATION_ID = 1001
        
        const val ACTION_START = "ACTION_START"
        const val ACTION_PAUSE = "ACTION_PAUSE"
        const val ACTION_STOP = "ACTION_STOP"
        const val ACTION_COMPLETE = "ACTION_COMPLETE"
        
        const val EXTRA_TIME_LEFT = "EXTRA_TIME_LEFT"
        const val EXTRA_IS_FOCUS = "EXTRA_IS_FOCUS"
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> {
                val timeLeft = intent.getLongExtra(EXTRA_TIME_LEFT, 0)
                val isFocus = intent.getBooleanExtra(EXTRA_IS_FOCUS, true)
                startForeground(NOTIFICATION_ID, createNotification(timeLeft, isFocus, true))
            }
            ACTION_PAUSE -> {
                val timeLeft = intent.getLongExtra(EXTRA_TIME_LEFT, 0)
                val isFocus = intent.getBooleanExtra(EXTRA_IS_FOCUS, true)
                startForeground(NOTIFICATION_ID, createNotification(timeLeft, isFocus, false))
            }
            ACTION_COMPLETE -> {
                val isFocus = intent.getBooleanExtra(EXTRA_IS_FOCUS, true)
                showCompletionNotification(isFocus)
                stopSelf()
            }
            ACTION_STOP -> {
                stopForeground(STOP_FOREGROUND_REMOVE)
                stopSelf()
            }
        }
        return START_STICKY
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Pomodoro Timer",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Focus timer notifications"
                setSound(null, null)
            }
            
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }
    
    /**
     * 创建运行中通知
     */
    private fun createNotification(timeLeft: Long, isFocus: Boolean, isRunning: Boolean): Notification {
        val minutes = (timeLeft / 1000) / 60
        val seconds = (timeLeft / 1000) % 60
        val timeText = "%02d:%02d".format(minutes, seconds)
        
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(if (isFocus) "Focus Session" else "Break Time")
            .setContentText(timeText)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(isRunning)
            .setContentIntent(pendingIntent)
            .setCategory(NotificationCompat.CATEGORY_PROGRESS)
            .setSilent(true)
            .build()
    }
    
    /**
     * 显示完成通知
     */
    private fun showCompletionNotification(isFocus: Boolean) {
        val title = if (isFocus) "Focus Complete!" else "Break Over!"
        val message = if (isFocus) "Great work! Time for a break." else "Ready to focus again?"
        
        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .build()
        
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID + 1, notification)
    }
}
