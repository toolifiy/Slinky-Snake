package com.example.slinkysnake.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.ui.components.DpadControls
import com.example.slinkysnake.ui.components.GameBoardCanvas
import com.example.slinkysnake.ui.components.swipeController
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

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .swipeController(uiState.direction) { dir -> viewModel.onDirectionInput(dir) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // 1. Top HUD
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            viewModel.exitGame()
                            onBackToHome()
                        },
                        modifier = Modifier.testTag("game_back_button")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }

                    // Score Card
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.85f),
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "Score: ${uiState.score}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            if (uiState.gameMode == GameMode.LEVELS) {
                                Text(
                                    text = "/ ${currentLevel.targetScore}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    // Mode / Level Badge
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = if (uiState.gameMode == GameMode.LEVELS) "Lvl ${currentLevel.level}" else "Classic",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }

                    // Pause Button
                    IconButton(
                        onClick = { viewModel.togglePause() },
                        modifier = Modifier.testTag("game_pause_button")
                    ) {
                        Icon(
                            if (uiState.isPaused) Icons.Default.PlayArrow else Icons.Default.Pause,
                            contentDescription = if (uiState.isPaused) "Resume" else "Pause"
                        )
                    }
                }

                // 2. Active Effects Badges & Combos
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Power Up Badges
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        if (uiState.activeEffects.immortal > 0L) {
                            EffectBadge(emoji = "👻", label = "${uiState.activeEffects.immortal / 1000}s", color = Color(0xFFA78BFA))
                        }
                        if (uiState.activeEffects.chili > 0L) {
                            EffectBadge(emoji = "🔥", label = "${uiState.activeEffects.chili / 1000}s", color = Color(0xFFF97316))
                        }
                        if (uiState.activeEffects.booster > 0L) {
                            EffectBadge(emoji = "🧪", label = "${uiState.activeEffects.booster / 1000}s", color = Color(0xFF3B82F6))
                        }
                        if (uiState.activeEffects.doublePoints > 0L) {
                            EffectBadge(emoji = "💎", label = "${uiState.activeEffects.doublePoints / 1000}s", color = Color(0xFFF472B6))
                        }
                        if (uiState.activeEffects.grape > 0L) {
                            EffectBadge(emoji = "🍇", label = "${uiState.activeEffects.grape / 1000}s", color = Color(0xFF8B5CF6))
                        }
                    }

                    // Combo Multiplier
                    if (uiState.comboCount > 1) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEF4444)
                        ) {
                            Text(
                                text = "🔥 ${uiState.comboMultiplier}x Combo (${uiState.comboCount})",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                // 3. Central Game Board
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
                    modifier = Modifier.weight(1f, fill = false)
                )

                // 4. On-screen Controls (D-Pad)
                DpadControls(
                    currentDirection = uiState.direction,
                    onDirectionChange = { dir -> viewModel.onDirectionInput(dir) }
                )
            }

            // Countdown Overlay
            if (uiState.countdown != null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.45f)),
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

            // Pause Overlay
            if (uiState.isPaused && uiState.countdown == null) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.6f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                text = "Game Paused ⏸️",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            Button(
                                onClick = { viewModel.togglePause() },
                                modifier = Modifier.fillMaxWidth().testTag("resume_button"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Resume")
                            }
                            FilledTonalButton(
                                onClick = { viewModel.startGame() },
                                modifier = Modifier.fillMaxWidth().testTag("restart_button"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Restart")
                            }
                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("quit_button"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Main Menu")
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
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Game Over! 💥",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.error
                            )

                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(
                                    modifier = Modifier.padding(16.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Final Score: ${uiState.score}",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Text(
                                        text = "Best: ${uiState.highScore}  •  Foods Eaten: ${uiState.foodEatenCount}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(4.dp))

                            Button(
                                onClick = { viewModel.startGame() },
                                modifier = Modifier.fillMaxWidth().testTag("game_over_play_again"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(Icons.Default.Replay, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Play Again")
                            }

                            FilledTonalButton(
                                onClick = { viewModel.respawnGame() },
                                modifier = Modifier.fillMaxWidth().testTag("game_over_respawn"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Ghost Respawn (5s Ghost)")
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("game_over_menu"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Main Menu")
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
                        .background(Color.Black.copy(alpha = 0.7f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "Level Complete! 🎉",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )

                            Text(
                                text = "Awesome slithering! You reached the target score of ${currentLevel.targetScore} pts!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Button(
                                onClick = { viewModel.nextLevel() },
                                modifier = Modifier.fillMaxWidth().testTag("next_level_button"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Next Level ➡️")
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Main Menu")
                            }
                        }
                    }
                }
            }

            // Victory Overlay (Game Complete!)
            if (uiState.showVictory) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        shape = RoundedCornerShape(28.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = Color(0xFFF59E0B),
                                modifier = Modifier.size(56.dp)
                            )

                            Text(
                                text = "Grand Champion! 👑",
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFF59E0B),
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = "You have conquered all 25 levels of Slinky Snake Adventures! You are a certified Snake Master!",
                                style = MaterialTheme.typography.bodyMedium,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Button(
                                onClick = {
                                    viewModel.setGameMode(GameMode.CLASSIC)
                                    viewModel.startGame()
                                },
                                modifier = Modifier.fillMaxWidth().testTag("victory_play_classic"),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Play Endless Classic 🌟")
                            }

                            OutlinedButton(
                                onClick = {
                                    viewModel.exitGame()
                                    onBackToHome()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Text("Main Menu")
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EffectBadge(
    emoji: String,
    label: String,
    color: Color
) {
    Surface(
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.2f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(emoji, fontSize = 12.sp)
            Text(label, style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = color)
        }
    }
}
