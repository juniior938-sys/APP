package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SportsGymnastics
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow

/**
 * Retorna uma miniatura gráfica de exercício representativa baseada no nome e equipamento
 * para fácil identificação visual no plano de treino.
 */
@Composable
fun ExerciseThumbnailBadge(
    exerciseName: String,
    equipment: String,
    modifier: Modifier = Modifier,
    size: Dp = 44.dp
) {
    val nameLower = exerciseName.lowercase()
    val equipLower = equipment.lowercase()

    val (icon: ImageVector, iconTint: Color, bgGlow: Color) = when {
        // Pernas / Agachamento / Leg
        nameLower.contains("agachamento") || nameLower.contains("leg") || nameLower.contains("passada") ||
        nameLower.contains("afundo") || nameLower.contains("panturrilha") || nameLower.contains("extensora") ||
        nameLower.contains("flexora") -> {
            Triple(Icons.Default.AccessibilityNew, NeonOrange, NeonOrangeGlow)
        }
        // Supino / Peito / Flexão / Crossover
        nameLower.contains("supino") || nameLower.contains("flexão") || nameLower.contains("peito") ||
        nameLower.contains("crucifixo") || nameLower.contains("cross") -> {
            Triple(Icons.Default.FitnessCenter, NeonRed, NeonRedGlow)
        }
        // Costas / Puxada / Remada / Barra
        nameLower.contains("puxada") || nameLower.contains("remada") || nameLower.contains("barra") ||
        nameLower.contains("dorsal") -> {
            Triple(Icons.Default.SportsGymnastics, NeonGreen, NeonGreen.copy(alpha = 0.2f))
        }
        // Ombros / Desenvolvimento / Elevação
        nameLower.contains("desenvolvimento") || nameLower.contains("ombro") || nameLower.contains("lateral") ||
        nameLower.contains("militar") -> {
            Triple(Icons.Default.Height, NeonRed, NeonRedGlow)
        }
        // Braços / Bíceps / Tríceps
        nameLower.contains("bíceps") || nameLower.contains("biceps") || nameLower.contains("tríceps") ||
        nameLower.contains("triceps") || nameLower.contains("rosca") || nameLower.contains("mergulho") -> {
            Triple(Icons.Default.FitnessCenter, NeonOrange, NeonOrangeGlow)
        }
        // Alongamento / Mobilidade / Yoga
        nameLower.contains("alongamento") || nameLower.contains("mobilidade") || nameLower.contains("respiração") -> {
            Triple(Icons.Default.SelfImprovement, Color(0xFF00D2FF), Color(0x3300D2FF))
        }
        // Abdômen / Prancha / Core
        nameLower.contains("abdominal") || nameLower.contains("prancha") || nameLower.contains("core") -> {
            Triple(Icons.Default.Straighten, NeonOrange, NeonOrangeGlow)
        }
        // Cardio / Corrida / Esteira / Bicicleta
        nameLower.contains("corrida") || nameLower.contains("caminhada") || nameLower.contains("bicicleta") ||
        nameLower.contains("esteira") || nameLower.contains("corda") -> {
            Triple(Icons.AutoMirrored.Filled.DirectionsRun, NeonGreen, NeonGreen.copy(alpha = 0.2f))
        }
        // Halteres ou Máquinas genéricos
        equipLower.contains("halter") || equipLower.contains("barra") || equipLower.contains("máquina") -> {
            Triple(Icons.Default.FitnessCenter, NeonRed, NeonRedGlow)
        }
        else -> {
            Triple(Icons.Default.FitnessCenter, NeonRed, NeonRedGlow)
        }
    }

    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(12.dp))
            .background(BlackSurfaceElevated)
            .border(1.dp, BlackBorder, RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        // Glow circular interno
        Box(
            modifier = Modifier
                .size(size - 10.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(bgGlow),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = exerciseName,
                tint = iconTint,
                modifier = Modifier.size(size * 0.55f)
            )
        }
    }
}
