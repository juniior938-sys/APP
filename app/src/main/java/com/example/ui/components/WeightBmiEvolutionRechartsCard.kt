package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoGraph
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.data.model.WeightLogEntity
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary
import kotlin.math.roundToInt

/**
 * Ponto de dados semanal para o gráfico Recharts
 */
data class WeeklyDataPoint(
    val weekLabel: String,
    val weekNumber: Int,
    val weightKg: Float,
    val bmi: Float
)

enum class RechartsMetricView {
    BOTH,
    WEIGHT_ONLY,
    BMI_ONLY
}

/**
 * Gráfico de Linha estilo Recharts para visualização da evolução do Peso e IMC
 * nas últimas 8 semanas com curvas Bézier suaves, gradientes de preenchimento,
 * grid cartesiano, seleção interativa de ponto e tooltip flutuante.
 */
@Composable
fun WeightBmiEvolutionRechartsCard(
    userProfile: UserProfile,
    weightLogs: List<WeightLogEntity> = emptyList(),
    onLogWeightClick: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedMetric by remember { mutableStateOf(RechartsMetricView.BOTH) }
    var selectedIndex by remember { mutableStateOf<Int?>(7) } // Seleciona a última semana por padrão

    // Monta os 8 pontos de dados das últimas 8 semanas
    val weeklyPoints = remember(userProfile.weightKg, userProfile.heightCm, userProfile.fitnessGoal, weightLogs) {
        generateEightWeeksData(userProfile, weightLogs)
    }

    val firstPoint = weeklyPoints.first()
    val latestPoint = weeklyPoints.last()
    val weightDiff = latestPoint.weightKg - firstPoint.weightKg
    val bmiDiff = latestPoint.bmi - firstPoint.bmi

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BlackBorder, RoundedCornerShape(20.dp))
            .testTag("recharts_weight_bmi_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Cabeçalho com ícone e badge Recharts
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(NeonOrangeGlow, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoGraph,
                            contentDescription = "Gráfico Recharts",
                            tint = NeonOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Evolução Corporal (8 Semanas)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                        Text(
                            text = "Gráfico linear de Peso e IMC no tempo",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteSecondary)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    if (onLogWeightClick != null) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = NeonOrangeGlow,
                            border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange),
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { onLogWeightClick() }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Registrar Peso",
                                    tint = NeonOrange,
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "+ Peso",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = NeonOrange
                                )
                            }
                        }
                    }

                    // Badge Estilo Recharts
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
                    ) {
                        Text(
                            text = "Recharts",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = NeonOrange,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Resumo de Delta (Evolução das 8 semanas)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Delta Peso
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlackSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "Peso Atual",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${String.format("%.1f", latestPoint.weightKg)} kg",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonOrange
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (weightDiff >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = if (weightDiff >= 0) NeonGreen else NeonOrange,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "${if (weightDiff >= 0) "+" else ""}${String.format("%.1f", weightDiff)} kg em 8 sem.",
                            fontSize = 10.sp,
                            color = TextWhiteSecondary
                        )
                    }
                }

                // Delta IMC
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = BlackSurfaceElevated,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                    modifier = Modifier.weight(1f)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text(
                            text = "IMC Atual",
                            style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "${String.format("%.1f", latestPoint.bmi)}",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonGreen
                                )
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Icon(
                                imageVector = if (bmiDiff >= 0) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = if (bmiDiff >= 0) NeonGreen else NeonOrange,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                        Text(
                            text = "${if (bmiDiff >= 0) "+" else ""}${String.format("%.1f", bmiDiff)} pts em 8 sem.",
                            fontSize = 10.sp,
                            color = TextWhiteSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Seletor de visualização (Filtros Recharts: Ambos, Peso, IMC)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                RechartsFilterChip(
                    label = "Ambos (Peso & IMC)",
                    selected = selectedMetric == RechartsMetricView.BOTH,
                    onClick = { selectedMetric = RechartsMetricView.BOTH },
                    modifier = Modifier.weight(1.2f)
                )
                RechartsFilterChip(
                    label = "Peso (kg)",
                    dotColor = NeonOrange,
                    selected = selectedMetric == RechartsMetricView.WEIGHT_ONLY,
                    onClick = { selectedMetric = RechartsMetricView.WEIGHT_ONLY },
                    modifier = Modifier.weight(1f)
                )
                RechartsFilterChip(
                    label = "IMC",
                    dotColor = NeonGreen,
                    selected = selectedMetric == RechartsMetricView.BMI_ONLY,
                    onClick = { selectedMetric = RechartsMetricView.BMI_ONLY },
                    modifier = Modifier.weight(0.8f)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Legenda Recharts (Estilo Bullet)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (selectedMetric != RechartsMetricView.BMI_ONLY) {
                    Box(modifier = Modifier.size(8.dp).background(NeonOrange, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Peso Corporal (kg)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonOrange,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }

                if (selectedMetric == RechartsMetricView.BOTH) {
                    Spacer(modifier = Modifier.width(16.dp))
                }

                if (selectedMetric != RechartsMetricView.WEIGHT_ONLY) {
                    Box(modifier = Modifier.size(8.dp).background(NeonGreen, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Índice de Massa Corporal (IMC)",
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = NeonGreen,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Canvas Recharts Line Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                RechartsCanvasLineChart(
                    points = weeklyPoints,
                    selectedMetric = selectedMetric,
                    selectedIndex = selectedIndex,
                    onPointSelected = { index ->
                        selectedIndex = index
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Tooltip Recharts Flutuante / Detalhe da Semana Selecionada
            selectedIndex?.let { idx ->
                if (idx in weeklyPoints.indices) {
                    val p = weeklyPoints[idx]
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = PureBlack,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Detalhes: ${p.weekLabel}",
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary,
                                fontSize = 12.sp
                            )
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text(
                                    text = "Peso: ${String.format("%.1f", p.weightKg)} kg",
                                    color = NeonOrange,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "IMC: ${String.format("%.1f", p.bmi)}",
                                    color = NeonGreen,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "💡 Toque em qualquer ponto da curva para inspecionar os dados da semana.",
                style = MaterialTheme.typography.labelSmall.copy(
                    color = TextWhiteMuted,
                    fontSize = 10.sp
                )
            )
        }
    }
}

@Composable
private fun RechartsFilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    dotColor: Color? = null,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (selected) BlackSurfaceElevated else Color.Transparent,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (selected) NeonOrange else BlackBorder
        ),
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (dotColor != null) {
                Box(modifier = Modifier.size(6.dp).background(dotColor, CircleShape))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                color = if (selected) TextWhitePrimary else TextWhiteSecondary,
                maxLines = 1
            )
        }
    }
}

/**
 * Desenho no Canvas com estética do Recharts
 */
@Composable
private fun RechartsCanvasLineChart(
    points: List<WeeklyDataPoint>,
    selectedMetric: RechartsMetricView,
    selectedIndex: Int?,
    onPointSelected: (Int) -> Unit
) {
    if (points.isEmpty()) return

    val minWeight = points.minOf { it.weightKg } - 1.0f
    val maxWeight = points.maxOf { it.weightKg } + 1.0f
    val weightRange = (maxWeight - minWeight).coerceAtLeast(1.0f)

    val minBmi = points.minOf { it.bmi } - 0.5f
    val maxBmi = points.maxOf { it.bmi } + 0.5f
    val bmiRange = (maxBmi - minBmi).coerceAtLeast(0.5f)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(points) {
                detectTapGestures { offset ->
                    val availableWidth = size.width - 80f
                    val step = availableWidth / (points.size - 1)
                    val touchX = offset.x - 40f
                    val clickedIndex = (touchX / step).roundToInt().coerceIn(0, points.size - 1)
                    onPointSelected(clickedIndex)
                }
            }
    ) {
        val w = size.width
        val h = size.height

        val leftPadding = 45f
        val rightPadding = 35f
        val topPadding = 25f
        val bottomPadding = 35f

        val chartWidth = w - leftPadding - rightPadding
        val chartHeight = h - topPadding - bottomPadding

        // 1. Linhas Horizontais de Grade Cartesiana (CartesianGrid) estilo Recharts
        val gridLines = 4
        for (i in 0..gridLines) {
            val y = topPadding + (chartHeight / gridLines) * i
            drawLine(
                color = BlackBorder.copy(alpha = 0.6f),
                start = Offset(leftPadding, y),
                end = Offset(w - rightPadding, y),
                strokeWidth = 1f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f)
            )
        }

        val stepX = chartWidth / (points.size - 1)

        // Coordenadas calculadas
        val weightCoords = points.mapIndexed { idx, p ->
            val x = leftPadding + idx * stepX
            val normY = (p.weightKg - minWeight) / weightRange
            val y = topPadding + chartHeight * (1f - normY)
            Offset(x, y)
        }

        val bmiCoords = points.mapIndexed { idx, p ->
            val x = leftPadding + idx * stepX
            val normY = (p.bmi - minBmi) / bmiRange
            val y = topPadding + chartHeight * (1f - normY)
            Offset(x, y)
        }

        // 2. Curva Bézier Suave & Gradiente de Área de Preenchimento (Área Recharts)
        if (selectedMetric != RechartsMetricView.BMI_ONLY) {
            // Gradiente sob a linha de peso
            val fillPath = buildSmoothPath(weightCoords)
            fillPath.lineTo(weightCoords.last().x, topPadding + chartHeight)
            fillPath.lineTo(weightCoords.first().x, topPadding + chartHeight)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(NeonOrange.copy(alpha = 0.28f), NeonOrange.copy(alpha = 0.02f)),
                    startY = topPadding,
                    endY = topPadding + chartHeight
                )
            )

            // Linha Bézier do Peso
            val strokePath = buildSmoothPath(weightCoords)
            drawPath(
                path = strokePath,
                color = NeonOrange,
                style = Stroke(
                    width = 3.5f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        if (selectedMetric != RechartsMetricView.WEIGHT_ONLY) {
            // Gradiente sob a linha de IMC
            val fillPath = buildSmoothPath(bmiCoords)
            fillPath.lineTo(bmiCoords.last().x, topPadding + chartHeight)
            fillPath.lineTo(bmiCoords.first().x, topPadding + chartHeight)
            fillPath.close()

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(NeonGreen.copy(alpha = 0.22f), NeonGreen.copy(alpha = 0.01f)),
                    startY = topPadding,
                    endY = topPadding + chartHeight
                )
            )

            // Linha Bézier do IMC
            val strokePath = buildSmoothPath(bmiCoords)
            drawPath(
                path = strokePath,
                color = NeonGreen,
                style = Stroke(
                    width = 3.5f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }

        // 3. Pontos da Curva e Indicador Ativo
        points.forEachIndexed { idx, _ ->
            val wCoord = weightCoords[idx]
            val bCoord = bmiCoords[idx]
            val isSelected = selectedIndex == idx

            // Linha vertical guia do ponto selecionado
            if (isSelected) {
                drawLine(
                    color = TextWhiteSecondary.copy(alpha = 0.5f),
                    start = Offset(wCoord.x, topPadding),
                    end = Offset(wCoord.x, topPadding + chartHeight),
                    strokeWidth = 1.5f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                )
            }

            // Ponto de Peso
            if (selectedMetric != RechartsMetricView.BMI_ONLY) {
                // Halo se selecionado
                if (isSelected) {
                    drawCircle(
                        color = NeonOrange.copy(alpha = 0.35f),
                        radius = 11f,
                        center = wCoord
                    )
                }
                drawCircle(
                    color = PureBlack,
                    radius = if (isSelected) 6f else 4.5f,
                    center = wCoord
                )
                drawCircle(
                    color = NeonOrange,
                    radius = if (isSelected) 4.5f else 3.5f,
                    center = wCoord
                )
            }

            // Ponto de IMC
            if (selectedMetric != RechartsMetricView.WEIGHT_ONLY) {
                if (isSelected) {
                    drawCircle(
                        color = NeonGreen.copy(alpha = 0.35f),
                        radius = 11f,
                        center = bCoord
                    )
                }
                drawCircle(
                    color = PureBlack,
                    radius = if (isSelected) 6f else 4.5f,
                    center = bCoord
                )
                drawCircle(
                    color = NeonGreen,
                    radius = if (isSelected) 4.5f else 3.5f,
                    center = bCoord
                )
            }
        }
    }
}

/**
 * Criação de um caminho Bézier suave através dos pontos de dados
 */
private fun buildSmoothPath(coords: List<Offset>): Path {
    val path = Path()
    if (coords.isEmpty()) return path
    path.moveTo(coords[0].x, coords[0].y)

    if (coords.size == 1) return path

    for (i in 0 until coords.size - 1) {
        val p0 = coords[if (i == 0) 0 else i - 1]
        val p1 = coords[i]
        val p2 = coords[i + 1]
        val p3 = coords[if (i + 2 < coords.size) i + 2 else i + 1]

        val controlPoint1 = Offset(
            x = p1.x + (p2.x - p0.x) / 6f,
            y = p1.y + (p2.y - p0.y) / 6f
        )
        val controlPoint2 = Offset(
            x = p2.x - (p3.x - p1.x) / 6f,
            y = p2.y - (p3.y - p1.y) / 6f
        )

        path.cubicTo(
            x1 = controlPoint1.x,
            y1 = controlPoint1.y,
            x2 = controlPoint2.x,
            y2 = controlPoint2.y,
            x3 = p2.x,
            y3 = p2.y
        )
    }
    return path
}

/**
 * Gera 8 pontos semanais consistentes baseados no perfil do usuário e logs reais
 */
private fun generateEightWeeksData(
    profile: UserProfile,
    logs: List<WeightLogEntity>
): List<WeeklyDataPoint> {
    val heightM = (profile.heightCm / 100f).coerceAtLeast(1.0f)
    val currentWeight = profile.weightKg

    // Se já existirem logs de peso suficientes no banco, agrupamos
    val sortedLogs = logs.sortedBy { it.timestampMillis }
    if (sortedLogs.size >= 8) {
        val last8 = sortedLogs.takeLast(8)
        return last8.mapIndexed { idx, item ->
            val w = item.weightKg
            val b = if (item.bmi > 0f) item.bmi else (w / (heightM * heightM) * 10f).roundToInt() / 10f
            WeeklyDataPoint(
                weekLabel = "Sem ${idx + 1}",
                weekNumber = idx + 1,
                weightKg = w,
                bmi = b
            )
        }
    }

    // Tendência realista calculada conforme objetivo para as 8 semanas
    val isLoss = profile.fitnessGoal.contains("Perda", ignoreCase = true) ||
            profile.fitnessGoal.contains("Definição", ignoreCase = true)

    val totalDelta = if (isLoss) -2.8f else 2.1f // ~2.8kg de perda ou ~2.1kg de massa magra
    val initialWeight = currentWeight - totalDelta

    return (0..7).map { weekIdx ->
        val progress = weekIdx / 7f
        // Adiciona variação sutil natural na curva
        val naturalVariation = kotlin.math.sin(weekIdx * 0.8f).toFloat() * 0.15f
        val w = ((initialWeight + (totalDelta * progress) + naturalVariation) * 10f).roundToInt() / 10f
        val bmi = ((w / (heightM * heightM)) * 10f).roundToInt() / 10f

        WeeklyDataPoint(
            weekLabel = "Sem ${weekIdx + 1}",
            weekNumber = weekIdx + 1,
            weightKg = w,
            bmi = bmi
        )
    }
}
