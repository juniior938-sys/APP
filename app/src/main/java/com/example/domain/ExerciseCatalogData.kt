package com.example.domain

import com.example.data.model.WorkoutExercise
import java.util.UUID

data class ExerciseCategoryGroup(
    val id: String,
    val title: String,
    val subtitle: String,
    val gender: String, // "Masculino", "Feminino", "Ambos"
    val muscleGroup: String, // "Peito", "Costas", "Pernas", "Glúteos", "Ombros", "Bíceps", "Tríceps", "Abdômen", "Cardio"
    val durationMinutes: Int,
    val caloriesEstimate: Int,
    val level: String = "Intermediário",
    val exercises: List<WorkoutExercise>
)

object ExerciseCatalogData {

    // ==========================================
    // EXERCÍCIOS MASCULINO - SINGLE BODY PART
    // (Fiel à imagem 2 enviada: Yadav Fitness Gym)
    // ==========================================

    val maleChestExercises = listOf(
        WorkoutExercise(
            id = "m_ch_1",
            name = "1. Barbell Bench Press (Supino Reto com Barra)",
            targetMuscle = "Peitoral Maior & Médio",
            sets = 4,
            reps = "8-12 reps",
            restSeconds = 60,
            equipment = "Barra Olímpica e Banco Reto",
            tips = "Apoie os pés firmes no chão, mantenha as escápulas retraídas e desça a barra controlada até a linha dos mamilos."
        ),
        WorkoutExercise(
            id = "m_ch_2",
            name = "2. Incline Dumbbell Press (Supino Inclinado com Halteres)",
            targetMuscle = "Peitoral Superior (Clavicular)",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Banco Inclinado (30°-45°) e Halteres",
            tips = "Alongue bem o peito na descida e empurre os halteres para cima convergindo sem encostá-los no topo."
        ),
        WorkoutExercise(
            id = "m_ch_3",
            name = "3. Decline Bench Press (Supino Declinado)",
            targetMuscle = "Peitoral Inferior",
            sets = 3,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Banco Declinado e Barra/Halteres",
            tips = "Foco na porção inferior do peitoral, segurando com firmeza e controlando a descida."
        ),
        WorkoutExercise(
            id = "m_ch_4",
            name = "4. Chest Fly (Crucifixo Máquina / Halteres)",
            targetMuscle = "Peitoral Esternal & Abertura",
            sets = 3,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Aparelho Voador (Peck Deck) ou Halteres",
            tips = "Mantenha uma leve flexão nos cotovelos e sinta o peitoral alongar ao máximo antes de apertar no meio."
        ),
        WorkoutExercise(
            id = "m_ch_5",
            name = "5. Cable Crossover (Crossover na Polia)",
            targetMuscle = "Definição Peitoral & Miolo",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Polia Dupla com Pegadores",
            tips = "Tronco levemente inclinado para frente, cruze os punhos à frente do quadril concentrando a contração."
        ),
        WorkoutExercise(
            id = "m_ch_6",
            name = "6. Push-Ups (Flexão de Braços)",
            targetMuscle = "Peitoral, Tríceps e Core",
            sets = 3,
            reps = "Até a Falha (15-20 reps)",
            restSeconds = 45,
            equipment = "Peso Corporal",
            tips = "Corpo alinhado em linha reta, desça o peito próximo ao chão e suba com explosão controlada."
        )
    )

