package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.repository.StreakStats
import com.example.domain.CoachEngine
import com.example.ui.components.AmplaPersonalChatCard
import com.example.ui.components.CalorieAndActivitiesRechartsCard
import com.example.ui.components.StreakCard
import com.example.ui.components.WeightBmiEvolutionRechartsCard
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.DiscreetAppGradient
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

@Composable
fun DashboardScreen(
    userProfile: UserProfile,
    streakStats: StreakStats,
    weeklyPlan: List<WorkoutDay>,
    chatMessages: List<ChatMessage> = emptyList(),
    isChatLoading: Boolean = false,
    workoutHistory: List<WorkoutHistoryEntity> = emptyList(),
    weightLogs: List<WeightLogEntity> = emptyList(),
    onSendMessage: (String) -> Unit = {},
    onClearChat: () -> Unit = {},
    onStartWorkout: (WorkoutDay) -> Unit,
    onLogWeight: (Float) -> Unit,
    onLogQuickActivity: (title: String, durationMinutes: Int, caloriesBurned: Int, category: String) -> Unit = { _, _, _, _ -> },
    onNavigateToPlan: () -> Unit,
    onNavigateToProfile: () -> Unit = {},
    onTriggerAlarmPopup: () -> Unit = {},
    onTriggerMembershipPopup: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var showWeightDialog by remember { mutableStateOf(false) }
    var weightInput by remember { mutableStateOf(userProfile.weightKg.toString()) }

    // Identificar treino de hoje ou próximo ativo
    val todayWorkout = weeklyPlan.firstOrNull { !it.isCompletedThisWeek && !it.isRestDay }
        ?: weeklyPlan.firstOrNull()

    val coachTip = remember(userProfile.fitnessGoal) {
        CoachEngine.getDailyCoachTip(userProfile.fitnessGoal)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DiscreetAppGradient),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 680.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabeçalho Principal com Foto de Perfil do Atleta
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "AMPLA PERSONAL",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = TextWhitePrimary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NeonRedGlow,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "IA Ativa",
                                    tint = NeonRed,
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "IA",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonRed
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Olá, ${userProfile.name}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhiteSecondary
                        )
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Tag de Objetivo & Local
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (userProfile.workoutLocation.contains("Academia", true))
                                    Icons.Default.FitnessCenter
                                else
                                    Icons.Default.Home,
                                contentDescription = "Local",
                                tint = NeonOrange,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${userProfile.fitnessGoal} • ${userProfile.workoutLocation}",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = TextWhitePrimary,
                                    fontSize = 11.sp
                                )
                            )
                        }
                    }
                }

                // Avatar Circular do Usuário no Cabeçalho (com navegação direta para Perfil)
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, NeonRed, CircleShape)
                        .clickable { onNavigateToProfile() }
                        .testTag("dashboard_profile_avatar"),
                    contentAlignment = Alignment.Center
                ) {
                    if (!userProfile.profilePictureUri.isNullOrBlank()) {
                        AsyncImage(
                            model = userProfile.profilePictureUri,
                            contentDescription = "Foto do perfil",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(NeonRedGlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = userProfile.name.take(1).uppercase().ifBlank { "A" },
                                color = NeonRed,
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }

            // BARRA RÁPIDA: ALARME DA ACADEMIA & MENSALIDADE (Respeitando botões de ativação)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Alarme da Academia
                val isAlarmOn = userProfile.gymAlarmEnabled
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BlackSurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isAlarmOn) NeonOrange.copy(alpha = 0.6f) else BlackBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onTriggerAlarmPopup() }
                        .testTag("dashboard_alarm_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (isAlarmOn) NeonOrangeGlow else BlackSurfaceElevated,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Alarm,
                                contentDescription = null,
                                tint = if (isAlarmOn) NeonOrange else TextWhiteMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Alarme Academia",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextWhiteMuted,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = if (isAlarmOn)
                                    String.format("%02d:%02d • Ativo", userProfile.gymAlarmHour, userProfile.gymAlarmMinute)
                                else
                                    "Desativado",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isAlarmOn) NeonOrange else TextWhiteMuted
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }

                // Mensalidade da Academia
                val isMembershipReminderOn = userProfile.gymMembershipReminderEnabled
                val isPaid = userProfile.gymMembershipStatus == "Em dia"
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = BlackSurfaceCard,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isMembershipReminderOn) (if (isPaid) NeonGreen.copy(alpha = 0.5f) else NeonRed.copy(alpha = 0.5f)) else BlackBorder
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(14.dp))
                        .clickable { onTriggerMembershipPopup() }
                        .testTag("dashboard_membership_pill")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    if (isMembershipReminderOn) (if (isPaid) NeonGreen.copy(alpha = 0.15f) else NeonRedGlow) else BlackSurfaceElevated,
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Payments,
                                contentDescription = null,
                                tint = if (isMembershipReminderOn) (if (isPaid) NeonGreen else NeonRed) else TextWhiteMuted,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Mensalidade",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    color = TextWhiteMuted,
                                    fontSize = 11.sp
                                )
                            )
                            Text(
                                text = if (isMembershipReminderOn)
                                    "Dia ${userProfile.gymMembershipDueDay} • ${userProfile.gymMembershipStatus}"
                                else
                                    "Desativado",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMembershipReminderOn) (if (isPaid) NeonGreen else NeonRed) else TextWhiteMuted
                                ),
                                maxLines = 1
                            )
                        }
                    }
                }
            }

            // 1. Treino Programado para Hoje (Foco Imediato)
            if (todayWorkout != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.5.dp, NeonRed, RoundedCornerShape(20.dp))
                        .testTag("today_workout_card"),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clip(CircleShape)
                                        .background(NeonRedGlow),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                        contentDescription = "Treino",
                                        tint = NeonRed,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = todayWorkout.dayTitle,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = NeonRed,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        text = if (todayWorkout.isRestDay) "Dia de Recuperação" else "Treino de Hoje",
                                        style = MaterialTheme.typography.titleMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhitePrimary
                                        )
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = BlackSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Schedule,
                                        contentDescription = "Duração",
                                        modifier = Modifier.size(12.dp),
                                        tint = NeonOrange
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "${todayWorkout.durationMinutes} min",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = TextWhitePrimary
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = todayWorkout.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )

                        Text(
                            text = "Foco: ${todayWorkout.focus} • ${todayWorkout.exercises.size} exercícios",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Button(
                                onClick = { onStartWorkout(todayWorkout) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("start_workout_button"),
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonRed,
                                    contentColor = TextWhitePrimary
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Iniciar",
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (todayWorkout.isRestDay) "Ver Descanso" else "Iniciar Treino",
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            OutlinedButton(
                                onClick = onNavigateToPlan,
                                modifier = Modifier.testTag("view_plan_button"),
                                shape = RoundedCornerShape(14.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonOrange)
                            ) {
                                Text("Ver Plano", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // 2. Chat Ampla Personal IA
            AmplaPersonalChatCard(
                messages = chatMessages,
                isLoading = isChatLoading,
                onSendMessage = onSendMessage,
                onClearChat = onClearChat
            )

            // 3. Gráfico Recharts Unificado: Evolução Corporal (Peso & IMC 8 semanas)
            WeightBmiEvolutionRechartsCard(
                userProfile = userProfile,
                weightLogs = weightLogs,
                onLogWeightClick = {
                    weightInput = userProfile.weightKg.toString()
                    showWeightDialog = true
                }
            )

            // 4. Mini Gráfico Recharts de Perda de Calorias e Registro de Atividades Diárias
            CalorieAndActivitiesRechartsCard(
                workoutHistory = workoutHistory,
                onLogQuickActivity = onLogQuickActivity
            )

            // 5. Card de Sequência e Foco (Streak)
            StreakCard(streakStats = streakStats)

            // 6. Dica Diária do Coach com IA
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, NeonOrange.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
                    .testTag("coach_tip_card"),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(NeonOrangeGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lightbulb,
                            contentDescription = "Dica IA",
                            tint = NeonOrange,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Orientação do Treinador IA",
                            style = MaterialTheme.typography.titleSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonOrange
                            )
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = coachTip,
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextWhiteSecondary)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
        }
    }

    // Diálogo Atualizar Peso
    if (showWeightDialog) {
        AlertDialog(
            onDismissRequest = { showWeightDialog = false },
            containerColor = BlackSurfaceCard,
            title = {
                Text(
                    text = "Registrar Peso Atual",
                    fontWeight = FontWeight.Bold,
                    color = TextWhitePrimary
                )
            },
            text = {
                Column {
                    Text(
                        text = "Informe seu peso para atualizar seu IMC e histórico corporal.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextWhiteSecondary)
                    )
                    Spacer(modifier = Modifier.height(14.dp))
                    OutlinedTextField(
                        value = weightInput,
                        onValueChange = { weightInput = it },
                        label = { Text("Peso (kg)", color = TextWhiteSecondary) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("weight_input_field"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhitePrimary,
                            unfocusedTextColor = TextWhitePrimary,
                            focusedBorderColor = NeonRed,
                            unfocusedBorderColor = BlackBorder
                        )
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        val parsed = weightInput.toFloatOrNull()
                        if (parsed != null && parsed in 20f..300f) {
                            onLogWeight(parsed)
                            showWeightDialog = false
                        }
                    },
                    modifier = Modifier.testTag("confirm_weight_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("Salvar", color = TextWhitePrimary, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showWeightDialog = false }) {
                    Text("Cancelar", color = TextWhiteSecondary)
                }
            }
        )
    }
}
