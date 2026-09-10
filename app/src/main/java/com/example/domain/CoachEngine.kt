package com.example.domain

import com.example.data.model.UserProfile
import com.example.data.model.WorkoutDay
import com.example.data.model.WorkoutExercise
import java.util.UUID

object CoachEngine {

    fun generateWeeklyPlan(profile: UserProfile): List<WorkoutDay> {
        val isGym = profile.workoutLocation.contains("Academia", ignoreCase = true) ||
                profile.workoutLocation.equals("Gym", ignoreCase = true)
        val goal = profile.fitnessGoal
        val level = profile.fitnessLevel

        return when {
            goal.contains("Perda", ignoreCase = true) || goal.contains("Weight", ignoreCase = true) ->
                generateWeightLossPlan(profile, isGym, level)
            goal.contains("Força", ignoreCase = true) || goal.contains("Strength", ignoreCase = true) ->
                generateStrengthPlan(profile, isGym, level)
            goal.contains("Resistência", ignoreCase = true) || goal.contains("Endurance", ignoreCase = true) ->
                generateEndurancePlan(profile, isGym, level)
            else -> generateMuscleGainPlan(profile, isGym, level) // Padrão Ganho de Massa
        }
    }

    private fun generateWeightLossPlan(profile: UserProfile, isGym: Boolean, level: String): List<WorkoutDay> {
        val restSec = 40
        val duration = if (level.contains("Iniciante", ignoreCase = true) || level == "Beginner") 35 else 45

        return listOf(
            WorkoutDay(
                dayNumber = 1,
                dayTitle = "Dia 1: Seg",
                name = "HIIT Corpo Inteiro & Queima Metabólica",
                focus = "Condicionamento metabólico & Abdômen",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Goblet com Halter / Kettlebell", "Quadríceps e Glúteos", 4, "15 reps", restSec, "Halter", "Mantenha o peito aberto e desça com controle nos quadris."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Thruster com Halteres", "Corpo Inteiro e Ombros", 4, "12 reps", restSec, "Halteres", "Agache e suba empurrando os halteres acima da cabeça em um só movimento."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Lenhador na Polia (Woodchops)", "Oblíquos e Core", 3, "15 cada lado", restSec, "Polia", "Gire o tronco mantendo a contração constante do abdômen."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Remo Seco / Esteira HIIT", "Cardio e Costas", 5, "45s tiro", 30, "Aparelho Cardio", "Impulsione com força nas pernas em alta intensidade."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação de Pernas na Barra", "Abdômen Inferior", 3, "15 reps", restSec, "Barra Fixa", "Suba as pernas sem balançar o corpo.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento com Salto Explosivo", "Quadríceps e Glúteos", 4, "15 reps", restSec, "Peso Corporal", "Amorteça a queda com a ponta dos pés."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão de Braços com Toque no Peito", "Peito e Tríceps", 4, "12 reps", restSec, "Peso Corporal", "Desça até encostar o peito no chão e suba firme."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Alpinista (Mountain Climbers)", "Core e Cardio", 4, "40 segundos", 30, "Peso Corporal", "Acelere os joelhos em direção ao peito com costas retas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Abdominal Bicicleta", "Oblíquos", 3, "20 reps", restSec, "Colchonete", "Giro controlado do cotovelo ao joelho oposto."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Burpees com Salto", "Cardio Intenso", 4, "10 reps", restSec, "Peso Corporal", "Peito no chão, levante e salte estendendo os braços.")
                )
            ),
            WorkoutDay(
                dayNumber = 2,
                dayTitle = "Dia 2: Ter",
                name = "Superiores Tonificados & Definição",
                focus = "Costas, Ombros e Peitoral",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Puxada Frontal na Polia", "Costas e Dorsais", 4, "12-15 reps", restSec, "Puxador", "Puxe a barra em direção à clavícula retraindo as escápulas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Supino Inclinado com Halteres", "Peitoral Superior", 4, "12 reps", restSec, "Banco e Halteres", "Desça alongando o peitoral e empurre com controle."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação Lateral com Halteres", "Ombros Laterais", 3, "15 reps", 30, "Halteres", "Cotovelos levemente flexionados, segure 1 segundo no topo."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Corda Naval (Battle Ropes)", "Cardio e Ombros", 4, "30 segundos", 30, "Corda Naval", "Ondas rápidas e ritmadas com joelhos semi-flexionados.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Pike (Foco em Ombros)", "Deltóides", 4, "10-12 reps", restSec, "Peso Corporal", "Quadril elevado formando um V invertido."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada Australiana / Toalha na Porta", "Dorsais", 4, "15 reps", restSec, "Toalha ou Barra Baixa", "Puxe o peito contra o apoio contraindo bem as costas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Diamante", "Tríceps e Peito", 3, "10-12 reps", restSec, "Peso Corporal", "Mãos unidas formando um triângulo sob o peito."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Toque nos Ombros em Prancha", "Estabilidade do Core", 4, "20 toques", 30, "Peso Corporal", "Evite balançar os quadris durante os toques.")
                )
            ),
            WorkoutDay(
                dayNumber = 3,
                dayTitle = "Dia 3: Qua",
                name = "Recuperação Ativa & Mobilidade",
                focus = "Descanso, caminhada e alongamento",
                durationMinutes = 25,
                isRestDay = true,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Caminhada Rápida ao Ar Livre", "Base Aeróbica", 1, "30 min", 0, "Caminhada", "Mantenha um ritmo firme e respiração constante."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Alongamento Dinâmico de Quadril", "Mobilidade", 3, "5 cada lado", 0, "Colchonete", "Avanço profundo abrindo a caixa torácica."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Postura do Pombo & Posterior", "Glúteos e Isquiotibiais", 2, "60s por perna", 0, "Colchonete", "Respire fundo aliviando a tensão lombar e pélvica.")
                )
            ),
            WorkoutDay(
                dayNumber = 4,
                dayTitle = "Dia 4: Qui",
                name = "Inferiores & Queima de Gordura",
                focus = "Glúteos, Posteriores e Quadríceps",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Leg Press 45° (Pés Altos)", "Glúteos e Coxas", 4, "15 reps", restSec, "Leg Press", "Desça com amplitude sem tirar a lombar do encosto."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Passada Caminhando com Halteres", "Pernas e Core", 3, "20 passos", restSec, "Halteres", "Tronco ereto, joelho de trás rente ao chão."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Swing com Kettlebell", "Cadeia Posterior e Cardio", 4, "20 reps", 30, "Kettlebell", "Projete o quadril para frente com explosão."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Panturrilha em Pé", "Panturrilhas", 3, "20 reps", restSec, "Máquina / Degrau", "Alongue bem embaixo e segure 1 segundo no topo.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Passada com Insistência", "Glúteos e Coxas", 4, "20 passos", restSec, "Peso Corporal", "Dois pulsos curtos no fundo de cada passada."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação Pélvica Unilateral", "Glúteos e Posterior", 3, "12 cada perna", restSec, "Colchonete", "Empurre o chão com o calcanhar e contraia o glúteo."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Saltos Patinador (Skater Jumps)", "Cardio e Estabilidade", 4, "40 segundos", 30, "Peso Corporal", "Salte lateralmente aterrissando com equilíbrio."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Isométrico na Parede", "Quadríceps", 3, "45 segundos", restSec, "Parede", "Coxas paralelas ao chão formando 90 graus.")
                )
            ),
            WorkoutDay(
                dayNumber = 5,
                dayTitle = "Dia 5: Sex",
                name = "Abdômen de Ferro & Queima Final",
                focus = "Lombar, Abdômen e Resistência",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Abdominal na Polia Alta com Corda", "Reto Abdominal", 4, "15 reps", 30, "Polia Alta", "Enrole o tronco aproximando as costelas do quadril."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Caminhada Inclinada na Esteira", "Queima de Gordura", 1, "20 min", 0, "Esteira (12% inc, 5km/h)", "Zona ideal de oxidação lipídica constante."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação de Joelhos na Cadeira do Capitão", "Abdômen Inferior", 3, "15 reps", 30, "Aparelho Torre", "Controle a descida sem balanço."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Slam com Medicine Ball", "Potência e Core", 4, "15 reps", 30, "Med Ball", "Arremesse a bola com força contra o chão usando o abdômen.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Prancha Baixa para Alta", "Estabilidade de Core", 4, "12 reps", 30, "Peso Corporal", "Suba alternando os braços sem oscilar o quadril."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Russian Twist com Pernas Elevadas", "Oblíquos", 4, "30 giros", 30, "Peso Corporal", "Gire os ombros tocando as mãos ao lado do quadril."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Tesoura Abdominal (Flutter Kicks)", "Abdômen Inferior", 3, "45 segundos", 30, "Colchonete", "Lombar colada no chão enquanto bate as pernas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Sombra de Boxe HIIT", "Cardio e Agilidade", 4, "60 segundos", 30, "Peso Corporal", "Golpes rápidos com movimentação de pernas.")
                )
            ),
            WorkoutDay(
                dayNumber = 6,
                dayTitle = "Dia 6: Sáb",
                name = if (level.contains("Iniciante", ignoreCase = true)) "Passeio ao Ar Livre & Lazer" else "Circuito Funcional & Agilidade",
                focus = if (level.contains("Iniciante", ignoreCase = true)) "Estilo de vida ativo" else "Condicionamento físico geral",
                durationMinutes = 35,
                isRestDay = level.contains("Iniciante", ignoreCase = true),
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Corrida Estacionária Joelhos Altos", "Cardio", 4, "30 segundos", 30, "Peso Corporal", "Eleve os joelhos até a linha da cintura."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Crawl do Urso (Bear Crawl)", "Ombros e Core", 3, "30 passos", 45, "Peso Corporal", "Joelhos a 5cm do chão, rasteje com firmeza."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Prancha Lateral Isométrica", "Core Lateral", 3, "30s cada lado", 30, "Colchonete", "Corpo em linha reta da cabeça aos tornozelos.")
                )
            ),
            WorkoutDay(
                dayNumber = 7,
                dayTitle = "Dia 7: Dom",
                name = "Descanso Total & Repouso Muscular",
                focus = "Descanso, recuperação e hidratação",
                durationMinutes = 15,
                isRestDay = true,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Liberação Miofascial / Auto-massagem", "Alívio Muscular", 1, "15 min", 0, "Rolo / Colchonete", "Passe o rolo nas pernas, costas e panturrilhas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Respiração Diafragmática & Relaxamento", "Redução do Cortisol", 1, "5 min", 0, "Colchonete", "Inspire pelo nariz enchendo o abdômen e expire devagar.")
                )
            )
        )
    }

    private fun generateMuscleGainPlan(profile: UserProfile, isGym: Boolean, level: String): List<WorkoutDay> {
        val restSec = 75
        val duration = if (level.contains("Iniciante", ignoreCase = true) || level == "Beginner") 45 else 55

        return listOf(
            WorkoutDay(
                dayNumber = 1,
                dayTitle = "Dia 1: Seg",
                name = "Treino Push (Peito, Ombros e Tríceps)",
                focus = "Hipertrofia peitoral e deltoides",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Supino Reto com Barra", "Peitoral Maior", 4, "8-10 reps", 90, "Barra e Banco Reto", "Retraia as escápulas, toque no osso esterno e empurre com força."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Supino Inclinado com Halteres", "Peitoral Superior", 3, "10-12 reps", restSec, "Banco 30° e Halteres", "Alongamento total na descida, contração forte no topo."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Desenvolvimento com Halteres Sentado", "Deltóide Anterior", 3, "10-12 reps", restSec, "Halteres", "Desça até a linha das orelhas e empurre sem bater os pesos."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Crucifixo na Polia (Crossover)", "Isolamento de Peito", 3, "12-15 reps", 60, "Polia Dupla", "Movimento de abraçar uma árvore grande, aperte no centro."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Tríceps Corda na Polia", "Tríceps Lateral", 4, "12 reps", 60, "Polia Alta", "Cotovelos travados ao lado do tronco, abra a corda embaixo.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão de Braços Tradicional com Cadência", "Peitoral", 4, "12-15 reps", restSec, "Peso Corporal", "3 segundos na descida e subida explosiva."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Declinada (Pés no Sofá)", "Peitoral Superior", 3, "10-12 reps", restSec, "Cadeira / Sofá", "Pés apoiados no alto, foco na parte alta do peito."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Pike (Desenvolvimento Corporal)", "Ombros", 3, "8-10 reps", restSec, "Peso Corporal", "Desça a cabeça à frente das mãos em triângulo."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Mergulho entre Cadeiras / Banco", "Tríceps", 4, "12-15 reps", 60, "Cadeira / Apoio", "Costas coladas no assento, desça até 90 graus nos cotovelos.")
                )
            ),
            WorkoutDay(
                dayNumber = 2,
                dayTitle = "Dia 2: Ter",
                name = "Treino Pull (Costas, Deltóide Posterior e Bíceps)",
                focus = "Dorsais, espessura de costas e pico do bíceps",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada Curvada com Barra", "Costas e Rombóides", 4, "8-10 reps", 90, "Barra Olímpica", "Tronco a 45 graus, puxe a barra no umbigo apertando as costas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Puxada Frontal Aberta / Barra Fixa", "Dorsal e Asa", 4, "8-12 reps", restSec, "Puxador ou Barra", "Puxe os cotovelos para baixo em direção aos bolsos."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada Unilateral com Halter (Serrote)", "Dorsal Profunda", 3, "10-12 reps", restSec, "Halter e Banco", "Puxe com o cotovelo rentes às costelas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Face Pull com Corda na Polia", "Deltóide Posterior e Manguito", 4, "15 reps", 60, "Polia Alta", "Puxe a corda em direção aos olhos com cotovelos altos."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Rosca Direta Inclinada com Halteres", "Bíceps Cabeça Longa", 3, "10-12 reps", 60, "Halteres e Banco 45°", "Alongamento atrás do tronco com supinação no topo.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada Australiana com Toalha na Porta", "Costas e Dorsais", 4, "12-15 reps", restSec, "Toalha e Porta", "Puxe o peito firme contra a porta contraindo a musculatura dorsal."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Superman com Retração Escapular", "Lombar e Costas", 3, "15 reps", 60, "Colchonete", "Eleve o peito e junte as escápulas com firmeza."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Anjos Inversos no Chão", "Deltóide Posterior", 3, "15 reps", 60, "Peso Corporal", "Deitado de bruços, varra os braços acima da cabeça sem tocar o chão."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Rosca Bíceps com Mochila Carregada", "Bíceps", 3, "12 reps", 60, "Mochila com Peso", "Cotovelos colados ao corpo, contração máxima no pico.")
                )
            ),
            WorkoutDay(
                dayNumber = 3,
                dayTitle = "Dia 3: Qua",
                name = "Pernas Completas & Panturrilhas",
                focus = "Quadríceps, posteriores e glúteos",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Livre com Barra", "Quadríceps e Glúteos", 4, "8-10 reps", 90, "Gaiola de Agachamento", "Quebre a linha paralela com peito aberto e joelhos alinhados aos pés."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Stiff com Barra ou Halteres", "Posteriores de Coxa", 4, "10-12 reps", restSec, "Barra / Halteres", "Empurre o quadril para trás mantendo a coluna lombar neutra."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Cadeira Extensora", "Isolamento de Quadríceps", 3, "12-15 reps", 60, "Máquina Extensora", "Segure 1 segundo no pico de contração em cada repetição."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Mesa Flexora", "Posteriores de Coxa", 3, "12 reps", 60, "Mesa Flexora", "Controle a volta do peso por 3 segundos."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Gêmeos Sentado na Máquina", "Panturrilhas e Sóleo", 4, "15 reps", 45, "Máquina Panturrilha", "Alongamento total na descida e contração máxima na ponta dos pés.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Búlgaro", "Quadríceps e Glúteos", 4, "10 cada perna", restSec, "Cadeira / Apoio", "Pé de trás elevado, desça o joelho traseiro quase até o chão."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Livre com Pausa de 2s", "Quadríceps", 4, "15-20 reps", restSec, "Peso Corporal", "Desça lento e segure 2 segundos no fundo."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Stiff Unilateral (Perna Única)", "Posteriores e Equilíbrio", 3, "12 cada perna", restSec, "Peso Corporal", "Flexione o quadril mantendo a perna de trás em linha reta."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Panturrilha Unilateral no Degrau", "Panturrilhas", 4, "15 cada perna", 45, "Degrau", "Deixe o calcanhar descer além da borda do degrau.")
                )
            ),
            WorkoutDay(
                dayNumber = 4,
                dayTitle = "Dia 4: Qui",
                name = "Descanso Ativo & Síntese Proteica",
                focus = "Nutrição, recuperação e descanso celular",
                durationMinutes = 20,
                isRestDay = true,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Mobilidade Articular e Quadril", "Flexibilidade", 1, "20 min", 0, "Colchonete", "Alongamento do pombo, rotação torácica e gato-vaca."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Meta de Hidratação e Proteína", "Recuperação Muscular", 1, "Meta Diária", 0, "Nutrição", "Consuma de 1,6g a 2,0g de proteína por kg corporal e 3L de água.")
                )
            ),
            WorkoutDay(
                dayNumber = 5,
                dayTitle = "Dia 5: Sex",
                name = "Treino Superior com Foco em Braços & Ombros",
                focus = "Deltoides, bíceps, tríceps e peito",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Desenvolvimento Arnold com Halteres", "Deltóides Completos", 4, "10-12 reps", restSec, "Halteres", "Gire as palmas das mãos para fora durante a subida."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada Baixa Triângulo na Polia", "Espessura de Costas", 4, "10-12 reps", restSec, "Polia Baixa", "Mantenha o tronco firme e puxe o triângulo no abdômen."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação Lateral com Halteres", "Deltóide Lateral", 3, "15 reps", 45, "Halteres", "Suba até a altura dos ombros controlando a descida."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Rosca Direta Barra W", "Bíceps", 3, "10-12 reps", 60, "Barra W", "Cotovelos travados na lateral do corpo."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Tríceps Testa com Barra W", "Tríceps Cabeça Longa", 3, "12 reps", 60, "Barra W e Banco", "Desça a barra na direção da testa sem abrir os cotovelos.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Arqueiro", "Peito e Tríceps Unilateral", 3, "8 cada lado", restSec, "Peso Corporal", "Estenda um braço de lado enquanto flexiona o outro."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Rosca Isométrica no Batente da Porta", "Bíceps", 3, "45 segundos", 60, "Batente da Porta", "Puxe para dentro gerando tensão máxima contra a madeira."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Diamante Fechada", "Tríceps e Miolo de Peito", 4, "12 reps", 60, "Peso Corporal", "Mantenha o ritmo lento e constante."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Canoa Isométrica (Hollow Body)", "Estabilidade de Core", 3, "45 segundos", 45, "Colchonete", "Pressione a coluna lombar totalmente contra o chão.")
                )
            ),
            WorkoutDay(
                dayNumber = 6,
                dayTitle = "Dia 6: Sáb",
                name = if (level.contains("Iniciante", ignoreCase = true)) "Abdômen & Caminhada Leve" else "Cadeia Posterior, Glúteos & Core",
                focus = "Posteriores, abdômen e fortalecimento lombar",
                durationMinutes = 35,
                isRestDay = level.contains("Iniciante", ignoreCase = true),
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação de Pernas no Chão / Barra", "Abdômen Inferior", 4, "12-15 reps", 45, "Colchonete", "Elevação controlada do quadril sem impulso."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Giro Russo com Carga / Peso", "Oblíquos", 3, "15 cada lado", 45, "Halter / Anilha", "Giro potente com pausa de 1 segundo de cada lado."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Prancha Militar (Sobe e Desce)", "Core e Ombros", 3, "12 reps", 45, "Peso Corporal", "Alterne o braço de subida a cada repetição.")
                )
            ),
            WorkoutDay(
                dayNumber = 7,
                dayTitle = "Dia 7: Dom",
                name = "Descanso Absoluto & Crescimento",
                focus = "Recuperação muscular e recarga mental",
                durationMinutes = 15,
                isRestDay = true,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Hidratação & Reposição Hídrica", "Hidratação", 1, "3+ Litros", 0, "Água", "Beba água regularmente para recarregar o glicogênio muscular."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Sono Reparador de 8 Horas", "Liberação de GH", 1, "8 Horas", 0, "Sono", "O sono profundo é a janela primordial da hipertrofia.")
                )
            )
        )
    }

    private fun generateStrengthPlan(profile: UserProfile, isGym: Boolean, level: String): List<WorkoutDay> {
        val restSec = 100
        val duration = 50

        return listOf(
            WorkoutDay(
                dayNumber = 1,
                dayTitle = "Dia 1: Seg",
                name = "Agachamento Pesado & Força de Pernas",
                focus = "Força máxima em membros inferiores",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Livre com Barra Pesado", "Pernas e Core", 5, "5 reps", 120, "Gaiola", "Séries de força com respiração diafragmática travada."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Stiff com Barra Olímpica", "Isquiotibiais e Glúteos", 4, "6-8 reps", restSec, "Barra", "Desça controlando a fase excêntrica e suba com explosão."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Passo do Fazendeiro com Halteres Pesados", "Pegada e Core", 4, "40 passos", restSec, "Halteres Pesados", "Caminhe ereto com ombros estabilizados para baixo.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Pistola (Pistol Squat)", "Força Unilateral", 4, "5 cada perna", 90, "Peso Corporal", "Desça controlando o equilíbrio sobre um pé só."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Elevação Pélvica com Carga / Mochila", "Glúteos e Força", 4, "8 cada perna", restSec, "Mochila / Sofá", "Segure 2s no topo em extensão completa do quadril."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Agachamento Búlgaro com Descida de 4s", "Quadríceps", 4, "8 cada perna", restSec, "Cadeira", "Descida lenta de 4 segundos e subida potente.")
                )
            ),
            WorkoutDay(
                dayNumber = 2,
                dayTitle = "Dia 2: Ter",
                name = "Supino Pesado & Potência Superior",
                focus = "Empurrar horizontal e força peitoral",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Supino Reto com Barra Olímpica", "Peito e Tríceps", 5, "5 reps", 120, "Banco e Barra", "Pés firmes no chão, arco escapular e explosão ao subir."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada Curvada com Carga", "Costas e Dorsais", 4, "6 reps", 90, "Barra", "Movimento estrito sem jogar o tronco para trás."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Desenvolvimento Militar em Pé com Barra", "Deltóides e Core", 4, "6 reps", 90, "Barra", "Trave o abdômen e empurre a barra acima da cabeça.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão com Mochila Pesada", "Peito e Tríceps", 5, "6-8 reps", 90, "Mochila com Livros", "Coloque peso na mochila mantendo a postura reta."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Pike com Pés na Cadeira", "Força de Ombros", 4, "6-8 reps", 90, "Cadeira", "Pés na cadeira, abaixe o topo da cabeça com controle."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada na Toalha com Pausa de 2s", "Costas", 4, "8 reps", 90, "Toalha e Porta", "Puxe com força máxima e segure 2 segundos no peito.")
                )
            ),
            WorkoutDay(dayNumber = 3, dayTitle = "Dia 3: Qua", name = "Descanso do SNC & Mobilidade", focus = "Recuperação neural ativa", durationMinutes = 20, isRestDay = true),
            WorkoutDay(
                dayNumber = 4,
                dayTitle = "Dia 4: Qui",
                name = "Levantamento Terra & Potência Posterior",
                focus = "Puxada bruta e cadeia posterior total",
                durationMinutes = duration,
                isRestDay = false,
                exercises = if (isGym) listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Levantamento Terra Convencional", "Cadeia Posterior", 5, "5 reps", 120, "Barra Olímpica", "Tire a folga da barra e empurre o chão com os pés."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Barra Fixa com Peso / Puxador Pesado", "Dorsais e Bíceps", 4, "6 reps", 90, "Barra / Puxador", "Extensão completa embaixo, queixo claramente acima da barra."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Encolhimento com Barra Pesada", "Trapézio", 3, "8 reps", 60, "Barra", "Eleve os ombros em direção às orelhas sem girar.")
                ) else listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexão Nórdica Excêntrica", "Tendões Isquiotibiais", 4, "5 reps", 90, "Apoio Firme", "Desça o mais devagar possível segurando com as pernas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Barra Fixa ou Remada Invertida", "Costas e Bíceps", 4, "6-8 reps", 90, "Mesa Firme ou Barra", "Amplitude total com pausa de contração."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Roda Abdominal / Prancha Longa", "Anti-extensão de Core", 4, "8 reps", 60, "Colchonete", "Trave o abdômen sem deixar a lombar curvar.")
                )
            ),
            WorkoutDay(
                dayNumber = 5,
                dayTitle = "Dia 5: Sex",
                name = "Acessórios de Força & Estabilização",
                focus = "Ombros, braços e anti-rotação",
                durationMinutes = 45,
                isRestDay = false,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Pressão Pallof na Polia ou Elástico", "Anti-rotação de Core", 4, "10 cada lado", 45, "Elástico / Polia", "Resista ao giro do tronco, segure 2 segundos esticado."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Supino Fechado ou Flexão Fechada", "Força de Tríceps", 4, "8 reps", 60, "Barra / Chão", "Cotovelos rentes ao tronco."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Rosca Martelo com Halteres", "Braquial e Antebraço", 3, "8-10 reps", 60, "Halteres", "Pegada neutra, subida controlada e firme.")
                )
            ),
            WorkoutDay(dayNumber = 6, dayTitle = "Dia 6: Sáb", name = "Recuperação & Alongamentos Finais", focus = "Articulações e relaxamento", durationMinutes = 20, isRestDay = true),
            WorkoutDay(dayNumber = 7, dayTitle = "Dia 7: Dom", name = "Recarga Total de Força", focus = "Preparação para a nova semana", durationMinutes = 15, isRestDay = true)
        )
    }

    private fun generateEndurancePlan(profile: UserProfile, isGym: Boolean, level: String): List<WorkoutDay> {
        val restSec = 30
        val duration = 45

        return listOf(
            WorkoutDay(
                dayNumber = 1,
                dayTitle = "Dia 1: Seg",
                name = "Resistência Muscular de Alto Volume",
                focus = "Limiar de lactato e ritmo contínuo",
                durationMinutes = duration,
                isRestDay = false,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Circuito de Agachamentos Livres", "Quadríceps e Glúteos", 4, "25 reps", restSec, "Peso Corporal", "Ritmo contínuo sem pausas longas."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Flexões de Braço com Prancha Isométrica", "Peito e Core", 4, "15 reps + 20s prancha", restSec, "Peso Corporal", "Controle de respiração para resistência."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Passada Alternada sem Parar", "Fôlego e Pernas", 3, "24 passos", restSec, "Peso Corporal", "Mantenha passada constante."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Polichinelos em Alta Velocidade", "Cardiovascular", 4, "60 segundos", 20, "Peso Corporal", "Ritmo acelerado e respiração sincronizada.")
                )
            ),
            WorkoutDay(
                dayNumber = 2,
                dayTitle = "Dia 2: Ter",
                name = "Base Aeróbica & Corrida Constante",
                focus = "Resistência cardiovascular na Zona 2",
                durationMinutes = 40,
                isRestDay = false,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Trote Contínuo / Ciclismo / Caminhada Veloz", "Base Aeróbica", 1, "35 min", 0, "Corrida / Bike", "Ritmo onde você consiga manter uma conversa."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Mobilidade de Tornozelos e Panturrilhas", "Prevenção de Lesões", 2, "10 cada lado", 0, "Parede", "Movimentos articulares para preservar as canelas.")
                )
            ),
            WorkoutDay(dayNumber = 3, dayTitle = "Dia 3: Qua", name = "Recuperação no Meio da Semana", focus = "Alongar e Hidratar", durationMinutes = 20, isRestDay = true),
            WorkoutDay(
                dayNumber = 4,
                dayTitle = "Dia 4: Qui",
                name = "Resistência de Superiores & Core",
                focus = "Ombros, costas e abdômen",
                durationMinutes = duration,
                isRestDay = false,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Remada com Elástico ou Toalha", "Costas", 4, "20 reps", restSec, "Elástico / Toalha", "Volume alto mantendo a contração constante."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Alpinista Contínuo", "Cardio e Core", 4, "45 segundos", 30, "Peso Corporal", "Pés ágeis com abdômen bem travado."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Caminhada em Prancha com as Mãos", "Cadeia Anterior", 3, "12 reps", restSec, "Peso Corporal", "Caminhe com as mãos à frente da cabeça e retorne.")
                )
            ),
            WorkoutDay(
                dayNumber = 5,
                dayTitle = "Dia 5: Sex",
                name = "Desafio da Pirâmide Metabólica",
                focus = "Frequência cardíaca e fôlego de aço",
                durationMinutes = duration,
                isRestDay = false,
                exercises = listOf(
                    WorkoutExercise(UUID.randomUUID().toString(), "Pirâmide de Burpees (10 até 1 rep)", "Corpo Inteiro", 1, "55 reps totais", 30, "Peso Corporal", "Faça 10 reps, respire, faça 9 reps... até chegar a 1."),
                    WorkoutExercise(UUID.randomUUID().toString(), "Corrida com Joelhos Altos", "Agilidade", 4, "45 segundos", 30, "Peso Corporal", "Suba os joelhos com intensidade máxima.")
                )
            ),
            WorkoutDay(dayNumber = 6, dayTitle = "Dia 6: Sáb", name = "Esporte Livre ou Ciclismo", focus = "Resistência funcional ao ar livre", durationMinutes = 40, isRestDay = false, exercises = listOf(
                WorkoutExercise(UUID.randomUUID().toString(), "Futebol, Ciclismo, Natação ou Corrida", "Lazer Ativo", 1, "40 min", 0, "Livre", "Aproveite para se movimentar ao ar livre com energia.")
            )),
            WorkoutDay(dayNumber = 7, dayTitle = "Dia 7: Dom", name = "Recuperação Total & Sono", focus = "Regeneração celular", durationMinutes = 15, isRestDay = true)
        )
    }

    fun getDailyCoachTip(goal: String): String {
        return when {
            goal.contains("Perda", ignoreCase = true) || goal.contains("Weight", ignoreCase = true) ->
                "Dica do Coach: Uma caminhada de 15 minutos logo após as refeições reduz os picos de glicose no sangue e acelera a queima de gordura ao longo do dia!"
            goal.contains("Ganho", ignoreCase = true) || goal.contains("Muscle", ignoreCase = true) ->
                "Dica do Coach: O músculo não cresce no treino, cresce no descanso! Garanta de 7 a 9 horas de sono profundo para maximizar a liberação natural de GH."
            goal.contains("Força", ignoreCase = true) || goal.contains("Strength", ignoreCase = true) ->
                "Dica do Coach: O descanso entre séries de força é sagrado! Esperar de 2 a 3 minutos restaura os estoques de ATP e garante potência máxima na próxima série."
            goal.contains("Resistência", ignoreCase = true) || goal.contains("Endurance", ignoreCase = true) ->
                "Dica do Coach: Controle o ritmo! Sincronize sua respiração com a cadência dos passos para otimizar o oxigênio e retardar a queima do ácido lático."
            else ->
                "Dica do Coach: A consistência vence a intensidade todos os dias. Vista o tênis, vença a preguiça e mantenha seus dias seguidos ativos!"
        }
    }
}
