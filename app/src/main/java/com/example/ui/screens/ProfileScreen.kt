package com.example.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalDrink
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.R
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
    onUpdateProfilePhoto: (Uri) -> Unit = {},
    onRemoveProfilePhoto: () -> Unit = {},
    onTriggerMembershipPopup: () -> Unit = {},
    onTriggerAlarmPopup: () -> Unit = {},
    onTriggerWaterPopup: () -> Unit = {},
    onTriggerBlock: () -> Unit = {},
    onReplayEntranceVideo: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onUpdateProfilePhoto(uri)
        }
    }

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
            val context = LocalContext.current
            val onOpenWhatsApp = {
                val phoneNumber = "5571981341942"
                val text = "Olá! Sou aluno(a) da Ampla Fitness."
                val encodedText = Uri.encode(text)
                try {
                    val waIntent = Intent(Intent.ACTION_VIEW).apply {
                        data = Uri.parse("whatsapp://send?phone=$phoneNumber&text=$encodedText")
                        setPackage("com.whatsapp")
                    }
                    context.startActivity(waIntent)
                } catch (_: Exception) {
                    try {
                        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://api.whatsapp.com/send?phone=$phoneNumber&text=$encodedText"))
                        context.startActivity(browserIntent)
                    } catch (_: Exception) {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://wa.me/$phoneNumber")))
                    }
                }
            }

            // Cabeçalho com Ícone do WhatsApp no perfil alto clicável e Botão Salvar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .background(NeonRedGlow, CircleShape)
                            .border(1.5.dp, NeonRed, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = NeonRed,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = "Perfil do Atleta",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontWeight = FontWeight.Black,
                                color = TextWhitePrimary
                            )
                        )
                        Text(
                            text = "Configurações & Suporte",
                            style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                        )
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Ícone do WhatsApp no perfil alto clicável direcionando para o app (71 98134-1942)
                    Button(
                        onClick = onOpenWhatsApp,
                        modifier = Modifier.testTag("top_whatsapp_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF25D366),
                            contentColor = Color.White
                        ),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_whatsapp),
                            contentDescription = "WhatsApp 71 98134-1942",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text(
                            text = "WhatsApp",
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 13.sp
                        )
                    }

                    // Botão de salvar perfil fixado no Topo à Direita
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
                        modifier = Modifier.testTag("top_save_profile_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = NeonGreen),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                        enabled = !isSaving
                    ) {
                        if (isSaving) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                color = TextWhitePrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Salvar Perfil",
                                tint = TextWhitePrimary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Salvar",
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // FOTO DE PERFIL DO ATLETA (Opção de carregar foto solicitada pelo usuário)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.5.dp, NeonRed.copy(alpha = 0.7f), RoundedCornerShape(20.dp))
                    .testTag("profile_picture_card"),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .border(2.dp, NeonRed, CircleShape)
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            }
                            .testTag("profile_photo_avatar_container"),
                        contentAlignment = Alignment.Center
                    ) {
                        if (!userProfile.profilePictureUri.isNullOrBlank()) {
                            AsyncImage(
                                model = userProfile.profilePictureUri,
                                contentDescription = "Foto do perfil",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(NeonRedGlow),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = NeonRed,
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }

                        // Badge de câmera no canto inferior
                        Box(
                            modifier = Modifier
                                .align(Alignment.BottomEnd)
                                .size(26.dp)
                                .clip(CircleShape)
                                .background(NeonRed)
                                .border(1.5.dp, PureBlack, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.PhotoCamera,
                                contentDescription = "Carregar foto",
                                tint = TextWhitePrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Foto do Perfil",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhitePrimary
                            )
                        )
                        Text(
                            text = if (!userProfile.profilePictureUri.isNullOrBlank())
                                "Foto ativa no perfil e na tela de início"
                            else
                                "Carregue sua foto para personalizar o app",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextWhiteMuted,
                                fontSize = 12.sp
                            )
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                modifier = Modifier.testTag("upload_profile_photo_button"),
                                shape = RoundedCornerShape(10.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                    horizontal = 12.dp,
                                    vertical = 6.dp
                                )
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PhotoCamera,
                                    contentDescription = null,
                                    tint = TextWhitePrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (!userProfile.profilePictureUri.isNullOrBlank()) "Trocar Foto" else "Carregar Foto",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = TextWhitePrimary
                                )
                            }

                            if (!userProfile.profilePictureUri.isNullOrBlank()) {
                                OutlinedButton(
                                    onClick = onRemoveProfilePhoto,
                                    modifier = Modifier.testTag("remove_profile_photo_button"),
                                    shape = RoundedCornerShape(10.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, TextWhiteMuted),
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                        horizontal = 10.dp,
                                        vertical = 6.dp
                                    )
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Remover foto",
                                        tint = TextWhiteMuted,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Remover",
                                        fontSize = 12.sp,
                                        color = TextWhiteMuted
                                    )
                                }
                            }
                        }
                    }
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

                    // Data de Vencimento com Campo Numérico Direto e Botão de Calendário
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = "Dia de Vencimento da Mensalidade (1 a 31):",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedTextField(
                                    value = if (gymDueDay > 0) gymDueDay.toString() else "",
                                    onValueChange = { newVal ->
                                        val filtered = newVal.filter { it.isDigit() }
                                        val num = filtered.toIntOrNull()
                                        if (num != null) {
                                            gymDueDay = num.coerceIn(1, 31)
                                        } else if (filtered.isEmpty()) {
                                            gymDueDay = 1
                                        }
                                    },
                                    label = { Text("Dia", color = TextWhiteSecondary) },
                                    placeholder = { Text("Ex: 10", color = TextWhiteMuted) },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    modifier = Modifier
                                        .weight(1f)
                                        .testTag("due_day_direct_input"),
                                    singleLine = true,
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedTextColor = TextWhitePrimary,
                                        unfocusedTextColor = TextWhitePrimary,
                                        focusedBorderColor = NeonRed,
                                        unfocusedBorderColor = BlackBorder
                                    )
                                )

                                Button(
                                    onClick = { showDatePicker = true },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = NeonRed),
                                    modifier = Modifier
                                        .height(54.dp)
                                        .testTag("open_calendar_picker_button")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarMonth,
                                        contentDescription = "Abrir Calendário",
                                        tint = TextWhitePrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Calendário",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = TextWhitePrimary
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Configurado: Todo dia $gymDueDay de cada mês",
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = NeonRed,
                                    fontWeight = FontWeight.SemiBold
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Status da Mensalidade (Organizado abaixo do título e do valor, com layout limpo e sem aperto lateral)
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Status da Mensalidade:",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = TextWhiteSecondary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
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
                            FilterChip(
                                selected = gymStatus == "Pendente",
                                onClick = { gymStatus = "Pendente" },
                                label = { Text("Pendente") },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = NeonOrange.copy(alpha = 0.3f),
                                    selectedLabelColor = NeonOrange,
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
                                    text = "Alertas Visuais Automáticos (Silencioso)",
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhitePrimary
                                    )
                                )
                                Text(
                                    text = if (gymReminderEnabled)
                                        "O aviso de vencimento é exibido na tela no dia $gymDueDay de cada mês de forma visual e silenciosa (sem som)."
                                    else
                                        "Lembretes desativados. Ative a chave acima para receber os avisos na tela.",
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

                    // Campos Diretos e Interativos de Edição de Hora (00-23) e Minutos (00-59)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("alarm_time_interactive_editor")
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Ajustar Horário do Alarme (Hora & Minuto Editáveis):",
                                style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted),
                                modifier = Modifier.align(Alignment.Start)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Coluna de Horas (00-23)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { gymHour = if (gymHour >= 23) 0 else gymHour + 1 },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Aumentar Hora",
                                            tint = NeonOrange
                                        )
                                    }
                                    OutlinedTextField(
                                        value = String.format("%02d", gymHour),
                                        onValueChange = { newVal ->
                                            val filtered = newVal.filter { it.isDigit() }
                                            val num = filtered.toIntOrNull()
                                            if (num != null) {
                                                gymHour = num.coerceIn(0, 23)
                                            } else if (filtered.isEmpty()) {
                                                gymHour = 0
                                            }
                                        },
                                        modifier = Modifier
                                            .width(72.dp)
                                            .testTag("alarm_hour_direct_input"),
                                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            color = NeonOrange
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NeonOrange,
                                            unfocusedTextColor = NeonOrange,
                                            focusedBorderColor = NeonOrange,
                                            unfocusedBorderColor = BlackBorder
                                        )
                                    )
                                    IconButton(
                                        onClick = { gymHour = if (gymHour <= 0) 23 else gymHour - 1 },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Diminuir Hora",
                                            tint = NeonOrange
                                        )
                                    }
                                    Text(
                                        text = "HORA (0-23)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhiteMuted
                                    )
                                }

                                Text(
                                    text = ":",
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Black,
                                    color = NeonOrange,
                                    modifier = Modifier.padding(horizontal = 14.dp)
                                )

                                // Coluna de Minutos (00-59)
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    IconButton(
                                        onClick = { gymMinute = if (gymMinute >= 55) 0 else gymMinute + 5 },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowUp,
                                            contentDescription = "Aumentar Minutos",
                                            tint = NeonOrange
                                        )
                                    }
                                    OutlinedTextField(
                                        value = String.format("%02d", gymMinute),
                                        onValueChange = { newVal ->
                                            val filtered = newVal.filter { it.isDigit() }
                                            val num = filtered.toIntOrNull()
                                            if (num != null) {
                                                gymMinute = num.coerceIn(0, 59)
                                            } else if (filtered.isEmpty()) {
                                                gymMinute = 0
                                            }
                                        },
                                        modifier = Modifier
                                            .width(72.dp)
                                            .testTag("alarm_minute_direct_input"),
                                        textStyle = MaterialTheme.typography.headlineMedium.copy(
                                            fontWeight = FontWeight.Black,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                            color = NeonOrange
                                        ),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        singleLine = true,
                                        colors = OutlinedTextFieldDefaults.colors(
                                            focusedTextColor = NeonOrange,
                                            unfocusedTextColor = NeonOrange,
                                            focusedBorderColor = NeonOrange,
                                            unfocusedBorderColor = BlackBorder
                                        )
                                    )
                                    IconButton(
                                        onClick = { gymMinute = if (gymMinute <= 0) 55 else gymMinute - 5 },
                                        modifier = Modifier.size(34.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.KeyboardArrowDown,
                                            contentDescription = "Diminuir Minutos",
                                            tint = NeonOrange
                                        )
                                    }
                                    Text(
                                        text = "MIN (0-59)",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextWhiteMuted
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Horário Programado: ${String.format("%02d:%02d", gymHour, gymMinute)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhitePrimary
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "Atalhos Rápidos de Horário:",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

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

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "Dias de Treino (Todos os Dias da Semana)",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )
                    Text(
                        text = "Selecione os dias da semana programados para o seu treino:",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextWhiteSecondary)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    val allWeekDays = listOf(
                        "Dom" to "Domingo",
                        "Seg" to "Segunda",
                        "Ter" to "Terça",
                        "Qua" to "Quarta",
                        "Qui" to "Quinta",
                        "Sex" to "Sexta",
                        "Sáb" to "Sábado"
                    )

                    // Presets rápidos de seleção
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val isAllSelected = allWeekDays.all { (abbr, _) ->
                            gymDays.contains(abbr, ignoreCase = true) || gymDays.contains("Todos", ignoreCase = true)
                        }
                        FilterChip(
                            selected = isAllSelected,
                            onClick = {
                                gymDays = "Dom, Seg, Ter, Qua, Qui, Sex, Sáb"
                            },
                            label = { Text("Todos os Dias (7 dias)", fontSize = 11.sp, fontWeight = FontWeight.Bold) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonOrange,
                                selectedLabelColor = TextWhitePrimary,
                                containerColor = BlackSurfaceElevated,
                                labelColor = TextWhiteSecondary
                            )
                        )
                        FilterChip(
                            selected = gymDays == "Seg, Ter, Qua, Qui, Sex",
                            onClick = {
                                gymDays = "Seg, Ter, Qua, Qui, Sex"
                            },
                            label = { Text("Seg a Sex (5d)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonOrange,
                                selectedLabelColor = TextWhitePrimary,
                                containerColor = BlackSurfaceElevated,
                                labelColor = TextWhiteSecondary
                            )
                        )
                        FilterChip(
                            selected = gymDays == "Seg, Ter, Qua, Qui, Sex, Sáb",
                            onClick = {
                                gymDays = "Seg, Ter, Qua, Qui, Sex, Sáb"
                            },
                            label = { Text("Seg a Sáb (6d)", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = NeonOrange,
                                selectedLabelColor = TextWhitePrimary,
                                containerColor = BlackSurfaceElevated,
                                labelColor = TextWhiteSecondary
                            )
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Linha com todos os 7 dias da semana (Dom, Seg, Ter, Qua, Qui, Sex, Sáb)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        allWeekDays.forEach { (abbr, _) ->
                            val isSelected = gymDays.contains(abbr, ignoreCase = true) || gymDays.contains("Todos", ignoreCase = true)
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) NeonOrange else BlackSurfaceElevated,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) NeonOrange else BlackBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .clickable {
                                        val currentList = allWeekDays
                                            .map { it.first }
                                            .filter {
                                                gymDays.contains(it, ignoreCase = true) || gymDays.contains("Todos", ignoreCase = true)
                                            }
                                            .toMutableList()

                                        if (isSelected) {
                                            if (currentList.size > 1) {
                                                currentList.remove(abbr)
                                            }
                                        } else {
                                            if (!currentList.contains(abbr)) {
                                                currentList.add(abbr)
                                            }
                                        }
                                        val ordered = allWeekDays
                                            .map { it.first }
                                            .filter { currentList.contains(it) }
                                        gymDays = ordered.joinToString(", ")
                                    }
                                    .testTag("week_day_chip_$abbr")
                            ) {
                                Column(
                                    modifier = Modifier.padding(vertical = 8.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = abbr,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp,
                                        color = if (isSelected) TextWhitePrimary else TextWhiteSecondary
                                    )
                                    Text(
                                        text = if (abbr == "Dom" || abbr == "Sáb") "FDS" else "Treino",
                                        fontSize = 8.sp,
                                        color = if (isSelected) TextWhitePrimary.copy(alpha = 0.8f) else TextWhiteMuted
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = BlackSurfaceElevated,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = NeonOrange,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Dias ativos: $gymDays",
                                fontSize = 12.sp,
                                color = TextWhitePrimary,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
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
