package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.DarkTextMuted
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MintGreenDark
import com.example.ui.theme.MintGreenLight
import java.util.Calendar

data class DayActivityItem(
    val dayLetter: String, // "S", "T", "Q", "Q", "S", "S", "D" or "M", "T", "W", "T", "F", "S", "S"
    val fullName: String,
    val fillFraction: Float, // 0.0f to 1.0f
    val isToday: Boolean,
    val isCompleted: Boolean
)

@Composable
fun WeeklyActivityCapsuleCard(
    modifier: Modifier = Modifier,
    onDayClick: (String) -> Unit = {}
) {
    val currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) // 1=Dom, 2=Seg, ... 7=Sab

    val weekDays = listOf(
        DayActivityItem("S", "Segunda", 0.65f, currentDayOfWeek == Calendar.MONDAY, true),
        DayActivityItem("T", "Terça", 0.40f, currentDayOfWeek == Calendar.TUESDAY, true),
        DayActivityItem("Q", "Quarta", 0.90f, currentDayOfWeek == Calendar.WEDNESDAY, true),
        DayActivityItem("Q", "Quinta", 0.75f, currentDayOfWeek == Calendar.THURSDAY, false),
        DayActivityItem("S", "Sexta", 0.50f, currentDayOfWeek == Calendar.FRIDAY, false),
        DayActivityItem("S", "Sábado", 0.85f, currentDayOfWeek == Calendar.SATURDAY, false),
        DayActivityItem("D", "Domingo", 0.30f, currentDayOfWeek == Calendar.SUNDAY, false)
    )

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
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
                Column {
                    Text(
                        text = "Atividade Semanal",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkTextPrimary
                    )
                    Text(
                        text = "Consistência e frequência semanal",
                        fontSize = 12.sp,
                        color = DarkTextSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .background(MintGreenLight, RoundedCornerShape(12.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Esta Semana",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MintGreenDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 7 Vertical Capsule Bars
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                weekDays.forEach { item ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .clickable { onDayClick(item.fullName) }
                            .padding(horizontal = 2.dp)
                    ) {
                        // Capsule container
                        Box(
                            modifier = Modifier
                                .width(16.dp)
                                .height(78.dp)
                                .background(Color(0xFF1E212A), RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp)),
                            contentAlignment = Alignment.BottomCenter
                        ) {
                            Box(
                                modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight(item.fillFraction)
                                .background(
                                    if (item.isToday) MintGreen
                                    else if (item.isCompleted) MintGreen.copy(alpha = 0.5f)
                                    else Color(0xFF282C38),
                                    RoundedCornerShape(12.dp)
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Day label indicator
                        if (item.isToday) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(MintGreen, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = item.dayLetter,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        } else {
                            Text(
                                text = item.dayLetter,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (item.isCompleted) DarkTextPrimary else DarkTextMuted
                            )
                        }
                    }
                }
            }
        }
    }
}
