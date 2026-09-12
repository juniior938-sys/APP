package com.example.ui.components

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
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Snooze
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.data.model.UserProfile
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

/**
 * Popup de Lembrete de Pagamento de Mensalidade da Academia (Perfil / Alerta)
 */
@Composable
fun GymPaymentReminderDialog(
    profile: UserProfile,
    onConfirmPayment: () -> Unit,
    onDismiss: () -> Unit
) {
    val cal = remember { java.util.Calendar.getInstance() }
    val currentDay = remember { cal.get(java.util.Calendar.DAY_OF_MONTH) }
    val diffDays = profile.gymMembershipDueDay - currentDay

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .border(1.5.dp, Brush.horizontalGradient(listOf(NeonRed, NeonOrange)), RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .testTag("gym_payment_dialog"),
            colors = CardDefaults.cardColors(containerColor = BlackSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Icon with Neon Red Glow
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(NeonRedGlow, CircleShape)
                        .border(1.5.dp, NeonRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Payments,
                        contentDescription = "Pagamento",
                        tint = NeonRed,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Aviso de Mensalidade",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Black,
                        color = TextWhitePrimary,
                        letterSpacing = 0.5.sp
                    ),
                    textAlign = TextAlign.Center
                )

                val dueStatusText = when {
                    diffDays > 1 -> "Faltam $diffDays dias para o vencimento da sua matrícula (Dia ${profile.gymMembershipDueDay})"
                    diffDays == 1 -> "Sua matrícula vence amanhã (Dia ${profile.gymMembershipDueDay})!"
                    diffDays == 0 -> "Sua matrícula vence HOJE (Dia ${profile.gymMembershipDueDay})!"
                    else -> "Sua matrícula está pendente desde o dia ${profile.gymMembershipDueDay}!"
                }

                Text(
                    text = dueStatusText,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = if (diffDays <= 0) NeonRed else NeonOrange,
                        fontWeight = FontWeight.SemiBold
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
                )

                // CARD MOTIVACIONAL DE TREINO: NÃO DEIXE DE IR MALHAR NA ACADEMIA
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.2.dp, NeonOrange.copy(alpha = 0.6f), RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = NeonOrange.copy(alpha = 0.12f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(NeonOrange),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.FitnessCenter,
                                contentDescription = "Treino",
                                tint = PureBlack,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Não Deixe de Ir Malhar!",
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                color = TextWhitePrimary
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "Mantenha o foco e a constância. Seu treino na ${profile.gymName} é prioridade para sua saúde!",
                                fontSize = 12.sp,
                                color = TextWhiteSecondary,
                                lineHeight = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Info Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BlackBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = NeonOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Academia:",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                                )
                            }
                            Text(
                                text = profile.gymName.ifBlank { "Minha Academia" },
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Valor da Matrícula:",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                            )
                            Text(
                                text = "R$ ${profile.gymMembershipFee}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NeonOrange
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Dia de Vencimento:",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                            )
                            Text(
                                text = "Todo dia ${profile.gymMembershipDueDay}",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        val dateFormatter = remember { SimpleDateFormat("dd/MM/yyyy", Locale("pt", "BR")) }
                        val lastDateStr = remember(profile.lastPaymentDateMillis) {
                            if (profile.lastPaymentDateMillis > 0) dateFormatter.format(Date(profile.lastPaymentDateMillis))
                            else "Nenhum anterior"
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Último Pagamento:",
                                style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                            )
                            Text(
                                text = lastDateStr,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonOrange
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Actions: Confirmação solicitada pelo usuário
                Button(
                    onClick = onConfirmPayment,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("confirm_payment_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonGreen)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = PureBlack,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Já Efetuei a Mensalidade (Confirmar)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = PureBlack
                        )
                    )
                }

                Text(
                    text = "Toque acima para confirmar o pagamento e pausar os avisos diários deste mês.",
                    fontSize = 11.sp,
                    color = TextWhiteMuted,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp, bottom = 8.dp)
                )

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("dismiss_payment_button"),
                    shape = RoundedCornerShape(14.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(BlackBorder, BlackBorder)))
                ) {
                    Text(
                        text = "Lembrar Mais Tarde (Amanhã)",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextWhiteSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}

/**
 * Popup de Lembrete de Horário de Academia & Alarme
 */
@Composable
fun GymAlarmReminderDialog(
    profile: UserProfile,
    onStartWorkout: () -> Unit,
    onSnooze: () -> Unit,
    onDismiss: () -> Unit
) {
    val timeFormatted = String.format("%02d:%02d", profile.gymAlarmHour, profile.gymAlarmMinute)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .border(1.5.dp, Brush.horizontalGradient(listOf(NeonOrange, NeonRed)), RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .testTag("gym_alarm_dialog"),
            colors = CardDefaults.cardColors(containerColor = BlackSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Animated Alarm Icon Container
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(NeonOrangeGlow, CircleShape)
                        .border(1.5.dp, NeonOrange, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsActive,
                        contentDescription = "Alarme Academia",
                        tint = NeonOrange,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hora da Academia!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = TextWhitePrimary
                    ),
                    textAlign = TextAlign.Center
                )

                // Time Display Badge
                Box(
                    modifier = Modifier
                        .padding(top = 10.dp, bottom = 8.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(PureBlack)
                        .border(1.dp, NeonOrange.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 20.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Alarm,
                            contentDescription = null,
                            tint = NeonOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = timeFormatted,
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = NeonOrange,
                                letterSpacing = 1.sp
                            )
                        )
                    }
                }

                Text(
                    text = "Dias ativos: ${profile.gymAlarmDays}",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "O seu corpo aguenta quase tudo. É a sua mente que você precisa convencer. Vamos focar no treino agora!",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = TextWhiteSecondary,
                        lineHeight = 20.sp
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Actions
                Button(
                    onClick = onStartWorkout,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("start_gym_session_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonOrange)
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = TextWhitePrimary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Bora Treinar Agora!",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onSnooze,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("snooze_alarm_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(BlackBorder, BlackBorder)))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Snooze,
                            contentDescription = null,
                            tint = TextWhiteSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "+10 min",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextWhiteSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(46.dp)
                            .testTag("dismiss_alarm_button"),
                        shape = RoundedCornerShape(14.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(BlackBorder, BlackBorder)))
                    ) {
                        Text(
                            text = "Fechar",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextWhiteSecondary,
                                fontWeight = FontWeight.SemiBold
                            )
                        )
                    }
                }
            }
        }
    }
}

