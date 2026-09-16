package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutExercise
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.CoralPeach
import com.example.ui.theme.CoralPeachDark
import com.example.ui.theme.CoralPeachLight
import com.example.ui.theme.DarkTextMuted
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.DiscreetAppGradient
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MintGreenDark
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.WarmCreamBackground

@Composable
fun WeeklyPlanScreen(
    userProfile: UserProfile,
    weeklyPlan: List<WorkoutDay>,
    isGenerating: Boolean,
    onStartWorkout: (WorkoutDay) -> Unit,
    onRegeneratePlan: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Todos") }
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    val filterOptions = listOf(
        "Todos",
        "Peito",
        "Costas",
        "Pernas & Glúteos",
        "Ombros",
        "Braços",
        "Abdômen",
        "Cardio"
    )

    // Filtragem da Lista Principal de Treinos
    val filteredWeeklyPlan = remember(weeklyPlan, searchQuery, selectedFilter) {
        weeklyPlan.filter { day ->
            val matchesFilter = when (selectedFilter) {
                "Todos" -> true
                "Peito" -> day.focus.contains("Peito", true) || day.name.contains("Peito", true) || day.exercises.any { it.targetMuscle.contains("Peito", true) }
                "Costas" -> day.focus.contains("Costas", true) || day.name.contains("Costas", true) || day.exercises.any { it.targetMuscle.contains("Costas", true) }
                "Pernas & Glúteos" -> day.focus.contains("Perna", true) || day.focus.contains("Glúteo", true) || day.name.contains("Perna", true) || day.exercises.any { it.targetMuscle.contains("Perna", true) || it.targetMuscle.contains("Glúteo", true) || it.targetMuscle.contains("Quadríceps", true) }
                "Ombros" -> day.focus.contains("Ombro", true) || day.name.contains("Ombro", true) || day.exercises.any { it.targetMuscle.contains("Ombro", true) || it.targetMuscle.contains("Deltoide", true) }
                "Braços" -> day.focus.contains("Braço", true) || day.focus.contains("Bíceps", true) || day.focus.contains("Tríceps", true) || day.name.contains("Braço", true) || day.exercises.any { it.targetMuscle.contains("Bíceps", true) || it.targetMuscle.contains("Tríceps", true) }
                "Abdômen" -> day.focus.contains("Abdômen", true) || day.name.contains("Abdômen", true) || day.exercises.any { it.targetMuscle.contains("Abdômen", true) || it.targetMuscle.contains("Core", true) }
                "Cardio" -> day.focus.contains("Cardio", true) || day.name.contains("Cardio", true) || day.exercises.any { it.targetMuscle.contains("Cardio", true) }
                else -> true
            }

            val matchesQuery = if (searchQuery.isBlank()) true else {
                day.name.contains(searchQuery, true) ||
                        day.focus.contains(searchQuery, true) ||
                        day.dayTitle.contains(searchQuery, true) ||
                        day.exercises.any { it.name.contains(searchQuery, true) || it.targetMuscle.contains(searchQuery, true) }
            }

            matchesFilter && matchesQuery
        }
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
                .padding(horizontal = 16.dp, vertical = 20.dp)
        ) {
            // Header Bar: "Plano de Treinos Principal"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Lista Principal de Treinos",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "Divisão Semanal Personalizada • ${userProfile.name}",
                        fontSize = 12.sp,
                        color = DarkTextSecondary,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalButton(
                    onClick = onRegeneratePlan,
                    enabled = !isGenerating,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = MintGreenLight,
                        contentColor = MintGreenDark
                    )
                ) {
                    if (isGenerating) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = MintGreenDark
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Recalibrar",
                            modifier = Modifier.size(16.dp),
                            tint = MintGreenDark
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Calibrar IA", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar nos treinos (ex: Supino, Stiff, Rosca)...",
                        fontSize = 13.sp,
                        color = DarkTextMuted
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Buscar",
                        tint = MintGreen,
                        modifier = Modifier.size(20.dp)
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Filled.Clear,
                                contentDescription = "Limpar",
                                tint = DarkTextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MintGreen,
                    unfocusedBorderColor = CardBorder,
                    focusedContainerColor = CardWhite,
                    unfocusedContainerColor = CardWhite,
                    focusedTextColor = DarkTextPrimary,
                    unfocusedTextColor = DarkTextPrimary
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Filter Pills
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { filter ->
                    val isSelected = selectedFilter == filter

                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedFilter = filter },
                        label = {
                            Text(
                                text = filter,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        shape = RoundedCornerShape(20.dp),
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = CardWhite,
                            labelColor = DarkTextSecondary,
                            selectedContainerColor = MintGreen,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = CardBorder,
                            selectedBorderColor = MintGreen,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // ==========================================
            // ÚNICA LISTA DE TREINO PRINCIPAL (DIVISÃO SEMANAL COMPLETA)
            // ==========================================
            if (filteredWeeklyPlan.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Nenhum treino encontrado para a busca",
                        color = DarkTextSecondary,
                        fontSize = 14.sp
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    itemsIndexed(filteredWeeklyPlan, key = { index, day -> "plan_day_${index}_${day.dayNumber}_${day.name.hashCode()}" }) { _, day ->
                        val isExpanded = expandedStates[day.dayNumber] ?: (day.dayNumber == 1)

                        WeeklySplitDayCard(
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
private fun WeeklySplitDayCard(
    day: WorkoutDay,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onStartWorkout: () -> Unit
) {
    val borderColor = if (day.isCompletedThisWeek) MintGreen.copy(alpha = 0.6f)
    else CardBorder

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, borderColor, RoundedCornerShape(20.dp))
            .testTag("workout_day_card_${day.dayNumber}"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
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
                                if (day.isRestDay) CardBorder
                                else if (day.isCompletedThisWeek) MintGreenLight
                                else MintGreenLight
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (day.isRestDay) Icons.Default.SelfImprovement
                            else if (day.isCompletedThisWeek) Icons.Default.CheckCircle
                            else Icons.Default.FitnessCenter,
                            contentDescription = day.dayTitle,
                            tint = if (day.isRestDay) DarkTextSecondary
                            else MintGreenDark,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = day.dayTitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintGreenDark,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = day.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = day.focus,
                            fontSize = 11.sp,
                            color = DarkTextSecondary,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }

                IconButton(
                    onClick = onToggleExpand,
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expandir",
                        tint = DarkTextSecondary
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(10.dp))

                    day.exercises.forEachIndexed { idx, ex ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${idx + 1}.",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MintGreenDark
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = ex.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = DarkTextPrimary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "${ex.sets} séries × ${ex.reps} • ${ex.equipment}",
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MintGreen,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (day.isRestDay) "Ver Descanso" else "Iniciar Treino do Dia",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

