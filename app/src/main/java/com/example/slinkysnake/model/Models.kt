package com.example.slinkysnake.model

enum class Direction {
    UP, DOWN, LEFT, RIGHT
}

data class Position(
    val x: Int,
    val y: Int
)

data class Particle(
    val id: Long,
    var x: Float,
    var y: Float,
    var vx: Float,
    var vy: Float,
    val color: Long,
    val size: Float,
    var life: Float,
    val maxLife: Float
)

data class FloatingText(
    val id: Long,
    val text: String,
    var x: Float,
    var y: Float,
    val color: Long,
    var life: Int
)

enum class Accessory {
    NONE, CROWN, SUNGLASSES, BANDANA, MUSTACHE
}

enum class Pattern {
    SOLID, STRIPES, SPOTS, GLOW
}

data class Skin(
    val id: String,
    val name: String,
    val primaryColor: Long,
    val secondaryColor: Long,
    val eyeColor: Long,
    val accessory: Accessory,
    val pattern: Pattern,
    val description: String,
    val price: Int = 0
)

enum class GameMode {
    CLASSIC, LEVELS
}

data class LevelTheme(
    val name: String,
    val bgCol1: Long,
    val bgCol2: Long,
    val gridColor: Long,
    val borderColor: Long
)

data class LevelConfig(
    val level: Int,
    val targetScore: Int,
    val speed: Long, // base tick delay in ms
    val obstacles: List<Position>,
    val theme: LevelTheme
)

data class BoardTheme(
    val id: String,
    val name: String,
    val color1: Long,
    val color2: Long,
    val price: Int = 0,
    val description: String = ""
)

data class Achievement(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val category: String = "ALL",
    val rewardCoins: Int = 50,
    val targetGoal: Int = 1,
    val accentColor: Long = 0xFF10B981
)

enum class FoodCategory {
    FRESH_FRUIT, SWEET_TREAT, SAVORY_MEAL, POWER_UP
}

data class FoodTemplate(
    val type: String,
    val emoji: String,
    val name: String,
    val color: Long,
    val points: Int,
    val prob: Int,
    val category: FoodCategory,
    val effectDescription: String
) {
    // Number of food items required to sell for 1 coin based on spawn rate / rarity
    val unitsPerCoin: Int
        get() = when {
            prob >= 20 -> 20 // High spawn rate (e.g. Apple: 20 pack = 1 Coin)
            prob >= 10 -> 10 // Common spawn rate (e.g. Banana, Orange, Chili: 10 pack = 1 Coin)
            prob >= 7 -> 5   // Uncommon spawn rate (e.g. Watermelon, Coconut: 5 pack = 1 Coin)
            prob >= 5 -> 2   // Rare spawn rate (e.g. Cake, Pizza, Burger, Dragon Fruit: 2 pack = 1 Coin)
            else -> 1        // Ultra rare (1 food = 1 Coin)
        }

    val sellPrice: Int get() = 1
}

data class Food(
    val position: Position,
    val type: String,
    val color: Long,
    val emoji: String,
    val points: Int,
    val isPower: Boolean = false,
    val spawnTime: Long = System.currentTimeMillis(),
    val remainingLifeMs: Long = 15000L
)

data class ActiveEffects(
    val chili: Long = 0L, // ms remaining
    val grape: Long = 0L,
    val booster: Long = 0L,
    val immortal: Long = 0L,
    val doublePoints: Long = 0L,
    val magnet: Long = 0L,
    val chiliCrying: Long = 0L,
    val freeze: Long = 0L,
    val shield: Boolean = false
)

data class DailyMission(
    val id: String,
    val title: String,
    val description: String,
    val icon: String,
    val targetGoal: Int,
    val currentProgress: Int,
    val rewardCoins: Int,
    val isClaimed: Boolean = false
)

