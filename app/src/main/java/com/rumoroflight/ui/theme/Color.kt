package com.rumoroflight.ui.theme

import androidx.compose.ui.graphics.Color

// === Rumor of Light 配色系统 ===
// 灵感来源: 米白纸张、石墨墨迹、雾灰背景
// 设计原则: 低对比度、高雅致、无视觉疲劳

// --- 主色调 (Primary) - 深青灰 ---
val Primary = Color(0xFF466270)           // 主色 - 深海蓝灰
val OnPrimary = Color(0xFFF0F9FF)         // 主色上的文字 - 浅冰蓝白
val PrimaryContainer = Color(0xFFC9E7F7) // 主色容器 - 天空蓝
val OnPrimaryContainer = Color(0xFF395663) // 主色容器上的文字

// --- 次要色调 (Secondary) - 橄榄绿 ---
val Secondary = Color(0xFF5A6344)         // 次要色 - 橄榄绿
val OnSecondary = Color(0xFFF4FDD6)       // 次要色上的文字 - 嫩绿白
val SecondaryContainer = Color(0xFFDEE7C0) // 次要色容器 - 淡绿米
val OnSecondaryContainer = Color(0xFF4D5538) // 次要色容器上的文字

// --- 强调色调 (Tertiary) - 暖棕 ---
val Tertiary = Color(0xFF685E4A)          // 强调色 - 暖棕色
val OnTertiary = Color(0xFFFFF8F0)        // 强调色上的文字
val TertiaryContainer = Color(0xFFF5E6CD) // 强调色容器 - 米黄
val OnTertiaryContainer = Color(0xFF5E5441) // 强调色容器上的文字

// --- 错误色调 (Error) ---
val Error = Color(0xFF9F403D)             // 错误色 - 深砖红
val OnError = Color(0xFFFFF7F6)           // 错误色上的文字
val ErrorContainer = Color(0xFFFE8983)    // 错误容器 - 淡红
val OnErrorContainer = Color(0xFF752121) // 错误容器上的文字

// --- 背景与表面 (Background & Surface) ---
val Background = Color(0xFFFAF9F5)        // 背景 - 米白纸张色
val OnBackground = Color(0xFF2F342E)      // 背景上的文字 - 深石墨

val Surface = Color(0xFFFAF9F5)           // 表面 - 与背景相同
val OnSurface = Color(0xFF2F342E)         // 表面上的文字
val SurfaceVariant = Color(0xFFE0E4DC)    // 表面变体 - 浅灰绿
val OnSurfaceVariant = Color(0xFF5C605A)  // 表面变体上的文字

// --- 表面层级系统 (Surface Elevation) ---
val SurfaceContainerLowest = Color(0xFFFFFFFF)  // 最低层 - 纯白
val SurfaceContainerLow = Color(0xFFF4F4EF)     // 低层 - 浅米白
val SurfaceContainer = Color(0xFFEDEEE8)        // 标准层 - 灰米白
val SurfaceContainerHigh = Color(0xFFE6E9E2)    // 高层 - 灰绿白
val SurfaceContainerHighest = Color(0xFFE0E4DC) // 最高层 - 深灰绿白

val SurfaceBright = Color(0xFFFAF9F5)     // 明亮表面
val SurfaceDim = Color(0xFFD6DCD2)        // 暗淡表面

// --- 轮廓与边框 (Outline) ---
val Outline = Color(0xFF787C75)           // 标准轮廓 - 中灰
val OutlineVariant = Color(0xFFAFB3AC)    // 轮廓变体 - 浅灰

// --- 反转色调 (Inverse) ---
val InverseSurface = Color(0xFF0D0F0D)    // 反转表面 - 深黑绿
val InverseOnSurface = Color(0xFF9D9D9A)  // 反转表面上的文字
val InversePrimary = Color(0xFFCEEDFD)    // 反转主色

// --- 暗色主题色彩 (暂时保留,未来实现午夜模式时启用) ---
val DarkPrimary = Color(0xFFBBD9E9)
val DarkOnPrimary = Color(0xFF264350)
val DarkPrimaryContainer = Color(0xFF3A5663)
val DarkOnPrimaryContainer = Color(0xFFC9E7F7)

val DarkSecondary = Color(0xFFCFD9B3)
val DarkOnSecondary = Color(0xFF3A4227)
val DarkSecondaryContainer = Color(0xFF4E5639)
val DarkOnSecondaryContainer = Color(0xFFDEE7C0)

val DarkTertiary = Color(0xFFE7D8C0)
val DarkOnTertiary = Color(0xFF4B4230)
val DarkTertiaryContainer = Color(0xFF5C523F)
val DarkOnTertiaryContainer = Color(0xFFF5E6CD)

val DarkBackground = Color(0xFF1A1C18)
val DarkOnBackground = Color(0xFFE0E4DC)
val DarkSurface = Color(0xFF1A1C18)
val DarkOnSurface = Color(0xFFE0E4DC)