    val maleBackExercises = listOf(
        WorkoutExercise(
            id = "m_bk_1",
            name = "1. Lat Pulldown (Puxada Frontal na Polia)",
            targetMuscle = "Grande Dorsal (Asas)",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Puxador Alto e Barra Aberta",
            tips = "Puxe a barra em direção ao peito estufando a caixa torácica e espremendo as escápulas."
        ),
        WorkoutExercise(
            id = "m_bk_2",
            name = "2. Pull-ups (Barra Fixa Pronada)",
            targetMuscle = "Dorsal e Bíceps",
            sets = 4,
            reps = "6-10 reps (ou até a falha)",
            restSeconds = 75,
            equipment = "Barra Fixa",
            tips = "Suba até passar o queixo da barra sem chutar as pernas, controlando totalmente a descida."
        ),
        WorkoutExercise(
            id = "m_bk_3",
            name = "3. Barbell Deadlift (Levantamento Terra)",
            targetMuscle = "Costas Completa, Trapézio e Posterior",
            sets = 4,
            reps = "6-8 reps",
            restSeconds = 90,
            equipment = "Barra Olímpica e Anilhas",
            tips = "Coluna reta, barra colada nas canelas, empurre o chão com os calcanhares mantendo o core blindado."
        ),
        WorkoutExercise(
            id = "m_bk_4",
            name = "4. Seated Cable Row (Remada Baixa Sentada)",
            targetMuscle = "Rombóides e Meio das Costas",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Remada Baixa com Triângulo",
            tips = "Coluna ereta, puxe o triângulo contra o umbigo retraindo as escápulas no final do movimento."
        ),
        WorkoutExercise(
            id = "m_bk_5",
            name = "5. T-Bar Row (Remada Cavalinho / Barra T)",
            targetMuscle = "Espessura de Costas e Dorsais",
            sets = 4,
            reps = "8-10 reps",
            restSeconds = 60,
            equipment = "Aparelho Cavalinho ou Barra no Canto",
            tips = "Incline o tronco a 45 graus, puxe puxando com os cotovelos sem puxar com a lombar."
        ),
        WorkoutExercise(
            id = "m_bk_6",
            name = "6. One-Arm Dumbbell Row (Remada Unilateral / Serrote)",
            targetMuscle = "Dorsal Unilateral",
            sets = 3,
            reps = "10-12 cada braço",
            restSeconds = 45,
            equipment = "Banco e Halter Pesado",
            tips = "Puxe o halter na direção do quadril mantendo as costas paralelas ao chão."
        )
    )

    val maleLegsExercises = listOf(
        WorkoutExercise(
            id = "m_lg_1",
            name = "1. Barbell Squats (Agachamento Livre com Barra)",
            targetMuscle = "Quadríceps, Glúteos e Core",
            sets = 4,
            reps = "8-10 reps",
            restSeconds = 90,
            equipment = "Gaiola de Agachamento e Barra",
            tips = "Pés na largura dos ombros, desça abaixo da linha dos joelhos mantendo o peito erguido."
        ),
        WorkoutExercise(
            id = "m_lg_2",
            name = "2. Leg Press (Leg Press 45°)",
            targetMuscle = "Quadríceps e Glúteos",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 75,
            equipment = "Aparelho Leg Press 45°",
            tips = "Pés firmes na plataforma, não tire a lombar do apoio nem estenda totalmente os joelhos no topo."
        ),
        WorkoutExercise(
            id = "m_lg_3",
            name = "3. Lunges (Avanço / Passada com Halteres)",
            targetMuscle = "Quadríceps e Glúteos",
            sets = 3,
            reps = "12 passos por perna",
            restSeconds = 60,
            equipment = "Halteres",
            tips = "Dê o passo largo e desça o joelho de trás até quase encostar no solo com tronco vertical."
        ),
        WorkoutExercise(
            id = "m_lg_4",
            name = "4. Leg Extension (Cadeira Extensora)",
            targetMuscle = "Isolamento de Quadríceps",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Cadeira Extensora",
            tips = "Segure 1 segundo na contração máxima no alto antes de descer de forma controlada."
        ),
        WorkoutExercise(
            id = "m_lg_5",
            name = "5. Leg Curl (Mesa Flexora)",
            targetMuscle = "Isquiotibiais (Posterior de Coxa)",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Mesa Flexora Deitada",
            tips = "Puxe os calcanhares em direção aos glúteos sem levantar a cintura do estofado."
        ),
        WorkoutExercise(
            id = "m_lg_6",
            name = "6. Calf Raises (Elevação de Panturrilha)",
            targetMuscle = "Panturrilhas (Gastrocnêmio & Sóleo)",
            sets = 4,
            reps = "15-20 reps",
            restSeconds = 45,
            equipment = "Aparelho de Panturrilha ou Degrau",
            tips = "Alongue ao máximo no ponto mais baixo e suba na ponta dos pés segurando 2 segundos no topo."
        )
    )

