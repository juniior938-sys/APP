package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkNeonColorScheme = darkColorScheme(
    primary = NeonRed,
    onPrimary = Color.White,
    primaryContainer = NeonRedContainer,
    onPrimaryContainer = NeonRedBright,
    secondary = NeonOrange,
    onSecondary = Color.White,
    secondaryContainer = NeonOrangeContainer,
    onSecondaryContainer = NeonOrangeBright,
    tertiary = NeonGold,
    onTertiary = Color.Black,
    tertiaryContainer = NeonGoldContainer,
    onTertiaryContainer = NeonGold,
    background = DarkAppBackground,
    onBackground = DarkTextPrimary,
    surface = CardDark,
    onSurface = DarkTextPrimary,
    surfaceVariant = CardDarkElevated,
    onSurfaceVariant = DarkTextSecondary,
    outline = CardBorder,
    outlineVariant = CardBorderSubtle
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkNeonColorScheme,
        typography = Typography,
        content = content
    )
}


