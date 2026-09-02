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
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // 1. TOP COMPACT BLACK HEADER BAR (Pitch Black, fixed height so layout never shifts)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF000000))
                        .padding(
                            top = statusBarPadding + 2.dp,
                            bottom = 4.dp,
                            start = 14.dp,
                            end = 14.dp
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(38.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Game Mode / Level Title OR Live Notification message in same place without shifting layout
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.weight(1f, fill = false).padding(end = 8.dp)
                        ) {
                            if (uiState.bannerMessage != null) {
                                // Notification badge in place of title without shifting layout or overlapping MENU
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF1E293B),
                                    border = BorderStroke(1.dp, Color(0xFF6366F1))
                                ) {
                                    Text(
                                        text = uiState.bannerMessage,
                                        color = Color(0xFF38BDF8),
                                        fontSize = 11.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        maxLines = 1
                                    )
                                }
                            } else {
                                Text(
                                    text = if (uiState.gameMode == GameMode.LEVELS) "LEVEL ${currentLevel.level}" else "CLASSIC ARENA",
                                    color = Color(0xFF38BDF8), // Crisp Sky Blue
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 1.sp,
                                    maxLines = 1
                                )
                            }
                        }

                        // Right: MENU Button (Clean Yellow / Amber Capsule)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(Color(0xFFF59E0B))
                                .clickable {
                                    viewModel.pauseGame()
                                    showMenuDialog = true
                                }
                                .padding(horizontal = 14.dp, vertical = 6.dp)
                                .testTag("game_menu_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "⚙️",
                                    fontSize = 12.sp
                                )
                                Text(
                                    text = "MENU",
                                    color = Color(0xFF0F172A),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                            }
                        }
                    }
                }

                // 2. MAIN GAMEPLAY BOX (Directly attached below Top Header Bar)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clipToBounds()
                        .border(width = 2.dp, color = Color(0xFF0F172A))
                ) {
                    GameBoardCanvas(
                        snake = uiState.snake,
                        prevSnake = uiState.prevSnake,
                        moveProgress = uiState.moveProgress,
                        food = uiState.food,
                        powerUp = uiState.powerUp,
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

                    // On-Board Pause Overlay
                    if (uiState.isPaused && !uiState.showGameOver && !showMenuDialog && !showExitDialog && uiState.countdown == null) {
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

                    // On-Board Game Over / Crash Card (Sleek, just like Pause card with Food & XP stats, Restart & Continue)
                    if (uiState.showGameOver && uiState.countdown == null) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color.Black.copy(alpha = 0.70f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Surface(
                                shape = RoundedCornerShape(22.dp),
                                color = Color(0xFF1E293B),
                                border = BorderStroke(2.5.dp, Color(0xFFEF4444)),
                                shadowElevation = 14.dp,
                                modifier = Modifier.padding(horizontal = 18.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Text(
                                        text = "💥 OUT / CRASHED",
                                        color = Color(0xFFEF4444),
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )

                                    // Food Eaten & Score Stats in sleek compact bar
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = Color(0xFF0F172A),
                                        border = BorderStroke(1.dp, Color(0xFF334155))
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("🍎 FOODS EATEN", fontSize = 10.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                                                Text("${uiState.foodEatenCount}", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color.White)
                                            }
                                            Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color(0xFF334155)))
                                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                                Text("⚡ EARNED XP", fontSize = 10.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
                                                Text("${uiState.score} XP", fontSize = 16.sp, fontWeight = FontWeight.Black, color = Color(0xFFA855F7))
                                            }
                                        }
                                    }

                                    // Action Buttons: CONTINUE (Respawn with Shield) & RESTART
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        // Continue Button (Emerald Green)
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFF10B981),
                                            modifier = Modifier
                                                .clickable { viewModel.respawnGame() }
                                                .testTag("continue_respawn_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = "🛡️ CONTINUE",
                                                    color = Color.White,
                                                    fontSize = 12.5.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }

                                        // Restart Button (Amber)
                                        Surface(
                                            shape = RoundedCornerShape(12.dp),
                                            color = Color(0xFFF59E0B),
                                            modifier = Modifier
                                                .clickable { viewModel.startGame() }
                                                .testTag("restart_game_button")
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                Text(
                                                    text = "🔄 RESTART",
                                                    color = Color(0xFF0F172A),
                                                    fontSize = 12.5.sp,
                                                    fontWeight = FontWeight.Black
                                                )
                                            }
                                        }
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

                // 2. SCORE & HIGHSCORE BAR (Directly below Game Board Canvas with zero layout shift)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF0A0F1D))
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Left: SCORE: 30 XP
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SCORE: ",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "${uiState.score} XP",
                            color = Color(0xFFA855F7),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }

                    // Right: HIGHSCORE / LEVEL TARGET
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = if (uiState.gameMode == GameMode.LEVELS) "TARGET: " else "HIGHSCORE: ",
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = if (uiState.gameMode == GameMode.LEVELS) "${currentLevel.targetScore} XP" else "${uiState.highScore} XP",
                            color = Color(0xFF10B981),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // 5. ARCADE CONTROLLER (Shifted slightly upwards)
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
                    modifier = Modifier.padding(bottom = navBarPadding + 22.dp)
                )
            }

            // In-Game Arcade Menu Popup Dialog (Speed, Volume, Resume, Restart, Exit)
            if (showMenuDialog) {
                InGameMenuDialog(
                    speedMultiplier = uiState.speedMultiplier,
                    isSoundEnabled = uiState.isSoundEnabled,
                    soundVolume = uiState.soundVolume,
                    onSpeedChange = { speed -> viewModel.setSpeedMultiplier(speed) },
                    onSoundToggle = { enabled -> viewModel.setSoundEnabled(enabled) },
                    onVolumeChange = { vol -> viewModel.setSoundVolume(vol) },
                    onResume = {
                        showMenuDialog = false
                        viewModel.resumeWithCountdown()
                    },
                    onRestart = {
                        showMenuDialog = false
                        viewModel.startGame()
                    },
                    onExit = {
                        showMenuDialog = false
                        showExitDialog = true
                    },
                    onDismiss = {
                        showMenuDialog = false
                        viewModel.resumeWithCountdown()
                    }
                )
            }

            // Exit Confirmation Popup Dialog
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
                            .background(Color.Black.copy(alpha = 0.82f))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(28.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF131D2E)),
                            border = BorderStroke(2.5.dp, Color(0xFFEF4444)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .widthIn(max = 390.dp)
                                .shadow(20.dp, RoundedCornerShape(28.dp))
                        ) {
                            Column(
                                modifier = Modifier.padding(horizontal = 24.dp, vertical = 28.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(20.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(68.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFF7F1D1D))
                                        .border(2.dp, Color(0xFFEF4444), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "🚪",
                                        fontSize = 32.sp
                                    )
                                }

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "EXIT TO MENU?",
                                        color = Color(0xFFEF4444),
                                        fontSize = 23.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Text(
                                        text = "Your current session will end and score will be finalized.",
                                        color = Color(0xFF94A3B8),
                                        fontSize = 13.5.sp,
                                        textAlign = TextAlign.Center,
                                        lineHeight = 19.sp
                                    )
                                }

                                Surface(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color(0xFF0F172A),
                                    border = BorderStroke(1.dp, Color(0xFF334155)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                        horizontalArrangement = Arrangement.SpaceAround,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "CURRENT XP", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "${uiState.score} XP", color = Color(0xFF10B981), fontSize = 15.sp, fontWeight = FontWeight.Black)
                                        }
                                        Box(modifier = Modifier.width(1.dp).height(28.dp).background(Color(0xFF334155)))
                                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                            Text(text = "TOTAL COINS", color = Color(0xFF64748B), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                            Text(text = "🪙 ${uiState.coins}", color = Color(0xFFFBBF24), fontSize = 15.sp, fontWeight = FontWeight.Black)
                                        }
                                    }
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Button(
                                        onClick = {
                                            showExitDialog = false
                                            viewModel.exitGame()
                                            onBackToHome()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp)
                                            .testTag("confirm_exit_button")
                                    ) {
                                        Text(
                                            text = "🚪 EXIT",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                    }

                                    Button(
                                        onClick = {
                                            showExitDialog = false
                                            viewModel.resumeWithCountdown()
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp)
                                            .testTag("resume_game_button")
                                    ) {
                                        Text(
                                            text = "▶️ RESUME",
                                            color = Color.White,
                                            fontWeight = FontWeight.Black,
                                            fontSize = 14.sp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Level Clear Overlay
            if (uiState.showLevelClear) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.78f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        border = BorderStroke(2.5.dp, Color(0xFF10B981)),
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "LEVEL COMPLETED! 🌟",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFF10B981)
                            )

                            Text(
                                text = "Awesome job! You reached the target score and earned +25 Bonus Coins! 🪙",
                                color = Color(0xFFCBD5E1),
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = Color(0xFF0F172A),
                                border = BorderStroke(1.dp, Color(0xFF334155)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("SCORE", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                        Text("${uiState.score}", color = Color(0xFF10B981), fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text("TOTAL COINS", color = Color(0xFF94A3B8), fontSize = 11.sp)
                                        Text("🪙 ${uiState.coins}", color = Color(0xFFFBBF24), fontSize = 18.sp, fontWeight = FontWeight.Black)
                                    }
                                }
                            }

                            Button(
                                onClick = { viewModel.nextLevel() },
                                modifier = Modifier.fillMaxWidth().height(50.dp).testTag("next_level_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF10B981)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("NEXT LEVEL ➔", fontWeight = FontWeight.Black, fontSize = 15.sp)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("HOME MENU", color = Color.White, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Victory Overlay
            if (uiState.showVictory) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.85f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B)),
                        border = BorderStroke(2.5.dp, Color(0xFFF59E0B)),
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "🏆 GAME COMPLETED! 🏆",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFF59E0B),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "Congratulations! You have conquered all worlds in Slinky Snake Adventures! You are a legendary Snake Master! 👑🐍",
                                color = Color(0xFFCBD5E1),
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp
                            )

                            Button(
                                onClick = {
                                    viewModel.selectLevel(0)
                                    viewModel.startGame()
                                },
                                modifier = Modifier.fillMaxWidth().height(50.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF59E0B)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("PLAY AGAIN (WORLD 1)", color = Color(0xFF0F172A), fontWeight = FontWeight.Black)
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth().height(48.dp),
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