    val maleShouldersExercises = listOf(
        WorkoutExercise(
            id = "m_sh_1",
            name = "1. Overhead Press (Desenvolvimento com Barra/Halteres)",
            targetMuscle = "Deltoide Anterior & Médio",
            sets = 4,
            reps = "8-10 reps",
            restSeconds = 60,
            equipment = "Barra ou Halteres",
            tips = "Empurre o peso acima da cabeça com o abdômen travado e cotovelos alinhados."
        ),
        WorkoutExercise(
            id = "m_sh_2",
            name = "2. Lateral Raises (Elevação Lateral com Halteres)",
            targetMuscle = "Deltoide Lateral (Largura dos Ombros)",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Halteres",
            tips = "Eleve os braços até a linha dos ombros com os cotovelos ligeiramente flexionados."
        ),
        WorkoutExercise(
            id = "m_sh_3",
            name = "3. Front Raises (Elevação Frontal)",
            targetMuscle = "Deltoide Anterior",
            sets = 3,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Halteres ou Barra",
            tips = "Suba o peso controlado na linha do olhar sem balançar o tronco."
        ),
        WorkoutExercise(
            id = "m_sh_4",
            name = "4. Rear Delt Fly (Crucifixo Invertido)",
            targetMuscle = "Deltoide Posterior",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Aparelho Peck Deck Invertido ou Halteres",
            tips = "Abra os braços focando exclusivamente na parte de trás dos ombros."
        ),
        WorkoutExercise(
            id = "m_sh_5",
            name = "5. Shrugs (Encolhimento de Ombros)",
            targetMuscle = "Trapézio Superior",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Halteres Pesados ou Barra",
            tips = "Encolha os ombros em direção às orelhas em movimento vertical puro, sem rodar a articulação."
        )
    )

    val maleBicepsExercises = listOf(
        WorkoutExercise(
            id = "m_bi_1",
            name = "1. Barbell Curl (Rosca Direta com Barra W)",
            targetMuscle = "Bíceps Braquial Completo",
            sets = 4,
            reps = "8-10 reps",
            restSeconds = 60,
            equipment = "Barra W e Anilhas",
            tips = "Mantenha os cotovelos colados nas costelas e suba sem jogar o quadril para frente."
        ),
        WorkoutExercise(
            id = "m_bi_2",
            name = "2. Dumbbell Curl (Rosca Alternada com Halteres)",
            targetMuscle = "Bíceps com Supinação",
            sets = 3,
            reps = "10-12 cada braço",
            restSeconds = 45,
            equipment = "Halteres",
            tips = "Inicie com pegada neutra e faça a supinação (gire a palma para cima) ao levantar."
        ),
        WorkoutExercise(
            id = "m_bi_3",
            name = "3. Hammer Curl (Rosca Martelo)",
            targetMuscle = "Braquial e Braquiorradial (Antebraço)",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Halteres",
            tips = "Pegada neutra (palmas voltadas para dentro), promovendo volume na lateral do braço."
        ),
        WorkoutExercise(
            id = "m_bi_4",
            name = "4. Preacher Curl (Rosca Scott)",
            targetMuscle = "Pico do Bíceps (Porção Curta)",
            sets = 3,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Banco Scott e Barra W",
            tips = "Apoie os braços totalmente na almofada evitando descanso no ponto mais alto."
        ),
        WorkoutExercise(
            id = "m_bi_5",
            name = "5. Cable Curl (Rosca na Polia Baixa)",
            targetMuscle = "Tensão Contínua no Bíceps",
            sets = 3,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Polia Baixa com Barra Reta/Corda",
            tips = "Tensão constante do início ao fim da repetição, espremendo no ponto alto."
        )
    )

