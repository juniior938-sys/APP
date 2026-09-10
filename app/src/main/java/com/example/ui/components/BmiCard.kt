package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonOrangeGlow
import com.example.ui.theme.NeonRed
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary

@Composable
fun BmiCard(
    userProfile: UserProfile,
    onLogWeightClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bmi = userProfile.bmi
    val category = userProfile.bmiCategory
    val idealRange = userProfile.idealWeightRangeKg

    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, BlackBorder, RoundedCornerShape(20.dp))
            .testTag("bmi_card"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
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
                            .clip(CircleShape)
                            .background(NeonOrangeGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.MonitorWeight,
                            contentDescription = "Ícone IMC",
                            tint = NeonOrange,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Índice de Massa Corporal (IMC)",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(category.colorHex).copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(category.colorHex).copy(alpha = 0.5f))
                ) {
                    Text(
                        text = category.label,
                        color = Color(category.colorHex),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Column {
                    Text(
                        text = "$bmi",
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = TextWhitePrimary,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = "Peso: ${userProfile.weightKg} kg  •  Altura: ${userProfile.heightCm.toInt()} cm",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                    )
                }

                OutlinedButton(
                    onClick = onLogWeightClick,
                    modifier = Modifier.testTag("log_weight_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonOrange),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Adicionar Peso",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Atualizar Peso", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Barra visual do IMC
            BmiScaleBar(bmi = bmi)

            Spacer(modifier = Modifier.height(12.dp))

            // Faixa Ideal
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Faixa de Peso Ideal Recomendada:",
                    style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                )
                Text(
                    text = "${idealRange.first} kg - ${idealRange.second} kg",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = NeonOrange
                    )
                )
            }
        }
    }
}

@Composable
private fun BmiScaleBar(bmi: Float) {
    val clampedBmi = bmi.coerceIn(15f, 40f)
    val fraction = (clampedBmi - 15f) / (40f - 15f)
    val animatedFraction by animateFloatAsState(targetValue = fraction, label = "bmi_bar_progress")

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(18.dp)
    ) {
        val barHeight = 8.dp.toPx()
        val cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
        val topOffset = (size.height - barHeight) / 2

        val gradient = Brush.horizontalGradient(
            colors = listOf(
                Color(0xFFFF9900), // Abaixo
                Color(0xFFFF6B00), // Normal / Neon Laranja
                Color(0xFFFF3358), // Sobrepeso / Neon Vermelho
                Color(0xFFFF1E44)  // Obesidade
            ),
            startX = 0f,
            endX = size.width
        )

        drawRoundRect(
            brush = gradient,
            topLeft = Offset(0f, topOffset),
            size = Size(size.width, barHeight),
            cornerRadius = cornerRadius
        )

        val indicatorX = (size.width * animatedFraction).coerceIn(8.dp.toPx(), size.width - 8.dp.toPx())
        val indicatorRadius = 7.dp.toPx()

        drawCircle(
            color = Color.White,
            radius = indicatorRadius,
            center = Offset(indicatorX, size.height / 2)
        )
        drawCircle(
            color = Color(0xFF111111),
            radius = indicatorRadius - 2.5.dp.toPx(),
            center = Offset(indicatorX, size.height / 2)
        )
    }
}
