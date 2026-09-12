package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.repository.StreakStats
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

@Composable
fun StreakCard(
    streakStats: StreakStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BlackBorder, RoundedCornerShape(20.dp))
            .testTag("streak_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Streak Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(NeonOrangeGlow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalFireDepartment,
                        contentDescription = "Fogo Dias Seguidos",
                        tint = NeonOrange,
                        modifier = Modifier.size(28.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${streakStats.currentStreakDays}",
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = NeonOrange
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (streakStats.currentStreakDays == 1) "Dia Seguido" else "Dias Seguidos",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                    }
                    Text(
                        text = if (streakStats.currentStreakDays > 0)
                            "Foco total! Mantenha a chama acesa."
                        else "Treine hoje para iniciar seus dias seguidos!",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Recorde movido para logo abaixo do contador e sub-texto
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NeonRed.copy(alpha = 0.18f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.4f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = "Melhor Sequência",
                                tint = NeonRed,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Recorde: ${streakStats.longestStreakDays}d",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonRed
                                )
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Meta Semanal
            val progressFraction = if (streakStats.weeklyTarget > 0) {
                (streakStats.workoutsThisWeek.toFloat() / streakStats.weeklyTarget).coerceIn(0f, 1f)
            } else 0f

            val animatedProgress by animateFloatAsState(
                targetValue = progressFraction,
                animationSpec = tween(durationMillis = 600),
                label = "weekly_progress"
            )

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = BlackSurfaceElevated,
                border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Progresso da Meta Semanal",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.SemiBold,
                                color = TextWhitePrimary
                            )
                        )
                        Text(
                            text = "${streakStats.workoutsThisWeek} de ${streakStats.weeklyTarget} treinos",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonOrange
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonOrange,
                        trackColor = PureBlack,
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Contadores (Treinos Feitos + Minutos Totais)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                LocalMetricPill(
                    icon = Icons.Default.CheckCircle,
                    value = "${streakStats.totalWorkouts}",
                    label = "Treinos Feitos",
                    tint = NeonRed,
                    modifier = Modifier.weight(1f)
                )

                LocalMetricPill(
                    icon = Icons.Default.Timer,
                    value = "${streakStats.totalMinutes}m",
                    label = "Tempo Total",
                    tint = NeonOrange,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun LocalMetricPill(
    icon: ImageVector,
    value: String,
    label: String,
    tint: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = BlackSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(tint.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = tint,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteSecondary)
                )
            }
        }
    }
}