    val maleTricepsExercises = listOf(
        WorkoutExercise(
            id = "m_tr_1",
            name = "1. Triceps Pushdown (Tríceps Pulley na Polia)",
            targetMuscle = "Cabeça Lateral do Tríceps",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Polia Alta com Barra Reta ou Corda",
            tips = "Cotovelos fixos ao lado do tronco, empurre estendendo totalmente os antebraços."
        ),
        WorkoutExercise(
            id = "m_tr_2",
            name = "2. Skull Crushers (Tríceps Testa com Barra W)",
            targetMuscle = "Cabeça Longa do Tríceps",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Banco Reto e Barra W",
            tips = "Desça a barra em direção à testa mantendo os cotovelos apontados para o teto."
        ),
        WorkoutExercise(
            id = "m_tr_3",
            name = "3. Dips (Mergulho nas Paralelas)",
            targetMuscle = "Tríceps e Peitoral Inferior",
            sets = 3,
            reps = "8-12 reps",
            restSeconds = 60,
            equipment = "Barras Paralelas ou Graviton",
            tips = "Corpo ligeiramente inclinado, desça até 90 graus nos cotovelos e suba empurrando firme."
        ),
        WorkoutExercise(
            id = "m_tr_4",
            name = "4. Overhead Triceps Extension (Tríceps Francês)",
            targetMuscle = "Cabeça Longa do Tríceps",
            sets = 3,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Halter ou Corda na Polia",
            tips = "Segure o peso acima da cabeça com as duas mãos e desça atrás da nuca alongando o tríceps."
        ),
        WorkoutExercise(
            id = "m_tr_5",
            name = "5. Close Grip Bench Press (Supino com Pegada Fechada)",
            targetMuscle = "Massa Geral de Tríceps",
            sets = 3,
            reps = "8-10 reps",
            restSeconds = 60,
            equipment = "Banco e Barra Olímpica",
            tips = "Mãos alinhadas na largura dos ombros, cotovelos rentes ao tronco na descida."
        )
    )

    val maleAbsExercises = listOf(
        WorkoutExercise(
            id = "m_ab_1",
            name = "1. Crunches (Abdominal Supra Tradicional)",
            targetMuscle = "Reto Abdominal Superior",
            sets = 4,
            reps = "15-20 reps",
            restSeconds = 30,
            equipment = "Colchonete",
            tips = "Enrole o tronco aproximando a caixa torácica do quadril e solte o ar na subida."
        ),
        WorkoutExercise(
            id = "m_ab_2",
            name = "2. Leg Raises (Elevação de Pernas no Solo)",
            targetMuscle = "Reto Abdominal Inferior",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 30,
            equipment = "Colchonete",
            tips = "Mantenha a lombar bem apoiada no solo e eleve as pernas sem usar impulso."
        ),
        WorkoutExercise(
            id = "m_ab_3",
            name = "3. Hanging Leg Raises (Elevação de Pernas na Barra Fixa)",
            targetMuscle = "Infra Abdominal Intenso & Core",
            sets = 3,
            reps = "10-12 reps",
            restSeconds = 45,
            equipment = "Barra Fixa",
            tips = "Pendurado na barra, suba os joelhos ou pernas retas sem balançar o corpo."
        ),
        WorkoutExercise(
            id = "m_ab_4",
            name = "4. Plank (Prancha Isométrica)",
            targetMuscle = "Transverso do Abdômen e Core",
            sets = 3,
            reps = "45 a 60 segundos",
            restSeconds = 45,
            equipment = "Colchonete",
            tips = "Cotovelos no solo, glúteos contraídos e abdômen firme sem deixar o quadril cair."
        ),
        WorkoutExercise(
            id = "m_ab_5",
            name = "5. Russian Twist (Giro Russo com Carga)",
            targetMuscle = "Oblíquos e Cintura",
            sets = 3,
            reps = "20 a 30 giros totais",
            restSeconds = 30,
            equipment = "Anilha ou Medicine Ball",
            tips = "Pés ligeiramente suspensos, gire o tronco tocando a anilha de cada lado do quadril."
        )
    )

