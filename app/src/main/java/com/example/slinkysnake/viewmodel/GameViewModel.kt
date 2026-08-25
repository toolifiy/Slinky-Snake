package com.example.slinkysnake.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.data.PreferencesManager
import com.example.slinkysnake.model.ActiveEffects
import com.example.slinkysnake.model.Direction
import com.example.slinkysnake.model.FloatingText
import com.example.slinkysnake.model.Food
import com.example.slinkysnake.model.FoodTemplate
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.model.LevelConfig
import com.example.slinkysnake.model.Particle
import com.example.slinkysnake.model.Position
import com.example.slinkysnake.model.Skin
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.cos
import kotlin.math.sin

private const val GRID_SIZE = 20

data class GameUiState(
    val isPlaying: Boolean = false,
    val isPaused: Boolean = false,
    val showGameOver: Boolean = false,
    val showLevelClear: Boolean = false,
    val showVictory: Boolean = false,
    val countdown: Int? = null,
    val gameMode: GameMode = GameMode.CLASSIC,
    val currentLevelIdx: Int = 0,
    val unlockedLevel: Int = 1,
    val score: Int = 0,
    val comboMultiplier: Int = 1,
    val comboCount: Int = 0,
    val highScore: Int = 0,
    val foodEatenCount: Int = 0,
    val boosterEatenCount: Int = 0,
    val snake: List<Position> = listOf(Position(10, 8), Position(10, 9), Position(10, 10)),
    val prevSnake: List<Position> = listOf(Position(10, 8), Position(10, 9), Position(10, 10)),
    val moveProgress: Float = 1.0f,
    val direction: Direction = Direction.UP,
    val food: Food? = null,
    val activeEffects: ActiveEffects = ActiveEffects(),
    val particles: List<Particle> = emptyList(),
    val floatingTexts: List<FloatingText> = emptyList(),
    val screenShake: Float = 0f,
    val mouthOpen: Boolean = false,
    val selectedSkin: Skin = GameData.SNAKE_SKINS[0],
    val unlockedAchievements: Set<String> = emptySet(),
    val boardThemeId: String = "mint",
    val speedMultiplier: Float = 1.0f,
    val isSoundEnabled: Boolean = true,
    val soundVolume: Float = 0.8f,
    val allowedFruits: Set<String> = emptySet()
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)

    private val _uiState = MutableStateFlow(
        GameUiState(
            unlockedLevel = prefs.getUnlockedLevel(),
            highScore = prefs.getHighScore(GameMode.CLASSIC),
            selectedSkin = GameData.SNAKE_SKINS.find { it.id == prefs.getSelectedSkinId() } ?: GameData.SNAKE_SKINS[0],
            unlockedAchievements = prefs.getUnlockedAchievements(),
            boardThemeId = prefs.getBoardTheme(),
            speedMultiplier = prefs.getSpeedMultiplier(),
            isSoundEnabled = prefs.isSoundEnabled(),
            soundVolume = prefs.getSoundVolume(),
            allowedFruits = prefs.getAllowedFruits()
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameLoopJob: Job? = null
    private var nextDirection: Direction = Direction.UP

    init {
        SoundSynth.isSoundEnabled = _uiState.value.isSoundEnabled
        SoundSynth.soundVolume = _uiState.value.soundVolume
    }

    fun setGameMode(mode: GameMode) {
        _uiState.update {
            it.copy(
                gameMode = mode,
                highScore = prefs.getHighScore(mode),
                showGameOver = false,
                showLevelClear = false,
                showVictory = false,
                isPlaying = false,
                score = 0,
                comboMultiplier = 1,
                comboCount = 0
            )
        }
    }

    fun selectLevel(idx: Int) {
        if (idx in 0 until GameData.LEVEL_CONFIGS.size) {
            _uiState.update { it.copy(currentLevelIdx = idx) }
            SoundSynth.playClick()
        }
    }

    fun selectSkin(skin: Skin) {
        prefs.setSelectedSkinId(skin.id)
        val played = prefs.addPlayedSkinId(skin.id)
        if (played.size >= 3) unlockAchievementDirect("all_skins")
        if (played.size >= 6) unlockAchievementDirect("skin_collector")

        _uiState.update { it.copy(selectedSkin = skin) }
        SoundSynth.playClick()
    }

    fun setSpeedMultiplier(multiplier: Float) {
        prefs.setSpeedMultiplier(multiplier)
        _uiState.update { it.copy(speedMultiplier = multiplier) }
    }

    fun setBoardTheme(themeId: String) {
        prefs.setBoardTheme(themeId)
        _uiState.update { it.copy(boardThemeId = themeId) }
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.setSoundEnabled(enabled)
        SoundSynth.isSoundEnabled = enabled
        _uiState.update { it.copy(isSoundEnabled = enabled) }
        SoundSynth.playClick()
    }

    fun setSoundVolume(volume: Float) {
        prefs.setSoundVolume(volume)
        SoundSynth.soundVolume = volume
        _uiState.update { it.copy(soundVolume = volume, isSoundEnabled = if (volume > 0f) true else it.isSoundEnabled) }
    }

    fun toggleFruit(fruitType: String) {
        val current = _uiState.value.allowedFruits.toMutableSet()
        if (current.contains(fruitType)) {
            if (current.size > 1) current.remove(fruitType)
        } else {
            current.add(fruitType)
        }
        prefs.setAllowedFruits(current)
        _uiState.update { it.copy(allowedFruits = current) }
    }

    fun resetAllProgress() {
        prefs.resetAllProgress()
        _uiState.update {
            it.copy(
                highScore = 0,
                unlockedLevel = 1,
                currentLevelIdx = 0,
                unlockedAchievements = emptySet()
            )
        }
        SoundSynth.playClick()
    }

    private val directionQueue = ArrayDeque<Direction>()

    fun onDirectionInput(dir: Direction) {
        if (directionQueue.size < 2) {
            val lastPending = directionQueue.lastOrNull() ?: nextDirection
            val isValid = when (dir) {
                Direction.UP -> lastPending != Direction.DOWN && lastPending != Direction.UP
                Direction.DOWN -> lastPending != Direction.UP && lastPending != Direction.DOWN
                Direction.LEFT -> lastPending != Direction.RIGHT && lastPending != Direction.LEFT
                Direction.RIGHT -> lastPending != Direction.LEFT && lastPending != Direction.RIGHT
            }
            if (isValid) {
                directionQueue.addLast(dir)
            }
        }
    }

    fun startGame() {
        SoundSynth.playClick()
        val initialSnake = listOf(Position(10, 8), Position(10, 9), Position(10, 10))
        nextDirection = Direction.UP
        directionQueue.clear()

        val currentLevelConfig = GameData.LEVEL_CONFIGS[_uiState.value.currentLevelIdx]
        val activeObstacles = if (_uiState.value.gameMode == GameMode.CLASSIC) emptyList() else currentLevelConfig.obstacles
        val initialFood = spawnFood(initialSnake, activeObstacles)

        _uiState.update {
            it.copy(
                isPlaying = true,
                isPaused = false,
                showGameOver = false,
                showLevelClear = false,
                showVictory = false,
                countdown = null,
                score = 0,
                comboMultiplier = 1,
                comboCount = 0,
                foodEatenCount = 0,
                boosterEatenCount = 0,
                snake = initialSnake,
                prevSnake = initialSnake,
                moveProgress = 1.0f,
                direction = Direction.UP,
                food = initialFood,
                activeEffects = ActiveEffects(),
                particles = emptyList(),
                floatingTexts = emptyList(),
                screenShake = 0f
            )
        }

        launchGameLoop()
    }

    fun togglePause() {
        val current = _uiState.value.isPaused
        SoundSynth.playClick()
        if (current) {
            resumeWithCountdown()
        } else {
            _uiState.update { it.copy(isPaused = true) }
        }
    }

    fun exitGame() {
        SoundSynth.playClick()
        gameLoopJob?.cancel()
        _uiState.update { it.copy(isPlaying = false, isPaused = false, countdown = null) }
    }

    fun nextLevel() {
        SoundSynth.playClick()
        val nextIdx = (_uiState.value.currentLevelIdx + 1).coerceAtMost(GameData.LEVEL_CONFIGS.size - 1)
        _uiState.update { it.copy(currentLevelIdx = nextIdx) }
        startGame()
    }

    fun respawnGame() {
        SoundSynth.playClick()
        _uiState.update {
            it.copy(
                showGameOver = false,
                activeEffects = it.activeEffects.copy(immortal = 5000L)
            )
        }
        resumeWithCountdown()
    }

    private fun resumeWithCountdown() {
        viewModelScope.launch {
            _uiState.update { it.copy(countdown = 3) }
            for (i in 3 downTo 1) {
                _uiState.update { it.copy(countdown = i) }
                delay(800)
            }
            _uiState.update { it.copy(countdown = null, isPaused = false) }
        }
    }

    private fun launchGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = viewModelScope.launch {
            var lastTickTime = System.currentTimeMillis()

            while (_uiState.value.isPlaying) {
                val state = _uiState.value

                if (state.isPaused || state.countdown != null) {
                    delay(50)
                    lastTickTime = System.currentTimeMillis()
                    continue
                }

                val now = System.currentTimeMillis()
                val dt = now - lastTickTime

                // Calculate base speed
                val baseSpeed = calculateCurrentSpeed(state)

                if (dt >= baseSpeed) {
                    lastTickTime = now
                    stepGame()
                }

                // Update interpolation progress
                val progress = (dt.toFloat() / baseSpeed.toFloat()).coerceIn(0f, 1f)
                _uiState.update { it.copy(moveProgress = progress) }

                // Decay timers & particles
                updateVisualDecays(16L)

                delay(16) // ~60 FPS
            }
        }
    }

    private fun calculateCurrentSpeed(state: GameUiState): Long {
        var speed = if (state.gameMode == GameMode.CLASSIC) 220L else GameData.LEVEL_CONFIGS[state.currentLevelIdx].speed

        // Gradual speedup for every 5 foods eaten
        val foodSpeedAdj = (state.foodEatenCount / 5) * 3L
        speed = (speed - foodSpeedAdj.coerceAtMost(30L)).coerceAtLeast(100L)

        // Active modifiers
        if (state.activeEffects.chili > 0L) {
            speed = (speed * 0.82f).toLong()
        } else if (state.activeEffects.grape > 0L) {
            speed = (speed * 1.5f).toLong()
        }

        if (state.activeEffects.booster > 0L) {
            val boostPercent = (0.50f - (state.boosterEatenCount - 1) * 0.10f).coerceAtLeast(0.15f)
            speed = (speed * (1f - boostPercent)).toLong()
        }

        speed = (speed / state.speedMultiplier).toLong().coerceIn(30L, 1200L)
        return speed
    }

    private fun stepGame() {
        val state = _uiState.value
        val snake = state.snake
        if (snake.isEmpty()) return

        if (directionQueue.isNotEmpty()) {
            nextDirection = directionQueue.removeFirst()
        }

        val dir = nextDirection
        val head = snake[0]
        var nextHead = when (dir) {
            Direction.UP -> Position(head.x, head.y - 1)
            Direction.DOWN -> Position(head.x, head.y + 1)
            Direction.LEFT -> Position(head.x - 1, head.y)
            Direction.RIGHT -> Position(head.x + 1, head.y)
        }

        val isImmortal = state.activeEffects.immortal > 0L

        // 1. Wall Collision Check
        if (nextHead.x < 0 || nextHead.x >= GRID_SIZE || nextHead.y < 0 || nextHead.y >= GRID_SIZE) {
            if (state.gameMode == GameMode.CLASSIC || isImmortal) {
                nextHead = Position(
                    (nextHead.x + GRID_SIZE) % GRID_SIZE,
                    (nextHead.y + GRID_SIZE) % GRID_SIZE
                )
            } else {
                handleCrash()
                return
            }
        }

        // 2. Self Collision Check
        val hitSelf = snake.any { it.x == nextHead.x && it.y == nextHead.y }
        if (hitSelf && !isImmortal) {
            handleCrash()
            return
        }

        // 3. Obstacle Collision Check
        if (state.gameMode == GameMode.LEVELS) {
            val levelConfig = GameData.LEVEL_CONFIGS[state.currentLevelIdx]
            val hitObstacle = levelConfig.obstacles.any { it.x == nextHead.x && it.y == nextHead.y }
            if (hitObstacle && !isImmortal) {
                handleCrash()
                return
            }
        }

        // 4. Food Eating Check
        val newSnake = mutableListOf(nextHead).apply { addAll(snake) }
        val currentFood = state.food

        if (currentFood != null && nextHead.x == currentFood.position.x && nextHead.y == currentFood.position.y) {
            onFoodEaten(currentFood, newSnake)
        } else {
            newSnake.removeAt(newSnake.size - 1)
            _uiState.update {
                it.copy(
                    prevSnake = snake,
                    snake = newSnake,
                    direction = dir,
                    mouthOpen = false
                )
            }
        }
    }

    private fun onFoodEaten(food: Food, newSnake: MutableList<Position>) {
        val state = _uiState.value
        SoundSynth.playEat(food.type, state.comboCount)

        val nextEaten = state.foodEatenCount + 1
        var nextCombo = state.comboCount + 1
        var multiplier = 1 + (nextCombo / 2)

        if (state.activeEffects.chili > 0L) multiplier *= 2
        if (state.activeEffects.doublePoints > 0L) multiplier *= 2

        val earned = food.points * multiplier
        val nextScore = state.score + earned

        if (nextScore > state.highScore) {
            prefs.setHighScore(state.gameMode, nextScore)
        }

        // Handle specific food effects
        var newEffects = state.activeEffects
        var boosterCount = state.boosterEatenCount

        when (food.type) {
            "BOOSTER" -> {
                newEffects = newEffects.copy(booster = 10000L)
                boosterCount += 1
                unlockAchievementDirect("blue_magic")
                addFloatingText("BLUE MAGIC! 🧪", food.position.x, food.position.y, 0xFF3B82F6)
            }
            "GOLDEN_STAR" -> {
                unlockAchievementDirect("star_power")
                addFloatingText("STAR BLAST! ⭐", food.position.x, food.position.y, 0xFFFACC15)
                triggerScreenShake(6f)
            }
            "CHILI" -> {
                newEffects = newEffects.copy(chili = 8000L, chiliCrying = 10000L)
                unlockAchievementDirect("spicy_run")
                unlockAchievementDirect("chili_crying")
                addFloatingText("SPICY FIRE! 🔥", food.position.x, food.position.y, 0xFFF97316)
            }
            "GRAPE" -> {
                newEffects = newEffects.copy(grape = 6000L)
                addFloatingText("CHILL MODE 🍇", food.position.x, food.position.y, 0xFF8B5CF6)
            }
            "CAKE" -> {
                // Grow 2 extra segments
                if (newSnake.isNotEmpty()) {
                    newSnake.add(newSnake.last())
                    newSnake.add(newSnake.last())
                }
                addFloatingText("FEAST CAKE! 🍰", food.position.x, food.position.y, 0xFFFF007F)
            }
            "POWER_SPEED" -> {
                newEffects = newEffects.copy(chili = 8000L)
                unlockAchievementDirect("perfect_reflexes")
                addFloatingText("HYPER SPEED! ⚡", food.position.x, food.position.y, 0xFF38BDF8)
            }
            "POWER_IMMORTAL" -> {
                newEffects = newEffects.copy(immortal = 10000L)
                unlockAchievementDirect("immortal_ghost")
                addFloatingText("GHOST IMMORTAL! 👻", food.position.x, food.position.y, 0xFFA78BFA)
            }
            "POWER_DOUBLE" -> {
                newEffects = newEffects.copy(doublePoints = 15000L)
                unlockAchievementDirect("double_deal")
                addFloatingText("DOUBLE DEAL! 💎", food.position.x, food.position.y, 0xFFF472B6)
            }
            "POWER_MAGNET" -> {
                newEffects = newEffects.copy(magnet = 12000L)
                unlockAchievementDirect("magnet_pull")
                addFloatingText("MAGNET PULL! 🧲", food.position.x, food.position.y, 0xFFF87171)
            }
            "POWER_SHRINK" -> {
                val targetLength = (newSnake.size * 0.65f).toInt().coerceAtLeast(3)
                while (newSnake.size > targetLength) newSnake.removeAt(newSnake.size - 1)
                unlockAchievementDirect("shrink_master")
                addFloatingText("SHRINK SHROOM! 🍄", food.position.x, food.position.y, 0xFF34D399)
            }
        }

        // Achievements check
        unlockAchievementDirect("first_bite")
        if (nextScore >= 50) unlockAchievementDirect("half_century")
        if (nextScore >= 100) unlockAchievementDirect("century")
        if (nextScore >= 300) unlockAchievementDirect("snake_master")
        if (nextScore >= 500) unlockAchievementDirect("score_500")
        if (nextScore >= 1000) unlockAchievementDirect("score_1000")
        if (nextEaten >= 30) unlockAchievementDirect("hungry_slitherer")
        if (nextCombo >= 8) unlockAchievementDirect("combo_king")

        // Level Complete check
        if (state.gameMode == GameMode.LEVELS) {
            val levelConfig = GameData.LEVEL_CONFIGS[state.currentLevelIdx]
            if (nextScore >= levelConfig.targetScore) {
                SoundSynth.playLevelUp()
                val nextLevel = levelConfig.level + 1
                prefs.setUnlockedLevel(nextLevel)

                if (state.currentLevelIdx >= GameData.LEVEL_CONFIGS.size - 1) {
                    _uiState.update { it.copy(showVictory = true, isPlaying = false, unlockedLevel = prefs.getUnlockedLevel()) }
                } else {
                    _uiState.update { it.copy(showLevelClear = true, isPlaying = false, unlockedLevel = prefs.getUnlockedLevel()) }
                }
                unlockAchievementDirect("level_clear")
                if (levelConfig.level == 3) unlockAchievementDirect("level_3_master")
                if (levelConfig.level == 4) unlockAchievementDirect("volcano_conqueror")
                if (levelConfig.level == 5) unlockAchievementDirect("cyber_god")
                return
            }
        }

        val spawnBooster = (nextEaten > 0 && nextEaten % 10 == 0)
        val obstacles = if (state.gameMode == GameMode.CLASSIC) emptyList() else GameData.LEVEL_CONFIGS[state.currentLevelIdx].obstacles
        val nextFood = spawnFood(newSnake, obstacles, spawnBooster)

        createExplosion(food.position.x, food.position.y, food.color, 12)
        addFloatingText("+$earned${if (multiplier > 1) " (x$multiplier)" else ""}", food.position.x, food.position.y, food.color)

        _uiState.update {
            it.copy(
                prevSnake = state.snake,
                snake = newSnake,
                direction = nextDirection,
                score = nextScore,
                highScore = nextScore.coerceAtLeast(state.highScore),
                comboMultiplier = multiplier,
                comboCount = nextCombo,
                foodEatenCount = nextEaten,
                boosterEatenCount = boosterCount,
                food = nextFood,
                activeEffects = newEffects,
                mouthOpen = true
            )
        }
    }

    private fun handleCrash() {
        SoundSynth.playCrash()
        triggerScreenShake(12f)
        _uiState.update {
            it.copy(
                isPlaying = false,
                showGameOver = true
            )
        }
    }

    private fun spawnFood(currentSnake: List<Position>, obstacles: List<Position>, forceBooster: Boolean = false): Food {
        var newPos = Position(5, 5)
        var attempts = 0
        var found = false

        while (!found && attempts < 200) {
            newPos = Position(
                (0 until GRID_SIZE).random(),
                (0 until GRID_SIZE).random()
            )
            val inSnake = currentSnake.any { it.x == newPos.x && it.y == newPos.y }
            val inObstacle = obstacles.any { it.x == newPos.x && it.y == newPos.y }
            if (!inSnake && !inObstacle) {
                found = true
            }
            attempts++
        }

        if (forceBooster) {
            return Food(
                position = newPos,
                type = "BOOSTER",
                color = 0xFF2563EB,
                emoji = "🧪",
                points = 50
            )
        }

        val allowed = _uiState.value.allowedFruits
        val templates = GameData.ALL_FOOD_TEMPLATES.filter { allowed.contains(it.type) || it.type.startsWith("POWER_") }
        val pool = if (templates.isNotEmpty()) templates else GameData.ALL_FOOD_TEMPLATES.take(1)

        val totalWeight = pool.sumOf { it.prob }
        var rand = (1..totalWeight.coerceAtLeast(1)).random()
        var selected = pool[0]

        for (t in pool) {
            if (rand <= t.prob) {
                selected = t
                break
            }
            rand -= t.prob
        }

        return Food(
            position = newPos,
            type = selected.type,
            color = selected.color,
            emoji = selected.emoji,
            points = selected.points
        )
    }

    private fun createExplosion(x: Int, y: Int, color: Long, count: Int = 10) {
        val startX = x.toFloat() + 0.5f
        val startY = y.toFloat() + 0.5f
        val newParticles = mutableListOf<Particle>()
        for (i in 0 until count) {
            val angle = Math.random() * Math.PI * 2.0
            val speed = (Math.random() * 0.15 + 0.08).toFloat()
            newParticles.add(
                Particle(
                    id = System.nanoTime() + i,
                    x = startX,
                    y = startY,
                    vx = (cos(angle) * speed).toFloat(),
                    vy = (sin(angle) * speed).toFloat(),
                    color = color,
                    size = (Math.random() * 4.0 + 3.0).toFloat(),
                    life = 0f,
                    maxLife = (Math.random() * 20.0 + 15.0).toFloat()
                )
            )
        }
        _uiState.update { it.copy(particles = it.particles + newParticles) }
    }

    private fun addFloatingText(text: String, x: Int, y: Int, color: Long) {
        val ft = FloatingText(
            id = System.nanoTime(),
            text = text,
            x = x.toFloat() + 0.5f,
            y = y.toFloat() + 0.2f,
            color = color,
            life = 30
        )
        _uiState.update { it.copy(floatingTexts = it.floatingTexts + ft) }
    }

    private fun triggerScreenShake(amount: Float) {
        _uiState.update { it.copy(screenShake = amount) }
    }

    private fun unlockAchievementDirect(id: String) {
        if (prefs.unlockAchievement(id)) {
            SoundSynth.playAchievement()
            _uiState.update { it.copy(unlockedAchievements = prefs.getUnlockedAchievements()) }
        }
    }

    private fun updateVisualDecays(dt: Long) {
        _uiState.update { state ->
            // Active effects decay
            val nextEffects = state.activeEffects.copy(
                chili = (state.activeEffects.chili - dt).coerceAtLeast(0L),
                grape = (state.activeEffects.grape - dt).coerceAtLeast(0L),
                booster = (state.activeEffects.booster - dt).coerceAtLeast(0L),
                immortal = (state.activeEffects.immortal - dt).coerceAtLeast(0L),
                doublePoints = (state.activeEffects.doublePoints - dt).coerceAtLeast(0L),
                magnet = (state.activeEffects.magnet - dt).coerceAtLeast(0L),
                chiliCrying = (state.activeEffects.chiliCrying - dt).coerceAtLeast(0L)
            )

            // Screen shake decay
            val nextShake = (state.screenShake - 0.4f).coerceAtLeast(0f)

            // Particles decay
            val updatedParticles = state.particles.mapNotNull { p ->
                p.x += p.vx
                p.y += p.vy
                p.life += 1f
                if (p.life < p.maxLife) p else null
            }

            // Floating texts decay (drift upward smoothly)
            val updatedTexts = state.floatingTexts.mapNotNull { ft ->
                ft.y -= 0.025f
                ft.life -= 1
                if (ft.life > 0) ft else null
            }

            // Food expiration (10s)
            var currentFood = state.food
            if (currentFood != null && System.currentTimeMillis() - currentFood.spawnTime >= 10000L) {
                currentFood = spawnFood(state.snake, if (state.gameMode == GameMode.CLASSIC) emptyList() else GameData.LEVEL_CONFIGS[state.currentLevelIdx].obstacles)
            }

            state.copy(
                activeEffects = nextEffects,
                screenShake = nextShake,
                particles = updatedParticles,
                floatingTexts = updatedTexts,
                food = currentFood
            )
        }
    }
}
