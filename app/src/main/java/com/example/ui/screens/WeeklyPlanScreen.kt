package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutExercise
import com.example.ui.components.ExerciseThumbnailBadge
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
fun WeeklyPlanScreen(
    userProfile: UserProfile,
    weeklyPlan: List<WorkoutDay>,
    isGenerating: Boolean,
    onStartWorkout: (WorkoutDay) -> Unit,
    onRegeneratePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

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
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Plano Semanal de Treinos",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TextWhitePrimary
                        )
                    )
                    Text(
                        text = "${userProfile.fitnessGoal} • ${userProfile.fitnessLevel} • ${userProfile.workoutLocation}",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextWhiteSecondary)
                    )
                }

                FilledTonalButton(
                    onClick = onRegeneratePlan,
                    enabled = !isGenerating,
                    modifier = Modifier.testTag("regenerate_plan_button"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = BlackSurfaceElevated,
                        contentColor = NeonOrange
                    )
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = NeonOrange
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Recalibrar",
                            modifier = Modifier.size(16.dp),
                            tint = NeonOrange
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Recalibrar", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (weeklyPlan.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NeonRed)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    itemsIndexed(weeklyPlan, key = { index, day -> "plan_day_${index}_${day.dayNumber}_${day.name.hashCode()}" }) { index, day ->
                        val isExpanded = expandedStates[day.dayNumber] ?: (day.dayNumber == 1)

                        WorkoutDayCard(
                            day = day,
                            isExpanded = isExpanded,
                            onToggleExpand = {
                                expandedStates[day.dayNumber] = !isExpanded
                            },
                            onStartWorkout = { onStartWorkout(day) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun WorkoutDayCard(
    day: WorkoutDay,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onStartWorkout: () -> Unit
) {
    val borderColor = if (day.isCompletedThisWeek) NeonGreen.copy(alpha = 0.6f)
    else if (day.isRestDay) BlackBorder
    else NeonRed.copy(alpha = 0.5f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(18.dp))
            .testTag("workout_day_card_${day.dayNumber}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Day Top Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggleExpand() },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                if (day.isRestDay) BlackSurfaceElevated
                                else if (day.isCompletedThisWeek) NeonGreen.copy(alpha = 0.2f)
                                else NeonRedGlow
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (day.isRestDay) Icons.Default.SelfImprovement
                            else if (day.isCompletedThisWeek) Icons.Default.CheckCircle
                            else Icons.Default.FitnessCenter,
                            contentDescription = day.dayTitle,
                            tint = if (day.isRestDay) TextWhiteSecondary
                            else if (day.isCompletedThisWeek) NeonGreen
                            else NeonRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = day.dayTitle,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (day.isRestDay) TextWhiteSecondary else NeonRed
                                )
                            )
                            if (day.isCompletedThisWeek) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = NeonGreen.copy(alpha = 0.2f)
                                ) {
                                    Text(
                                        text = "FEITO",
                                        color = NeonGreen,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = day.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
                    ) {
                        Text(
                            text = "${day.durationMinutes} min",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Medium,
                                color = NeonOrange
                            ),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expandir",
                        tint = TextWhiteSecondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Foco: ${day.focus}",
                style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
            )

            // Lista de Exercícios expandida
            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    HorizontalDivider(color = BlackBorder)

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = if (day.isRestDay) "Rotina de Recuperação:" else "Exercícios Programados (${day.exercises.size}):",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    day.exercises.forEachIndexed { index, exercise ->
                        ExerciseItemView(index = index + 1, exercise = exercise)
                        if (index < day.exercises.size - 1) {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_day_workout_button_${day.dayNumber}"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (day.isRestDay) NeonOrange else NeonRed,
                            contentColor = TextWhitePrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Iniciar",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (day.isRestDay) "Ver Atividades de Descanso" else "Iniciar Treino Deste Dia",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ExerciseItemView(index: Int, exercise: WorkoutExercise) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BlackSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Miniatura Desenho / Ilustração gráfica do Exercício para melhor identificação
            ExerciseThumbnailBadge(
                exerciseName = exercise.name,
                equipment = exercise.equipment,
                size = 48.dp
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = "$index.",
                            fontWeight = FontWeight.Bold,
                            color = NeonRed,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = NeonOrange.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = exercise.equipment,
                            color = NeonOrange,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "${exercise.sets} Séries",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhitePrimary
                        )
                    )
                    Text(
                        text = "${exercise.reps}",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = TextWhitePrimary
                        )
                    )
                    if (exercise.restSeconds > 0) {
                        Text(
                            text = "${exercise.restSeconds}s descanso",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteSecondary)
                        )
                    }
                }

                if (exercise.tips.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Dica: ${exercise.tips}",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                    )
                }
            }
        }
    }
}