    // ==========================================
    // EXERCÍCIOS FEMININO - FOCO GLÚTEOS, PERNAS,
    // CINTURA FINA, COSTAS E BRAÇOS DEFINIDOS
    // ==========================================

    val femaleGlutesExercises = listOf(
        WorkoutExercise(
            id = "f_gl_1",
            name = "1. Barbell Hip Thrust (Elevação Pélvica com Barra)",
            targetMuscle = "Glúteo Máximo (Construção Principal)",
            sets = 4,
            reps = "10-12 reps (com 2s no pico)",
            restSeconds = 60,
            equipment = "Banco Estofado, Barra e Almofada",
            tips = "Apoie as costas no banco, queixo no peito e suba o quadril contraindo com máxima força os glúteos no alto."
        ),
        WorkoutExercise(
            id = "f_gl_2",
            name = "2. Bulgarian Split Squat (Agachamento Búlgaro)",
            targetMuscle = "Glúteo Máximo, Médio e Posterior",
            sets = 3,
            reps = "10-12 cada perna",
            restSeconds = 60,
            equipment = "Banco e Halteres",
            tips = "Pé de trás apoiado no banco, desça o quadril na diagonal sentindo o alongamento profundo do glúteo."
        ),
        WorkoutExercise(
            id = "f_gl_3",
            name = "3. Seated Hip Abduction (Cadeira Abdutora 45°)",
            targetMuscle = "Glúteo Médio e Desenho Lateral",
            sets = 4,
            reps = "15-20 reps",
            restSeconds = 45,
            equipment = "Aparelho Cadeira Abdutora",
            tips = "Incline o tronco levemente à frente para aumentar a ativação do glúteo superior lateral."
        ),
        WorkoutExercise(
            id = "f_gl_4",
            name = "4. Romanian Deadlift / Stiff com Barra ou Halteres",
            targetMuscle = "Glúteo e Isquiotibiais (Posterior de Coxa)",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Halteres ou Barra",
            tips = "Joelhos semi-flexionados, empurre o bumbum para trás sentindo esticar toda a parte posterior da coxa."
        ),
        WorkoutExercise(
            id = "f_gl_5",
            name = "5. Cable Glute Kickback (Coice no Cabo na Polia)",
            targetMuscle = "Isolamento Superior de Glúteo",
            sets = 3,
            reps = "12-15 cada perna",
            restSeconds = 45,
            equipment = "Polia Baixa com Caneleira",
            tips = "Chute para trás e ligeiramente para fora, segurando 1 segundo no topo sem arquear a lombar."
        ),
        WorkoutExercise(
            id = "f_gl_6",
            name = "6. Single-Leg Glute Bridge (Elevação Pélvica Unilateral)",
            targetMuscle = "Glúteo Máximo Unilateral & Core",
            sets = 3,
            reps = "15 cada perna",
            restSeconds = 30,
            equipment = "Colchonete",
            tips = "Excelente para corrigir assimetrias musculares e ativar os glúteos intensamente."
        )
    )

