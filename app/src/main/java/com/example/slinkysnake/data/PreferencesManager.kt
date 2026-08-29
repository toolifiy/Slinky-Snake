package com.example.slinkysnake.data

import android.content.Context
import android.content.SharedPreferences
import com.example.slinkysnake.model.GameMode

class PreferencesManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("slinky_snake_prefs", Context.MODE_PRIVATE)

    fun getHighScore(mode: GameMode): Int {
        return prefs.getInt("snake_hs_${mode.name}", 0)
    }

    fun setHighScore(mode: GameMode, score: Int) {
        val current = getHighScore(mode)
        if (score > current) {
            prefs.edit().putInt("snake_hs_${mode.name}", score).apply()
        }
    }

    fun getUnlockedLevel(): Int {
        return prefs.getInt("snake_unlocked_level", 1)
    }

    fun setUnlockedLevel(level: Int) {
        val current = getUnlockedLevel()
        if (level > current) {
            prefs.edit().putInt("snake_unlocked_level", level).apply()
        }
    }

    fun getSelectedSkinId(): String {
        return prefs.getString("snake_selected_skin_id", "slinky") ?: "slinky"
    }

    fun setSelectedSkinId(skinId: String) {
        prefs.edit().putString("snake_selected_skin_id", skinId).apply()
    }

    fun getCoins(): Int {
        return prefs.getInt("snake_coins_balance", 100) // 100 welcome gift coins
    }

    fun setCoins(coins: Int) {
        prefs.edit().putInt("snake_coins_balance", coins.coerceAtLeast(0)).apply()
    }

    fun addCoins(amount: Int): Int {
        val current = getCoins()
        val updated = (current + amount).coerceAtLeast(0)
        prefs.edit().putInt("snake_coins_balance", updated).apply()
        return updated
    }

    fun getUnlockedSkinIds(): Set<String> {
        return prefs.getStringSet("snake_unlocked_skins", setOf("slinky")) ?: setOf("slinky")
    }

    fun unlockSkin(skinId: String): Boolean {
        val current = getUnlockedSkinIds().toMutableSet()
        if (!current.contains(skinId)) {
            current.add(skinId)
            prefs.edit().putStringSet("snake_unlocked_skins", current).apply()
            return true
        }
        return false
    }

    fun isSkinUnlocked(skinId: String): Boolean {
        if (skinId == "slinky") return true
        return getUnlockedSkinIds().contains(skinId)
    }

    fun getPlayedSkinIds(): Set<String> {
        return prefs.getStringSet("snake_played_skins", setOf("slinky")) ?: setOf("slinky")
    }

    fun addPlayedSkinId(skinId: String): Set<String> {
        val current = getPlayedSkinIds().toMutableSet()
        current.add(skinId)
        prefs.edit().putStringSet("snake_played_skins", current).apply()
        return current
    }

    fun getUnlockedAchievements(): Set<String> {
        return prefs.getStringSet("snake_unlocked_achievements", emptySet()) ?: emptySet()
    }

    fun unlockAchievement(achievementId: String): Boolean {
        val current = getUnlockedAchievements().toMutableSet()
        if (!current.contains(achievementId)) {
            current.add(achievementId)
            prefs.edit().putStringSet("snake_unlocked_achievements", current).apply()
            return true
        }
        return false
    }

    fun getClaimedAchievements(): Set<String> {
        return prefs.getStringSet("snake_claimed_achievements", emptySet()) ?: emptySet()
    }

    fun claimAchievement(achievementId: String, rewardCoins: Int): Boolean {
        val current = getClaimedAchievements().toMutableSet()
        if (!current.contains(achievementId)) {
            current.add(achievementId)
            prefs.edit().putStringSet("snake_claimed_achievements", current).apply()
            addCoins(rewardCoins)
            return true
        }
        return false
    }

    fun getSpeedMultiplier(): Float {
        return prefs.getFloat("snake_speed_multiplier", 1.0f)
    }

    fun setSpeedMultiplier(multiplier: Float) {
        prefs.edit().putFloat("snake_speed_multiplier", multiplier).apply()
    }

    fun getBoardTheme(): String {
        return prefs.getString("snake_board_theme", "mint") ?: "mint"
    }

    fun setBoardTheme(themeId: String) {
        prefs.edit().putString("snake_board_theme", themeId).apply()
    }

    fun isSoundEnabled(): Boolean {
        return prefs.getBoolean("snake_sound_enabled", true)
    }

    fun setSoundEnabled(enabled: Boolean) {
        prefs.edit().putBoolean("snake_sound_enabled", enabled).apply()
    }

    fun getSoundVolume(): Float {
        return prefs.getFloat("snake_sound_volume", 0.8f)
    }

    fun setSoundVolume(volume: Float) {
        prefs.edit().putFloat("snake_sound_volume", volume).apply()
    }

    fun getAllowedFruits(): Set<String> {
        // Defaults to first 25 standard foods
        val defaults = GameData.ALL_FOOD_TEMPLATES.take(25).map { it.type }.toSet()
        return prefs.getStringSet("snake_allowed_fruits", defaults) ?: defaults
    }

    fun setAllowedFruits(fruits: Set<String>) {
        prefs.edit().putStringSet("snake_allowed_fruits", fruits).apply()
    }

    fun getFoodStock(foodType: String): Int {
        // Default starter stock of 5 for every food so player can sell immediately!
        return prefs.getInt("snake_food_stock_$foodType", 5)
    }

    fun setFoodStock(foodType: String, count: Int) {
        prefs.edit().putInt("snake_food_stock_$foodType", count.coerceAtLeast(0)).apply()
    }

    fun addFoodStock(foodType: String, amount: Int = 1): Int {
        val updated = (getFoodStock(foodType) + amount).coerceAtLeast(0)
        setFoodStock(foodType, updated)
        return updated
    }

    fun getAllFoodInventory(): Map<String, Int> {
        val map = mutableMapOf<String, Int>()
        GameData.ALL_FOOD_TEMPLATES.forEach { food ->
            map[food.type] = getFoodStock(food.type)
        }
        return map
    }

    private fun getTodayDateString(): String {
        return java.text.SimpleDateFormat("yyyyMMdd", java.util.Locale.US).format(java.util.Date())
    }

    fun checkAndResetDailyMissions() {
        val today = getTodayDateString()
        val lastDate = prefs.getString("snake_daily_missions_date", "") ?: ""
        if (lastDate != today) {
            prefs.edit()
                .putString("snake_daily_missions_date", today)
                .remove("snake_daily_progress_eat_food")
                .remove("snake_daily_progress_power_surge")
                .remove("snake_daily_progress_high_score")
                .remove("snake_daily_claimed_eat_food")
                .remove("snake_daily_claimed_power_surge")
                .remove("snake_daily_claimed_high_score")
                .apply()
        }
    }

    fun getDailyMissionProgress(missionId: String): Int {
        checkAndResetDailyMissions()
        return prefs.getInt("snake_daily_progress_$missionId", 0)
    }

    fun incrementDailyMissionProgress(missionId: String, amount: Int = 1): Int {
        checkAndResetDailyMissions()
        val current = getDailyMissionProgress(missionId)
        val updated = current + amount
        prefs.edit().putInt("snake_daily_progress_$missionId", updated).apply()
        return updated
    }

    fun setDailyMissionProgress(missionId: String, value: Int): Int {
        checkAndResetDailyMissions()
        val current = getDailyMissionProgress(missionId)
        val updated = maxOf(current, value)
        prefs.edit().putInt("snake_daily_progress_$missionId", updated).apply()
        return updated
    }

    fun isDailyMissionClaimed(missionId: String): Boolean {
        checkAndResetDailyMissions()
        return prefs.getBoolean("snake_daily_claimed_$missionId", false)
    }

    fun claimDailyMission(missionId: String, rewardCoins: Int): Boolean {
        checkAndResetDailyMissions()
        if (!isDailyMissionClaimed(missionId)) {
            prefs.edit().putBoolean("snake_daily_claimed_$missionId", true).apply()
            addCoins(rewardCoins)
            return true
        }
        return false
    }

    fun resetAllProgress() {
        prefs.edit().clear().apply()
    }
}

