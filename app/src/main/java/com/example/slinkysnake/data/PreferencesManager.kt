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

    fun resetAllProgress() {
        prefs.edit().clear().apply()
    }
}