/**
 * Popup de Lembrete para Beber Água durante o Treino
 */
@Composable
fun WaterIntakeReminderDialog(
    consumedMl: Int,
    onDrinkWater: (Int) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .border(1.5.dp, Brush.horizontalGradient(listOf(NeonBlue, NeonOrange)), RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp))
                .testTag("water_intake_dialog"),
            colors = CardDefaults.cardColors(containerColor = BlackSurface)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Water Droplet Glow Icon
                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .background(NeonBlue.copy(alpha = 0.2f), CircleShape)
                        .border(1.5.dp, NeonBlue, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.LocalDrink,
                        contentDescription = "Beber Água",
                        tint = NeonBlue,
                        modifier = Modifier.size(34.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Hora de se Hidratar!",
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Black,
                        color = TextWhitePrimary
                    ),
                    textAlign = TextAlign.Center
                )

                Text(
                    text = "Beber água durante o treino previne quedas de rendimento e cãibras musculares.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextWhiteSecondary),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 6.dp, bottom = 16.dp)
                )

                // Current Workout Hydration Counter
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, BlackBorder, RoundedCornerShape(16.dp)),
                    colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Consumido neste treino",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$consumedMl",
                                style = MaterialTheme.typography.headlineMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = NeonBlue
                                )
                            )
                            Text(
                                text = " ml",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhiteSecondary
                                ),
                                modifier = Modifier.padding(bottom = 4.dp, start = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Quick Hydration Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = { onDrinkWater(250) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("drink_250ml_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                    ) {
                        Text(
                            text = "+ 250 ml",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PureBlack
                            )
                        )
                    }

                    Button(
                        onClick = { onDrinkWater(500) },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("drink_500ml_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonOrange)
                    ) {
                        Text(
                            text = "+ 500 ml",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("dismiss_water_button"),
                    shape = RoundedCornerShape(12.dp),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = Brush.horizontalGradient(listOf(BlackBorder, BlackBorder)))
                ) {
                    Text(
                        text = "Já bebi / Depois",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextWhiteSecondary,
                            fontWeight = FontWeight.Medium
                        )
                    )
                }
            }
        }
    }
}
