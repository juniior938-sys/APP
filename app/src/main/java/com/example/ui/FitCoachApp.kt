package com.example.ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Dashboard
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.GymAlarmReminderDialog
import com.example.ui.components.GymPaymentReminderDialog
import com.example.ui.components.MembershipBlockedOverlay
import com.example.ui.components.WaterIntakeReminderDialog
import com.example.ui.screens.ActiveWorkoutScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.EntranceVideoScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.WeeklyPlanScreen
import com.example.ui.theme.BlackBorder
import com.example.ui.theme.BlackSurface
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.NeonRed
import com.example.ui.theme.NeonRedGlow
import com.example.ui.theme.PureBlack
import com.example.ui.theme.TextWhiteMuted
import com.example.ui.theme.TextWhitePrimary
import com.example.ui.theme.TextWhiteSecondary
import kotlinx.coroutines.launch

enum class FitCoachTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val testTag: String
) {
    DASHBOARD("Início", Icons.Filled.Dashboard, Icons.Outlined.Dashboard, "tab_dashboard"),
    PLAN("Plano", Icons.Filled.CalendarMonth, Icons.Outlined.CalendarMonth, "tab_plan"),
    HISTORY("Histórico", Icons.Filled.History, Icons.Outlined.History, "tab_history"),
    PROFILE("Perfil", Icons.Filled.Person, Icons.Outlined.Person, "tab_profile")
}

