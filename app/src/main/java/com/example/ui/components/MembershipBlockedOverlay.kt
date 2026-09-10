package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.UserProfile
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.BlackSurfaceCard
import com.example.ui.theme.BlackSurfaceElevated
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MembershipBlockedOverlay(
    userProfile: UserProfile,
    onUnlockWithPin: (String) -> Boolean,
    onContactAdmin: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var enteredPin by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    val dateFormatter = remember { SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("pt", "BR")) }
    val lastPaymentFormatted = remember(userProfile.lastPaymentDateMillis) {
        if (userProfile.lastPaymentDateMillis > 0) {
            dateFormatter.format(Date(userProfile.lastPaymentDateMillis))
        } else {
            "Nenhum pagamento anterior registrado"
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_lock")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_lock_scale"
    )

    // Non-dismissible full-screen barrier
    Dialog(
        onDismissRequest = { /* Bloqueio estrito: não permite fechar sem a senha do Admin */ },
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(PureBlack.copy(alpha = 0.98f))
                .testTag("membership_blocked_screen"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .padding(16.dp)
                    .border(2.dp, NeonRed, RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = BlackSurfaceCard)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Logo Ampla Fitness Header
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.5.dp, NeonRed),
                            color = PureBlack,
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Text(
                                text = "AMPLA",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = TextWhitePrimary,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                        Text(
                            text = "FITNESS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = NeonRed,
                            letterSpacing = 2.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pulsing Lock Icon
                    Box(
                        modifier = Modifier
                            .scale(pulseScale)
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(NeonRedGlow),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Acesso Bloqueado",
                            tint = NeonRed,
                            modifier = Modifier.size(40.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = "ACESSO BLOQUEADO",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Black,
                            color = NeonRed,
                            letterSpacing = 1.sp
                        )
                    )

                    Text(
                        text = "Mensalidade Pendente de Validação",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = TextWhitePrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "O acesso aos treinos foi suspenso para o aluno ${userProfile.name}. Para liberar o aplicativo, valide seu pagamento na recepção da ${userProfile.gymName}.",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextWhiteSecondary,
                            textAlign = TextAlign.Center
                        ),
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Quadro de Dados do Aluno & Último Pagamento Anterior
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = BlackSurfaceElevated,
                        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = null,
                                        tint = NeonOrange,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Último Pagamento Anterior:",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextWhiteSecondary
                                        )
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = lastPaymentFormatted,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = NeonOrange
                                )
                            )

                            HorizontalDivider(
                                color = BlackBorder,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Dia de Vencimento",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                                    )
                                    Text(
                                        text = "Todo dia ${userProfile.gymMembershipDueDay}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhitePrimary
                                        )
                                    )
                                }

                                Column(horizontalAlignment = Alignment.End) {
                                    Text(
                                        text = "Valor Mensal",
                                        style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted)
                                    )
                                    Text(
                                        text = "R$ ${userProfile.gymMembershipFee}",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = NeonRed
                                        )
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // Seção de Senha do Administrador (6 dígitos)
                    Text(
                        text = "DESBLOQUEIO DO ADMINISTRADOR",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = NeonOrange,
                            letterSpacing = 1.sp
                        )
                    )

                    Text(
                        text = "Apenas o Administrador da academia pode inserir a senha de 6 dígitos:",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = TextWhiteSecondary,
                            textAlign = TextAlign.Center
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Indicadores dos 6 dígitos (● ● ● ● ● ●)
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 0 until 6) {
                            val isFilled = i < enteredPin.length
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clip(CircleShape)
                                    .background(
                                        when {
                                            isSuccess -> NeonGreen
                                            isFilled -> NeonRed
                                            else -> BlackSurfaceElevated
                                        }
                                    )
                                    .border(
                                        width = 1.5.dp,
                                        color = if (isFilled || isSuccess) NeonRed else BlackBorder,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }

                    // Mensagem de Erro ou Sucesso
                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Erro",
                                tint = NeonRed,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = errorMessage ?: "",
                                color = NeonRed,
                                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    if (isSuccess) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Sucesso",
                                tint = NeonGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Acesso Liberado com Sucesso!",
                                color = NeonGreen,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Teclado Numérico (Keypad 1-9, 0, Backspace, Limpar)
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        val rows = listOf(
                            listOf("1", "2", "3"),
                            listOf("4", "5", "6"),
                            listOf("7", "8", "9"),
                            listOf("C", "0", "⌫")
                        )

                        for (row in rows) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (key in row) {
                                    KeypadButton(
                                        text = key,
                                        modifier = Modifier.weight(1f),
                                        onClick = {
                                            errorMessage = null
                                            when (key) {
                                                "C" -> enteredPin = ""
                                                "⌫" -> {
                                                    if (enteredPin.isNotEmpty()) {
                                                        enteredPin = enteredPin.dropLast(1)
                                                    }
                                                }
                                                else -> {
                                                    if (enteredPin.length < 6) {
                                                        val updated = enteredPin + key
                                                        enteredPin = updated
                                                        if (updated.length == 6) {
                                                            val success = onUnlockWithPin(updated)
                                                            if (success) {
                                                                isSuccess = true
                                                                errorMessage = null
                                                            } else {
                                                                errorMessage = "Senha incorreta! Digite os 6 dígitos do Administrador."
                                                                enteredPin = ""
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botão de Contato com Recepção
                    OutlinedButton(
                        onClick = onContactAdmin,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, NeonOrange),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonOrange),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = "Contato Recepção",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Falar com a Recepção (${userProfile.adminContactPhone})",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Senha padrão inicial do Administrador: 123456",
                        style = MaterialTheme.typography.labelSmall.copy(color = TextWhiteMuted),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = BlackSurfaceElevated,
        border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
        modifier = modifier
            .height(48.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (text) {
                "⌫" -> {
                    Icon(
                        imageVector = Icons.Default.Backspace,
                        contentDescription = "Apagar",
                        tint = NeonRed,
                        modifier = Modifier.size(20.dp)
                    )
                }
                "C" -> {
                    Text(
                        text = "LIMPAR",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhiteSecondary
                    )
                }
                else -> {
                    Text(
                        text = text,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextWhitePrimary
                    )
                }
            }
        }
    }
}
