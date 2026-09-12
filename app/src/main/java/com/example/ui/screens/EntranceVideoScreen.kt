package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary
import kotlinx.coroutines.delay

@Composable
fun EntranceVideoScreen(
    onEnterApp: () -> Unit,
    modifier: Modifier = Modifier
) {
    var progress by remember { mutableFloatStateOf(0f) }

    // Duração do vídeo de introdução: ~7 segundos com transição suave
    LaunchedEffect(Unit) {
        val totalDurationMs = 6500L
        val intervalMs = 50L
        val step = intervalMs.toFloat() / totalDurationMs.toFloat()
        while (progress < 1f) {
            delay(intervalMs)
            progress = (progress + step).coerceAtMost(1f)
        }
        delay(300)
        onEnterApp()
    }

    // Animação de respiração e elevação do halterofilista
    val infiniteTransition = rememberInfiniteTransition(label = "video_movement")
    val heroScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hero_scale"
    )

    val verticalPressOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = -12f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "press_motion"
    )

    val glowAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.85f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow_alpha"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(PureBlack)
            .testTag("entrance_video_screen")
    ) {
        // Camada de Imagem de Fundo (Mulher e Homem frente a frente malhando com halteres)
        Image(
            painter = painterResource(id = R.drawable.img_ampla_entrance_duo),
            contentDescription = "Mulher e homem frente a frente treinando com halteres na Ampla Fitness",
            modifier = Modifier
                .fillMaxSize()
                .scale(heroScale)
                .offset(y = verticalPressOffset.dp)
                .alpha(0.90f),
            contentScale = ContentScale.Crop
        )

        // Degradê Escuro Superior e Inferior para legibilidade impecável
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            PureBlack.copy(alpha = 0.92f),
                            Color.Transparent,
                            PureBlack.copy(alpha = 0.75f),
                            PureBlack.copy(alpha = 0.98f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // Efeito de Brilho Vermelho Neon da base
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(280.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            NeonRed.copy(alpha = 0.15f * glowAlpha),
                            NeonRed.copy(alpha = 0.35f * glowAlpha)
                        )
                    )
                )
        )

        // Conteúdo Superior (Logo Ampla Fitness do Vídeo)
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 28.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // "ACADEMIA"
            Text(
                text = "ACADEMIA",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhiteSecondary,
                letterSpacing = 4.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Caixa Estilizada de "AMPLA" com os colchetes vermelhos do logo
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Colchete Vermelho Superior Esquerdo ┏
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(2.dp, NeonRed),
                    color = PureBlack.copy(alpha = 0.8f),
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = "AMPLA",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = TextWhitePrimary,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // "FITNESS"
            Text(
                text = "FITNESS",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = NeonRed,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Frase do Vídeo: "FOCO NO SEU OBJETIVO"
            Surface(
                shape = RoundedCornerShape(30.dp),
                color = PureBlack.copy(alpha = 0.7f),
                border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder)
            ) {
                Text(
                    text = "FOCO NO SEU OBJETIVO",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = TextWhitePrimary,
                    letterSpacing = 2.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp)
                )
            }
        }

        // Conteúdo Inferior: "VEM SER AMPLA" e Controles
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Texto do vídeo "VEM SER"
            Text(
                text = "VEM SER",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextWhitePrimary,
                letterSpacing = 4.sp
            )

            // "AMPLA" com contorno vermelho neon brilhante
            Text(
                text = "AMPLA",
                fontSize = 44.sp,
                fontWeight = FontWeight.Black,
                color = NeonRed,
                letterSpacing = 4.sp,
                style = MaterialTheme.typography.displayMedium.copy(
                    shadow = androidx.compose.ui.graphics.Shadow(
                        color = NeonRed,
                        blurRadius = 24f * glowAlpha
                    )
                )
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Barra de Progresso do Vídeo de Entrada
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp)),
                color = NeonRed,
                trackColor = BlackBorder,
                strokeCap = StrokeCap.Round
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Botão Principal para Entrar no App / Pular
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onEnterApp,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BlackBorder),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhiteSecondary),
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Pular Vídeo",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Pular", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onEnterApp,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NeonRed,
                        contentColor = TextWhitePrimary
                    ),
                    modifier = Modifier
                        .weight(1.5f)
                        .testTag("enter_ampla_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Entrar no App",
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Entrar no App", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
