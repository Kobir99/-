package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MutedGoldBright,
    onPrimary = MidnightNavyDark,
    primaryContainer = MidnightNavyCard,
    onPrimaryContainer = MutedGoldPale,
    secondary = DeepEmeraldLight,
    onSecondary = SoftOffWhite,
    secondaryContainer = DeepEmeraldDark,
    onSecondaryContainer = SoftOffWhiteDim,
    tertiary = CrimsonWaxSealLight,
    onTertiary = SoftOffWhite,
    background = MidnightNavyDark,
    onBackground = SoftOffWhite,
    surface = MidnightNavySurface,
    onSurface = SoftOffWhite,
    surfaceVariant = MidnightNavyCard,
    onSurfaceVariant = SoftOffWhiteDim,
    outline = CharcoalBorder
)

private val LightColorScheme = lightColorScheme(
    primary = DeepEmerald,
    onPrimary = Color.White,
    primaryContainer = WarmPaperKraft,
    onPrimaryContainer = CharcoalInk,
    secondary = MutedGoldDeep,
    onSecondary = Color.White,
    secondaryContainer = WarmPaperMuted,
    onSecondaryContainer = CharcoalInk,
    tertiary = CrimsonWaxSeal,
    onTertiary = Color.White,
    background = WarmPaperCream,
    onBackground = CharcoalInk,
    surface = Color.White,
    onSurface = CharcoalInk,
    surfaceVariant = WarmPaperMuted,
    onSurfaceVariant = CharcoalMuted,
    outline = Color(0xFFD3C8B4)
)

@Composable
fun ChithiGhorTheme(
    darkTheme: Boolean = true, // Default to deep midnight navy literary room atmosphere
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Keep MyApplicationTheme alias so any generated tests or references still compile
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    ChithiGhorTheme(darkTheme = darkTheme, content = content)
}
