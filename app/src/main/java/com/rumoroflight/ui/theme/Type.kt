package com.rumoroflight.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font         // 必须有这一行
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.rumoroflight.R

// 定义 Manrope 字体族
val Manrope = FontFamily(
    Font(R.font.manrope_regular6, FontWeight.Normal),
    Font(R.font.manrope_medium_5, FontWeight.Medium),
    Font(R.font.manrope_semibold_7, FontWeight.SemiBold)
)

// 定义 Newsreader 字体族
val Newsreader = FontFamily(
    Font(R.font.newsreader_regular, FontWeight.Normal),
    Font(R.font.newsreader_mediumitalic, FontWeight.Medium, FontStyle.Italic)
)

val Typography = Typography(
    displayLarge = TextStyle(
        fontFamily = Newsreader,
        fontWeight = FontWeight.Normal,
        fontSize = 34.sp,
        letterSpacing = (-0.5).sp
    ),
    titleLarge = TextStyle(
        fontFamily = Newsreader,
        fontWeight = FontWeight.Medium,
        fontStyle = FontStyle.Italic,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp
    ),
    labelSmall = TextStyle(
        fontFamily = Manrope,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        letterSpacing = 1.5.sp
    )
)
