package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.math.pow
import kotlin.math.roundToInt

@Entity(tableName = "user_profile")
data class UserProfile(
    @PrimaryKey val id: Int = 1,
    val name: String = "Atleta",
    val age: Int = 26,
    val gender: String = "Masculino", // "Masculino", "Feminino", "Outro"
    val weightKg: Float = 72.0f,
    val heightCm: Float = 175.0f,
    val fitnessLevel: String = "Intermediário", // "Iniciante", "Intermediário", "Avançado"
    val workoutLocation: String = "Academia", // "Academia", "Casa"
    val fitnessGoal: String = "Ganho de Massa", // "Perda de Peso", "Ganho de Massa", "Força", "Resistência"
    val hasCompletedOnboarding: Boolean = false,

    // Lembrete de Pagamento de Mensalidade da Academia & Bloqueio por Administrador
    val gymName: String = "Academia Ampla Fitness",
    val gymMembershipFee: String = "119,90",
    val gymMembershipDueDay: Int = 10,
    val gymMembershipStatus: String = "Em dia", // "Em dia", "Vence em breve", "Pendente", "Bloqueado"
    val gymMembershipReminderEnabled: Boolean = true,
    val lastPaymentDateMillis: Long = System.currentTimeMillis() - (28L * 24 * 60 * 60 * 1000L),
    val isMembershipBlocked: Boolean = false,
    val adminPin: String = "123456", // Senha de 6 dígitos do Administrador
    val isFirstSetupDone: Boolean = true, // Ativa bloqueio somente após o primeiro cadastro
    val adminContactPhone: String = "(71) 98134-1942",

    // Lembrete de Horário de Academia & Alarmes
    val gymAlarmHour: Int = 18,
    val gymAlarmMinute: Int = 30,
    val gymAlarmDays: String = "Dom, Seg, Ter, Qua, Qui, Sex, Sáb",
    val gymAlarmEnabled: Boolean = true,

    // Lembrete de Beber Água durante os Treinos
    val waterReminderIntervalMinutes: Int = 10,
    val waterReminderEnabled: Boolean = true,

    // Foto do Perfil do Atleta
    val profilePictureUri: String? = null,

    val updatedAt: Long = System.currentTimeMillis()
) {
    val bmi: Float
        get() {
            if (heightCm <= 0f || weightKg <= 0f) return 22.0f
            val heightM = heightCm / 100f
            val calculated = weightKg / heightM.pow(2)
            return (calculated * 10f).roundToInt() / 10f
        }

    val bmiCategory: BmiCategory
        get() = when {
            bmi < 18.5f -> BmiCategory.UNDERWEIGHT
            bmi < 25.0f -> BmiCategory.NORMAL
            bmi < 30.0f -> BmiCategory.OVERWEIGHT
            else -> BmiCategory.OBESE
        }

    val idealWeightRangeKg: Pair<Float, Float>
        get() {
            val heightM = (heightCm.coerceAtLeast(100f)) / 100f
            val minWeight = (18.5f * heightM.pow(2) * 10f).roundToInt() / 10f
            val maxWeight = (24.9f * heightM.pow(2) * 10f).roundToInt() / 10f
            return Pair(minWeight, maxWeight)
        }
}

enum class BmiCategory(val label: String, val colorHex: Long) {
    UNDERWEIGHT("Abaixo do peso", 0xFFFF9900),
    NORMAL("Peso Saudável", 0xFFFF6B00),
    OVERWEIGHT("Sobrepeso", 0xFFFF3358),
    OBESE("Obesidade", 0xFFFF1E44)
}

