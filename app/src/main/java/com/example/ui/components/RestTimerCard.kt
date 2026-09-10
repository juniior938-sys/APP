package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

@Composable
fun RestTimerCard(
    remainingSeconds: Int,
    totalSeconds: Int,
    isRunning: Boolean,
    onPlayPauseClick: () -> Unit,
    onAdd30sClick: () -> Unit,
    onResetClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val progress = if (totalSeconds > 0) {
        (remainingSeconds.toFloat() / totalSeconds).coerceIn(0f, 1f)
    } else 0f

    val animatedProgress by animateFloatAsState(targetValue = progress, label = "timer_progress")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.5.dp, NeonOrange, RoundedCornerShape(20.dp))
            .testTag("rest_timer_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Circular timer graphic with remaining seconds
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier.size(54.dp),
                        color = if (remainingSeconds > 5) NeonOrange else NeonGreen,
                        trackColor = PureBlack,
                        strokeWidth = 5.dp,
                        strokeCap = StrokeCap.Round
                    )
                    Icon(
                        imageVector = Icons.Default.HourglassBottom,
                        contentDescription = "Temporizador",
                        tint = NeonOrange,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column {
                    val minutes = remainingSeconds / 60
                    val seconds = remainingSeconds % 60
                    val formattedTime = String.format("%02d:%02d", minutes, seconds)

                    Text(
                        text = "Descanso Entre Séries",
                        style = MaterialTheme.typography.labelMedium.copy(color = TextWhiteSecondary)
                    )
                    Text(
                        text = formattedTime,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = if (remainingSeconds == 0) NeonGreen else TextWhitePrimary
                    )
                }
            }

            // Controls
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // +30s button
                FilledTonalButton(
                    onClick = onAdd30sClick,
                    modifier = Modifier.testTag("add_30s_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BlackSurfaceElevated,
                        contentColor = NeonOrange
                    )
                ) {
                    Text("+30s", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                // Play / Pause
                FilledTonalIconButton(
                    onClick = onPlayPauseClick,
                    modifier = Modifier.testTag("timer_play_pause_button"),
                    colors = IconButtonDefaults.filledTonalIconButtonColors(
                        containerColor = NeonOrangeGlow,
                        contentColor = NeonOrange
                    )
                ) {
                    Icon(
                        imageVector = if (isRunning) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isRunning) "Pausar" else "Iniciar",
                        modifier = Modifier.size(20.dp)
                    )
                }

                // Reset
                OutlinedIconButton(
                    onClick = onResetClick,
                    modifier = Modifier.testTag("timer_reset_button"),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reiniciar",
                        tint = TextWhiteSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
