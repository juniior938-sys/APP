package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

data class WorkoutExercise(
    val id: String,
    val name: String,
    val targetMuscle: String,
    val sets: Int,
    val reps: String, // e.g. "10-12 reps" or "45s"
    val restSeconds: Int, // e.g. 60
    val equipment: String, // e.g. "Bodyweight", "Dumbbells", "Barbell"
    val tips: String,
    var isCompleted: Boolean = false
)

data class WorkoutDay(
    val dayNumber: Int, // 1 to 7
    val dayTitle: String, // e.g. "Day 1: Mon"
    val name: String, // e.g. "Upper Body Strength"
    val focus: String, // e.g. "Chest, Shoulders & Triceps"
    val durationMinutes: Int, // e.g. 45
    val isRestDay: Boolean,
    val exercises: List<WorkoutExercise> = emptyList(),
    val isCompletedThisWeek: Boolean = false
)

@Entity(tableName = "workout_plans")
data class WorkoutPlanEntity(
    @PrimaryKey val id: Int = 1,
    val title: String,
    val goal: String,
    val fitnessLevel: String,
    val location: String,
    val daysJson: String, // Serialized list of WorkoutDay
    val generatedAtMillis: Long = System.currentTimeMillis()
)

@Entity(tableName = "workout_history")
data class WorkoutHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMillis: Long = System.currentTimeMillis(),
    val dayNumber: Int,
    val workoutTitle: String,
    val durationMinutes: Int,
    val exercisesCompletedCount: Int,
    val totalExercisesCount: Int,
    val caloriesBurnedEstimated: Int,
    val notes: String = ""
)

@Entity(tableName = "weight_logs")
data class WeightLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestampMillis: Long = System.currentTimeMillis(),
    val weightKg: Float,
    val bmi: Float
)
