package com.example.ui

import android.app.Application
import android.content.Context
import android.media.AudioManager
import android.media.RingtoneManager
import android.media.ToneGenerator
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

    init {
        loadInitialData()
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

    fun updateProfile(newProfile: UserProfile, regeneratePlanNow: Boolean = true) {
        viewModelScope.launch {
            _isGeneratingPlan.value = true
            repository.saveUserProfile(newProfile.copy(hasCompletedOnboarding = true))
            if (newProfile.isFirstSetupDone && (newProfile.isMembershipBlocked || newProfile.gymMembershipStatus == "Bloqueado")) {
                _isMembershipBlocked.value = true
            } else {
                _isMembershipBlocked.value = false
            }
            checkAutomaticMembershipReminder(newProfile)
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
                    vibrateDevice()
                }
            }
        }
    }

    fun drinkWater(ml: Int = 250) {
        _waterConsumedMl.value = _waterConsumedMl.value + ml
        _showWaterReminderPopup.value = false
        vibrateDevice()
    }

    fun dismissWaterReminderPopup() {
        _showWaterReminderPopup.value = false
    }

    fun triggerWaterReminderManual() {
        _showWaterReminderPopup.value = true
        vibrateDevice()
    }

    fun triggerWaterReminderNow() {
        triggerWaterReminderManual()
    }

    fun triggerGymAlarm() {
        _showGymAlarmPopup.value = true
        playAlarmSound()
    }

    fun triggerGymAlarmNow() {
        triggerGymAlarm()
    }

    fun dismissGymAlarm() {
        _showGymAlarmPopup.value = false
    }

    fun snoozeGymAlarm(minutes: Int = 10) {
        _showGymAlarmPopup.value = false
        viewModelScope.launch {
            delay(minutes * 60 * 1000L)
            _showGymAlarmPopup.value = true
            playAlarmSound()
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

        if (!prof.gymMembershipReminderEnabled) return

        val calendar = java.util.Calendar.getInstance()
        val currentDay = calendar.get(java.util.Calendar.DAY_OF_MONTH)

        val isStatusAlert = prof.gymMembershipStatus == "Vence em breve" || prof.gymMembershipStatus == "Pendente"
        val diff = prof.gymMembershipDueDay - currentDay
        val isDueDayNear = diff in -5..3

        if (isStatusAlert || isDueDayNear) {
            _showMembershipPopup.value = true
            playAlarmSound()
        }
    }

    fun triggerMembershipPopup() {
        _showMembershipPopup.value = true
        playAlarmSound()
    }

    fun triggerMembershipReminderNow() {
        triggerMembershipPopup()
    }

    fun dismissMembershipPopup() {
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
            val updated = current.copy(
                gymMembershipStatus = "Em dia",
                isMembershipBlocked = false,
                lastPaymentDateMillis = System.currentTimeMillis()
            )
            repository.saveUserProfile(updated)
            _showMembershipPopup.value = false
            _isMembershipBlocked.value = false
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

    fun playAlarmSound() {
        try {
            val context = getApplication<Application>()
            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
                ?: RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
            val ringtone = RingtoneManager.getRingtone(context, uri)
            ringtone?.play()
        } catch (_: Exception) {
            try {
                val toneGen = ToneGenerator(AudioManager.STREAM_ALARM, 90)
                toneGen.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 500)
            } catch (_: Exception) {}
        }
        vibrateDevice(durationMs = 600)
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