@Composable
fun FitCoachApp(
    viewModel: FitCoachViewModel = viewModel()
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val weeklyPlan by viewModel.weeklyPlan.collectAsStateWithLifecycle()
    val isGeneratingPlan by viewModel.isGeneratingPlan.collectAsStateWithLifecycle()
    val workoutHistory by viewModel.workoutHistory.collectAsStateWithLifecycle()
    val streakStats by viewModel.streakStats.collectAsStateWithLifecycle()
    val weightLogs by viewModel.weightLogs.collectAsStateWithLifecycle()

    val activeWorkout by viewModel.activeWorkout.collectAsStateWithLifecycle()
    val activeExercises by viewModel.activeExercisesState.collectAsStateWithLifecycle()
    val restTimerSeconds by viewModel.restTimerSeconds.collectAsStateWithLifecycle()
    val totalRestDuration by viewModel.totalRestDuration.collectAsStateWithLifecycle()
    val isTimerRunning by viewModel.isTimerRunning.collectAsStateWithLifecycle()
    val workoutElapsedSeconds by viewModel.workoutElapsedSeconds.collectAsStateWithLifecycle()
    val waterConsumedMl by viewModel.waterConsumedMl.collectAsStateWithLifecycle()

    // Estados dos Popups solicitados pelo usuário
    val showWaterReminderPopup by viewModel.showWaterReminderPopup.collectAsStateWithLifecycle()
    val showGymAlarmPopup by viewModel.showGymAlarmPopup.collectAsStateWithLifecycle()
    val showMembershipPopup by viewModel.showMembershipPopup.collectAsStateWithLifecycle()

    // Vídeo de Entrada e Bloqueio de Mensalidade
    val showEntranceVideo by viewModel.showEntranceVideo.collectAsStateWithLifecycle()
    val isMembershipBlocked by viewModel.isMembershipBlocked.collectAsStateWithLifecycle()

    // Chat Ampla Personal IA
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    val isChatLoading by viewModel.isChatLoading.collectAsStateWithLifecycle()

    var currentTab by remember { mutableStateOf(FitCoachTab.DASHBOARD) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Se o vídeo de entrada estiver ativo, exibe o vídeo estilizado em tela cheia
    if (showEntranceVideo) {
        EntranceVideoScreen(
            onEnterApp = { viewModel.dismissEntranceVideo() }
        )
        return
    }

    // Se a mensalidade estiver bloqueada, exibe a tela de bloqueio com teclado numérico de 6 dígitos
    if (isMembershipBlocked) {
        MembershipBlockedOverlay(
            userProfile = userProfile,
            onUnlockWithPin = { pin ->
                viewModel.unblockWithAdminPin(pin)
            },
            onContactAdmin = {
                scope.launch {
                    snackbarHostState.showSnackbar("Contate a recepção da ${userProfile.gymName} para auxílio.")
                }
            }
        )
    }

    // 1. Popup de Lembrete de Pagamento de Mensalidade da Academia
    if (showMembershipPopup) {
        GymPaymentReminderDialog(
            profile = userProfile,
            onConfirmPayment = {
                viewModel.markGymMembershipPaid()
                scope.launch {
                    snackbarHostState.showSnackbar("Mensalidade marcada como paga! Tudo em dia na ${userProfile.gymName}.")
                }
            },
            onDismiss = { viewModel.dismissMembershipPopup() }
        )
    }

    // 2. Popup de Alarme / Horário da Academia
    if (showGymAlarmPopup) {
        GymAlarmReminderDialog(
            profile = userProfile,
            onStartWorkout = {
                viewModel.dismissGymAlarm()
                val nextWorkout = weeklyPlan.firstOrNull { !it.isRestDay && !it.isCompletedThisWeek }
                    ?: weeklyPlan.firstOrNull { !it.isRestDay }
                    ?: weeklyPlan.firstOrNull()
                if (nextWorkout != null) {
                    viewModel.startWorkoutSession(nextWorkout)
                }
            },
            onSnooze = {
                viewModel.snoozeGymAlarm()
                scope.launch {
                    snackbarHostState.showSnackbar("Alarme adiado por 10 minutos.")
                }
            },
            onDismiss = { viewModel.dismissGymAlarm() }
        )
    }

    // 3. Popup de Beber Água durante o treino
    if (showWaterReminderPopup) {
        WaterIntakeReminderDialog(
            consumedMl = waterConsumedMl,
            onDrinkWater = { ml ->
                viewModel.drinkWater(ml)
                scope.launch {
                    snackbarHostState.showSnackbar("Ótimo! +$ml ml de água registrados.")
                }
            },
            onDismiss = { viewModel.dismissWaterReminderPopup() }
        )
    }

    // Se o treino estiver ativo, exibe a tela ActiveWorkoutScreen
    val currentActiveWorkout = activeWorkout
    if (currentActiveWorkout != null) {
        ActiveWorkoutScreen(
            workoutDay = currentActiveWorkout,
            exercises = activeExercises,
            restTimerSeconds = restTimerSeconds,
            totalRestDuration = totalRestDuration,
            isTimerRunning = isTimerRunning,
            waterConsumedMl = waterConsumedMl,
            workoutElapsedSeconds = workoutElapsedSeconds,
            onToggleExercise = { id -> viewModel.toggleExerciseCompleted(id) },
            onPlayPauseTimer = { viewModel.pauseResumeTimer() },
            onAdd30sTimer = { viewModel.addTimerSeconds(30) },
            onResetTimer = { viewModel.resetRestTimer() },
            onDrinkWater = { ml -> viewModel.drinkWater(ml) },
            onTriggerWaterReminder = { viewModel.triggerWaterReminderNow() },
            onFinishWorkout = { notes ->
                viewModel.completeWorkoutSession(notes)
                scope.launch {
                    snackbarHostState.showSnackbar("Treino registrado com sucesso! Sua sequência de dias seguidos aumentou.")
                }
            },
            onClose = { viewModel.dismissActiveWorkout() }
        )
    } else {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = PureBlack,
            bottomBar = {
                NavigationBar(
                    containerColor = BlackSurface,
                    modifier = Modifier
                        .navigationBarsPadding()
                        .border(1.dp, BlackBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                        .testTag("bottom_navigation_bar")
                ) {
                    FitCoachTab.values().forEach { tab ->
                        val isSelected = currentTab == tab
                        NavigationBarItem(
                            selected = isSelected,
                            onClick = { currentTab = tab },
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                    contentDescription = tab.title
                                )
                            },
                            label = {
                                Text(
                                    text = tab.title,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            modifier = Modifier.testTag(tab.testTag),
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = NeonRed,
                                selectedTextColor = NeonRed,
                                indicatorColor = NeonRedGlow,
                                unselectedIconColor = TextWhiteMuted,
                                unselectedTextColor = TextWhiteMuted
                            )
                        )
                    }
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "tab_transition"
                ) { targetTab ->
                    when (targetTab) {
                        FitCoachTab.DASHBOARD -> DashboardScreen(
                            userProfile = userProfile,
                            streakStats = streakStats,
                            weeklyPlan = weeklyPlan,
                            chatMessages = chatMessages,
                            isChatLoading = isChatLoading,
                            workoutHistory = workoutHistory,
                            weightLogs = weightLogs,
                            onSendMessage = { text -> viewModel.sendChatMessage(text) },
                            onClearChat = { viewModel.clearChatHistory() },
                            onStartWorkout = { day -> viewModel.startWorkoutSession(day) },
                            onLogWeight = { weight ->
                                viewModel.logWeight(weight)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Peso registrado: $weight kg. IMC recalculado!")
                                }
                            },
                            onLogQuickActivity = { title, duration, calories, category ->
                                viewModel.logQuickActivity(title, duration, calories, category)
                                scope.launch {
                                    snackbarHostState.showSnackbar("🔥 $title registrado! +$calories kcal queimadas.")
                                }
                            },
                            onNavigateToPlan = { currentTab = FitCoachTab.PLAN },
                            onNavigateToProfile = { currentTab = FitCoachTab.PROFILE },
                            onTriggerAlarmPopup = { viewModel.triggerGymAlarmNow() },
                            onTriggerMembershipPopup = { viewModel.triggerMembershipReminderNow() }
                        )

                        FitCoachTab.PLAN -> WeeklyPlanScreen(
                            userProfile = userProfile,
                            weeklyPlan = weeklyPlan,
                            isGenerating = isGeneratingPlan,
                            onStartWorkout = { day -> viewModel.startWorkoutSession(day) },
                            onRegeneratePlan = {
                                viewModel.regeneratePlan()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Plano semanal recalibrado com sucesso!")
                                }
                            }
                        )

                        FitCoachTab.HISTORY -> HistoryScreen(
                            workoutHistory = workoutHistory,
                            weightLogs = weightLogs,
                            streakStats = streakStats,
                            onDeleteHistoryItem = { id -> viewModel.deleteHistoryItem(id) },
                            onStartWorkoutClick = { currentTab = FitCoachTab.DASHBOARD }
                        )

                        FitCoachTab.PROFILE -> ProfileScreen(
                            userProfile = userProfile,
                            isSaving = isGeneratingPlan,
                            onSaveProfile = { updated ->
                                viewModel.updateProfile(updated, regeneratePlanNow = true)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Perfil salvo e plano de treinos atualizado!")
                                }
                                currentTab = FitCoachTab.DASHBOARD
                            },
                            onUpdateProfilePhoto = { uri ->
                                viewModel.updateProfilePicture(uri)
                                scope.launch {
                                    snackbarHostState.showSnackbar("Foto de perfil atualizada com sucesso!")
                                }
                            },
                            onRemoveProfilePhoto = {
                                viewModel.removeProfilePicture()
                                scope.launch {
                                    snackbarHostState.showSnackbar("Foto de perfil removida.")
                                }
                            },
                            onTriggerMembershipPopup = { viewModel.triggerMembershipReminderNow() },
                            onTriggerAlarmPopup = { viewModel.triggerGymAlarmNow() },
                            onTriggerWaterPopup = { viewModel.triggerWaterReminderNow() },
                            onTriggerBlock = { viewModel.blockMembershipManually() },
                            onReplayEntranceVideo = { viewModel.replayEntranceVideo() }
                        )
                    }
                }
            }
        }
    }
}
