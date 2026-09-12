package com.example.ui

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.Ringtone
import android.media.RingtoneManager
import android.media.ToneGenerator
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutExercise
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.repository.FitnessRepository
import com.example.data.repository.GeminiChatRepository
import com.example.data.repository.StreakStats
import com.example.domain.CoachEngine
import com.example.util.NotificationHelper
import java.io.File
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FitCoachViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = FitnessRepository(db.userDao(), db.workoutDao())

    val userProfile: StateFlow<UserProfile> = repository.userProfileFlow
        .combine(MutableStateFlow(Unit)) { profile, _ ->
            profile ?: UserProfile()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserProfile()
        )

    private val _weeklyPlan = MutableStateFlow<List<WorkoutDay>>(emptyList())
    val weeklyPlan: StateFlow<List<WorkoutDay>> = _weeklyPlan.asStateFlow()

    private val _isGeneratingPlan = MutableStateFlow(false)
    val isGeneratingPlan: StateFlow<Boolean> = _isGeneratingPlan.asStateFlow()

    val workoutHistory: StateFlow<List<WorkoutHistoryEntity>> = repository.workoutHistoryFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val streakStats: StateFlow<StreakStats> = combine(
        workoutHistory,
        userProfile
    ) { history, profile ->
        repository.computeStreakStats(history, profile.fitnessLevel)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = StreakStats()
    )

    val weightLogs: StateFlow<List<WeightLogEntity>> = repository.weightLogsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _activeWorkout = MutableStateFlow<WorkoutDay?>(null)
    val activeWorkout: StateFlow<WorkoutDay?> = _activeWorkout.asStateFlow()

    private val _activeExercisesState = MutableStateFlow<List<WorkoutExercise>>(emptyList())
    val activeExercisesState: StateFlow<List<WorkoutExercise>> = _activeExercisesState.asStateFlow()

    private val _restTimerSeconds = MutableStateFlow(0)
    val restTimerSeconds: StateFlow<Int> = _restTimerSeconds.asStateFlow()

    private val _totalRestDuration = MutableStateFlow(0)
    val totalRestDuration: StateFlow<Int> = _totalRestDuration.asStateFlow()

    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning: StateFlow<Boolean> = _isTimerRunning.asStateFlow()

    private var timerJob: Job? = null
    private var workoutElapsedJob: Job? = null

    // Water tracking & active session duration
    private val _waterConsumedMl = MutableStateFlow(0)
    val waterConsumedMl: StateFlow<Int> = _waterConsumedMl.asStateFlow()

    private val _workoutElapsedSeconds = MutableStateFlow(0)
    val workoutElapsedSeconds: StateFlow<Int> = _workoutElapsedSeconds.asStateFlow()

    // Popups requested by user
    private val _showWaterReminderPopup = MutableStateFlow(false)
    val showWaterReminderPopup: StateFlow<Boolean> = _showWaterReminderPopup.asStateFlow()

    private val _showGymAlarmPopup = MutableStateFlow(false)
    val showGymAlarmPopup: StateFlow<Boolean> = _showGymAlarmPopup.asStateFlow()

    private val _showMembershipPopup = MutableStateFlow(false)
    val showMembershipPopup: StateFlow<Boolean> = _showMembershipPopup.asStateFlow()

    // Vídeo de Entrada da Academia Ampla Fitness
    private val _showEntranceVideo = MutableStateFlow(true)
    val showEntranceVideo: StateFlow<Boolean> = _showEntranceVideo.asStateFlow()

    // Bloqueio de Mensalidade (só ativa após o primeiro cadastro)
    private val _isMembershipBlocked = MutableStateFlow(false)
    val isMembershipBlocked: StateFlow<Boolean> = _isMembershipBlocked.asStateFlow()

    // Chat Ampla Personal IA (Gemini 3.5 Flash)
    private val geminiChatRepository = GeminiChatRepository()

    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Olá! Sou o Ampla Personal IA, seu assistente oficial da Academia Ampla. Posso tirar dúvidas sobre postura correta dos exercícios, horários de treino, dicas de desempenho, plano de alimentação, dietas e recuperação muscular. Como posso te orientar hoje?",
                isUser = false
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _isChatLoading = MutableStateFlow(false)
    val isChatLoading: StateFlow<Boolean> = _isChatLoading.asStateFlow()

    private var activeRingtone: Ringtone? = null
    private var stopSoundJob: Job? = null
    private var hasShownAutomaticMembershipThisSession = false
    private var lastAlarmSlotKey: String = ""

    init {
        loadInitialData()
        startGymAlarmClockObserver()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            val profile = repository.getUserProfile()
            val plan = repository.getParsedWeeklyPlan()
            _weeklyPlan.value = plan
            // Verifica bloqueio de mensalidade após primeiro cadastro
            if (profile.isFirstSetupDone && (profile.isMembershipBlocked || profile.gymMembershipStatus == "Bloqueado")) {
                _isMembershipBlocked.value = true
            }
            checkAutomaticMembershipReminder(profile)
        }
    }

    private fun startGymAlarmClockObserver() {
        viewModelScope.launch {
            while (true) {
                delay(25_000L) // Checa o relógio a cada 25 segundos
                val prof = userProfile.value
                if (prof.gymAlarmEnabled) {
                    val cal = java.util.Calendar.getInstance()
                    val currentHour = cal.get(java.util.Calendar.HOUR_OF_DAY)
                    val currentMinute = cal.get(java.util.Calendar.MINUTE)
                    val dayOfWeek = cal.get(java.util.Calendar.DAY_OF_WEEK)
                    val dayAbbr = when (dayOfWeek) {
                        java.util.Calendar.SUNDAY -> "Dom"
                        java.util.Calendar.MONDAY -> "Seg"
                        java.util.Calendar.TUESDAY -> "Ter"
                        java.util.Calendar.WEDNESDAY -> "Qua"
                        java.util.Calendar.THURSDAY -> "Qui"
                        java.util.Calendar.FRIDAY -> "Sex"
                        java.util.Calendar.SATURDAY -> "Sáb"
                        else -> ""
                    }

                    val currentSlotKey = "${cal.get(java.util.Calendar.YEAR)}-${cal.get(java.util.Calendar.DAY_OF_YEAR)}-$currentHour-$currentMinute"
                    if (currentHour == prof.gymAlarmHour && currentMinute == prof.gymAlarmMinute && currentSlotKey != lastAlarmSlotKey) {
                        val daysActive = prof.gymAlarmDays
                        if (daysActive.contains(dayAbbr, ignoreCase = true) || daysActive.contains("Todos", ignoreCase = true)) {
                            lastAlarmSlotKey = currentSlotKey
                            _showGymAlarmPopup.value = true
                            // Alerta automático visual sem som sonoro
                            vibrateDevice(200)
                        }
                    }
                }
            }
        }
    }

    fun updateProfile(newProfile: UserProfile, regeneratePlanNow: Boolean = true) {
        viewModelScope.launch {
            _isGeneratingPlan.value = true
            repository.saveUserProfile(newProfile.copy(hasCompletedOnboarding = true))
            if (newProfile.isFirstSetupDone && (newProfile.isMembershipBlocked || newProfile.gymMembershipStatus == "Bloqueado")) {
                _isMembershipBlocked.value = true
            } else {
                _isMembershipBlocked.value = false
            }
            if (regeneratePlanNow) {
                delay(400) // Smooth UX feedback
                val updatedPlan = repository.regeneratePlan(newProfile)
                _weeklyPlan.value = updatedPlan
            }
            _isGeneratingPlan.value = false
        }
    }

    fun regeneratePlan() {
        viewModelScope.launch {
            _isGeneratingPlan.value = true
            val profile = repository.getUserProfile()
            delay(500)
            val updatedPlan = repository.regeneratePlan(profile)
            _weeklyPlan.value = updatedPlan
            _isGeneratingPlan.value = false
        }
    }

    fun startWorkoutSession(day: WorkoutDay) {
        _activeWorkout.value = day
        _activeExercisesState.value = day.exercises.map { it.copy(isCompleted = false) }
        _waterConsumedMl.value = 0
        _workoutElapsedSeconds.value = 0
        resetRestTimer()

        // Start tracking elapsed workout duration & periodic water reminder
        workoutElapsedJob?.cancel()
        workoutElapsedJob = viewModelScope.launch {
            val intervalSec = (userProfile.value.waterReminderIntervalMinutes.coerceAtLeast(3)) * 60
            while (_activeWorkout.value != null) {
                delay(1000)
                _workoutElapsedSeconds.value = _workoutElapsedSeconds.value + 1
                if (userProfile.value.waterReminderEnabled &&
                    _workoutElapsedSeconds.value > 0 &&
                    _workoutElapsedSeconds.value % intervalSec == 0
                ) {
                    _showWaterReminderPopup.value = true
                    vibrateDevice(150)
                }
            }
        }
    }

    fun drinkWater(ml: Int = 250) {
        stopAlarmSound()
        _waterConsumedMl.value = _waterConsumedMl.value + ml
        _showWaterReminderPopup.value = false
        vibrateDevice(150)
    }

    fun dismissWaterReminderPopup() {
        stopAlarmSound()
        _showWaterReminderPopup.value = false
    }

    fun triggerWaterReminderManual(forceTest: Boolean = false) {
        if (!forceTest && !userProfile.value.waterReminderEnabled) return
        _showWaterReminderPopup.value = true
        vibrateDevice(150)
    }

    fun triggerWaterReminderNow(forceTest: Boolean = false) {
        triggerWaterReminderManual(forceTest)
    }

    fun triggerGymAlarm(forceTest: Boolean = false) {
        if (!forceTest && !userProfile.value.gymAlarmEnabled) return
        _showGymAlarmPopup.value = true
        vibrateDevice(200)
    }

    fun triggerGymAlarmNow(forceTest: Boolean = false) {
        triggerGymAlarm(forceTest)
    }

    fun dismissGymAlarm() {
        stopAlarmSound()
        _showGymAlarmPopup.value = false
    }

    fun snoozeGymAlarm(minutes: Int = 10) {
        stopAlarmSound()
        _showGymAlarmPopup.value = false
        if (!userProfile.value.gymAlarmEnabled) return
        viewModelScope.launch {
            delay(minutes * 60 * 1000L)
            if (userProfile.value.gymAlarmEnabled) {
                _showGymAlarmPopup.value = true
                vibrateDevice(150)
            }
        }
    }

    fun dismissEntranceVideo() {
        _showEntranceVideo.value = false
        checkAutomaticMembershipReminder()
    }

    fun replayEntranceVideo() {
        _showEntranceVideo.value = true
    }

    fun checkAutomaticMembershipReminder(profile: UserProfile? = null) {
        val prof = profile ?: userProfile.value
        if (prof.isFirstSetupDone && (prof.isMembershipBlocked || prof.gymMembershipStatus == "Bloqueado")) {
            _isMembershipBlocked.value = true
            return
        }

        // Conforme configuração do perfil: só dispara se o botão estiver ATIVADO!
        if (!prof.gymMembershipReminderEnabled) return

        val calendar = java.util.Calendar.getInstance()
        val year = calendar.get(java.util.Calendar.YEAR)
        val month = calendar.get(java.util.Calendar.MONTH) + 1
        val day = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val todayDateString = String.format(Locale.US, "%04d-%02d-%02d", year, month, day)
        val currentBillingCycle = String.format(Locale.US, "%04d-%02d", year, month)

        // Se o atleta já clicou no botão confirmando que efetuou a mensalidade deste mês, não exibe mais até o próximo mês
        if (prof.lastPaidBillingCycle == currentBillingCycle && prof.gymMembershipStatus == "Em dia") {
            return
        }

        // Dias até o vencimento da matrícula
        val maxDays = calendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        val dueDay = prof.gymMembershipDueDay.coerceIn(1, maxDays)
        val diffDays = dueDay - day

        // Ativa a partir de 3 dias antes do vencimento (diffDays <= 3) e continua ativo diariamente até confirmarem o pagamento
        val isReminderWindowActive = diffDays <= 3 || prof.gymMembershipStatus == "Vence em breve" || prof.gymMembershipStatus == "Pendente"

        if (isReminderWindowActive) {
            // 1. Abre o popup interno na interface do APK
            _showMembershipPopup.value = true
            vibrateDevice(200)

            // 2. Dispara popup externo do APK (notificação de alta prioridade do sistema Android) UMA VEZ AO DIA
            if (prof.lastMembershipPromptDate != todayDateString) {
                val dueText = when {
                    diffDays > 1 -> "Sua matrícula na ${prof.gymName} vence em $diffDays dias (dia $dueDay)!"
                    diffDays == 1 -> "Sua matrícula na ${prof.gymName} vence amanhã (dia $dueDay)!"
                    diffDays == 0 -> "Sua matrícula na ${prof.gymName} vence HOJE (dia $dueDay)!"
                    else -> "Sua matrícula na ${prof.gymName} está pendente desde o dia $dueDay!"
                }
                val workoutMotivationalText = "$dueText Não deixe de ir malhar hoje na academia! Mantenha a constância e o foco nos treinos."

                NotificationHelper.showMembershipExternalNotification(
                    context = getApplication<Application>().applicationContext,
                    title = "Aviso de Mensalidade & Foco no Treino! 💪",
                    message = workoutMotivationalText,
                    gymName = prof.gymName
                )

                // Salva que hoje já foi disparado o popup externo
                viewModelScope.launch {
                    val updated = prof.copy(
                        lastMembershipPromptDate = todayDateString,
                        gymMembershipStatus = if (diffDays < 0) "Pendente" else if (diffDays in 0..3) "Vence em breve" else prof.gymMembershipStatus
                    )
                    repository.saveUserProfile(updated)
                }
            }
        }
    }

    fun triggerMembershipPopup(forceTest: Boolean = false) {
        if (!forceTest && !userProfile.value.gymMembershipReminderEnabled) return
        _showMembershipPopup.value = true
        vibrateDevice(200)
    }

    fun triggerMembershipReminderNow(forceTest: Boolean = false) {
        triggerMembershipPopup(forceTest)
    }

    fun dismissMembershipPopup() {
        stopAlarmSound()
        _showMembershipPopup.value = false
    }

    fun blockMembershipManually() {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = current.copy(
                isMembershipBlocked = true,
                gymMembershipStatus = "Bloqueado",
                isFirstSetupDone = true
            )
            repository.saveUserProfile(updated)
            _isMembershipBlocked.value = true
            vibrateDevice()
        }
    }

    fun unblockWithAdminPin(enteredPin: String): Boolean {
        val current = userProfile.value
        val expectedPin = current.adminPin.ifBlank { "123456" }
        if (enteredPin == expectedPin) {
            viewModelScope.launch {
                val updated = current.copy(
                    isMembershipBlocked = false,
                    gymMembershipStatus = "Em dia",
                    lastPaymentDateMillis = System.currentTimeMillis()
                )
                repository.saveUserProfile(updated)
                _isMembershipBlocked.value = false
                vibrateDevice()
            }
            return true
        } else {
            vibrateDevice()
            return false
        }
    }

    fun updateAdminPin(newPin: String): Boolean {
        if (newPin.length == 6 && newPin.all { it.isDigit() }) {
            viewModelScope.launch {
                val current = userProfile.value
                val updated = current.copy(adminPin = newPin)
                repository.saveUserProfile(updated)
            }
            return true
        }
        return false
    }

    fun markGymMembershipPaid() {
        viewModelScope.launch {
            val current = userProfile.value
            val now = java.util.Calendar.getInstance()
            val currentCycle = String.format(Locale.US, "%04d-%02d", now.get(java.util.Calendar.YEAR), now.get(java.util.Calendar.MONTH) + 1)
            val updated = current.copy(
                gymMembershipStatus = "Em dia",
                isMembershipBlocked = false,
                lastPaymentDateMillis = System.currentTimeMillis(),
                lastPaidBillingCycle = currentCycle
            )
            repository.saveUserProfile(updated)
            _showMembershipPopup.value = false
            _isMembershipBlocked.value = false
            NotificationHelper.cancelMembershipNotification(getApplication<Application>().applicationContext)
            vibrateDevice(100)
        }
    }

    fun updateGymMembership(
        gymName: String,
        fee: String,
        dueDay: Int,
        status: String,
        reminderEnabled: Boolean
    ) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = current.copy(
                gymName = gymName,
                gymMembershipFee = fee,
                gymMembershipDueDay = dueDay,
                gymMembershipStatus = status,
                gymMembershipReminderEnabled = reminderEnabled
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateGymAlarm(
        hour: Int,
        minute: Int,
        days: String,
        alarmEnabled: Boolean
    ) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = current.copy(
                gymAlarmHour = hour,
                gymAlarmMinute = minute,
                gymAlarmDays = days,
                gymAlarmEnabled = alarmEnabled
            )
            repository.saveUserProfile(updated)
        }
    }

    fun updateWaterSettings(
        intervalMinutes: Int,
        enabled: Boolean
    ) {
        viewModelScope.launch {
            val current = userProfile.value
            val updated = current.copy(
                waterReminderIntervalMinutes = intervalMinutes,
                waterReminderEnabled = enabled
            )
            repository.saveUserProfile(updated)
        }
    }

    fun toggleExerciseCompleted(exerciseId: String) {
        val current = _activeExercisesState.value.toMutableList()
        val index = current.indexOfFirst { it.id == exerciseId }
        if (index != -1) {
            val item = current[index]
            val newState = !item.isCompleted
            current[index] = item.copy(isCompleted = newState)
            _activeExercisesState.value = current

            if (newState) {
                // Toque sonoro de sucesso ao concluir tarefa de treino
                playTaskCompletionSound()
            }

            // If checked as completed and has rest time, auto-trigger rest timer
            if (newState && item.restSeconds > 0) {
                startRestTimer(item.restSeconds)
            }
        }
    }

    fun startRestTimer(seconds: Int) {
        timerJob?.cancel()
        _totalRestDuration.value = seconds
        _restTimerSeconds.value = seconds
        _isTimerRunning.value = true

        timerJob = viewModelScope.launch {
            while (_restTimerSeconds.value > 0 && _isTimerRunning.value) {
                delay(1000)
                _restTimerSeconds.value = _restTimerSeconds.value - 1
            }
            if (_restTimerSeconds.value == 0 && _isTimerRunning.value) {
                _isTimerRunning.value = false
                playTaskCompletionSound()
            }
        }
    }

    fun pauseResumeTimer() {
        if (_isTimerRunning.value) {
            _isTimerRunning.value = false
            timerJob?.cancel()
        } else if (_restTimerSeconds.value > 0) {
            _isTimerRunning.value = true
            timerJob = viewModelScope.launch {
                while (_restTimerSeconds.value > 0 && _isTimerRunning.value) {
                    delay(1000)
                    _restTimerSeconds.value = _restTimerSeconds.value - 1
                }
                if (_restTimerSeconds.value == 0) {
                    _isTimerRunning.value = false
                    vibrateDevice()
                }
            }
        }
    }

    fun addTimerSeconds(extraSeconds: Int) {
        _restTimerSeconds.value = _restTimerSeconds.value + extraSeconds
        _totalRestDuration.value = _totalRestDuration.value.coerceAtLeast(_restTimerSeconds.value)
    }

    fun resetRestTimer() {
        timerJob?.cancel()
        _isTimerRunning.value = false
        _restTimerSeconds.value = 0
        _totalRestDuration.value = 0
    }

    fun playTaskCompletionSound() {
        try {
            val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 85)
            toneGen.startTone(ToneGenerator.TONE_PROP_ACK, 200)
        } catch (_: Exception) {
            try {
                val context = getApplication<Application>()
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                ringtone?.play()
            } catch (_: Exception) {}
        }
        vibrateDevice(durationMs = 150)
    }

    fun stopAlarmSound() {
        try {
            stopSoundJob?.cancel()
            stopSoundJob = null
            activeRingtone?.stop()
            activeRingtone = null
        } catch (_: Exception) {}
    }

    /**
     * O som do alarme só deve alarmar UMA VEZ como lembrete sonoro (não fica disparando direto).
     * Para automaticamente após 1.5s ou imediatamente quando qualquer popup é dispensado/fechado.
     */
    fun playAlarmSound() {
        stopAlarmSound()
        viewModelScope.launch(Dispatchers.Main) {
            try {
                val context = getApplication<Application>()
                // Usa som de notificação/lembrete amigável de toque único
                val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                val ringtone = RingtoneManager.getRingtone(context, uri)
                activeRingtone = ringtone
                ringtone?.play()

                // Alarma só uma vez como lembrete - cancela/para após 1.5s
                stopSoundJob = launch {
                    delay(1500)
                    stopAlarmSound()
                }
            } catch (_: Exception) {
                try {
                    val toneGen = ToneGenerator(AudioManager.STREAM_NOTIFICATION, 80)
                    toneGen.startTone(ToneGenerator.TONE_PROP_BEEP, 300)
                } catch (_: Exception) {}
            }
        }
        vibrateDevice(durationMs = 250)
    }

    fun updateProfilePicture(uri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val context = getApplication<Application>()
                val file = File(context.filesDir, "profile_avatar_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(uri)?.use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                val localUri = Uri.fromFile(file).toString()
                val current = userProfile.value
                val updated = current.copy(profilePictureUri = localUri)
                repository.saveUserProfile(updated)
                vibrateDevice(100)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun removeProfilePicture() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val current = userProfile.value
                val updated = current.copy(profilePictureUri = null)
                repository.saveUserProfile(updated)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun vibrateDevice(durationMs: Long = 500) {
        try {
            val context = getApplication<Application>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
                vibratorManager?.defaultVibrator?.vibrate(
                    VibrationEffect.createOneShot(durationMs, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
                @Suppress("DEPRECATION")
                vibrator?.vibrate(durationMs)
            }
        } catch (_: Exception) {}
    }

    fun completeWorkoutSession(notes: String = "") {
        val workout = _activeWorkout.value ?: return
        val exercises = _activeExercisesState.value
        val completedCount = exercises.count { it.isCompleted }
        val totalCount = exercises.size

        viewModelScope.launch {
            repository.logCompletedWorkout(
                dayNumber = workout.dayNumber,
                workoutTitle = workout.name,
                durationMinutes = workout.durationMinutes,
                completedExercisesCount = completedCount,
                totalExercisesCount = totalCount,
                notes = notes
            )
            // Update plan day completed status
            val currentPlan = _weeklyPlan.value.toMutableList()
            val dayIdx = currentPlan.indexOfFirst { it.dayNumber == workout.dayNumber }
            if (dayIdx != -1) {
                currentPlan[dayIdx] = currentPlan[dayIdx].copy(isCompletedThisWeek = true)
                _weeklyPlan.value = currentPlan
            }
            dismissActiveWorkout()
        }
    }

    fun dismissActiveWorkout() {
        resetRestTimer()
        workoutElapsedJob?.cancel()
        _activeWorkout.value = null
        _activeExercisesState.value = emptyList()
        _workoutElapsedSeconds.value = 0
    }

    fun logWeight(weight: Float) {
        viewModelScope.launch {
            val profile = repository.getUserProfile()
            repository.logWeight(weight, profile.heightCm)
        }
    }

    fun logQuickActivity(
        title: String,
        durationMinutes: Int,
        caloriesBurned: Int,
        category: String = "Cardio"
    ) {
        viewModelScope.launch {
            repository.logCompletedWorkout(
                dayNumber = 0,
                workoutTitle = title,
                durationMinutes = durationMinutes,
                completedExercisesCount = 1,
                totalExercisesCount = 1,
                notes = category,
                customCalories = caloriesBurned
            )
            playTaskCompletionSound()
        }
    }

    fun deleteHistoryItem(id: Long) {
        viewModelScope.launch {
            repository.deleteWorkoutHistory(id)
        }
    }

    fun sendChatMessage(userText: String) {
        val trimmed = userText.trim()
        if (trimmed.isBlank() || _isChatLoading.value) return

        val userMessage = ChatMessage(text = trimmed, isUser = true)
        _chatMessages.value = _chatMessages.value + userMessage

        viewModelScope.launch {
            _isChatLoading.value = true
            try {
                val history = _chatMessages.value.filter { it.id != userMessage.id }
                val profile = userProfile.value
                val responseText = geminiChatRepository.sendMessage(trimmed, history, profile)
                val assistantMessage = ChatMessage(text = responseText, isUser = false)
                _chatMessages.value = _chatMessages.value + assistantMessage
            } catch (e: Exception) {
                val errorMessage = ChatMessage(
                    text = "Desculpe, tive uma instabilidade momentânea na conexão. Lembre-se: preserve sempre a postura e execute os movimentos com controle. Como posso te orientar agora?",
                    isUser = false
                )
                _chatMessages.value = _chatMessages.value + errorMessage
            } finally {
                _isChatLoading.value = false
            }
        }
    }

    fun clearChatHistory() {
        val name = userProfile.value.name
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Conversa reiniciada! Olá, $name. Sou o Ampla Personal IA. Pode tirar dúvidas sobre treinos, horários, posturas, dietas e recuperação na Academia Ampla. No que posso te ajudar?",
                isUser = false
            )
        )
    }
}
