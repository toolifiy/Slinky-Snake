package com.example.slinkysnake.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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

    var showExitDialog by remember { mutableStateOf(false) }
    var showMenuDialog by remember { mutableStateOf(false) }

    // Outer Navy Background (#131C2E) with Golden Outer Frame (#F59E0B)
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF131C2E),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { padding ->
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFF131C2E))
                .arcadeSwipeController(uiState.direction) { dir -> viewModel.onDirectionInput(dir) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = statusBarPadding,
                        bottom = navBarPadding + 4.dp
                    ),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. TOP GAME BOARD AREA
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clipToBounds()
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
                        isPaused = uiState.isPaused,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Top-Left Floating 3-Second Food Popup Banner & Active Boosts
                    val activeBoostText = when {
                        uiState.activeEffects.chili > 0L -> "🔥 Speed Boost (${String.format("%.1f", uiState.activeEffects.chili / 1000f)}s)"
                        uiState.activeEffects.booster > 0L -> "⚡ Hyper Surge (${String.format("%.1f", uiState.activeEffects.booster / 1000f)}s)"
                        uiState.activeEffects.immortal > 0L -> "👻 Ghost Shield (${String.format("%.1f", uiState.activeEffects.immortal / 1000f)}s)"
                        uiState.activeEffects.doublePoints > 0L -> "💎 2X Points (${String.format("%.1f", uiState.activeEffects.doublePoints / 1000f)}s)"
                        uiState.activeEffects.grape > 0L -> "🍇 Chill Slow (${String.format("%.1f", uiState.activeEffects.grape / 1000f)}s)"
                        uiState.comboCount > 1 -> "🔥 ${uiState.comboMultiplier}x Combo (${uiState.comboCount})"
                        else -> null
                    }

                    val bannerText = uiState.bannerMessage ?: activeBoostText

                    if (bannerText != null) {
                        Box(
                            modifier = Modifier
                                .padding(8.dp)
                                .align(Alignment.TopStart)
                                .shadow(6.dp, RoundedCornerShape(12.dp))
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    Brush.horizontalGradient(
                                        if (uiState.bannerMessage != null)
                                            listOf(Color(0xFFD97706), Color(0xFFFBBF24))
                                        else
                                            listOf(Color(0xFFEA580C), Color(0xFFF59E0B))
                                    )
                                )
                                .border(1.5.dp, Color(0xFFFEF08A), RoundedCornerShape(12.dp))
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = bannerText,
                                color = if (uiState.bannerMessage != null) Color(0xFF0F172A) else Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }

                    // On-Board Pause Overlay (When paused without menu or exit dialog)
                    if (uiState.isPaused && !showMenuDialog && !showExitDialog && uiState.countdown == null) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.65f))
                                .clickable { viewModel.resumeWithCountdown() },
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = Color(0xFF1E293B),
                                border = BorderStroke(2.5.dp, Color(0xFFF59E0B)),
                                shadowElevation = 12.dp
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 20.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "⏸️ PAUSED",
                                        color = Color(0xFFF59E0B),
                                        fontSize = 26.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 2.sp
                                    )
                                    Surface(
                                        shape = RoundedCornerShape(14.dp),
                                        color = Color(0xFF10B981),
                                        modifier = Modifier.clickable { viewModel.resumeWithCountdown() }
                                    ) {
                                        Text(
                                            text = "▶️ TAP TO RESUME",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // On-Board 3-Second Countdown Overlay
                    if (uiState.countdown != null) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.65f)),
                            contentAlignment = Alignment.Center
                        ) {
                            val countText = if (uiState.countdown == 0) "GO! 🚀" else "${uiState.countdown}"
                            val countColor = if (uiState.countdown == 0) Color(0xFF10B981) else Color(0xFFF59E0B)
                            Text(
                                text = countText,
                                fontSize = if (uiState.countdown == 0) 56.sp else 80.sp,
                                fontWeight = FontWeight.Black,
                                color = countColor,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = Color.Black,
                                        offset = Offset(4f, 4f),
                                        blurRadius = 10f
                                    )
                                )
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
                                viewModel.pauseGame()
                                showExitDialog = true
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
                                viewModel.pauseGame()
                                showMenuDialog = true
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
                    onPauseClick = {
                        if (uiState.isPaused) {
                            viewModel.resumeWithCountdown()
                        } else {
                            viewModel.pauseGame()
                        }
                    },
                    isPaused = uiState.isPaused,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
            }

            // In-Game Arcade Menu Popup Dialog (ONLY when ⚙️ MENU is clicked)
            if (showMenuDialog) {
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
                    onDismiss = {
                        showMenuDialog = false
                        viewModel.resumeWithCountdown()
                    }
                )
            }

            // Exit Confirmation Popup Dialog (When 🚪 EXIT is clicked)
            if (showExitDialog) {
                Dialog(
                    onDismissRequest = {
                        showExitDialog = false
                        viewModel.resumeWithCountdown()
                    },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.75f))
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(24.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                            border = BorderStroke(2.dp, Color(0xFFEF4444)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 380.dp)
                                .shadow(16.dp, RoundedCornerShape(24.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Text(
                                    text = "🚪 EXIT GAME?",
                                    color = Color(0xFFEF4444),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "Are you sure you want to exit to the main menu? Your current session progress will end.",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 14.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 20.sp
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    // Exit Button
                                    Button(
                                        onClick = {
                                            showExitDialog = false
                                            viewModel.exitGame()
                                            onBackToHome()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .testTag("confirm_exit_button")
                                    ) {
                                        Text(
                                            text = "🚪 EXIT APP",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp
                                        )
                                    }

                                    // Resume Button
                                    Button(
                                        onClick = {
                                            showExitDialog = false
                                            viewModel.resumeWithCountdown()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                        shape = RoundedCornerShape(14.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(48.dp)
                                            .testTag("resume_game_button")
                                    ) {
                                        Text(
                                            text = "▶️ RESUME",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 13.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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
