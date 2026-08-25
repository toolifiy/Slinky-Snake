package com.example.slinkysnake.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.ui.components.ArcadeDpadControls
import com.example.slinkysnake.ui.components.GameBoardCanvas
import com.example.slinkysnake.ui.components.arcadeSwipeController
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

@Composable
fun GamePlayScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onBackToHome: () -> Unit
) {
    val currentTheme = GameData.BOARD_THEMES.find { it.id == uiState.boardThemeId } ?: GameData.BOARD_THEMES[0]
    val currentLevel = GameData.LEVEL_CONFIGS[uiState.currentLevelIdx]

    // Outer Navy Background (#131C2E) with Golden Outer Frame (#F59E0B)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF131C2E)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF131C2E))
                .border(6.dp, Color(0xFFF59E0B)) // Golden Outer Frame
                .arcadeSwipeController(uiState.direction) { dir -> viewModel.onDirectionInput(dir) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. TOP GAME BOARD AREA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(width = 3.dp, color = Color(0xFF0F172A))
                ) {
                    // Checkered Game Board
                    GameBoardCanvas(
                        snake = uiState.snake,
                        prevSnake = uiState.prevSnake,
                        moveProgress = uiState.moveProgress,
                        food = uiState.food,
                        obstacles = if (uiState.gameMode == GameMode.CLASSIC) emptyList() else currentLevel.obstacles,
                        selectedSkin = uiState.selectedSkin,
                        gameMode = uiState.gameMode,
                        levelConfig = currentLevel,
                        boardThemeColor1 = currentTheme.color1,
                        boardThemeColor2 = currentTheme.color2,
                        activeEffects = uiState.activeEffects,
                        particles = uiState.particles,
                        floatingTexts = uiState.floatingTexts,
                        screenShake = uiState.screenShake,
                        direction = uiState.direction,
                        mouthOpen = uiState.mouthOpen,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Top-Left Floating Active Power-Up Banner (e.g. 🔥 Speed Boost (4.2s))
                    val activeBoostText = when {
                        uiState.activeEffects.chili > 0L -> "🔥 Speed Boost (${String.format("%.1f", uiState.activeEffects.chili / 1000f)}s)"
                        uiState.activeEffects.booster > 0L -> "⚡ Hyper Surge (${String.format("%.1f", uiState.activeEffects.booster / 1000f)}s)"
                        uiState.activeEffects.immortal > 0L -> "👻 Ghost Shield (${String.format("%.1f", uiState.activeEffects.immortal / 1000f)}s)"
                        uiState.activeEffects.doublePoints > 0L -> "💎 2X Points (${String.format("%.1f", uiState.activeEffects.doublePoints / 1000f)}s)"
                        uiState.activeEffects.grape > 0L -> "🍇 Chill Slow (${String.format("%.1f", uiState.activeEffects.grape / 1000f)}s)"
                        uiState.comboCount > 1 -> "🔥 ${uiState.comboMultiplier}x Combo (${uiState.comboCount})"
                        else -> null
                    }

                    if (activeBoostText != null) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                                .shadow(6.dp, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        listOf(Color(0xFFEA580C), Color(0xFFF59E0B))
                                    )
                                )
                                .border(1.5.dp, Color(0xFFFEF08A), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = activeBoostText,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }

                // 2. SCORE & HIGHSCORE BAR (Exact match to screenshot)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A0F1D))
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: SCORE: 30 PTS
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SCORE: ",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${uiState.score} PTS",
                            color = Color(0xFFA855F7), // Vibrant Purple
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    // Right: HIGHSCORE: 1986 PTS
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "HIGHSCORE: ",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${uiState.highScore} PTS",
                            color = Color(0xFF10B981), // Vibrant Mint Green
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // 3. ACTION BUTTONS ROW (🚪 EXIT & ⚙️ MENU)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 🚪 EXIT Button (Red Rounded Capsule)
                    Box(
                        modifier = Modifier
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFEF4444))
                            .border(2.dp, Color(0xFFDC2626), RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.exitGame()
                                onBackToHome()
                            }
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                            .testTag("game_exit_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🚪 EXIT",
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    // ⚙️ MENU Button (Yellow / Amber Rounded Capsule)
                    Box(
                        modifier = Modifier
                            .shadow(6.dp, RoundedCornerShape(16.dp))
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFFF59E0B))
                            .border(2.dp, Color(0xFFD97706), RoundedCornerShape(16.dp))
                            .clickable {
                                viewModel.togglePause()
                            }
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                            .testTag("game_menu_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "⚙️ MENU",
                            color = Color(0xFF0F172A),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }

                // 4. ARCADE CONTROLLER (D-PAD)
                ArcadeDpadControls(
                    currentDirection = uiState.direction,
                    onDirectionChange = { dir -> viewModel.onDirectionInput(dir) },
                    onPauseClick = { viewModel.togglePause() },
                    isPaused = uiState.isPaused,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // Countdown Overlay
            if (uiState.countdown != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.5f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${uiState.countdown}",
                        fontSize = 72.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // Pause / Menu Overlay (Exact match to screenshot)
            if (uiState.isPaused && uiState.countdown == null) {
                InGameMenuDialog(
                    currentThemeId = uiState.boardThemeId,
                    speedMultiplier = uiState.speedMultiplier,
                    isSoundEnabled = uiState.isSoundEnabled,
                    soundVolume = uiState.soundVolume,
                    allowedFruits = uiState.allowedFruits,
                    selectedSkin = uiState.selectedSkin,
                    onSelectTheme = { themeId -> viewModel.setBoardTheme(themeId) },
                    onSpeedChange = { speed -> viewModel.setSpeedMultiplier(speed) },
                    onSoundToggle = { enabled -> viewModel.setSoundEnabled(enabled) },
                    onVolumeChange = { vol -> viewModel.setSoundVolume(vol) },
                    onFruitToggle = { fruitType -> viewModel.toggleFruit(fruitType) },
                    onSelectSkin = { skin -> viewModel.selectSkin(skin) },
                    onDismiss = { viewModel.togglePause() }
                )
            }

            // Game Over Overlay
            if (uiState.showGameOver) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.75f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "GAME OVER 💥",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFEF4444)
                            )

                            if (uiState.score >= uiState.highScore && uiState.score > 0) {
                                Surface(
                                    shape = RoundedCornerShape(14.dp),
                                    color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                                    border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFFF59E0B))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, tint = Color(0xFFF59E0B))
                                        Text(
                                            text = "NEW HIGH SCORE! 🎉",
                                            fontWeight = FontWeight.Black,
                                            color = Color(0xFFF59E0B),
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Final Score", fontSize = 12.sp, color = Color(0xFF94A3B8))
                                    Text("${uiState.score}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Foods Eaten", fontSize = 12.sp, color = Color(0xFF94A3B8))
                                    Text("${uiState.foodEatenCount}", fontSize = 24.sp, fontWeight = FontWeight.Black, color = Color.White)
                                }
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Button(
                                onClick = { viewModel.startGame() },
                                modifier = Modifier.fillMaxWidth().testTag("play_again_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Replay, contentDescription = null)
                                    Text("PLAY AGAIN", fontWeight = FontWeight.Black)
                                }
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("game_over_home_button"),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("HOME MENU", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }
    }
}
