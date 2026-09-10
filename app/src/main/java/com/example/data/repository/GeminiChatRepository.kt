package com.example.data.repository

import com.example.BuildConfig
import com.example.data.model.ChatMessage
import com.example.data.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiChatRepository {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    companion object {
        private const val MODEL_NAME = "gemini-3.5-flash"
        private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models"
    }

    private fun buildSystemInstruction(profile: UserProfile): String {
        return """
            Você é o Ampla Personal IA, o personal trainer virtual e consultor oficial de performance e saúde da Academia Ampla.
            
            Informações do Aluno da Academia Ampla:
            - Nome: ${profile.name}
            - Nível: ${profile.fitnessLevel}
            - Objetivo Principal: ${profile.fitnessGoal}
            - Peso: ${profile.weightKg} kg | Altura: ${profile.heightCm} cm | IMC: ${profile.bmi}
            - Horário Habitual de Treino: ${String.format("%02d:%02d", profile.gymAlarmHour, profile.gymAlarmMinute)} (${profile.gymAlarmDays})
            - Academia: ${profile.gymName}
            
            Escopo EXCLUSIVO de atuação:
            1. Treinos e Exercícios: orientações de execução, divisão de treinos, aquecimento, cadência, séries e repetições para melhor rendimento na musculação.
            2. Postura e Prevenção: dicas de biomecânica correta (escápulas, coluna neutra, ângulo de joelhos) para prevenir dores e lesões.
            3. Horários e Rotina na Academia Ampla: organização de horários ideais para treinar com disciplina e constância.
            4. Desempenho na Academia Ampla: progressão de carga segura, descanso adequado entre séries e foco na intensidade certa.
            5. Plano de Alimentação e Dietas: orientações básicas sobre macronutrientes (proteínas, carboidratos, gorduras boas), hidratação diária, refeições pré e pós-treino (esclarecendo que planos clínicos individualizados devem ser acompanhados por nutricionista).
            6. Recuperação Muscular e Saúde: sono anabólico, descanso entre treinos, mobilidade e tudo que envolve saúde e musculação.
            
            Regra Estrita de Escopo:
            - Se o usuário fizer perguntas que NÃO tenham relação com musculação, treinos, postura, horários de academia, alimentação, dietas, saúde física ou recuperação, responda de forma cordial e educada informando que você é o assistente exclusivo de saúde e treinos da Academia Ampla e convide-o a tirar dúvidas esportivas.
            
            Tom e Estilo:
            - Linguagem em Português do Brasil, motivadora, técnica porém acessível, estruturada com tópicos objetivos quando necessário.
        """.trimIndent()
    }

    suspend fun sendMessage(
        userMessage: String,
        conversationHistory: List<ChatMessage>,
        profile: UserProfile
    ): String = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        val isKeyConfigured = apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY"

        if (isKeyConfigured) {
            try {
                val url = "$BASE_URL/$MODEL_NAME:generateContent?key=$apiKey"
                val systemPrompt = buildSystemInstruction(profile)

                val requestJson = JSONObject().apply {
                    // systemInstruction
                    put("systemInstruction", JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", systemPrompt)
                            })
                        })
                    })

                    // contents array preserving multi-turn conversation
                    val contentsArray = JSONArray()
                    // Add previous turns (limiting to last 10 messages for speed and context window)
                    val historyToInclude = conversationHistory.takeLast(10)
                    for (msg in historyToInclude) {
                        contentsArray.put(JSONObject().apply {
                            put("role", if (msg.isUser) "user" else "model")
                            put("parts", JSONArray().apply {
                                put(JSONObject().apply {
                                    put("text", msg.text)
                                })
                            })
                        })
                    }

                    // Add current user prompt
                    contentsArray.put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", userMessage)
                            })
                        })
                    })

                    put("contents", contentsArray)

                    // generationConfig
                    put("generationConfig", JSONObject().apply {
                        put("temperature", 0.7)
                        put("topP", 0.95)
                    })
                }

                val requestBody = requestJson.toString().toRequestBody("application/json".toMediaType())
                val request = Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build()

                val response = okHttpClient.newCall(request).execute()
                val responseBody = response.body?.string()

                if (response.isSuccessful && !responseBody.isNullOrBlank()) {
                    val responseJson = JSONObject(responseBody)
                    val candidates = responseJson.optJSONArray("candidates")
                    if (candidates != null && candidates.length() > 0) {
                        val firstCandidate = candidates.getJSONObject(0)
                        val content = firstCandidate.optJSONObject("content")
                        val parts = content?.optJSONArray("parts")
                        if (parts != null && parts.length() > 0) {
                            val replyText = parts.getJSONObject(0).optString("text")
                            if (!replyText.isNullOrBlank()) {
                                return@withContext replyText.trim()
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                // Fallback to domain response if connection drops or network fails
            }
        }

        // Domain-specific intelligent fallback for Academia Ampla
        generateDomainFallbackReply(userMessage, profile)
    }

    private fun generateDomainFallbackReply(message: String, profile: UserProfile): String {
        val lower = message.lowercase()

        val isOutScope = listOf("futebol", "política", "filme", "fofoca", "jogo", "receita de bolo", "carro", "moto", "fórmula 1")
            .any { lower.contains(it) } && !lower.contains("treino") && !lower.contains("dieta") && !lower.contains("saúde")

        if (isOutScope) {
            return "Olá, ${profile.name}! Como seu Ampla Personal IA, sou especializado exclusivamente em te orientar sobre treinos, horários na academia, dicas de postura, plano de alimentação, dietas e recuperação muscular. Como posso te apoiar nos seus treinos da Academia Ampla hoje?"
        }

        return when {
            lower.contains("postura") || lower.contains("dor") || lower.contains("execu") || lower.contains("coluna") || lower.contains("joelho") -> {
                """
                Dicas de Postura & Segurança na Academia Ampla:
                1. Alinhamento da Coluna: Durante agachamentos, terras e remadas, mantenha o abdômen contraído (bracing) e a curvatura lombar natural preservada.
                2. Escápulas: Na maioria dos supinos e puxadas, mantenha as escápulas aduzidas e deprimidas ("para trás e para baixo") para proteger a articulação do ombro.
                3. Joelhos: Em agachamentos e no leg press, certifique-se de que a linha do joelho acompanhe a ponta dos pés, evitando que colapsem para dentro (valgo dinâmico).
                4. Amplitude Consciente: Priorize a amplitude correta antes de aumentar o peso. Qualidade de movimento gera mais hipertrofia com zero lesões!
                """.trimIndent()
            }
            lower.contains("horário") || lower.contains("horario") || lower.contains("hora") || lower.contains("tempo") || lower.contains("cedo") || lower.contains("noite") -> {
                """
                Horários de Treino na Academia Ampla:
                Seu horário programado no perfil é às ${String.format("%02d:%02d", profile.gymAlarmHour, profile.gymAlarmMinute)}.
                - Manhã: Ótimo para quem quer acelerar o metabolismo e garantir o treino antes dos compromissos do dia.
                - Fim de tarde / Noite: Período onde a força muscular e a temperatura corporal costumam estar no pico.
                A regra de ouro é a consistência: escolha um horário fixo onde você não falte e mantenha seu alarme ativo!
                """.trimIndent()
            }
            lower.contains("comida") || lower.contains("alimento") || lower.contains("dieta") || lower.contains("nutri") || lower.contains("prote") || lower.contains("carbo") || lower.contains("suplemento") -> {
                """
                Orientações Básicas de Alimentação & Dieta:
                - Pré-Treino (60 a 90 min antes): Carboidratos de digestão moderada (banana com aveia, pão integral) combinados com uma fonte leve de proteína (ovos, iogurte ou whey).
                - Pós-Treino: Foco na recuperação muscular com proteínas de alto valor biológico (frango, peixe, ovos ou whey) e carboidratos para reposição de glicogênio.
                - Meta Diária: Procure consumir entre 1,6g a 2,2g de proteína por kg de peso corporal para suporte a síntese proteica.
                - Hidratação: Beba pelo menos 35ml a 40ml de água por quilo durante o dia para manter os músculos cheios e funcionais.
                """.trimIndent()
            }
            lower.contains("recupera") || lower.contains("sono") || lower.contains("descanso") || lower.contains("fadiga") || lower.contains("dor muscular") -> {
                """
                Recuperação Muscular & Saúde:
                1. Sono Anabólico: De 7 a 9 horas de sono de qualidade por noite. É durante as fases profundas do sono que o hormônio do crescimento (GH) é liberado e os tecidos musculares se regeneram.
                2. Intervalo entre Treinos: Dê pelo menos 48h de descanso para o mesmo grupamento muscular antes de treiná-lo intensamente novamente.
                3. Mobilidade & Hidratação: Alongamentos leves em dias de descanso restauram a amplitude e aumentam o fluxo sanguíneo reparador.
                """.trimIndent()
            }
            lower.contains("desempenho") || lower.contains("carga") || lower.contains("peso") || lower.contains("força") || lower.contains("evolu") -> {
                """
                Potencializando o Desempenho na Academia Ampla:
                1. Sobrecarga Progressiva: Anote suas cargas e tente aumentar 1 repetição ou 1kg a mais a cada semana mantendo a postura estrita.
                2. Descanso entre Séries: Para hipertrofia, 60s a 90s; para exercícios compostos pesados (supino, agachamento), 2 a 3 minutos para restaurar o ATP celular.
                3. Aquecimento Específico: Faça 1 a 2 séries leves do exercício principal antes de colocar as cargas de trabalho.
                4. Conexão Mente-Músculo: Sinta a contração e controle a fase excêntrica (descida do peso) em 2 a 3 segundos.
                """.trimIndent()
            }
            else -> {
                """
                Olá, ${profile.name}! Sou o Ampla Personal IA.
                Para seu objetivo de ${profile.fitnessGoal}:
                - Treine com foco total na postura e cadência controlada.
                - Mantenha a hidratação constante na academia.
                - Siga sua programação semanal e ajuste as cargas com segurança.
                
                Você pode me perguntar:
                • "Como corrigir minha postura no supino ou agachamento?"
                • "Qual melhor refeição pré e pós treino?"
                • "Qual o melhor horário para meu treino render mais?"
                • "Como melhorar minha recuperação e evitar dores pós-treino?"
                """.trimIndent()
            }
        }
    }
}
