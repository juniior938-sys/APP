package com.example.ui.screens

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.filled.Alarm
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserProfile
import com.example.ui.components.DigitalDueDatePickerDialog
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.DiscreetAppGradient
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
import java.util.Date
import java.util.Locale
import kotlin.math.pow
import kotlin.math.roundToInt

@Composable
fun ProfileScreen(
    userProfile: UserProfile,
    isSaving: Boolean,
    onSaveProfile: (UserProfile) -> Unit,
    onTriggerMembershipPopup: () -> Unit = {},
    onTriggerAlarmPopup: () -> Unit = {},
    onTriggerWaterPopup: () -> Unit = {},
    onTriggerBlock: () -> Unit = {},
    onReplayEntranceVideo: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var name by remember(userProfile) { mutableStateOf(userProfile.name) }
    var ageStr by remember(userProfile) { mutableStateOf(userProfile.age.toString()) }
    var gender by remember(userProfile) { mutableStateOf(userProfile.gender) }
    var weightStr by remember(userProfile) { mutableStateOf(userProfile.weightKg.toString()) }
    var heightStr by remember(userProfile) { mutableStateOf(userProfile.heightCm.toInt().toString()) }
    var fitnessLevel by remember(userProfile) { mutableStateOf(userProfile.fitnessLevel) }
    var workoutLocation by remember(userProfile) { mutableStateOf(userProfile.workoutLocation) }
    var fitnessGoal by remember(userProfile) { mutableStateOf(userProfile.fitnessGoal) }

    // Mensalidade da academia & Senha do Administrador
    var gymName by remember(userProfile) { mutableStateOf(userProfile.gymName) }
    var gymFee by remember(userProfile) { mutableStateOf(userProfile.gymMembershipFee) }
    var gymDueDay by remember(userProfile) { mutableIntStateOf(userProfile.gymMembershipDueDay) }
    var gymStatus by remember(userProfile) { mutableStateOf(userProfile.gymMembershipStatus) }
    var gymReminderEnabled by remember(userProfile) { mutableStateOf(userProfile.gymMembershipReminderEnabled) }
    var adminPin by remember(userProfile) { mutableStateOf(userProfile.adminPin) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Alarme e Horário
    var gymHour by remember(userProfile) { mutableIntStateOf(userProfile.gymAlarmHour) }
    var gymMinute by remember(userProfile) { mutableIntStateOf(userProfile.gymAlarmMinute) }
    var gymDays by remember(userProfile) { mutableStateOf(userProfile.gymAlarmDays) }
    var gymAlarmEnabled by remember(userProfile) { mutableStateOf(userProfile.gymAlarmEnabled) }

    // Água
    var waterInterval by remember(userProfile) { mutableIntStateOf(userProfile.waterReminderIntervalMinutes) }
    var waterEnabled by remember(userProfile) { mutableStateOf(userProfile.waterReminderEnabled) }

    // Cálculo IMC em tempo real
    val weightNum = weightStr.toFloatOrNull() ?: userProfile.weightKg
    val heightNum = heightStr.toFloatOrNull() ?: userProfile.heightCm
    val previewBmi = if (heightNum > 0f) {
        val hM = heightNum / 100f
        ((weightNum / hM.pow(2)) * 10f).roundToInt() / 10f
    } else 22.0f

    val bmiCategoryLabel = when {
        previewBmi < 18.5f -> "Abaixo do peso"
        previewBmi < 25.0f -> "Peso Saudável"
        previewBmi < 30.0f -> "Sobrepeso"
        else -> "Obesidade"
    }

    val bmiCategoryColor = when {
        previewBmi < 18.5f -> NeonOrange
        previewBmi < 25.0f -> NeonGreen
        previewBmi < 30.0f -> NeonOrange
        else -> NeonRed
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Cabeçalho
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(NeonRedGlow, CircleShape)
                        .border(1.5.dp, NeonRed, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Perfil",
                        tint = NeonRed,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Perfil do Atleta & Configurações",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Black,
                            color = TextWhitePrimary
                        )
                    )
                    Text(
                        text = "Personalize seus dados, alarmes e mensalidade da academia",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                    )
                }
            }

            // 1. LEMBRETE DE MENSALIDADE DA ACADEMIA (Destaque solicitado)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NeonRed, RoundedCornerShape(20.dp))
                    .testTag("gym_membership_card"),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonRedGlow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Payments,
                                    contentDescription = null,
                                    tint = NeonRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Mensalidade da Academia",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextWhitePrimary
                                    )
                                )
                                Text(
                                    text = "Popups e alertas de vencimento",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteMuted)
                                )
                            }
                        }

                        Switch(
                            checked = gymReminderEnabled,
                            onCheckedChange = { gymReminderEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhitePrimary,
                                checkedTrackColor = NeonRed,
                                uncheckedThumbColor = TextWhiteMuted,
                                uncheckedTrackColor = BlackSurfaceElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = gymName,
                            onValueChange = { gymName = it },
                            label = { Text("Nome da Academia", color = TextWhiteSecondary) },
                            modifier = Modifier.weight(1.3f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedBorderColor = NeonRed,
                                unfocusedBorderColor = BlackBorder
                            )
                        )

                        OutlinedTextField(
                            value = gymFee,
                            onValueChange = { gymFee = it },
                            label = { Text("Valor (R$)", color = TextWhiteSecondary) },
                            modifier = Modifier.weight(0.9f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedBorderColor = NeonRed,
                                unfocusedBorderColor = BlackBorder
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Data de Vencimento com Calendário Digital Editável
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
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Vencimento da Mensalidade:",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                                )
                                Text(
                                    text = "Todo dia $gymDueDay de cada mês",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = NeonRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Button(
                                onClick = { showDatePicker = true },
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                                modifier = Modifier.testTag("open_calendar_picker_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = "Abrir Calendário",
                                    tint = TextWhitePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Calendário",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextWhitePrimary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Status: $gymStatus",
                            style = MaterialTheme.typography.bodyMedium.copy(color = TextWhiteSecondary)
                        )

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            FilterChip(
                                selected = gymStatus == "Em dia",
                                onClick = { gymStatus = "Em dia" },
                                label = { Text("Em dia") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonGreen.copy(alpha = 0.3f),
                                    selectedLabelColor = NeonGreen,
                                    containerColor = BlackSurfaceElevated,
                                    labelColor = TextWhiteSecondary
                                )
                            )
                            FilterChip(
                                selected = gymStatus == "Vence em breve",
                                onClick = { gymStatus = "Vence em breve" },
                                label = { Text("Vence em breve") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonRed.copy(alpha = 0.3f),
                                    selectedLabelColor = NeonRed,
                                    containerColor = BlackSurfaceElevated,
                                    labelColor = TextWhiteSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Informação da Data do Último Pagamento Anterior
                    val dateFormatter = remember { SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")) }
                    val lastPaymentDateStr = remember(userProfile.lastPaymentDateMillis) {
                        if (userProfile.lastPaymentDateMillis > 0) {
                            dateFormatter.format(Date(userProfile.lastPaymentDateMillis))
                        } else {
                            "Nenhum pagamento anterior registrado"
                        }
                    }

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
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = NeonOrange,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Data do Último Pagamento Anterior:",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                                )
                                Text(
                                    text = lastPaymentDateStr,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = NeonOrange
                                    )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Status de Alerta e Bloqueio Automáticos
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonGreen.copy(alpha = 0.4f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("auto_alert_status_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .background(NeonGreen.copy(alpha = 0.15f), androidx.compose.foundation.shape.CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.NotificationsActive,
                                    contentDescription = null,
                                    tint = NeonGreen,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Alertas Automáticos Ativos",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                )
                                Text(
                                    text = if (gymReminderEnabled)
                                        "O popup de aviso e o bloqueio são acionados automaticamente pelo status e no vencimento (todo dia $gymDueDay)."
                                    else
                                        "Lembretes desativados. Ative a chave acima para receber os avisos automáticos.",
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        color = TextWhiteSecondary,
                                        fontSize = 11.sp
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 2. HORÁRIO DA ACADEMIA & ALARME (Destaque solicitado)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NeonOrange, RoundedCornerShape(20.dp))
                    .testTag("gym_alarm_card"),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonOrangeGlow, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Alarm,
                                    contentDescription = null,
                                    tint = NeonOrange,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Alarme & Horário de Treino",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextWhitePrimary
                                    )
                                )
                                Text(
                                    text = "Lembretes com som e vibração para a academia",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteMuted)
                                )
                            }
                        }

                        Switch(
                            checked = gymAlarmEnabled,
                            onCheckedChange = { gymAlarmEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = TextWhitePrimary,
                                checkedTrackColor = NeonOrange,
                                uncheckedThumbColor = TextWhiteMuted,
                                uncheckedTrackColor = BlackSurfaceElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Horário Selecionado: ${String.format("%02d:%02d", gymHour, gymMinute)}",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = NeonOrange
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf(
                            Pair(6, 0),
                            Pair(7, 30),
                            Pair(18, 0),
                            Pair(18, 30),
                            Pair(20, 0)
                        ).forEach { (h, m) ->
                            val isSel = gymHour == h && gymMinute == m
                            FilterChip(
                                selected = isSel,
                                onClick = {
                                    gymHour = h
                                    gymMinute = m
                                },
                                label = { Text(String.format("%02d:%02d", h, m), fontSize = 12.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonOrange,
                                    selectedLabelColor = TextWhitePrimary,
                                    containerColor = BlackSurfaceElevated,
                                    labelColor = TextWhiteSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = gymDays,
                        onValueChange = { gymDays = it },
                        label = { Text("Dias de Treino", color = TextWhiteSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhitePrimary,
                            unfocusedTextColor = TextWhitePrimary,
                            focusedBorderColor = NeonOrange,
                            unfocusedBorderColor = BlackBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onTriggerAlarmPopup,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("test_alarm_popup_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonOrange)
                    ) {
                        Icon(
                            imageVector = Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = TextWhitePrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Disparar Alarme Agora (Testar Popup)",
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    }
                }
            }

            // 3. LEMBRETE DE BEBER ÁGUA (Destaque solicitado)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NeonBlue.copy(alpha = 0.8f), RoundedCornerShape(20.dp))
                    .testTag("water_reminder_card"),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .background(NeonBlue.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.LocalDrink,
                                    contentDescription = null,
                                    tint = NeonBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Lembrete de Água no Treino",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.Black,
                                        color = TextWhitePrimary
                                    )
                                )
                                Text(
                                    text = "Alertas periódicos durante a sessão de treino",
                                    style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteMuted)
                                )
                            }
                        }

                        Switch(
                            checked = waterEnabled,
                            onCheckedChange = { waterEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PureBlack,
                                checkedTrackColor = NeonBlue,
                                uncheckedThumbColor = TextWhiteMuted,
                                uncheckedTrackColor = BlackSurfaceElevated
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Intervalo do Lembrete: A cada $waterInterval minutos",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = TextWhitePrimary,
                            fontWeight = FontWeight.SemiBold
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(5, 10, 15, 20).forEach { mins ->
                            FilterChip(
                                selected = waterInterval == mins,
                                onClick = { waterInterval = mins },
                                label = { Text("$mins min") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonBlue,
                                    selectedLabelColor = PureBlack,
                                    containerColor = BlackSurfaceElevated,
                                    labelColor = TextWhiteSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Button(
                        onClick = onTriggerWaterPopup,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("test_water_popup_button"),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonBlue)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocalDrink,
                            contentDescription = null,
                            tint = PureBlack,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Testar Popup de Hidratação",
                            fontWeight = FontWeight.Bold,
                            color = PureBlack
                        )
                    }
                }
            }

            // 4. DADOS BIOMÉTRICOS & IMC
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BlackBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Dados Corporais & Biometria",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nome do Atleta", color = TextWhiteSecondary) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextWhitePrimary,
                            unfocusedTextColor = TextWhitePrimary,
                            focusedBorderColor = NeonRed,
                            unfocusedBorderColor = BlackBorder
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedTextField(
                            value = weightStr,
                            onValueChange = { weightStr = it },
                            label = { Text("Peso (kg)", color = TextWhiteSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedBorderColor = NeonRed,
                                unfocusedBorderColor = BlackBorder
                            )
                        )

                        OutlinedTextField(
                            value = heightStr,
                            onValueChange = { heightStr = it },
                            label = { Text("Altura (cm)", color = TextWhiteSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedBorderColor = NeonRed,
                                unfocusedBorderColor = BlackBorder
                            )
                        )

                        OutlinedTextField(
                            value = ageStr,
                            onValueChange = { ageStr = it },
                            label = { Text("Idade", color = TextWhiteSecondary) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.weight(0.8f),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhitePrimary,
                                unfocusedTextColor = TextWhitePrimary,
                                focusedBorderColor = NeonRed,
                                unfocusedBorderColor = BlackBorder
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // IMC Badge Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = PureBlack),
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
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
                                    text = "IMC Calculado",
                                    style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                                )
                                Text(
                                    text = "$previewBmi",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        color = bmiCategoryColor
                                    )
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(bmiCategoryColor.copy(alpha = 0.2f))
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = bmiCategoryLabel,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = bmiCategoryColor
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 5. LOCAL DE TREINO & OBJETIVO
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BlackBorder, RoundedCornerShape(20.dp)),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Local de Treino",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        LocalLocationOptionCard(
                            title = "Academia",
                            subtitle = "Aparelhos, pesos livres e barras",
                            icon = Icons.Default.FitnessCenter,
                            isSelected = workoutLocation == "Academia" || workoutLocation.equals("Gym", ignoreCase = true),
                            onClick = { workoutLocation = "Academia" },
                            modifier = Modifier.weight(1f)
                        )

                        LocalLocationOptionCard(
                            title = "Em Casa",
                            subtitle = "Peso corporal e calistenia",
                            icon = Icons.Default.Home,
                            isSelected = workoutLocation == "Casa" || workoutLocation.equals("Home", ignoreCase = true),
                            onClick = { workoutLocation = "Casa" },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Nível de Treino",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf("Iniciante", "Intermediário", "Avançado").forEach { lvl ->
                            FilterChip(
                                selected = fitnessLevel.equals(lvl, ignoreCase = true) ||
                                        (lvl == "Iniciante" && fitnessLevel == "Beginner") ||
                                        (lvl == "Intermediário" && fitnessLevel == "Intermediate") ||
                                        (lvl == "Avançado" && fitnessLevel == "Advanced"),
                                onClick = { fitnessLevel = lvl },
                                label = { Text(lvl) },
                                modifier = Modifier.weight(1f),
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonRed,
                                    selectedLabelColor = TextWhitePrimary,
                                    containerColor = BlackSurfaceElevated,
                                    labelColor = TextWhiteSecondary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Text(
                        text = "Objetivo Principal",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        LocalGoalSelectionRow(
                            title = "Ganho de Massa",
                            description = "Hipertrofia muscular com foco em volume e cargas progressivas",
                            icon = Icons.Default.FitnessCenter,
                            isSelected = fitnessGoal.contains("Massa", ignoreCase = true) || fitnessGoal == "Muscle Gain",
                            accentColor = NeonRed,
                            onClick = { fitnessGoal = "Ganho de Massa" }
                        )

                        LocalGoalSelectionRow(
                            title = "Perda de Peso",
                            description = "Déficit calórico, circuitos dinâmicos e queima metabólica",
                            icon = Icons.Default.FlashOn,
                            isSelected = fitnessGoal.contains("Perda", ignoreCase = true) || fitnessGoal == "Weight Loss",
                            accentColor = NeonOrange,
                            onClick = { fitnessGoal = "Perda de Peso" }
                        )

                        LocalGoalSelectionRow(
                            title = "Força Pura",
                            description = "Ganhos em levantamento com menor repetição e alta carga",
                            icon = Icons.Default.Shield,
                            isSelected = fitnessGoal.contains("Força", ignoreCase = true) || fitnessGoal == "Strength",
                            accentColor = NeonRed,
                            onClick = { fitnessGoal = "Força" }
                        )

                        LocalGoalSelectionRow(
                            title = "Resistência",
                            description = "Fôlego, capacidade aeróbica e resistência ao ácido lático",
                            icon = Icons.AutoMirrored.Filled.DirectionsRun,
                            isSelected = fitnessGoal.contains("Resistência", ignoreCase = true) || fitnessGoal == "Endurance",
                            accentColor = NeonOrange,
                            onClick = { fitnessGoal = "Resistência" }
                        )
                    }
                }
            }

            // BOTÃO SALVAR
            Button(
                onClick = {
                    val updated = userProfile.copy(
                        name = name.ifBlank { "Atleta" },
                        age = ageStr.toIntOrNull() ?: userProfile.age,
                        weightKg = weightNum,
                        heightCm = heightNum,
                        gender = gender,
                        fitnessLevel = fitnessLevel,
                        workoutLocation = workoutLocation,
                        fitnessGoal = fitnessGoal,
                        gymName = gymName,
                        gymMembershipFee = gymFee,
                        gymMembershipDueDay = gymDueDay,
                        gymMembershipStatus = gymStatus,
                        gymMembershipReminderEnabled = gymReminderEnabled,
                        gymAlarmHour = gymHour,
                        gymAlarmMinute = gymMinute,
                        gymAlarmDays = gymDays,
                        gymAlarmEnabled = gymAlarmEnabled,
                        waterReminderIntervalMinutes = waterInterval,
                        waterReminderEnabled = waterEnabled,
                        adminPin = adminPin.filter { it.isDigit() }.take(6).ifBlank { "123456" }
                    )
                    onSaveProfile(updated)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .testTag("save_profile_button"),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                enabled = !isSaving
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = TextWhitePrimary,
                        strokeWidth = 2.5.dp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Recalibrando Plano de Treino...",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = TextWhitePrimary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Salvar e Atualizar Treinos",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = TextWhitePrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        if (showDatePicker) {
            DigitalDueDatePickerDialog(
                initialDay = gymDueDay,
                onDaySelected = { day ->
                    gymDueDay = day
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@Composable
private fun LocalLocationOptionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) NeonOrange.copy(alpha = 0.15f) else BlackSurfaceElevated
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, NeonOrange) else androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = if (isSelected) NeonOrange else TextWhiteSecondary,
                modifier = Modifier.size(28.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    color = if (isSelected) NeonOrange else TextWhitePrimary
                )
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteMuted)
            )
        }
    }
}

@Composable
private fun LocalGoalSelectionRow(
    title: String,
    description: String,
    icon: ImageVector,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("goal_card_$title"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) accentColor.copy(alpha = 0.15f) else BlackSurfaceElevated
        ),
        border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, accentColor) else androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(accentColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = if (isSelected) accentColor else TextWhitePrimary
                    )
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                )
            }

            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Selecionado",
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}
