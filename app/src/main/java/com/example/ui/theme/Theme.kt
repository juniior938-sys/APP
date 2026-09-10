package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val NeonAthleticColorScheme = darkColorScheme(
    primary = NeonRed,
    onPrimary = Color.White,
    primaryContainer = NeonRedDark,
    onPrimaryContainer = Color(0xFFFFD6DC),
    secondary = NeonOrange,
    onSecondary = Color.White,
    secondaryContainer = NeonOrangeDark,
    onSecondaryContainer = Color(0xFFFFE2CC),
    tertiary = NeonGold,
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFF523600),
    onTertiaryContainer = Color(0xFFFFE6B3),
    background = PureBlack,
    onBackground = TextWhitePrimary,
    surface = BlackSurface,
    onSurface = TextWhitePrimary,
    surfaceVariant = BlackSurfaceCard,
    onSurfaceVariant = TextWhiteSecondary,
    outline = BlackBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NeonAthleticColorScheme,
        typography = Typography,
        content = content
    )
}
