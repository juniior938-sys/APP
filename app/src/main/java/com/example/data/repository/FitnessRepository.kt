package com.example.data.repository

import com.example.data.local.Converters
import com.example.data.local.UserDao
import com.example.data.local.WorkoutDao
import com.example.data.model.UserProfile
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.model.WorkoutPlanEntity
import com.example.domain.CoachEngine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

data class StreakStats(
    val currentStreakDays: Int = 0,
    val longestStreakDays: Int = 0,
    val totalWorkouts: Int = 0,
    val totalMinutes: Int = 0,
    val workoutsThisWeek: Int = 0,
    val weeklyTarget: Int = 4,
    val lastWorkoutFormatted: String = "Nenhum treino ainda"
)

class FitnessRepository(
    private val userDao: UserDao,
    private val workoutDao: WorkoutDao
) {
    private val converters = Converters()

    val userProfileFlow: Flow<UserProfile?> = userDao.getUserProfileFlow()

    suspend fun getUserProfile(): UserProfile {
        return userDao.getUserProfile() ?: UserProfile().also {
            userDao.insertOrUpdateUserProfile(it)
        }
    }

    suspend fun saveUserProfile(profile: UserProfile) {
        userDao.insertOrUpdateUserProfile(profile.copy(updatedAt = System.currentTimeMillis()))
    }

    val currentPlanFlow: Flow<WorkoutPlanEntity?> = workoutDao.getCurrentPlanFlow()

    suspend fun getParsedWeeklyPlan(): List<WorkoutDay> {
        val entity = workoutDao.getCurrentPlan()
        if (entity != null) {
            val list = converters.toWorkoutDayList(entity.daysJson)
            if (list.isNotEmpty()) return list
        }
        val profile = getUserProfile()
        val generated = CoachEngine.generateWeeklyPlan(profile)
        val json = converters.fromWorkoutDayList(generated)
        val newPlan = WorkoutPlanEntity(
            id = 1,
            title = "Plano ${profile.fitnessGoal} (${profile.workoutLocation})",
            goal = profile.fitnessGoal,
            fitnessLevel = profile.fitnessLevel,
            location = profile.workoutLocation,
            daysJson = json
        )
        workoutDao.saveCurrentPlan(newPlan)
        return generated
    }

    suspend fun regeneratePlan(profile: UserProfile): List<WorkoutDay> {
        val days = CoachEngine.generateWeeklyPlan(profile)
        val json = converters.fromWorkoutDayList(days)
        val plan = WorkoutPlanEntity(
            id = 1,
            title = "Plano Coach ${profile.fitnessGoal}",
            goal = profile.fitnessGoal,
            fitnessLevel = profile.fitnessLevel,
            location = profile.workoutLocation,
            daysJson = json,
            generatedAtMillis = System.currentTimeMillis()
        )
        workoutDao.saveCurrentPlan(plan)
        return days
    }

    val workoutHistoryFlow: Flow<List<WorkoutHistoryEntity>> = workoutDao.getAllHistoryFlow()

    suspend fun logCompletedWorkout(
        dayNumber: Int,
        workoutTitle: String,
        durationMinutes: Int,
        completedExercisesCount: Int,
        totalExercisesCount: Int,
        notes: String = "",
        customCalories: Int? = null
    ): Long {
        val estimatedCalories = customCalories ?: (durationMinutes * 7.5f).toInt().coerceAtLeast(60)
        val log = WorkoutHistoryEntity(
            timestampMillis = System.currentTimeMillis(),
            dayNumber = dayNumber,
            workoutTitle = workoutTitle,
            durationMinutes = durationMinutes,
            exercisesCompletedCount = completedExercisesCount,
            totalExercisesCount = totalExercisesCount,
            caloriesBurnedEstimated = estimatedCalories,
            notes = notes
        )
        return workoutDao.insertWorkoutHistory(log)
    }

    suspend fun deleteWorkoutHistory(id: Long) {
        workoutDao.deleteWorkoutHistory(id)
    }

    val weightLogsFlow: Flow<List<WeightLogEntity>> = workoutDao.getAllWeightLogsFlow()

    suspend fun logWeight(weightKg: Float, heightCm: Float) {
        val heightM = heightCm.coerceAtLeast(100f) / 100f
        val bmi = (weightKg / (heightM * heightM) * 10f).toInt() / 10f
        workoutDao.insertWeightLog(
            WeightLogEntity(
                timestampMillis = System.currentTimeMillis(),
                weightKg = weightKg,
                bmi = bmi
            )
        )
        // Also update current profile weight
        val current = getUserProfile()
        saveUserProfile(current.copy(weightKg = weightKg))
    }

    fun computeStreakStats(history: List<WorkoutHistoryEntity>, userLevel: String): StreakStats {
        if (history.isEmpty()) {
            val target = when {
                userLevel.contains("Iniciante", ignoreCase = true) || userLevel == "Beginner" -> 3
                userLevel.contains("Avançado", ignoreCase = true) || userLevel == "Advanced" -> 5
                else -> 4
            }
            return StreakStats(weeklyTarget = target)
        }

        val totalWorkouts = history.size
        val totalMinutes = history.sumOf { it.durationMinutes }

        val sdf = SimpleDateFormat("dd 'de' MMM, yyyy", Locale("pt", "BR"))
        val lastFormatted = sdf.format(Date(history.first().timestampMillis))

        // Group timestamps by unique calendar day (yyyy-MM-dd)
        val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val uniqueDays = history.map { dayFormat.format(Date(it.timestampMillis)) }.distinct().sortedDescending()

        // Calculate consecutive day streak
        var currentStreak = 0
        val cal = Calendar.getInstance()
        val todayStr = dayFormat.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = dayFormat.format(cal.time)

        // If active today or yesterday, count backwards
        if (uniqueDays.isNotEmpty()) {
            val startIdx = if (uniqueDays.contains(todayStr)) {
                uniqueDays.indexOf(todayStr)
            } else if (uniqueDays.contains(yesterdayStr)) {
                uniqueDays.indexOf(yesterdayStr)
            } else {
                -1
            }

            if (startIdx != -1) {
                currentStreak = 1
                val checkCal = Calendar.getInstance()
                // set to start day
                if (!uniqueDays.contains(todayStr)) {
                    checkCal.add(Calendar.DAY_OF_YEAR, -1)
                }

                for (i in (startIdx + 1) until uniqueDays.size) {
                    checkCal.add(Calendar.DAY_OF_YEAR, -1)
                    val expectedDay = dayFormat.format(checkCal.time)
                    if (uniqueDays[i] == expectedDay) {
                        currentStreak++
                    } else {
                        break
                    }
                }
            }
        }

        // Longest streak calculation
        var longest = currentStreak.coerceAtLeast(1)
        // Check workouts this current week (Monday through Sunday)
        val weekCal = Calendar.getInstance()
        weekCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        weekCal.set(Calendar.HOUR_OF_DAY, 0)
        weekCal.set(Calendar.MINUTE, 0)
        weekCal.set(Calendar.SECOND, 0)
        val weekStartMillis = weekCal.timeInMillis

        val workoutsThisWeek = history.count { it.timestampMillis >= weekStartMillis }

        val target = when (userLevel) {
            "Beginner" -> 3
            "Advanced" -> 5
            else -> 4
        }

        return StreakStats(
            currentStreakDays = currentStreak,
            longestStreakDays = longest,
            totalWorkouts = totalWorkouts,
            totalMinutes = totalMinutes,
            workoutsThisWeek = workoutsThisWeek,
            weeklyTarget = target,
            lastWorkoutFormatted = lastFormatted
        )
    }
}