    val femaleLegsExercises = listOf(
        WorkoutExercise(
            id = "f_lg_1",
            name = "1. Sumo Squat (Agachamento Sumô com Halter)",
            targetMuscle = "Adutores (Parte Interna) e Glúteos",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 60,
            equipment = "Halter Pesado ou Kettlebell",
            tips = "Pés mais afastados com as pontas viradas para fora a 45 graus, descendo verticalmente com o peito erguido."
        ),
        WorkoutExercise(
            id = "f_lg_2",
            name = "2. Leg Press 45° com Pés no Centro",
            targetMuscle = "Quadríceps e Volume de Coxa",
            sets = 4,
            reps = "10-12 reps",
            restSeconds = 60,
            equipment = "Aparelho Leg Press 45°",
            tips = "Desça com amplitude completa sem soltar a lombar do apoio, empurrando pelo meio do pé."
        ),
        WorkoutExercise(
            id = "f_lg_3",
            name = "3. Walking Lunges (Avanço / Passada com Halteres)",
            targetMuscle = "Quadríceps, Glúteos e Condicionamento",
            sets = 3,
            reps = "20 passos no total",
            restSeconds = 60,
            equipment = "Par de Halteres",
            tips = "Passadas firmes, mantendo o abdômen contraído e joelho traseiro rente ao solo."
        ),
        WorkoutExercise(
            id = "f_lg_4",
            name = "4. Leg Extension (Cadeira Extensora Drop-set)",
            targetMuscle = "Definição do Quadríceps",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Cadeira Extensora",
            tips = "Contração de pico no topo para desenhar o contorno frontal das coxas."
        ),
        WorkoutExercise(
            id = "f_lg_5",
            name = "5. Barbell Squat (Agachamento Livre com Barra)",
            targetMuscle = "Pernas Completas & Queima Calórica",
            sets = 4,
            reps = "8-10 reps",
            restSeconds = 75,
            equipment = "Barra Olímpica",
            tips = "Agachamento profundo com postura impecável para ganho de força e tônus muscular."
        ),
        WorkoutExercise(
            id = "f_lg_6",
            name = "6. Standing Calf Raise (Elevação de Panturrilha em Pé)",
            targetMuscle = "Panturrilhas Definidas e Tornozelos",
            sets = 4,
            reps = "15-20 reps",
            restSeconds = 30,
            equipment = "Degrau ou Máquina",
            tips = "Movimento lento para esculpir as panturrilhas com elegância."
        )
    )

    val femaleUpperExercises = listOf(
        WorkoutExercise(
            id = "f_up_1",
            name = "1. Lat Pulldown Aberta (Puxada Frontal)",
            targetMuscle = "Costas e Postura Feminina Elegante",
            sets = 4,
            reps = "12-15 reps",
            restSeconds = 45,
            equipment = "Puxador Alto",
            tips = "Afina visualmente a cintura ao desenhar uma linha de costas harmônica e bonita."
        ),
        WorkoutExercise(
            id = "f_up_2",
            name = "2. Seated Cable Row (Remada Baixa no Triângulo)",
            targetMuscle = "Meio das Costas & Correção Postural",
            sets = 4,
            reps = "12 reps",
            restSeconds = 45,
            equipment = "Remada Sentada",
            tips = "Puxe retraindo as escápulas, mantendo os ombros longe das orelhas."
        ),
        WorkoutExercise(
            id = "f_up_3",
            name = "3. Face Pull com Corda na Polia",
            targetMuscle = "Deltoide Posterior e Manguito",
            sets = 3,
            reps = "15 reps",
            restSeconds = 30,
            equipment = "Polia Alta e Corda",
            tips = "Puxe a corda em direção aos olhos separando as pontas; melhora a postura instantaneamente."
        ),
        WorkoutExercise(
            id = "f_up_4",
            name = "4. Dumbbell Lateral Raise (Elevação Lateral Leve)",
            targetMuscle = "Ombros Arredondados e Tonificados",
            sets = 3,
            reps = "12-15 reps",
            restSeconds = 30,
            equipment = "Halteres Leves",
            tips = "Ombros definidos criam proporção estética perfeita com o quadril."
        ),
        WorkoutExercise(
            id = "f_up_5",
            name = "5. Triceps Rope Pushdown (Tríceps Corda)",
            targetMuscle = "Tríceps (Adeus ao 'Músculo do Tchauzinho')",
            sets = 3,
            reps = "12-15 reps",
            restSeconds = 30,
            equipment = "Polia Alta e Corda",
            tips = "Abra a corda na parte inferior contraindo os tríceps com firmeza."
        ),
        WorkoutExercise(
            id = "f_up_6",
            name = "6. Dumbbell Bicep Curl (Rosca Alternada Suave)",
            targetMuscle = "Bíceps Tonificado",
            sets = 3,
            reps = "12 reps",
            restSeconds = 30,
            equipment = "Halteres",
            tips = "Braços firmes e desenhados sem excesso de volume."
        )
    )

