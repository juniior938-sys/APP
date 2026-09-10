package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Pure & Deep Black Backgrounds (Requested PRETO)
val PureBlack = Color(0xFF000000)
val BlackSurface = Color(0xFF0C0C0E)
val BlackSurfaceCard = Color(0xFF141417)
val BlackSurfaceElevated = Color(0xFF1B1B20)
val BlackBorder = Color(0xFF2C2C35)
val BlackBorderLight = Color(0xFF3F3F4D)

// Neon Red & Electric Crimson (Requested VERMELHO)
val NeonRed = Color(0xFFFF1E44)
val NeonRedBright = Color(0xFFFF3358)
val NeonRedDark = Color(0xFFC00A28)
val NeonRedGlow = Color(0x33FF1E44)

// Neon Orange & Vivid Amber (Requested LARANJA NEON)
val NeonOrange = Color(0xFFFF6B00)
val NeonOrangeBright = Color(0xFFFF851A)
val NeonOrangeDark = Color(0xFFD65500)
val NeonOrangeGlow = Color(0x33FF6B00)

// Text Colors
val TextWhitePrimary = Color(0xFFFFFFFF)
val TextWhiteSecondary = Color(0xFFA1A1AA)
val TextWhiteMuted = Color(0xFF71717A)

// Discreto Degradê Três Cores: Preto, Cinza Grafite, Vermelho Sutil
val GradientBlack = Color(0xFF0A0A0C)
val GradientCharcoalGray = Color(0xFF18181D)
val GradientSubtleRed = Color(0xFF280B12)

val DiscreetAppGradient = androidx.compose.ui.graphics.Brush.verticalGradient(
    colors = listOf(
        GradientBlack,
        GradientCharcoalGray,
        GradientSubtleRed
    )
)

// Status & Accents
val NeonGold = Color(0xFFFFB800)
val NeonGreen = Color(0xFF10B981)
val NeonBlue = Color(0xFF00D2FF)

// Compatibility aliases mapped to Neon Red & Neon Orange
val EmeraldPrimary = NeonRed
val EmeraldLight = NeonRedBright
val EmeraldDark = NeonRedDark
val CyanSecondary = NeonOrange
val CyanLight = NeonOrangeBright
val CyanDark = NeonOrangeDark
val FlameAccent = NeonOrange
val AmberStreak = NeonOrangeBright
val RoseHighlight = NeonRed
val SlateDarkBackground = PureBlack
val SlateDarkSurface = BlackSurface
val SlateDarkSurfaceVariant = BlackSurfaceCard
val SlateDarkBorder = BlackBorder
val SlateDarkTextPrimary = TextWhitePrimary
val SlateDarkTextSecondary = TextWhiteSecondary
val SlateLightBackground = PureBlack
val SlateLightSurface = BlackSurface
val SlateLightSurfaceVariant = BlackSurfaceCard
val SlateLightBorder = BlackBorder
val SlateLightTextPrimary = TextWhitePrimary
val SlateLightTextSecondary = TextWhiteSecondary

