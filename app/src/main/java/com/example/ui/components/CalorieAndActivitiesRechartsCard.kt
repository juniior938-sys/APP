package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Pool
import androidx.compose.material.icons.filled.Whatshot
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WorkoutHistoryEntity
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

data class DailyCalorieData(
    val dayLabel: String,
    val dayDate: String,
    val calories: Int,
    val isToday: Boolean,
    val hasCardio: Boolean
)

data class QuickCardioOption(
    val id: String,
    val title: String,
    val category: String,
    val durationMinutes: Int,
    val estimatedCalories: Int,
    val icon: ImageVector
)

@Composable
fun CalorieAndActivitiesRechartsCard(
    workoutHistory: List<WorkoutHistoryEntity>,
    onLogQuickActivity: (title: String, durationMinutes: Int, caloriesBurned: Int, category: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCustomCardioDialog by remember { mutableStateOf(false) }

    // Calcular dias da semana atual e calorias gastas em cada dia
    val weeklyDays = remember(workoutHistory) {
        computeWeeklyCalories(workoutHistory)
    }

    val todayCalories = weeklyDays.firstOrNull { it.isToday }?.calories ?: 0
    val totalWeekCalories = weeklyDays.sumOf { it.calories }
    val averageDailyCalories = (totalWeekCalories / 7f).roundToInt()

    // Filtrar atividades recentes (últimas 5)
    val recentActivities = remember(workoutHistory) {
        workoutHistory.take(4)
    }

    val quickCardios = listOf(
        QuickCardioOption(
            id = "c1",
            title = "Esteira HIIT",
            category = "Cardio Alta Intensidade",
            durationMinutes = 20,
            estimatedCalories = 210,
            icon = Icons.AutoMirrored.Filled.DirectionsRun
        ),
        QuickCardioOption(
            id = "c2",
            title = "Bicicleta Ergométrica",
            category = "Cardio Moderado",
            durationMinutes = 25,
            estimatedCalories = 220,
            icon = Icons.Default.DirectionsBike
        ),
        QuickCardioOption(
            id = "c3",
            title = "Caminhada Inclinada",
            category = "Queima de Gordura",
            durationMinutes = 30,
            estimatedCalories = 180,
            icon = Icons.Default.DirectionsWalk
        ),
        QuickCardioOption(
            id = "c4",
            title = "Pular Corda Intenso",
            category = "Cardio Queima Rápida",
            durationMinutes = 15,
            estimatedCalories = 160,
            icon = Icons.Default.FlashOn
        )
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BlackBorder, RoundedCornerShape(20.dp))
            .testTag("recharts_calories_activities_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(NeonRedGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalFireDepartment,
                            contentDescription = "Gasto Calórico",
                            tint = NeonRed,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Perda de Calorias & Cardios",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                        Text(
                            text = "Queima diária de treinos e cardios concluídos",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteSecondary)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = BlackSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
                ) {
                    Text(
                        text = "Diário",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = NeonRed,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Resumo de Queima
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlackSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Hoje",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                        )
                        Text(
                            text = "$todayCalories kcal",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonRed
                            )
                        )
                        Text(
                            text = if (todayCalories >= 300) "🔥 Meta batida!" else "Em progresso",
                            fontSize = 10.sp,
                            color = if (todayCalories >= 300) NeonGreen else TextWhiteSecondary
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlackSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Semana Total",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                        )
                        Text(
                            text = "$totalWeekCalories kcal",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = NeonOrange
                            )
                        )
                        Text(
                            text = "Média $averageDailyCalories kcal/dia",
                            fontSize = 10.sp,
                            color = TextWhiteSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mini Gráfico de Barras Estilo Recharts (Semana atual: Seg..Dom)
            Text(
                text = "Gasto Calórico dos Últimos 7 Dias (Kcal)",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = TextWhiteSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PureBlack.copy(alpha = 0.5f))
                    .padding(horizontal = 8.dp, vertical = 10.dp)
            ) {
                RechartsCalorieBarChart(
                    dailyData = weeklyDays,
                    targetCalories = 400
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // SEÇÃO: LISTA DE ATIVIDADES DO DIA A DIA E CARDIOS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Atividades Concluídas & Cardios",
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )
                )

                TextButton(
                    onClick = { showCustomCardioDialog = true },
                    modifier = Modifier.testTag("add_custom_cardio_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = NeonRed,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "+ Cardio",
                        color = NeonRed,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
            }

            // Lista das atividades concluídas recentemente
            if (recentActivities.isNotEmpty()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    recentActivities.forEach { item ->
                        val isCardio = item.notes.contains("Cardio", ignoreCase = true) ||
                                item.workoutTitle.contains("Cardio", ignoreCase = true) ||
                                item.workoutTitle.contains("Esteira", ignoreCase = true) ||
                                item.workoutTitle.contains("Bike", ignoreCase = true)

                        val timeFormat = SimpleDateFormat("dd/MM • HH:mm", Locale.getDefault())
                        val formattedDate = timeFormat.format(Date(item.timestampMillis))

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = BlackSurfaceElevated,
                            border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(
                                            if (isCardio) NeonOrangeGlow else NeonRedGlow,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isCardio) Icons.AutoMirrored.Filled.DirectionsRun else Icons.Default.FitnessCenter,
                                        contentDescription = null,
                                        tint = if (isCardio) NeonOrange else NeonRed,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = item.workoutTitle,
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhitePrimary
                                        ),
                                        maxLines = 1
                                    )
                                    Text(
                                        text = "${item.durationMinutes} min • $formattedDate",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = PureBlack,
                                    border = androidx.compose.foundation.BorderStroke(
                                        0.8.dp,
                                        if (isCardio) NeonOrange else NeonRed
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Whatshot,
                                            contentDescription = null,
                                            tint = if (isCardio) NeonOrange else NeonRed,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "${item.caloriesBurnedEstimated} kcal",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isCardio) NeonOrange else NeonRed
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlackSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Nenhuma atividade registrada hoje ainda. Escolha um cardio abaixo para registrar sua queima de calorias!",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary),
                        modifier = Modifier.padding(14.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Cardios Rápidos Sugeridos para Concluir com 1 Toque
            Text(
                text = "Registrar Cardio Rápido (1 Toque):",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextWhiteMuted,
                    fontWeight = FontWeight.SemiBold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                quickCardios.forEach { option ->
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onLogQuickActivity(
                                    option.title,
                                    option.durationMinutes,
                                    option.estimatedCalories,
                                    "Cardio"
                                )
                            }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = option.icon,
                                    contentDescription = null,
                                    tint = NeonOrange,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = option.title,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhitePrimary
                                        )
                                    )
                                    Text(
                                        text = "${option.durationMinutes} min • ${option.category}",
                                        fontSize = 10.sp,
                                        color = TextWhiteMuted
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = NeonRedGlow,
                                border = androidx.compose.foundation.BorderStroke(0.8.dp, NeonRed)
                            ) {
                                Text(
                                    text = "+${option.estimatedCalories} kcal",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonRed,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo para Adicionar Cardio / Atividade Personalizada
    if (showCustomCardioDialog) {
        CustomCardioDialog(
            onDismiss = { showCustomCardioDialog = false },
            onConfirm = { name, duration, calories ->
                onLogQuickActivity(name, duration, calories, "Cardio Personalizado")
                showCustomCardioDialog = false
            }
        )
    }
}

/**
 * Mini Gráfico de Barras Estilo Recharts para Gasto Calórico
 */
@Composable
private fun RechartsCalorieBarChart(
    dailyData: List<DailyCalorieData>,
    targetCalories: Int = 400
) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val bottomPadding = 25f
        val topPadding = 15f
        val chartHeight = h - topPadding - bottomPadding

        val maxVal = (dailyData.maxOfOrNull { it.calories } ?: 500).coerceAtLeast(targetCalories).toFloat()

        val barCount = dailyData.size
        val totalSpacing = w * 0.25f
        val barWidth = ((w - totalSpacing) / barCount).coerceAtLeast(14f)
        val spacing = totalSpacing / (barCount + 1)

        // Linha guia de meta pontilhada
        val targetY = topPadding + chartHeight * (1f - (targetCalories / maxVal))
        drawLine(
            color = NeonOrange.copy(alpha = 0.4f),
            start = Offset(0f, targetY),
            end = Offset(w, targetY),
            strokeWidth = 1f
        )

        dailyData.forEachIndexed { idx, day ->
            val x = spacing + idx * (barWidth + spacing)
            val barHeight = if (day.calories > 0) {
                ((day.calories / maxVal) * chartHeight).coerceAtLeast(6f)
            } else {
                3f
            }
            val y = topPadding + (chartHeight - barHeight)

            // Gradiente da barra
            val brush = if (day.isToday) {
                Brush.verticalGradient(
                    colors = listOf(NeonRed, NeonOrange)
                )
            } else if (day.calories > 0) {
                Brush.verticalGradient(
                    colors = listOf(NeonOrange, NeonOrange.copy(alpha = 0.6f))
                )
            } else {
                Brush.verticalGradient(
                    colors = listOf(BlackBorder, BlackBorder.copy(alpha = 0.3f))
                )
            }

            drawRoundRect(
                brush = brush,
                topLeft = Offset(x, y),
                size = Size(barWidth, barHeight),
                cornerRadius = CornerRadius(6f, 6f)
            )

            // Ponto indicador se bateu a meta
            if (day.calories >= targetCalories) {
                drawCircle(
                    color = NeonGreen,
                    radius = 3f,
                    center = Offset(x + barWidth / 2f, y - 6f)
                )
            }
        }
    }

    // Linha de Rótulos abaixo do gráfico
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 95.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            dailyData.forEach { day ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = day.dayLabel,
                        fontSize = 10.sp,
                        fontWeight = if (day.isToday) FontWeight.Black else FontWeight.Medium,
                        color = if (day.isToday) NeonRed else TextWhiteMuted
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomCardioDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, duration: Int, calories: Int) -> Unit
) {
    var activityType by remember { mutableStateOf("Corrida na Esteira") }
    var durationMinutes by remember { mutableIntStateOf(20) }

    val calorieFactor = when (activityType) {
        "Corrida na Esteira" -> 10.5f
        "Bicicleta Ergométrica" -> 8.5f
        "Caminhada Inclinada" -> 6.5f
        "Pular Corda" -> 11.0f
        "Elíptico" -> 8.0f
        "Natação" -> 9.5f
        else -> 7.5f
    }

    val calculatedCalories = (durationMinutes * calorieFactor).roundToInt()

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = BlackSurfaceCard,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocalFireDepartment,
                    contentDescription = null,
                    tint = NeonRed,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Registrar Atividade / Cardio",
                    fontWeight = FontWeight.Bold,
                    color = TextWhitePrimary
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = "Selecione o tipo de cardio realizado para adicionar ao gráfico de calorias da página de início:",
                    style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                )

                // Chips de Tipo de Cardio
                val types = listOf("Corrida na Esteira", "Bicicleta Ergométrica", "Caminhada Inclinada", "Pular Corda", "Elíptico", "Natação")
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    types.chunked(2).forEach { rowTypes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            rowTypes.forEach { type ->
                                val isSelected = activityType == type
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isSelected) NeonRedGlow else BlackSurfaceElevated,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        if (isSelected) NeonRed else BlackBorder
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { activityType = type }
                                ) {
                                    Text(
                                        text = type,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = if (isSelected) TextWhitePrimary else TextWhiteSecondary,
                                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 6.dp),
                                        maxLines = 1
                                    )
                                }
                            }
                        }
                    }
                }

                // Duração com Slider
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Duração da Atividade",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                        )
                        Text(
                            text = "$durationMinutes minutos",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = NeonOrange,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }

                    Slider(
                        value = durationMinutes.toFloat(),
                        onValueChange = { durationMinutes = it.roundToInt() },
                        valueRange = 5f..90f,
                        steps = 16,
                        colors = SliderDefaults.colors(
                            thumbColor = NeonRed,
                            activeTrackColor = NeonOrange,
                            inactiveTrackColor = BlackBorder
                        )
                    )
                }

                // Estimativa de Calorias
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = PureBlack,
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonRed.copy(alpha = 0.5f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Gasto Calórico Estimado",
                                fontSize = 11.sp,
                                color = TextWhiteMuted
                            )
                            Text(
                                text = "$calculatedCalories kcal",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Black,
                                color = NeonRed
                            )
                        }
                        Icon(
                            imageVector = Icons.Default.Whatshot,
                            contentDescription = null,
                            tint = NeonRed,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = { onConfirm(activityType, durationMinutes, calculatedCalories) },
                colors = ButtonDefaults.buttonColors(containerColor = NeonRed)
            ) {
                Text("Concluir e Salvar", color = TextWhitePrimary, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar", color = TextWhiteSecondary)
            }
        }
    )
}

