package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.WeightLogEntity
import com.example.data.model.WorkoutHistoryEntity
import com.example.data.model.WorkoutPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {
    // Current Active Plan
    @Query("SELECT * FROM workout_plans WHERE id = 1 LIMIT 1")
    fun getCurrentPlanFlow(): Flow<WorkoutPlanEntity?>

    @Query("SELECT * FROM workout_plans WHERE id = 1 LIMIT 1")
    suspend fun getCurrentPlan(): WorkoutPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveCurrentPlan(plan: WorkoutPlanEntity)

    // Workout History Logs
    @Query("SELECT * FROM workout_history ORDER BY timestampMillis DESC")
    fun getAllHistoryFlow(): Flow<List<WorkoutHistoryEntity>>

    @Query("SELECT * FROM workout_history ORDER BY timestampMillis DESC LIMIT :limit")
    fun getRecentHistoryFlow(limit: Int): Flow<List<WorkoutHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkoutHistory(log: WorkoutHistoryEntity): Long

    @Query("DELETE FROM workout_history WHERE id = :id")
    suspend fun deleteWorkoutHistory(id: Long)

    @Query("SELECT COUNT(*) FROM workout_history")
    fun getTotalWorkoutsCountFlow(): Flow<Int>

    @Query("SELECT SUM(durationMinutes) FROM workout_history")
    fun getTotalMinutesTrainedFlow(): Flow<Int?>

    // Weight Logs
    @Query("SELECT * FROM weight_logs ORDER BY timestampMillis DESC")
    fun getAllWeightLogsFlow(): Flow<List<WeightLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeightLog(weightLog: WeightLogEntity): Long
}
