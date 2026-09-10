package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutExercise
import com.example.ui.components.RestTimerCard
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveWorkoutScreen(
    workoutDay: WorkoutDay,
    exercises: List<WorkoutExercise>,
    restTimerSeconds: Int,
    totalRestDuration: Int,
    isTimerRunning: Boolean,
    waterConsumedMl: Int = 0,
    workoutElapsedSeconds: Int = 0,
    onToggleExercise: (String) -> Unit,
    onPlayPauseTimer: () -> Unit,
    onAdd30sTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onDrinkWater: (Int) -> Unit = {},
    onTriggerWaterReminder: () -> Unit = {},
    onFinishWorkout: (notes: String) -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showConfirmExitDialog by remember { mutableStateOf(false) }
    var showFinishCelebrationDialog by remember { mutableStateOf(false) }

    val completedCount = exercises.count { it.isCompleted }
    val totalCount = exercises.size
    val progressFraction = if (totalCount > 0) completedCount.toFloat() / totalCount else 0f
    val animatedProgress by animateFloatAsState(targetValue = progressFraction, label = "active_progress")

    val elapsedMinutes = workoutElapsedSeconds / 60
    val elapsedRemainderSeconds = workoutElapsedSeconds % 60
    val elapsedFormatted = String.format("%02d:%02d", elapsedMinutes, elapsedRemainderSeconds)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = workoutDay.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            ),
                            maxLines = 1
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${workoutDay.dayTitle} • ${workoutDay.durationMinutes} min",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteSecondary)
                            )
                            if (workoutElapsedSeconds > 0) {
                                Text(
                                    text = " • Em andamento: $elapsedFormatted",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonOrange,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = { showConfirmExitDialog = true },
                        modifier = Modifier.testTag("exit_workout_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Sair",
                            tint = TextWhitePrimary
                        )
                    }
                },
                actions = {
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = NeonRed.copy(alpha = 0.2f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.5f)),
                        modifier = Modifier.padding(end = 12.dp)
                    ) {
                        Text(
                            text = "$completedCount/$totalCount Feitos",
                            color = NeonRed,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = PureBlack)
            )
        },
        bottomBar = {
            Surface(
                color = BlackSurface,
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BlackBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = { showFinishCelebrationDialog = true },
                        modifier = Modifier
                            .widthIn(max = 480.dp)
                            .fillMaxWidth()
                            .height(54.dp)
                            .testTag("complete_workout_button"),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = NeonRed,
                            contentColor = TextWhitePrimary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Concluir",
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (completedCount == totalCount && totalCount > 0)
                                "Concluir Treino com Sucesso!"
                            else "Finalizar Sessão ($completedCount/$totalCount)",
                            fontWeight = FontWeight.Black,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        },
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(PureBlack),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 680.dp)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                // Progresso do Treino
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Progresso do Treino",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteSecondary)
                        )
                        Text(
                            text = "${(progressFraction * 100).toInt()}%",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonRed
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    LinearProgressIndicator(
                        progress = { animatedProgress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = NeonRed,
                        trackColor = BlackSurfaceElevated,
                        strokeCap = StrokeCap.Round
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // LEMBRETE DE BEBER ÁGUA DURANTE O TREINO (Card de Hidratação Dinâmica)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, NeonBlue.copy(alpha = 0.6f), RoundedCornerShape(14.dp)),
                    colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onTriggerWaterReminder() }
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .background(NeonBlue.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalDrink,
                                    contentDescription = "Água",
                                    tint = NeonBlue,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Hidratação no Treino",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                )
                                Text(
                                    text = "$waterConsumedMl ml consumidos hoje",
                                    style = MaterialTheme.typography.labelSmall.copy(color = NeonBlue)
                                )
                            }
                        }

                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            OutlinedButton(
                                onClick = { onDrinkWater(250) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(34.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, NeonBlue)
                            ) {
                                Text(
                                    text = "+250 ml",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = NeonBlue,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Button(
                                onClick = onTriggerWaterReminder,
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.height(34.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonOrange)
                            ) {
                                Text(
                                    text = "Alerta Água",
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = TextWhitePrimary,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Temporizador de Descanso
                AnimatedVisibility(visible = totalRestDuration > 0 || isTimerRunning) {
                    Column {
                        RestTimerCard(
                            remainingSeconds = restTimerSeconds,
                            totalSeconds = totalRestDuration,
                            isRunning = isTimerRunning,
                            onPlayPauseClick = onPlayPauseTimer,
                            onAdd30sClick = onAdd30sTimer,
                            onResetClick = onResetTimer
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }

                // Lista de Exercícios
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    itemsIndexed(exercises, key = { _, item -> item.id }) { index, item ->
                        ActiveExerciseCard(
                            index = index + 1,
                            exercise = item,
                            onToggle = { onToggleExercise(item.id) }
                        )
                    }

                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }

    // Diálogo Confirmar Saída
    if (showConfirmExitDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmExitDialog = false },
            containerColor = BlackSurface,
            title = {
                Text(
                    "Sair do Treino?",
                    color = TextWhitePrimary,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Text(
                    "Tem certeza que deseja sair agora? Você pode finalizar a sessão para registrar seus dias seguidos no histórico.",
                    color = TextWhiteSecondary
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        showConfirmExitDialog = false
                        onClose()
                    }
                ) {
                    Text("Sair Mesmo Assim", color = NeonRed)
                }
            },
            dismissButton = {
                Button(
                    onClick = { showConfirmExitDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonOrange)
                ) {
                    Text("Continuar Treinando", color = TextWhitePrimary)
                }
            }
        )
    }

    // Diálogo Celebrar Conclusão
    if (showFinishCelebrationDialog) {
        AlertDialog(
            onDismissRequest = { showFinishCelebrationDialog = false },
            containerColor = BlackSurface,
            icon = {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(NeonOrangeGlow),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Troféu",
                        tint = NeonOrange,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
            title = {
                Text(
                    text = "Treino Concluído!",
                    fontWeight = FontWeight.Black,
                    fontSize = 22.sp,
                    color = TextWhitePrimary
                )
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Excelente dedicação! Você finalizou $completedCount de $totalCount exercícios previstos.",
                        style = MaterialTheme.typography.bodyMedium.copy(color = TextWhiteSecondary)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Fogo",
                            tint = NeonOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+1 Dia nos seus Dias Seguidos!",
                            fontWeight = FontWeight.Bold,
                            color = NeonOrange
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        showFinishCelebrationDialog = false
                        onFinishWorkout("Treino finalizado com sucesso no FitAI Coach")
                    },
                    modifier = Modifier.testTag("confirm_finish_workout_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
                ) {
                    Text("Salvar no Histórico", fontWeight = FontWeight.Bold, color = TextWhitePrimary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishCelebrationDialog = false }) {
                    Text("Continuar Treino", color = TextWhiteSecondary)
                }
            }
        )
    }
}

@Composable
private fun ActiveExerciseCard(
    index: Int,
    exercise: WorkoutExercise,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onToggle() }
            .testTag("active_exercise_card_$index"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (exercise.isCompleted) BlackSurfaceElevated else BlackSurfaceCard
        ),
        border = if (exercise.isCompleted)
            androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.5f))
        else
            androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = exercise.isCompleted,
                onCheckedChange = { onToggle() },
                colors = CheckboxDefaults.colors(
                    checkedColor = NeonGreen,
                    uncheckedColor = TextWhiteMuted
                ),
                modifier = Modifier.testTag("exercise_checkbox_$index")
            )

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "$index. ${exercise.name}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (exercise.isCompleted) TextWhiteSecondary else TextWhitePrimary,
                            textDecoration = if (exercise.isCompleted) TextDecoration.LineThrough else TextDecoration.None
                        ),
                        modifier = Modifier.weight(1f)
                    )

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
                        text = "${exercise.sets} Séries × ${exercise.reps}",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = NeonRed
                        )
                    )

                    if (exercise.restSeconds > 0) {
                        Text(
                            text = "${exercise.restSeconds}s descanso",
                            style = MaterialTheme.typography.labelMedium.copy(color = TextWhiteSecondary)
                        )
                    }
                }

                if (exercise.tips.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Execução: ${exercise.tips}",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteMuted)
                    )
                }
            }
        }
    }
}
