package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberAccent
import com.example.ui.theme.AmberLight
import com.example.ui.theme.CardBorder
import com.example.ui.theme.CardWhite
import com.example.ui.theme.CoralPeach
import com.example.ui.theme.CoralPeachLight
import com.example.ui.theme.DarkTextPrimary
import com.example.ui.theme.DarkTextSecondary
import com.example.ui.theme.MintGreen
import com.example.ui.theme.MintGreenLight
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.PurpleLight

@Composable
fun QuickActionsGrid(
    onStartWorkout: () -> Unit,
    onLogFood: () -> Unit,
    onBodyStats: () -> Unit,
    onChallengesOrAi: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Ações Rápidas",
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            color = DarkTextPrimary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionTile(
                icon = Icons.Filled.FitnessCenter,
                iconTint = MintGreen,
                iconBg = MintGreenLight,
                title = "Iniciar Treino",
                subtitle = "Ficha do Dia",
                onClick = onStartWorkout,
                testTag = "action_start_workout",
                modifier = Modifier.weight(1f)
            )
            QuickActionTile(
                icon = Icons.Filled.Restaurant,
                iconTint = CoralPeach,
                iconBg = CoralPeachLight,
                title = "Registrar Refeição",
                subtitle = "Metas & Calorias",
                onClick = onLogFood,
                testTag = "action_log_food",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            QuickActionTile(
                icon = Icons.Filled.MonitorWeight,
                iconTint = PurpleAccent,
                iconBg = PurpleLight,
                title = "Dados Corporais",
                subtitle = "Registrar Peso",
                onClick = onBodyStats,
                testTag = "action_body_stats",
                modifier = Modifier.weight(1f)
            )
            QuickActionTile(
                icon = Icons.Filled.EmojiEvents,
                iconTint = AmberAccent,
                iconBg = AmberLight,
                title = "Desafios",
                subtitle = "Ampla IA Coach",
                onClick = onChallengesOrAi,
                testTag = "action_ai_challenges",
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionTile(
    icon: ImageVector,
    iconTint: Color,
    iconBg: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(82.dp)
            .clip(RoundedCornerShape(18.dp))
            .clickable { onClick() }
            .testTag(testTag),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = CardWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, CardBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(iconBg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = iconTint,
                    modifier = Modifier.size(18.dp)
                )
            }

            Spacer(modifier = Modifier.size(10.dp))

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = title,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DarkTextPrimary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 10.sp,
                    color = DarkTextSecondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

