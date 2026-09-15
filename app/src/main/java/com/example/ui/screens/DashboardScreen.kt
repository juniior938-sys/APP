package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.repository.StreakStats
import com.example.domain.CoachEngine
import com.example.ui.components.AmplaPersonalChatCard
import com.example.ui.components.NutritionFoodDialog
import com.example.ui.components.QuickActionsGrid
import com.example.ui.components.TodayProgressCard
import com.example.ui.components.WeeklyActivityCapsuleCard
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.DiscreetAppGradient
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.CoralPeach
import com.example.ui.theme.CoralPeachLight
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MintGreenDark
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.WarmCreamBackground

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
    val context = LocalContext.current
    var showWeightDialog by remember { mutableStateOf(false) }
    var showNutritionDialog by remember { mutableStateOf(false) }
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
            // ==========================================
            // HEADER BAR: "Hi, [Name] 👋 / Ready to crush goals?"
            // Matching Screen 2 from Image 1
            // ==========================================
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    // Profile Avatar with click to navigate
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .border(1.5.dp, MintGreen, CircleShape)
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
                                    .background(MintGreenLight),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = userProfile.name.take(1).uppercase().ifBlank { "A" },
                                    color = MintGreenDark,
                                    fontWeight = FontWeight.Black,
                                    fontSize = 18.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Olá, ${userProfile.name} 👋",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                        }
                        Text(
                            text = "Pronto para superar suas metas?",
                            fontSize = 12.sp,
                            color = DarkTextSecondary
                        )
                    }
                }

                // Notification Bell icon with badge matching screenshot
                IconButton(
                    onClick = { onTriggerMembershipPopup() },
                    modifier = Modifier
                        .size(44.dp)
                        .background(CardWhite, CircleShape)
                        .border(1.dp, CardBorder, CircleShape)
                ) {
                    Box(contentAlignment = Alignment.TopEnd) {
                        Icon(
                            imageVector = Icons.Filled.Notifications,
                            contentDescription = "Notificações & Lembretes",
                            tint = DarkTextPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        if (userProfile.gymMembershipReminderEnabled) {
                            Box(
                                modifier = Modifier
                                    .size(9.dp)
                                    .background(CoralPeach, CircleShape)
                                    .border(1.5.dp, CardWhite, CircleShape)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // TODAY'S PROGRESS CARD (Screen 2 from Image 1)
            // ==========================================
            val completedCount = weeklyPlan.count { it.isCompletedThisWeek }
            val progressPercent = if (weeklyPlan.isNotEmpty()) {
                ((completedCount.toFloat() / weeklyPlan.size.toFloat()) * 100).toInt().coerceIn(25, 100)
            } else 75

            val todayCalories = if (workoutHistory.isNotEmpty()) {
                workoutHistory.sumOf { it.caloriesBurnedEstimated }.coerceAtLeast(320)
            } else 520

            TodayProgressCard(
                progressPercent = progressPercent,
                workoutMinutes = todayWorkout?.durationMinutes ?: 45,
                caloriesBurned = todayCalories,
                stepsCount = 8752,
                activeTimeText = "1h 15m"
            )

            // ==========================================
            // WEEKLY ACTIVITY CARD (Screen 2 from Image 1)
            // ==========================================
            WeeklyActivityCapsuleCard(
                onDayClick = { onNavigateToPlan() }
            )

            // ==========================================
            // QUICK ACTIONS 2x2 GRID (Screen 2 from Image 1)
            // ==========================================
            QuickActionsGrid(
                onStartWorkout = {
                    if (todayWorkout != null) {
                        onStartWorkout(todayWorkout)
                    } else {
                        onNavigateToPlan()
                    }
                },
                onLogFood = { showNutritionDialog = true },
                onBodyStats = { showWeightDialog = true },
                onChallengesOrAi = {
                    onSendMessage("Olá treinador! Pode analisar meu progresso e me dar uma dica para o treino de hoje?")
                }
            )

            // ==========================================
            // TREINO EM DESTAQUE (Screen 3 & 4 from Image 1)
            // ==========================================
            if (todayWorkout != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, CardBorder, RoundedCornerShape(22.dp))
                        .testTag("today_workout_card"),
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = CardWhite)
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
                                        .background(MintGreenLight),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.DirectionsRun,
                                        contentDescription = "Treino",
                                        tint = MintGreenDark,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = todayWorkout.dayTitle,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MintGreenDark
                                    )
                                    Text(
                                        text = if (todayWorkout.isRestDay) "Recuperação Ativa" else "Treino Recomendado",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = DarkTextPrimary
                                    )
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = MintGreenLight
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Schedule,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp),
                                            tint = MintGreenDark
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "${todayWorkout.durationMinutes} min",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MintGreenDark
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = CoralPeachLight
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.LocalFireDepartment,
                                            contentDescription = null,
                                            modifier = Modifier.size(12.dp),
                                            tint = CoralPeach
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "320 kcal",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = CoralPeach
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = todayWorkout.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )

                        Text(
                            text = "Foco: ${todayWorkout.focus} • ${todayWorkout.exercises.size} exercícios",
                            fontSize = 12.sp,
                            color = DarkTextSecondary
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
                                    containerColor = MintGreen,
                                    contentColor = Color.White
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Iniciar",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (todayWorkout.isRestDay) "Ver Descanso" else "Iniciar Treino",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Button(
                                onClick = onNavigateToPlan,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MintGreenLight,
                                    contentColor = MintGreenDark
                                )
                            ) {
                                Text("Ver Ficha", fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }

            // ==========================================
            // STATUS DA MATRÍCULA ACADEMIA AMPLA FITNESS & WHATSAPP
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, CardBorder, RoundedCornerShape(20.dp)),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = CardWhite)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(MintGreenLight, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Payments,
                                    contentDescription = null,
                                    tint = MintGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = userProfile.gymName,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                                Text(
                                    text = "Vencimento: Dia ${userProfile.gymMembershipDueDay} • Status: ${userProfile.gymMembershipStatus}",
                                    fontSize = 12.sp,
                                    color = if (userProfile.gymMembershipStatus == "Em dia") MintGreenDark else CoralPeach
                                )
                            }
                        }

                        // Botão WhatsApp Direto com a Recepção/Gerência
                        Button(
                            onClick = {
                                val cleanPhone = userProfile.adminContactPhone.replace("[^0-9]".toRegex(), "")
                                val phoneWithCountry = if (cleanPhone.startsWith("55")) cleanPhone else "55$cleanPhone"
                                val text = "Olá! Gostaria de falar sobre minha matrícula na ${userProfile.gymName}."
                                val uri = Uri.parse("https://api.whatsapp.com/send?phone=$phoneWithCountry&text=${Uri.encode(text)}")
                                val intent = Intent(Intent.ACTION_VIEW, uri)
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    // fallback
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366))
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_whatsapp),
                                contentDescription = "WhatsApp",
                                tint = Color.White,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "WhatsApp",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }

            // ==========================================
            // AMPLA PERSONAL IA COACH CARD
            // ==========================================
            AmplaPersonalChatCard(
                messages = chatMessages,
                isLoading = isChatLoading,
                onSendMessage = onSendMessage,
                onClearChat = onClearChat
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // ==========================================
        // DIALOGS
        // ==========================================
        if (showNutritionDialog) {
            NutritionFoodDialog(onDismiss = { showNutritionDialog = false })
        }

        if (showWeightDialog) {
            AlertDialog(
                onDismissRequest = { showWeightDialog = false },
                title = {
                    Text(
                        text = "Registrar Peso Atual",
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                },
                text = {
                    Column {
                        Text(
                            text = "Mantenha seu IMC e evolução corporal atualizados.",
                            fontSize = 13.sp,
                            color = DarkTextSecondary
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        OutlinedTextField(
                            value = weightInput,
                            onValueChange = { weightInput = it },
                            label = { Text("Peso (kg)") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MintGreen,
                                unfocusedBorderColor = CardBorder,
                                focusedContainerColor = CardWhite,
                                unfocusedContainerColor = CardWhite,
                                focusedTextColor = DarkTextPrimary,
                                unfocusedTextColor = DarkTextPrimary
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            val newWeight = weightInput.replace(",", ".").toFloatOrNull()
                            if (newWeight != null && newWeight > 20f && newWeight < 300f) {
                                onLogWeight(newWeight)
                                showWeightDialog = false
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MintGreen)
                    ) {
                        Text("Salvar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showWeightDialog = false }) {
                        Text("Cancelar", color = DarkTextSecondary)
                    }
                },
                containerColor = CardWhite
            )
        }
    }
}
