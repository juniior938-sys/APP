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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.data.model.UserProfile
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutExercise
import com.example.domain.ExerciseCatalogData
import com.example.domain.ExerciseCategoryGroup
import com.example.ui.components.SingleBodyPartExerciseDialog
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
    var selectedFilter by remember {
        mutableStateOf(if (userProfile.gender.contains("Feminino", true)) "Mulher 🏋️‍♀️" else "Homem 🏋️‍♂️")
    }
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Fichas por Músculo, 1 = Divisão Semanal
    var selectedCategoryForDetail by remember { mutableStateOf<ExerciseCategoryGroup?>(null) }
    val expandedStates = remember { mutableStateMapOf<Int, Boolean>() }

    // Obter todos os grupos masculinos e femininos
    val maleGroups = ExerciseCatalogData.allMaleGroups
    val femaleGroups = ExerciseCatalogData.allFemaleGroups
    val allCatalogGroups = maleGroups + femaleGroups

    // Filtragem de catálogo
    val filteredCatalog = remember(searchQuery, selectedFilter) {
        allCatalogGroups.filter { group ->
            // Filtro de gênero/categoria
            val matchesFilter = when (selectedFilter) {
                "Homem 🏋️‍♂️" -> group.gender == "Masculino"
                "Mulher 🏋️‍♀️" -> group.gender == "Feminino"
                "Peito" -> group.muscleGroup.contains("Peito", true)
                "Costas" -> group.muscleGroup.contains("Costas", true)
                "Pernas & Glúteos" -> group.muscleGroup.contains("Pernas", true) || group.muscleGroup.contains("Glúteos", true)
                "Ombros" -> group.muscleGroup.contains("Ombros", true)
                "Braços" -> group.muscleGroup.contains("Bíceps", true) || group.muscleGroup.contains("Tríceps", true)
                "Abdômen" -> group.muscleGroup.contains("Abdômen", true)
                "Cardio" -> group.muscleGroup.contains("Cardio", true)
                else -> true
            }

            // Filtro de busca textual
            val matchesQuery = if (searchQuery.isBlank()) true else {
                group.title.contains(searchQuery, true) ||
                        group.subtitle.contains(searchQuery, true) ||
                        group.exercises.any { it.name.contains(searchQuery, true) || it.targetMuscle.contains(searchQuery, true) }
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
            // Header Bar: "Workouts & Exercícios"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Treinos & Exercícios",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Fichas masculinas e femininas completas",
                        fontSize = 12.sp,
                        color = DarkTextSecondary
                    )
                }

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

            // Search Bar matching Screen 3 from Image 1
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text(
                        text = "Buscar exercícios (ex: Supino, Stiff, Rosca)...",
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

            // Filter Pills matching Screen 3 from Image 1
            val filterOptions = listOf(
                "Homem 🏋️‍♂️",
                "Mulher 🏋️‍♀️",
                "Todos",
                "Peito",
                "Costas",
                "Pernas & Glúteos",
                "Ombros",
                "Braços",
                "Abdômen",
                "Cardio"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                filterOptions.forEach { filter ->
                    val isSelected = selectedFilter == filter
                    val isFemaleFilter = filter.contains("Mulher")

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
                            selectedContainerColor = if (isFemaleFilter) CoralPeach else MintGreen,
                            selectedLabelColor = Color.White
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            borderColor = CardBorder,
                            selectedBorderColor = if (isFemaleFilter) CoralPeach else MintGreen,
                            enabled = true,
                            selected = isSelected
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tabs: Fichas por Músculo vs Divisão Semanal
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.Transparent,
                contentColor = MintGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = MintGreen,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Fichas por Músculo (${filteredCatalog.size})",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 0) MintGreenDark else DarkTextSecondary
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Divisão Semanal (${weeklyPlan.size} Dias)",
                            fontSize = 13.sp,
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedTab == 1) MintGreenDark else DarkTextSecondary
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Conteúdo da Aba Selecionada
            if (selectedTab == 0) {
                // ==========================================
                // FICHAS POR MÚSCULO (SINGLE BODY PART WORKOUT)
                // Fiel à Imagem 2 (Homem) & Foco Glúteos/Pernas (Mulher)
                // ==========================================
                if (filteredCatalog.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Nenhum exercício encontrado para '$searchQuery'",
                            color = DarkTextSecondary,
                            fontSize = 14.sp
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        itemsIndexed(filteredCatalog, key = { _, group -> group.id }) { _, group ->
                            ExerciseCategoryCard(
                                group = group,
                                onClick = { selectedCategoryForDetail = group },
                                onStartWorkout = {
                                    val workoutDay = WorkoutDay(
                                        dayNumber = 1,
                                        dayTitle = group.gender,
                                        name = group.title,
                                        focus = group.subtitle,
                                        durationMinutes = group.durationMinutes,
                                        isRestDay = false,
                                        exercises = group.exercises,
                                        isCompletedThisWeek = false
                                    )
                                    onStartWorkout(workoutDay)
                                }
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(24.dp))
                        }
                    }
                }
            } else {
                // ==========================================
                // DIVISÃO SEMANAL (SEGUNDA A DOMINGO)
                // ==========================================
                if (weeklyPlan.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MintGreen)
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        itemsIndexed(weeklyPlan, key = { index, day -> "plan_day_${index}_${day.dayNumber}_${day.name.hashCode()}" }) { _, day ->
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

        // Modal com detalhes da ficha de exercícios
        selectedCategoryForDetail?.let { group ->
            SingleBodyPartExerciseDialog(
                group = group,
                onDismiss = { selectedCategoryForDetail = null },
                onStartWorkout = { day ->
                    onStartWorkout(day)
                }
            )
        }
    }
}

@Composable
private fun ExerciseCategoryCard(
    group: ExerciseCategoryGroup,
    onClick: () -> Unit,
    onStartWorkout: () -> Unit
) {
    val isFemale = group.gender.contains("Feminino", true)
    val accentColor = if (isFemale) CoralPeach else MintGreen
    val accentLight = if (isFemale) CoralPeachLight else MintGreenLight
    val accentDark = if (isFemale) CoralPeachDark else MintGreenDark
    val imageRes = if (isFemale) R.drawable.img_workout_woman else R.drawable.img_workout_man

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(accentLight, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Filled.FitnessCenter,
                            contentDescription = null,
                            tint = accentDark,
                            modifier = Modifier.size(16.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = group.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${group.gender} • ${group.exercises.size} Exercícios",
                    fontSize = 12.sp,
                    color = DarkTextSecondary
                )

                Text(
                    text = group.subtitle,
                    fontSize = 11.sp,
                    color = DarkTextMuted
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MintGreenLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Schedule,
                                contentDescription = null,
                                tint = MintGreenDark,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${group.durationMinutes} min",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MintGreenDark
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = CoralPeachLight
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.LocalFireDepartment,
                                contentDescription = null,
                                tint = CoralPeach,
                                modifier = Modifier.size(11.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${group.caloriesEstimate} kcal",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = CoralPeach
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            // Right side thumbnail image matching Screen 3 from reference image
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(accentLight)
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = group.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = "Abrir",
                tint = DarkTextMuted,
                modifier = Modifier.size(20.dp)
            )
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
                .padding(16.dp)
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

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = day.dayTitle,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintGreenDark
                        )
                        Text(
                            text = day.name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                        Text(
                            text = day.focus,
                            fontSize = 11.sp,
                            color = DarkTextSecondary
                        )
                    }
                }

                IconButton(onClick = onToggleExpand) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = "Expandir",
                        tint = DarkTextSecondary
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 14.dp)) {
                    HorizontalDivider(color = CardBorder)
                    Spacer(modifier = Modifier.height(12.dp))

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
                                    color = DarkTextPrimary
                                )
                                Text(
                                    text = "${ex.sets} séries × ${ex.reps} • ${ex.equipment}",
                                    fontSize = 11.sp,
                                    color = DarkTextSecondary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onStartWorkout,
                        modifier = Modifier.fillMaxWidth(),
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
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