    val femaleAbsAndWaistExercises = listOf(
        WorkoutExercise(
            id = "f_ab_1",
            name = "1. Stomach Vacuum (Vácuo Abdominal)",
            targetMuscle = "Transverso do Abdômen (Afinamento de Cintura)",
            sets = 4,
            reps = "20-30 segundos segurando",
            restSeconds = 45,
            equipment = "Em pé ou de quatro apoios",
            tips = "Expire todo o ar dos pulmões e puxe o umbigo para dentro como se fosse encostar na coluna."
        ),
        WorkoutExercise(
            id = "f_ab_2",
            name = "2. Side Plank (Prancha Lateral Isométrica)",
            targetMuscle = "Oblíquos e Estabilidade Lateral",
            sets = 3,
            reps = "35 a 45s cada lado",
            restSeconds = 30,
            equipment = "Colchonete",
            tips = "Corpo alinhado sem deixar o quadril afundar em direção ao solo."
        ),
        WorkoutExercise(
            id = "f_ab_3",
            name = "3. Bicycle Crunches (Abdominal Bicicleta)",
            targetMuscle = "Core e Oblíquos",
            sets = 3,
            reps = "20 a 30 repetições",
            restSeconds = 30,
            equipment = "Colchonete",
            tips = "Giro fluido sem puxar a nuca com as mãos."
        ),
        WorkoutExercise(
            id = "f_ab_4",
            name = "4. Reverse Crunches (Abdominal Infra no Solo)",
            targetMuscle = "Parte Baixa da Barriga",
            sets = 3,
            reps = "15 reps",
            restSeconds = 30,
            equipment = "Colchonete",
            tips = "Eleve os quadris ligeiramente do chão sem jogar as pernas com impulso."
        ),
        WorkoutExercise(
            id = "f_ab_5",
            name = "5. Traditional Plank Hold (Prancha Isométrica)",
            targetMuscle = "Abdômen Reto e Lombar",
            sets = 3,
            reps = "45 a 60 segundos",
            restSeconds = 45,
            equipment = "Colchonete",
            tips = "Respire de forma controlada mantendo o abdômen travado."
        )
    )

    val femaleCardioExercises = listOf(
        WorkoutExercise(
            id = "f_cd_1",
            name = "1. Incline Treadmill Walk (Caminhada Inclinada na Esteira)",
            targetMuscle = "Glúteos, Posterior e Alta Queima de Gordura",
            sets = 1,
            reps = "25 a 30 min (Inclinação 10-12%, 5km/h)",
            restSeconds = 0,
            equipment = "Esteira da Academia",
            tips = "Não se apoie nos corrimãos da esteira para maximizar o recrutamento dos glúteos e queima calórica."
        ),
        WorkoutExercise(
            id = "f_cd_2",
            name = "2. Spin Bike HIIT (Bicicleta Spinning Intervalada)",
            targetMuscle = "Pernas e Sistema Cardiovascular",
            sets = 8,
            reps = "30s tiro máximo / 30s leve",
            restSeconds = 30,
            equipment = "Bicicleta Ergométrica / Spinning",
            tips = "Alterne tiros de alta carga com giros de recuperação ativa."
        ),
        WorkoutExercise(
            id = "f_cd_3",
            name = "3. Battle Ropes (Corda Naval HIIT)",
            targetMuscle = "Corpo Inteiro e Queima Acelerada",
            sets = 4,
            reps = "30 segundos de ondas",
            restSeconds = 30,
            equipment = "Corda Naval",
            tips = "Ondas rápidas mantendo a postura firme de meio agachamento."
        ),
        WorkoutExercise(
            id = "f_cd_4",
            name = "4. Squat Jacks (Polichinelo com Agachamento)",
            targetMuscle = "Glúteos e Frequência Cardíaca",
            sets = 3,
            reps = "45 segundos contínuos",
            restSeconds = 30,
            equipment = "Peso Corporal",
            tips = "Amorteça com a ponta dos pés e desça em agachamento suave."
        )
    )

