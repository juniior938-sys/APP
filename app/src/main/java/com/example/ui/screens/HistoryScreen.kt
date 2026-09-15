package com.example.ui.screens

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.repository.StreakStats
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
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.WarmCreamBackground
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HistoryScreen(
    workoutHistory: List<WorkoutHistoryEntity>,
    weightLogs: List<WeightLogEntity>,
    streakStats: StreakStats,
    onDeleteHistoryItem: (Long) -> Unit,
    onStartWorkoutClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTimeTab by remember { mutableIntStateOf(1) } // 0 = Dia, 1 = Semana, 2 = Mês, 3 = Ano
    val totalCalories = workoutHistory.sumOf { it.caloriesBurnedEstimated }.coerceAtLeast(2450)
    val dateFormatter = SimpleDateFormat("EEE, d 'de' MMM • HH:mm", Locale("pt", "BR"))

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
            // Header Bar: "Activity / Atividade" (Matching Screen 5 from Image 1)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Atividade & Progresso",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Estatísticas em tempo real e evolução",
                        fontSize = 12.sp,
                        color = DarkTextSecondary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MintGreenLight,
                    border = androidx.compose.foundation.BorderStroke(1.dp, MintGreen.copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.TrendingUp,
                            contentDescription = null,
                            tint = MintGreenDark,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "+12% esta semana",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MintGreenDark
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Time tabs: Dia, Semana, Mês, Ano (Matching Screen 5 from Image 1)
            val timeTabs = listOf("Dia", "Semana", "Mês", "Ano")
            TabRow(
                selectedTabIndex = selectedTimeTab,
                containerColor = Color.Transparent,
                contentColor = MintGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTimeTab]),
                        color = MintGreen,
                        height = 3.dp
                    )
                },
                divider = {}
            ) {
                timeTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTimeTab == index,
                        onClick = { selectedTimeTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTimeTab == index) FontWeight.Bold else FontWeight.Medium,
                                color = if (selectedTimeTab == index) MintGreenDark else DarkTextSecondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Date Range Bar: < 12 Mai – 18 Mai >
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardWhite, RoundedCornerShape(14.dp))
                    .border(1.dp, CardBorder, RoundedCornerShape(14.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Filled.ChevronLeft,
                        contentDescription = "Anterior",
                        tint = DarkTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Text(
                    text = "Semana Atual • 12 Mai – 18 Mai",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DarkTextPrimary
                )

                IconButton(onClick = {}, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Próxima",
                        tint = DarkTextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ==========================================
                // CALORIES BURNED CARD WITH MINI BAR CHART (Screen 5)
                // ==========================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Calorias Queimadas",
                                        fontSize = 12.sp,
                                        color = DarkTextSecondary
                                    )
                                    Row(verticalAlignment = Alignment.Bottom) {
                                        Text(
                                            text = "$totalCalories",
                                            fontSize = 26.sp,
                                            fontWeight = FontWeight.Black,
                                            color = DarkTextPrimary
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "kcal",
                                            fontSize = 14.sp,
                                            color = DarkTextMuted,
                                            modifier = Modifier.padding(bottom = 3.dp)
                                        )
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = MintGreenLight
                                ) {
                                    Text(
                                        text = "▲ 12% vs semana passada",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MintGreenDark,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            // Mini Bar Chart S, T, Q, Q, S, S, D
                            val weeklyCalories = listOf(350, 480, 520, 290, 610, 450, 180)
                            val dayLabels = listOf("S", "T", "Q", "Q", "S", "S", "D")
                            val maxCal = weeklyCalories.maxOrNull()?.toFloat() ?: 600f

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(90.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.Bottom
                            ) {
                                weeklyCalories.forEachIndexed { i, cal ->
                                    val barHeightFrac = (cal / maxCal).coerceIn(0.15f, 1f)
                                    val isToday = i == 2 // Quarta-feira

                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .width(16.dp)
                                                .height((65 * barHeightFrac).dp)
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(if (isToday) MintGreen else Color(0xFFE8E5DD))
                                        )
                                        Spacer(modifier = Modifier.height(6.dp))
                                        Text(
                                            text = dayLabels[i],
                                            fontSize = 11.sp,
                                            fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                                            color = if (isToday) MintGreenDark else DarkTextMuted
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // ACTIVITY RINGS CARD (Screen 5)
                // ==========================================
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(22.dp),
                        colors = CardDefaults.cardColors(containerColor = CardWhite),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                    ) {
                        Column(
                            modifier = Modifier.padding(18.dp)
                        ) {
                            Text(
                                text = "Anéis de Atividade Diária",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = DarkTextPrimary
                            )
                            Text(
                                text = "Consistência de movimento, treino e postura",
                                fontSize = 12.sp,
                                color = DarkTextSecondary
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                // Triple Concentric Rings Canvas
                                Box(
                                    modifier = Modifier.size(104.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Canvas(modifier = Modifier.fillMaxSize()) {
                                        val strokeW = 8.dp.toPx()

                                        // Outer Ring - Move (Coral) 75%
                                        drawCircle(
                                            color = CoralPeachLight,
                                            radius = size.minDimension / 2 - strokeW / 2,
                                            style = Stroke(strokeW)
                                        )
                                        drawArc(
                                            color = CoralPeach,
                                            startAngle = -90f,
                                            sweepAngle = 270f,
                                            useCenter = false,
                                            style = Stroke(strokeW, cap = StrokeCap.Round)
                                        )

                                        // Middle Ring - Exercise (Mint) 60%
                                        val midRadius = size.minDimension / 2 - strokeW * 1.8f
                                        drawCircle(
                                            color = MintGreenLight,
                                            radius = midRadius,
                                            style = Stroke(strokeW)
                                        )
                                        drawArc(
                                            color = MintGreen,
                                            startAngle = -90f,
                                            sweepAngle = 220f,
                                            useCenter = false,
                                            style = Stroke(strokeW, cap = StrokeCap.Round)
                                        )

                                        // Inner Ring - Stand (Purple/Blue) 80%
                                        val innerRadius = size.minDimension / 2 - strokeW * 3.1f
                                        drawCircle(
                                            color = Color(0xFF282C38),
                                            radius = innerRadius,
                                            style = Stroke(strokeW)
                                        )
                                        drawArc(
                                            color = PurpleAccent,
                                            startAngle = -90f,
                                            sweepAngle = 290f,
                                            useCenter = false,
                                            style = Stroke(strokeW, cap = StrokeCap.Round)
                                        )
                                    }
                                }

                                Column(
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    RingLegendItem("Mover: 520 kcal", CoralPeach)
                                    RingLegendItem("Treino: 45 min", MintGreenDark)
                                    RingLegendItem("Em Pé: 10 h ativas", PurpleAccent)
                                }
                            }
                        }
                    }
                }

                // ==========================================
                // HISTÓRICO DE SESSÕES CONCLUÍDAS
                // ==========================================
                item {
                    Text(
                        text = "Histórico de Treinos (${workoutHistory.size} sessões)",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                }

                if (workoutHistory.isEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(containerColor = CardWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(24.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(MintGreenLight, CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.History,
                                        contentDescription = null,
                                        tint = MintGreenDark,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = "Nenhum treino salvo recentemente",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DarkTextPrimary
                                )
                                Text(
                                    text = "Conclua uma sessão para registrar suas calorias e tempo real!",
                                    fontSize = 12.sp,
                                    color = DarkTextSecondary
                                )
                                Spacer(modifier = Modifier.height(14.dp))
                                Button(
                                    onClick = onStartWorkoutClick,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MintGreen)
                                ) {
                                    Text("Iniciar Treino Agora", color = Color(0xFF1B2C24), fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                } else {
                    itemsIndexed(workoutHistory, key = { index, log -> "history_${index}_${log.id}" }) { _, log ->
                        WorkoutHistoryItemCard(
                            log = log,
                            formattedDate = dateFormatter.format(Date(log.timestampMillis)),
                            onDelete = { onDeleteHistoryItem(log.id) }
                        )
                    }
                }

                if (weightLogs.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Registros de Peso & IMC",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = DarkTextPrimary
                        )
                    }

                    itemsIndexed(weightLogs.take(5), key = { index, w -> "weight_${index}_${w.id}" }) { _, w ->
                        WeightLogItemCard(
                            weightLog = w,
                            formattedDate = dateFormatter.format(Date(w.timestampMillis))
                        )
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(30.dp))
                }
            }
        }
    }
}

@Composable
private fun RingLegendItem(text: String, dotColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .background(dotColor, CircleShape)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = text,
            fontSize = 10.sp,
            color = DarkTextSecondary
        )
    }
}

@Composable
private fun WorkoutHistoryItemCard(
    log: WorkoutHistoryEntity,
    formattedDate: String,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp))
            .testTag("history_item_${log.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MintGreenLight)
                        .border(1.dp, MintGreen.copy(alpha = 0.4f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.FitnessCenter,
                        contentDescription = null,
                        tint = MintGreenDark,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = log.workoutTitle,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = DarkTextSecondary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = "${log.durationMinutes} min",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MintGreenDark
                        )
                        Text(
                            text = "•",
                            fontSize = 11.sp,
                            color = DarkTextMuted
                        )
                        Text(
                            text = "${log.caloriesBurnedEstimated} kcal",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = CoralPeachDark
                        )
                    }
                }
            }

            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Excluir",
                    tint = DarkTextMuted,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun WeightLogItemCard(
    weightLog: WeightLogEntity,
    formattedDate: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, CardBorder, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(CoralPeachLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = null,
                        tint = CoralPeachDark,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "${weightLog.weightKg} kg",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = formattedDate,
                        fontSize = 11.sp,
                        color = DarkTextSecondary
                    )
                }
            }
        }
    }
}
