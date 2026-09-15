package com.example.ui.theme

import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

// ==========================================
// PALETA DARK COM DEGRADÊ, VERMELHO NEON & LARANJA NEON
// Fundo: Dark Slate / Charcoal com Degradê
// Detalhes: Vermelho Neon vibrante + Laranja Neon elétrico
// ==========================================

// Degradê de Fundo ("Dark com degrade de fundo")
val DarkAppBackground = Color(0xFF0C0D11)
val PureBlack = Color(0xFF08090C)

val DiscreetAppGradient = Brush.verticalGradient(
    colors = listOf(
        Color(0xFF191B22), // Topo grafite escuro texturizado
        Color(0xFF101217), // Corpo carvão profundo
        Color(0xFF07080B)  // Base ultra escura
    )
)

// Superfícies e Cards Dark
val CardDark = Color(0xFF14161C)
val CardDarkElevated = Color(0xFF1B1E26)
val CardBorder = Color(0xFF282C38)
val CardBorderSubtle = Color(0xFF1E212A)

// Vermelho Neon (Destaque Principal)
val NeonRed = Color(0xFFFF1E44)
val NeonRedBright = Color(0xFFFF3358)
val NeonRedDark = Color(0xFFD6002A)
val NeonRedGlow = Color(0x33FF1E44)
val NeonRedContainer = Color(0xFF2E0F15)

// Laranja Neon (Destaque Secundário & Energia)
val NeonOrange = Color(0xFFFF6D00)
val NeonOrangeBright = Color(0xFFFF851A)
val NeonOrangeDark = Color(0xFFD65800)
val NeonOrangeGlow = Color(0x33FF6D00)
val NeonOrangeContainer = Color(0xFF2D1609)

// Destaques de apoio
val NeonGold = Color(0xFFFFAB00)
val NeonGoldContainer = Color(0xFF2B1F07)

// Activity Rings (Move 🔴 Neon Red, Exercise 🟠 Neon Orange, Stand 🟡 Neon Gold)
val RingMoveRed = Color(0xFFFF1E44)
val RingExerciseGreen = Color(0xFFFF6D00)
val RingStandBlue = Color(0xFFFFAB00)

// Tipografia para Fundo Dark (Alto Contraste e Legibilidade)
val DarkTextPrimary = Color(0xFFFFFFFF)
val DarkTextSecondary = Color(0xFFA6AEBD)
val DarkTextMuted = Color(0xFF6B7485)

val TextWhitePrimary = DarkTextPrimary
val TextWhiteSecondary = DarkTextSecondary
val TextWhiteMuted = DarkTextMuted

// ==========================================
// COMPATIBILIDADE COM TELAS E COMPONENTES
// ==========================================
val WarmCreamBackground = DarkAppBackground
val WarmCreamSurface = CardDark
val CardWhite = CardDark
val BlackSurface = CardDark
val BlackSurfaceCard = CardDark
val BlackSurfaceElevated = CardDarkElevated
val BlackBorder = CardBorder
val BlackBorderLight = CardBorderSubtle

// Mapeamentos para detalhes Neon Red
val MintGreen = NeonRed
val MintGreenLight = NeonRedContainer
val MintGreenDark = NeonRedBright
val MintGreenGlow = NeonRedGlow
val NeonGreen = NeonRed
val NeonGreenLight = NeonRedContainer
val NeonGreenDark = NeonRedBright

// Mapeamentos para detalhes Neon Orange
val CoralPeach = NeonOrange
val CoralPeachLight = NeonOrangeContainer
val CoralPeachDark = NeonOrangeBright
val CoralPeachGlow = NeonOrangeGlow

val PurpleAccent = NeonRedBright
val PurpleLight = NeonRedContainer
val AmberAccent = NeonOrangeBright
val AmberLight = NeonOrangeContainer
val BlueAccent = NeonOrange
val BlueLight = NeonOrangeContainer
val NeonBlue = NeonOrange

val EmeraldPrimary = NeonRed
val EmeraldLight = NeonRedContainer
val EmeraldDark = NeonRedBright
val CyanSecondary = NeonOrange
val CyanLight = NeonOrangeContainer
val CyanDark = NeonOrangeBright
val FlameAccent = NeonOrange
val AmberStreak = NeonOrange
val RoseHighlight = NeonRed

val SlateDarkBackground = DarkAppBackground
val SlateDarkSurface = CardDark
val SlateDarkSurfaceVariant = CardDarkElevated
val SlateDarkBorder = CardBorder
val SlateDarkTextPrimary = DarkTextPrimary
val SlateDarkTextSecondary = DarkTextSecondary

val SlateLightBackground = DarkAppBackground
val SlateLightSurface = CardDark
val SlateLightSurfaceVariant = CardDarkElevated
val SlateLightBorder = CardBorder
val SlateLightTextPrimary = DarkTextPrimary
val SlateLightTextSecondary = DarkTextSecondary



