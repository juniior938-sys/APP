package com.example.data.local

import androidx.room.TypeConverter
import com.example.data.model.WorkoutDay
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class Converters {
    private val moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, WorkoutDay::class.java)
    private val adapter = moshi.adapter<List<WorkoutDay>>(listType)

    @TypeConverter
    fun fromWorkoutDayList(value: List<WorkoutDay>?): String {
        return if (value == null) "[]" else adapter.toJson(value)
    }

    @TypeConverter
    fun toWorkoutDayList(value: String?): List<WorkoutDay> {
        if (value.isNullOrEmpty()) return emptyList()
        return try {
            adapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
