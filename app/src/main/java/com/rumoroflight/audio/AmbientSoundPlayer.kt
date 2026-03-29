package com.rumoroflight.audio

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import com.rumoroflight.R

/**
 * 环境音播放器
 * 
 * 支持音效:
 *  - rain: 雨声
 *  - cafe: 咖啡馆
 *  - forest: 森林
 */
class AmbientSoundPlayer(private val context: Context) {
    
    private var mediaPlayer: MediaPlayer? = null
    private var currentSound: String = "none"
    
    /**
     * 播放环境音
     */
    fun play(soundType: String, volume: Float = 0.5f) {
        stop()
        
        if (soundType == "none") return
        
        val resId = when (soundType) {
            "rain" -> R.raw.ambient_rain
            "cafe" -> R.raw.ambient_cafe
            "forest" -> R.raw.ambient_forest
            else -> return
        }
        
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(context, android.net.Uri.parse("android.resource://${context.packageName}/$resId"))
                isLooping = true
                setVolume(volume, volume)
                prepare()
                start()
            }
            currentSound = soundType
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    
    /**
     * 停止播放
     */
    fun stop() {
        mediaPlayer?.apply {
            if (isPlaying) stop()
            release()
        }
        mediaPlayer = null
        currentSound = "none"
    }
    
    /**
     * 设置音量
     */
    fun setVolume(volume: Float) {
        mediaPlayer?.setVolume(volume, volume)
    }
    
    /**
     * 是否正在播放
     */
    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false
    
    /**
     * 获取当前音效
     */
    fun getCurrentSound(): String = currentSound
}
