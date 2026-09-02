package com.example.slinkysnake.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.data.PreferencesManager
import com.example.slinkysnake.model.ActiveEffects
import com.example.slinkysnake.model.BoardTheme
import com.example.slinkysnake.model.Direction
import com.example.slinkysnake.model.FloatingText
import com.example.slinkysnake.model.Food
import com.example.slinkysnake.model.FoodTemplate
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.model.LevelConfig
import com.example.slinkysnake.model.Particle
import com.example.slinkysnake.model.Position
import com.example.slinkysnake.model.Skin
import kotlinx.coroutines.Dispatchers
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
    val powerUp: Food? = null,
    val bannerMessage: String? = null,
    val bannerExpiryTime: Long = 0L,
    val activeEffects: ActiveEffects = ActiveEffects(),
    val particles: List<Particle> = emptyList(),
    val floatingTexts: List<FloatingText> = emptyList(),
    val screenShake: Float = 0f,
    val mouthOpen: Boolean = false,
    val selectedSkin: Skin = GameData.SNAKE_SKINS[0],
    val unlockedSkins: Set<String> = setOf("slinky"),
    val coins: Int = 100,
    val unlockedAchievements: Set<String> = emptySet(),
    val claimedAchievements: Set<String> = emptySet(),
    val boardThemeId: String = "mint",
    val boardThemeColor1: Long = 0xFFC2F5D3,
    val boardThemeColor2: Long = 0xFFE6FCEE,
    val unlockedThemes: Set<String> = setOf("mint", "crimson", "butter", "lavender", "sky"),
    val speedMultiplier: Float = 1.0f,
    val isSoundEnabled: Boolean = true,
    val soundVolume: Float = 0.8f,
    val allowedFruits: Set<String> = emptySet(),
    val allowedPowers: Set<String> = emptySet(),
    val foodInventory: Map<String, Int> = emptyMap(),
    val dailyMissions: List<com.example.slinkysnake.model.DailyMission> = emptyList()
)

class GameViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = PreferencesManager(application)

    private fun loadDailyMissions(): List<com.example.slinkysnake.model.DailyMission> {
        return listOf(
            com.example.slinkysnake.model.DailyMission(
                id = "eat_food",
                title = "Fruit Feast",
                description = "Eat 15 foods in the arena",
                icon = "🍎",
                targetGoal = 15,
                currentProgress = prefs.getDailyMissionProgress("eat_food"),
                rewardCoins = 15,
                isClaimed = prefs.isDailyMissionClaimed("eat_food")
            ),
            com.example.slinkysnake.model.DailyMission(
                id = "power_surge",
                title = "Surge Runner",
                description = "Eat 2 Chili or Booster items",
                icon = "⚡",
                targetGoal = 2,
                currentProgress = prefs.getDailyMissionProgress("power_surge"),
                rewardCoins = 20,
                isClaimed = prefs.isDailyMissionClaimed("power_surge")
            ),
            com.example.slinkysnake.model.DailyMission(
                id = "high_score",
                title = "High Scorer",
                description = "Reach 80 points in a run",
                icon = "🏆",
                targetGoal = 80,
                currentProgress = prefs.getDailyMissionProgress("high_score"),
                rewardCoins = 25,
                isClaimed = prefs.isDailyMissionClaimed("high_score")
            )
        )
    }

    private val _uiState = MutableStateFlow(
        GameUiState(
            unlockedLevel = prefs.getUnlockedLevel(),
            highScore = prefs.getHighScore(GameMode.CLASSIC),
            selectedSkin = GameData.SNAKE_SKINS.find { it.id == prefs.getSelectedSkinId() } ?: GameData.SNAKE_SKINS[0],
            unlockedSkins = prefs.getUnlockedSkinIds(),
            coins = prefs.getCoins(),
            unlockedAchievements = prefs.getUnlockedAchievements(),
            claimedAchievements = prefs.getClaimedAchievements(),
            boardThemeId = prefs.getBoardTheme(),
            boardThemeColor1 = (GameData.BOARD_THEMES.find { it.id == prefs.getBoardTheme() } ?: GameData.BOARD_THEMES[0]).color1,
            boardThemeColor2 = (GameData.BOARD_THEMES.find { it.id == prefs.getBoardTheme() } ?: GameData.BOARD_THEMES[0]).color2,
            unlockedThemes = prefs.getUnlockedThemeIds(),
            speedMultiplier = prefs.getSpeedMultiplier(),
            isSoundEnabled = prefs.isSoundEnabled(),
            soundVolume = prefs.getSoundVolume(),
            allowedFruits = prefs.getAllowedFruits(),
            allowedPowers = prefs.getAllowedPowers(),
            foodInventory = prefs.getAllFoodInventory(),
            dailyMissions = emptyList()
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var gameLoopJob: Job? = null
    private var nextDirection: Direction = Direction.UP

    init {
        SoundSynth.isSoundEnabled = _uiState.value.isSoundEnabled
        SoundSynth.soundVolume = _uiState.value.soundVolume
        _uiState.update { it.copy(dailyMissions = loadDailyMissions()) }
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

    fun buySkin(skin: Skin): Boolean {
        if (prefs.isSkinUnlocked(skin.id)) {
            selectSkin(skin)
            return true
        }
        val currentCoins = _uiState.value.coins
        if (currentCoins >= skin.price) {
            val updatedCoins = prefs.addCoins(-skin.price)
            prefs.unlockSkin(skin.id)
            prefs.setSelectedSkinId(skin.id)
            val played = prefs.addPlayedSkinId(skin.id)
            if (played.size >= 3) unlockAchievementDirect("all_skins")
            if (played.size >= 6) unlockAchievementDirect("skin_collector")

            SoundSynth.playPurchase()
            _uiState.update {
                it.copy(
                    coins = updatedCoins,
                    unlockedSkins = prefs.getUnlockedSkinIds(),
                    selectedSkin = skin
                )
            }
            return true
        }
        return false
    }

    fun selectSkin(skin: Skin) {
        if (!prefs.isSkinUnlocked(skin.id)) {
            buySkin(skin)
            return
        }
        prefs.setSelectedSkinId(skin.id)
        val played = prefs.addPlayedSkinId(skin.id)
        if (played.size >= 3) unlockAchievementDirect("all_skins")
        if (played.size >= 6) unlockAchievementDirect("skin_collector")

        _uiState.update { it.copy(selectedSkin = skin) }
        SoundSynth.playClick()
    }

    fun addCoins(amount: Int) {
        val updated = prefs.addCoins(amount)
        _uiState.update { it.copy(coins = updated) }
    }

    fun setSpeedMultiplier(multiplier: Float) {
        prefs.setSpeedMultiplier(multiplier)
        _uiState.update { it.copy(speedMultiplier = multiplier) }
    }

    fun setBoardTheme(themeId: String) {
        if (!prefs.isThemeUnlocked(themeId)) {
            val theme = GameData.BOARD_THEMES.find { it.id == themeId }
            if (theme != null) {
                buyTheme(theme)
            }
            return
        }
        prefs.setBoardTheme(themeId)
        val selectedTheme = GameData.BOARD_THEMES.find { it.id == themeId } ?: GameData.BOARD_THEMES[0]
        _uiState.update {
            it.copy(
                boardThemeId = themeId,
                boardThemeColor1 = selectedTheme.color1,
                boardThemeColor2 = selectedTheme.color2
            )
        }
        SoundSynth.playClick()
    }

    fun buyTheme(theme: BoardTheme): Boolean {
        val currentCoins = _uiState.value.coins
        if (currentCoins >= theme.price) {
            val updatedCoins = prefs.addCoins(-theme.price)
            prefs.unlockTheme(theme.id)
            prefs.setBoardTheme(theme.id)
            SoundSynth.playClick()
            _uiState.update {
                it.copy(
                    coins = updatedCoins,
                    unlockedThemes = prefs.getUnlockedThemeIds(),
                    boardThemeId = theme.id,
                    boardThemeColor1 = theme.color1,
                    boardThemeColor2 = theme.color2
                )
            }
            return true
        }
        return false
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

    fun togglePower(powerType: String) {
        val current = _uiState.value.allowedPowers.toMutableSet()
        if (current.contains(powerType)) {
            if (current.size > 1) current.remove(powerType)
        } else {
            current.add(powerType)
        }
        prefs.setAllowedPowers(current)
        _uiState.update { it.copy(allowedPowers = current) }
    }

    fun resetAllProgress() {
        prefs.resetAllProgress()
        GameData.ALL_FOOD_TEMPLATES.forEach { food ->
            prefs.setFoodStock(food.type, 0)
        }
        prefs.setCoins(0)
        _uiState.update {
            it.copy(
                highScore = 0,
                unlockedLevel = 1,
                currentLevelIdx = 0,
                coins = 0,
                unlockedAchievements = emptySet(),
                foodInventory = prefs.getAllFoodInventory()
            )
        }
        SoundSynth.playClick()
    }

    fun sellFood(foodType: String): Int {
        val template = GameData.ALL_FOOD_TEMPLATES.find { it.type == foodType } ?: return 0
        val currentStock = prefs.getFoodStock(foodType)
        val requiredUnits = template.unitsPerCoin
        val coinsEarned = currentStock / requiredUnits
        if (coinsEarned <= 0) return 0

        val unitsSold = coinsEarned * requiredUnits
        val remainingStock = currentStock - unitsSold
        prefs.setFoodStock(foodType, remainingStock)
        val newCoins = prefs.addCoins(coinsEarned)
        SoundSynth.playSell()
        _uiState.update {
            it.copy(
                coins = newCoins,
                foodInventory = it.foodInventory.toMutableMap().apply { put(foodType, remainingStock) }
            )
        }
        return coinsEarned
    }

    fun sellAllFoodStock(foodType: String): Int {
        return sellFood(foodType)
    }

    fun restockFood(foodType: String, count: Int = 3) {
        val newStock = prefs.addFoodStock(foodType, count)
        SoundSynth.playClick()
        _uiState.update {
            it.copy(
                foodInventory = it.foodInventory.toMutableMap().apply { put(foodType, newStock) }
            )
        }
    }

    fun claimDailyMission(missionId: String) {
        val mission = _uiState.value.dailyMissions.find { it.id == missionId } ?: return
        if (mission.currentProgress >= mission.targetGoal && !mission.isClaimed) {
            val success = prefs.claimDailyMission(mission.id, mission.rewardCoins)
            if (success) {
                SoundSynth.playSell()
                _uiState.update {
                    it.copy(
                        coins = prefs.getCoins(),
                        dailyMissions = loadDailyMissions()
                    )
                }
            }
        }
    }

    private val directionQueue = ArrayDeque<Direction>()


    fun onDirectionInput(dir: Direction) {
        val currentHeadDir = if (directionQueue.isEmpty()) _uiState.value.direction else directionQueue.last()
        val isOpposite = when (dir) {
            Direction.UP -> currentHeadDir == Direction.DOWN
            Direction.DOWN -> currentHeadDir == Direction.UP
            Direction.LEFT -> currentHeadDir == Direction.RIGHT
            Direction.RIGHT -> currentHeadDir == Direction.LEFT
        }
        val isSame = dir == currentHeadDir
        if (!isOpposite && !isSame) {
            if (directionQueue.size < 2) {
                directionQueue.addLast(dir)
            } else {
                // Replace the last queued direction with the user's latest fast input
                directionQueue.removeLast()
                val prev = directionQueue.lastOrNull() ?: _uiState.value.direction
                val isOppositeToPrev = when (dir) {
                    Direction.UP -> prev == Direction.DOWN
                    Direction.DOWN -> prev == Direction.UP
                    Direction.LEFT -> prev == Direction.RIGHT
                    Direction.RIGHT -> prev == Direction.LEFT
                }
                if (!isOppositeToPrev && dir != prev) {
                    directionQueue.addLast(dir)
                }
            }
        }
    }

    private var foodsSinceLastPower = 0

    fun startGame() {
        SoundSynth.playClick()
        val initialSnake = listOf(Position(10, 8), Position(10, 9), Position(10, 10))
        nextDirection = Direction.UP
        directionQueue.clear()
        foodsSinceLastPower = 0

        val currentLevelConfig = GameData.LEVEL_CONFIGS[_uiState.value.currentLevelIdx]
        val activeObstacles = if (_uiState.value.gameMode == GameMode.CLASSIC) emptyList() else currentLevelConfig.obstacles
        val initialFood = spawnFood(initialSnake, activeObstacles)

        _uiState.update {
            it.copy(
                isPlaying = true,
                isPaused = true,
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
                powerUp = null,
                activeEffects = ActiveEffects(),
                particles = emptyList(),
                floatingTexts = emptyList(),
                screenShake = 0f
            )
        }

        launchGameLoop()
        resumeWithCountdown()
    }

    fun togglePause() {
        val current = _uiState.value.isPaused
        SoundSynth.playClick()
        if (current) {
            resumeWithCountdown()
        } else {
            pauseGame()
        }
    }

    fun pauseGame() {
        _uiState.update { it.copy(isPaused = true) }
    }

    fun exitGame() {
        SoundSynth.playClick()
        gameLoopJob?.cancel()
        _uiState.update {
            it.copy(
                isPlaying = false,
                isPaused = false,
                countdown = null,
                showGameOver = false,
                showLevelClear = false,
                showVictory = false
            )
        }
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

    fun resumeWithCountdown() {
        viewModelScope.launch {
            _uiState.update { it.copy(isPaused = true) }
            for (i in 3 downTo 1) {
                _uiState.update { it.copy(countdown = i) }
                SoundSynth.playCountdownBeep(false)
                delay(650)
            }
            _uiState.update { it.copy(countdown = 0) } // 0 = "GO!"
            SoundSynth.playCountdownBeep(true)
            delay(300)
            _uiState.update { it.copy(countdown = null, isPaused = false) }
        }
    }

    private fun launchGameLoop() {
        gameLoopJob?.cancel()
        gameLoopJob = viewModelScope.launch(Dispatchers.Default) {
            var lastStepTime = System.currentTimeMillis()
            var lastFrameTime = System.currentTimeMillis()

            while (_uiState.value.isPlaying) {
                val state = _uiState.value

                if (state.isPaused || state.countdown != null) {
                    delay(50)
                    lastStepTime = System.currentTimeMillis()
                    lastFrameTime = System.currentTimeMillis()
                    continue
                }

                val now = System.currentTimeMillis()
                val deltaFrame = (now - lastFrameTime).coerceIn(1L, 100L)
                lastFrameTime = now

                val baseSpeed = calculateCurrentSpeed(state)

                if (now - lastStepTime >= baseSpeed) {
                    lastStepTime = now
                    stepGame()
                }

                // Smooth visual decays and particles
                updateVisualDecays(deltaFrame)

                delay(20) // Solid ~50fps game loop without GC stutter
            }
        }
    }

    private fun calculateCurrentSpeed(state: GameUiState): Long {
        var speed = if (state.gameMode == GameMode.CLASSIC) 280L else GameData.LEVEL_CONFIGS[state.currentLevelIdx].speed

        // Gradual speedup for every 5 foods eaten
        val foodSpeedAdj = (state.foodEatenCount / 5) * 3L
        speed = (speed - foodSpeedAdj.coerceAtMost(40L)).coerceAtLeast(140L)

        // Active modifiers
        if (state.activeEffects.dragon > 0L) {
            speed = (speed * 0.80f).toLong()
        } else if (state.activeEffects.chili > 0L) {
            speed = (speed * 0.82f).toLong()
        } else if (state.activeEffects.grape > 0L || state.activeEffects.freeze > 0L) {
            speed = (speed * 1.5f).toLong()
        }

        if (state.activeEffects.booster > 0L) {
            val boostPercent = (0.50f - (state.boosterEatenCount - 1) * 0.10f).coerceAtLeast(0.15f)
            speed = (speed * (1f - boostPercent)).toLong()
        }

        speed = (speed / state.speedMultiplier).toLong().coerceIn(60L, 1200L)
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

        val isImmortal = state.activeEffects.immortal > 0L || state.activeEffects.dragon > 0L
        val hasShield = state.activeEffects.shield

        // 1. Wall Collision Check
        if (nextHead.x < 0 || nextHead.x >= GRID_SIZE || nextHead.y < 0 || nextHead.y >= GRID_SIZE) {
            if (state.gameMode == GameMode.CLASSIC || isImmortal) {
                nextHead = Position(
                    (nextHead.x + GRID_SIZE) % GRID_SIZE,
                    (nextHead.y + GRID_SIZE) % GRID_SIZE
                )
            } else if (hasShield) {
                _uiState.update { it.copy(activeEffects = it.activeEffects.copy(shield = false)) }
                addFloatingText("SHIELD SAVED! 🛡️", head.x, head.y, 0xFFEAB308)
                triggerScreenShake(8f)
                SoundSynth.playEat("SHIELD")
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
            if (hasShield) {
                _uiState.update { it.copy(activeEffects = it.activeEffects.copy(shield = false)) }
                addFloatingText("SHIELD SAVED! 🛡️", head.x, head.y, 0xFFEAB308)
                triggerScreenShake(8f)
                SoundSynth.playEat("SHIELD")
            } else {
                handleCrash()
                return
            }
        }

        // 3. Obstacle Collision Check
        if (state.gameMode == GameMode.LEVELS) {
            val levelConfig = GameData.LEVEL_CONFIGS[state.currentLevelIdx]
            val hitObstacle = levelConfig.obstacles.any { it.x == nextHead.x && it.y == nextHead.y }
            if (hitObstacle && !isImmortal) {
                if (hasShield) {
                    _uiState.update { it.copy(activeEffects = it.activeEffects.copy(shield = false)) }
                    addFloatingText("SHIELD SAVED! 🛡️", head.x, head.y, 0xFFEAB308)
                    triggerScreenShake(8f)
                    SoundSynth.playEat("SHIELD")
                } else {
                    handleCrash()
                    return
                }
            }
        }

        // 4. Food & Power-Up Eating Check
        val newSnake = mutableListOf(nextHead).apply { addAll(snake) }
        val currentFood = state.food
        val currentPower = state.powerUp

        if (currentFood != null && nextHead.x == currentFood.position.x && nextHead.y == currentFood.position.y) {
            onFoodEaten(currentFood, newSnake)
        } else if (currentPower != null && nextHead.x == currentPower.position.x && nextHead.y == currentPower.position.y) {
            onPowerUpEaten(currentPower, newSnake)
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

        var newEffects = state.activeEffects
        var boosterCount = state.boosterEatenCount
        var newBannerMsg: String? = null

        when (food.type) {
            "CAKE" -> {
                if (newSnake.isNotEmpty()) {
                    newSnake.add(newSnake.last())
                    newSnake.add(newSnake.last())
                }
                newBannerMsg = "🍰 Feast Cake: +2 Length & Points!"
                addFloatingText("FEAST CAKE! 🍰", food.position.x, food.position.y, 0xFFFF007F)
            }
            else -> {
                if (food.points >= 25) {
                    val foodTemplate = GameData.ALL_FOOD_TEMPLATES.find { it.type == food.type }
                    val foodName = foodTemplate?.name ?: "Bonus Food"
                    newBannerMsg = "${food.emoji} $foodName: +$earned PTS!"
                }
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

        // Daily Missions progress update
        prefs.incrementDailyMissionProgress("eat_food", 1)
        prefs.setDailyMissionProgress("high_score", nextScore)

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

        val obstacles = if (state.gameMode == GameMode.CLASSIC) emptyList() else GameData.LEVEL_CONFIGS[state.currentLevelIdx].obstacles
        val nextFood = spawnFood(newSnake, obstacles)
        val updatedFoodStock = prefs.addFoodStock(food.type, 1)

        // 4th Food Power Spawn: exactly 3 foods gap between powers, on 4th food spawn power up
        var nextPowerUp = state.powerUp
        foodsSinceLastPower++
        if (nextPowerUp == null && foodsSinceLastPower >= 4) {
            nextPowerUp = spawnPowerUp(newSnake, obstacles, nextFood.position)
            foodsSinceLastPower = 0
        }

        createExplosion(food.position.x, food.position.y, food.color, 12)
        addFloatingText("+$earned${if (multiplier > 1) " (x$multiplier)" else ""}", food.position.x, food.position.y, food.color)

        val bannerToDisplay = newBannerMsg ?: if (nextCombo > 1) "🔥 ${multiplier}x Combo ($nextCombo)" else null

        _uiState.update {
            it.copy(
                prevSnake = state.snake,
                snake = newSnake,
                direction = nextDirection,
                score = nextScore,
                highScore = nextScore.coerceAtLeast(state.highScore),
                coins = prefs.getCoins(),
                comboMultiplier = multiplier,
                comboCount = nextCombo,
                foodEatenCount = nextEaten,
                boosterEatenCount = boosterCount,
                food = nextFood,
                powerUp = nextPowerUp,
                foodInventory = it.foodInventory.toMutableMap().apply { put(food.type, updatedFoodStock) },
                bannerMessage = bannerToDisplay ?: it.bannerMessage,
                bannerExpiryTime = if (bannerToDisplay != null) 2000L else it.bannerExpiryTime,
                activeEffects = newEffects,
                mouthOpen = true
            )
        }
    }

    private fun onPowerUpEaten(power: Food, newSnake: MutableList<Position>) {
        val state = _uiState.value
        SoundSynth.playEat(power.type, state.comboCount)

        var newEffects = state.activeEffects
        var boosterCount = state.boosterEatenCount
        var newBannerMsg: String? = null
        val earned = power.points

        when (power.type) {
            "POWER_DRAGON" -> {
                newEffects = newEffects.copy(
                    dragon = 12000L,
                    immortal = 12000L,
                    doublePoints = 12000L,
                    magnet = 12000L,
                    chili = 12000L,
                    booster = 12000L,
                    freeze = 0L,
                    shield = true
                )
                boosterCount += 1
                newBannerMsg = "🐉 DRAGON BEAST: ALL POWERS ACTIVATED (12s)!"
                unlockAchievementDirect("immortal_ghost")
                unlockAchievementDirect("double_deal")
                unlockAchievementDirect("magnet_pull")
                unlockAchievementDirect("perfect_reflexes")
                addFloatingText("ALL POWERS DRAGON! 🐉", power.position.x, power.position.y, 0xFFF59E0B)
                triggerScreenShake(12f)
            }
            "POWER_SPEED" -> {
                newEffects = newEffects.copy(chili = 10000L)
                newBannerMsg = "⚡ Hyper Speed Boost (10s)!"
                unlockAchievementDirect("perfect_reflexes")
                addFloatingText("HYPER SPEED! ⚡", power.position.x, power.position.y, 0xFF38BDF8)
            }
            "POWER_IMMORTAL" -> {
                newEffects = newEffects.copy(immortal = 10000L)
                newBannerMsg = "👻 Ghost Shield: Wall & Body Pass-Through (10s)!"
                unlockAchievementDirect("immortal_ghost")
                addFloatingText("GHOST IMMORTAL! 👻", power.position.x, power.position.y, 0xFFA78BFA)
            }
            "POWER_DOUBLE" -> {
                newEffects = newEffects.copy(doublePoints = 10000L)
                newBannerMsg = "💎 2X Points Multiplier Activated (10s)!"
                unlockAchievementDirect("double_deal")
                addFloatingText("DOUBLE DEAL! 💎", power.position.x, power.position.y, 0xFFF472B6)
            }
            "POWER_MAGNET" -> {
                newEffects = newEffects.copy(magnet = 10000L)
                newBannerMsg = "🧲 Magnet Pull: Attracting Food (10s)!"
                unlockAchievementDirect("magnet_pull")
                addFloatingText("MAGNET PULL! 🧲", power.position.x, power.position.y, 0xFFF87171)
            }
            "POWER_SHRINK" -> {
                val targetLength = (newSnake.size * 0.65f).toInt().coerceAtLeast(3)
                while (newSnake.size > targetLength) newSnake.removeAt(newSnake.size - 1)
                newBannerMsg = "🍄 Shrink Shroom: Tail Size Reduced!"
                unlockAchievementDirect("shrink_master")
                addFloatingText("SHRINK SHROOM! 🍄", power.position.x, power.position.y, 0xFF34D399)
            }
            "BOOSTER" -> {
                newEffects = newEffects.copy(booster = 10000L)
                boosterCount += 1
                newBannerMsg = "🧪 Blue Magic Surge (10s)!"
                unlockAchievementDirect("blue_magic")
                addFloatingText("BLUE MAGIC! 🧪", power.position.x, power.position.y, 0xFF3B82F6)
            }
            "GOLDEN_STAR" -> {
                newBannerMsg = "⭐ Golden Star: Mega Points!"
                unlockAchievementDirect("star_power")
                addFloatingText("STAR BLAST! ⭐", power.position.x, power.position.y, 0xFFFACC15)
                triggerScreenShake(8f)
            }
            "POWER_FREEZE" -> {
                newEffects = newEffects.copy(freeze = 10000L)
                newBannerMsg = "❄️ Frost Chill: Slow Precision Motion (10s)!"
                addFloatingText("FROST CHILL! ❄️", power.position.x, power.position.y, 0xFF06B6D4)
            }
            "POWER_SHIELD" -> {
                newEffects = newEffects.copy(shield = true)
                newBannerMsg = "🛡️ Safe Shield: Protects from 1 Crash!"
                addFloatingText("SAFE SHIELD! 🛡️", power.position.x, power.position.y, 0xFFEAB308)
            }
        }

        prefs.incrementDailyMissionProgress("power_surge", 1)
        val nextScore = state.score + earned

        createExplosion(power.position.x, power.position.y, power.color, 16)

        _uiState.update {
            it.copy(
                prevSnake = state.snake,
                snake = newSnake,
                direction = nextDirection,
                score = nextScore,
                highScore = nextScore.coerceAtLeast(state.highScore),
                coins = prefs.getCoins(),
                powerUp = null,
                boosterEatenCount = boosterCount,
                bannerMessage = newBannerMsg ?: it.bannerMessage,
                bannerExpiryTime = if (newBannerMsg != null) 2000L else it.bannerExpiryTime,
                activeEffects = newEffects,
                mouthOpen = true
            )
        }
    }

    private fun handleCrash() {
        SoundSynth.playCrash()
        triggerScreenShake(12f)
        val state = _uiState.value
        _uiState.update {
            it.copy(
                isPlaying = false,
                showGameOver = true,
                coins = prefs.getCoins(),
                dailyMissions = loadDailyMissions()
            )
        }
    }


    private fun spawnFood(currentSnake: List<Position>, obstacles: List<Position>, avoidPos: Position? = null): Food {
        var newPos = Position(5, 5)
        var attempts = 0
        var found = false

        // Safe bounds: (1 until GRID_SIZE - 1) ensures food never touches outer border or corner cells
        while (!found && attempts < 200) {
            newPos = Position(
                (1 until (GRID_SIZE - 1)).random(),
                (1 until (GRID_SIZE - 1)).random()
            )
            val inSnake = currentSnake.any { it.x == newPos.x && it.y == newPos.y }
            val inObstacle = obstacles.any { it.x == newPos.x && it.y == newPos.y }
            val onAvoid = avoidPos != null && (newPos.x == avoidPos.x && newPos.y == avoidPos.y)
            if (!inSnake && !inObstacle && !onAvoid) {
                found = true
            }
            attempts++
        }

        val allowed = _uiState.value.allowedFruits
        val pool = if (allowed.isNotEmpty()) {
            GameData.ALL_FOOD_TEMPLATES.filter { allowed.contains(it.type) }
        } else {
            GameData.ALL_FOOD_TEMPLATES
        }.ifEmpty { GameData.ALL_FOOD_TEMPLATES }

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
            points = selected.points,
            isPower = false,
            remainingLifeMs = 15000L
        )
    }

    private fun spawnPowerUp(currentSnake: List<Position>, obstacles: List<Position>, foodPos: Position): Food? {
        val allowed = _uiState.value.allowedPowers
        val pool = if (allowed.isNotEmpty()) {
            GameData.ALL_POWER_TEMPLATES.filter { allowed.contains(it.type) }
        } else {
            GameData.ALL_POWER_TEMPLATES
        }.ifEmpty { GameData.ALL_POWER_TEMPLATES }

        if (pool.isEmpty()) return null

        var newPos = Position(6, 6)
        var attempts = 0
        var found = false

        while (!found && attempts < 200) {
            newPos = Position(
                (1 until (GRID_SIZE - 1)).random(),
                (1 until (GRID_SIZE - 1)).random()
            )
            val inSnake = currentSnake.any { it.x == newPos.x && it.y == newPos.y }
            val inObstacle = obstacles.any { it.x == newPos.x && it.y == newPos.y }
            val onFood = (newPos.x == foodPos.x && newPos.y == foodPos.y)
            if (!inSnake && !inObstacle && !onFood) {
                found = true
            }
            attempts++
        }
        if (!found) return null

        val selected = pool.random()
        return Food(
            position = newPos,
            type = selected.type,
            color = selected.color,
            emoji = selected.emoji,
            points = selected.points,
            isPower = true,
            remainingLifeMs = 10000L // 10s eat time as requested!
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

    fun claimAchievement(id: String, rewardCoins: Int) {
        if (prefs.claimAchievement(id, rewardCoins)) {
            SoundSynth.playAchievement()
            _uiState.update {
                it.copy(
                    coins = prefs.getCoins(),
                    claimedAchievements = prefs.getClaimedAchievements()
                )
            }
        }
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
                chiliCrying = (state.activeEffects.chiliCrying - dt).coerceAtLeast(0L),
                freeze = (state.activeEffects.freeze - dt).coerceAtLeast(0L),
                dragon = (state.activeEffects.dragon - dt).coerceAtLeast(0L)
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

            // Food expiration (15 seconds of active gameplay time)
            var currentFood = state.food
            if (currentFood != null) {
                val nextFoodLife = currentFood.remainingLifeMs - dt
                if (nextFoodLife <= 0L) {
                    currentFood = spawnFood(
                        state.snake,
                        if (state.gameMode == GameMode.CLASSIC) emptyList() else GameData.LEVEL_CONFIGS[state.currentLevelIdx].obstacles
                    )
                } else {
                    currentFood = currentFood.copy(remainingLifeMs = nextFoodLife)
                }
            }

            // PowerUp expiration (10 seconds of active gameplay time)
            var currentPower = state.powerUp
            if (currentPower != null) {
                val nextPowerLife = currentPower.remainingLifeMs - dt
                if (nextPowerLife <= 0L) {
                    currentPower = null
                } else {
                    currentPower = currentPower.copy(remainingLifeMs = nextPowerLife)
                }
            }

            // Banner notification expiration (strictly 2 seconds of active gameplay time)
            val nextBannerExpiry = (state.bannerExpiryTime - dt).coerceAtLeast(0L)
            val currentBanner = if (nextBannerExpiry > 0L) state.bannerMessage else null

            state.copy(
                activeEffects = nextEffects,
                screenShake = nextShake,
                particles = updatedParticles,
                floatingTexts = updatedTexts,
                food = currentFood,
                powerUp = currentPower,
                bannerMessage = currentBanner,
                bannerExpiryTime = nextBannerExpiry
            )
        }
    }
}