/**
 * Calcula calorias para os últimos 7 dias da semana
 */
private fun computeWeeklyCalories(history: List<WorkoutHistoryEntity>): List<DailyCalorieData> {
    val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val labelFormat = SimpleDateFormat("EEE", Locale("pt", "BR"))

    val cal = Calendar.getInstance()
    val todayStr = dayFormat.format(cal.time)

    // Pegar os últimos 7 dias até hoje
    val result = mutableListOf<DailyCalorieData>()

    for (offset in 6 downTo 0) {
        val loopCal = Calendar.getInstance()
        loopCal.add(Calendar.DAY_OF_YEAR, -offset)

        val dateStr = dayFormat.format(loopCal.time)
        val dayLabel = labelFormat.format(loopCal.time)
            .replace(".", "")
            .replaceFirstChar { it.uppercase() }

        val workoutsOnDay = history.filter {
            dayFormat.format(Date(it.timestampMillis)) == dateStr
        }

        val totalCal = workoutsOnDay.sumOf { it.caloriesBurnedEstimated }
        val hasCardio = workoutsOnDay.any {
            it.notes.contains("Cardio", ignoreCase = true) ||
                    it.workoutTitle.contains("Cardio", ignoreCase = true)
        }

        result.add(
            DailyCalorieData(
                dayLabel = dayLabel,
                dayDate = dateStr,
                calories = totalCal,
                isToday = (dateStr == todayStr),
                hasCardio = hasCardio
            )
        )
    }

    return result
}
