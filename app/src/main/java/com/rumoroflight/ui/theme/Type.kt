package com.rumoroflight.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp
import com.rumoroflight.R

// 定义 Manrope 字体族 (无衬线正文)
val ManropeFontFamily = FontFamily(
    Font(R.font.manrope_regular6, FontWeight.Normal),
    Font(R.font.manrope_medium_5, FontWeight.Medium),
    Font(R.font.manrope_semibold_7, FontWeight.SemiBold)
)

// 定义 Newsreader 字体族 (衬线标题)
val NewsreaderFontFamily = FontFamily(
    Font(R.font.newsreader_regular, FontWeight.Normal),
    Font(R.font.newsreader_mediumitalic, FontWeight.Medium, FontStyle.Italic)
)

// Material 3 Typography - 遵循 Rumor of Light 设计系统
val AppTypography = Typography(
    // 大标题 - Newsreader 衬线字体
    displayLarge = TextStyle(
        fontFamily = NewsreaderFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = (-0.5).sp,
        lineHeight = 42.sp
    ),
    displayMedium = TextStyle(
        fontFamily = NewsreaderFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        letterSpacing = 0.sp,
        lineHeight = 36.sp
    ),
    
    // 标题 - Newsreader 斜体
    titleLarge = TextStyle(
        fontFamily = NewsreaderFontFamily,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp,
        letterSpacing = 0.sp,
        lineHeight = 28.sp
    ),
    titleMedium = TextStyle(
        fontFamily = NewsreaderFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 18.sp,
        letterSpacing = 0.15.sp,
        lineHeight = 24.sp
    ),
    
    // 正文 - Manrope 无衬线
    bodyLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 24.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        letterSpacing = 0.25.sp,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        letterSpacing = 0.4.sp,
        lineHeight = 16.sp
    ),
    
    // 标签 - Manrope 全大写小字号
    labelLarge = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        letterSpacing = 0.1.sp,
        lineHeight = 20.sp
    ),
    labelMedium = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        letterSpacing = 0.5.sp,
        lineHeight = 16.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 1.5.sp,
        lineHeight = 16.sp
    )
)
