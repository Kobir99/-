package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MidnightNavyCard
import com.example.ui.theme.MidnightNavyDark
import com.example.ui.theme.MidnightNavySurface
import com.example.ui.theme.WarmPaperCream
import com.example.ui.theme.WarmPaperKraft

data class PaperThemeColors(
    val backgroundBrush: Brush,
    val textColor: Color,
    val subtextColor: Color,
    val borderColor: Color,
    val accentColor: Color
)

fun getPaperThemeColors(paperStyle: String, isDarkTheme: Boolean = true): PaperThemeColors {
    return when (paperStyle) {
        "kraft_parchment" -> PaperThemeColors(
            backgroundBrush = Brush.verticalGradient(listOf(Color(0xFFE8DCB8), Color(0xFFDECFA5))),
            textColor = Color(0xFF2C2214),
            subtextColor = Color(0xFF5C4F3B),
            borderColor = Color(0xFFC4B287),
            accentColor = Color(0xFF8B4513)
        )
        "emerald_tint" -> PaperThemeColors(
            backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF0F3B2E), Color(0xFF09281F))),
            textColor = Color(0xFFF2FBF7),
            subtextColor = Color(0xFFA5D4C3),
            borderColor = Color(0xFF1E5E4A),
            accentColor = Color(0xFF57D1A2)
        )
        "midnight_charcoal" -> PaperThemeColors(
            backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF161E2E), Color(0xFF0E131F))),
            textColor = Color(0xFFF1F5F9),
            subtextColor = Color(0xFF94A3B8),
            borderColor = Color(0xFF283650),
            accentColor = Color(0xFFD4AF37)
        )
        else -> { // "warm_ivory"
            if (isDarkTheme) {
                PaperThemeColors(
                    backgroundBrush = Brush.verticalGradient(listOf(Color(0xFF1B2436), Color(0xFF131A28))),
                    textColor = Color(0xFFF5EFE6),
                    subtextColor = Color(0xFFAAB8CC),
                    borderColor = Color(0xFF2F3E5C),
                    accentColor = Color(0xFFE5C158)
                )
            } else {
                PaperThemeColors(
                    backgroundBrush = Brush.verticalGradient(listOf(Color(0xFFFCFAF5), Color(0xFFF5EFEB))),
                    textColor = Color(0xFF1C1E21),
                    subtextColor = Color(0xFF5A6270),
                    borderColor = Color(0xFFDFD6C4),
                    accentColor = Color(0xFFA67F2D)
                )
            }
        }
    }
}

@Composable
fun PaperSurface(
    paperStyle: String = "warm_ivory",
    isDarkTheme: Boolean = true,
    elevation: Dp = 4.dp,
    shapeRadius: Dp = 16.dp,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    val themeColors = getPaperThemeColors(paperStyle, isDarkTheme)
    val shape = RoundedCornerShape(shapeRadius)

    Box(
        modifier = modifier
            .shadow(elevation, shape)
            .clip(shape)
            .background(themeColors.backgroundBrush)
            .border(1.dp, themeColors.borderColor, shape),
        content = content
    )
}