    // Agrupamento em categorias para visualização no app
    val allMaleGroups = listOf(
        ExerciseCategoryGroup(
            id = "cat_m_chest",
            title = "Chest (Peito)",
            subtitle = "6 Exercícios de Supinos e Crucifixos",
            gender = "Masculino",
            muscleGroup = "Peito",
            durationMinutes = 45,
            caloriesEstimate = 340,
            exercises = maleChestExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_m_back",
            title = "Back (Costas)",
            subtitle = "6 Exercícios de Puxadas e Remadas",
            gender = "Masculino",
            muscleGroup = "Costas",
            durationMinutes = 45,
            caloriesEstimate = 360,
            exercises = maleBackExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_m_legs",
            title = "Legs (Pernas)",
            subtitle = "6 Exercícios de Agachamento e Força",
            gender = "Masculino",
            muscleGroup = "Pernas",
            durationMinutes = 50,
            caloriesEstimate = 420,
            exercises = maleLegsExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_m_shoulders",
            title = "Shoulders (Ombros)",
            subtitle = "5 Exercícios de Desenvolvimento e Elevação",
            gender = "Masculino",
            muscleGroup = "Ombros",
            durationMinutes = 40,
            caloriesEstimate = 290,
            exercises = maleShouldersExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_m_biceps",
            title = "Biceps (Bíceps)",
            subtitle = "5 Exercícios de Roscas e Pico",
            gender = "Masculino",
            muscleGroup = "Bíceps",
            durationMinutes = 35,
            caloriesEstimate = 240,
            exercises = maleBicepsExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_m_triceps",
            title = "Triceps (Tríceps)",
            subtitle = "5 Exercícios de Extensões e Paralelas",
            gender = "Masculino",
            muscleGroup = "Tríceps",
            durationMinutes = 35,
            caloriesEstimate = 240,
            exercises = maleTricepsExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_m_abs",
            title = "Abs (Abdômen)",
            subtitle = "5 Exercícios de Core e Definição",
            gender = "Masculino",
            muscleGroup = "Abdômen",
            durationMinutes = 30,
            caloriesEstimate = 210,
            exercises = maleAbsExercises
        )
    )

    val allFemaleGroups = listOf(
        ExerciseCategoryGroup(
            id = "cat_f_glutes",
            title = "Glúteos & Posterior (Bumbum Firme)",
            subtitle = "6 Exercícios de Hip Thrust, Búlgaro e Stiff",
            gender = "Feminino",
            muscleGroup = "Glúteos",
            durationMinutes = 45,
            caloriesEstimate = 380,
            exercises = femaleGlutesExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_f_legs",
            title = "Quadríceps & Pernas (Coxas Torneadas)",
            subtitle = "6 Exercícios de Agachamento Sumô e Leg",
            gender = "Feminino",
            muscleGroup = "Pernas",
            durationMinutes = 45,
            caloriesEstimate = 390,
            exercises = femaleLegsExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_f_upper",
            title = "Costas & Postura Elegante",
            subtitle = "6 Exercícios para Afinar Cintura e Postura",
            gender = "Feminino",
            muscleGroup = "Costas",
            durationMinutes = 40,
            caloriesEstimate = 280,
            exercises = femaleUpperExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_f_abs",
            title = "Abdômen Reto & Cintura Fina",
            subtitle = "5 Exercícios com Vacuum e Pranchas",
            gender = "Feminino",
            muscleGroup = "Abdômen",
            durationMinutes = 30,
            caloriesEstimate = 220,
            exercises = femaleAbsAndWaistExercises
        ),
        ExerciseCategoryGroup(
            id = "cat_f_cardio",
            title = "Cardio & Queima Metabólica HIIT",
            subtitle = "4 Exercícios de Alta Oxidação Calórica",
            gender = "Feminino",
            muscleGroup = "Cardio",
            durationMinutes = 35,
            caloriesEstimate = 340,
            exercises = femaleCardioExercises
        )
    )
}
