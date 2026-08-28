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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import com.example.slinkysnake.audio.SoundSynth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.ui.components.BottomGameNavBar
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.ui.components.SnakeArenaPreviewCanvas
import com.example.slinkysnake.ui.components.SnakeHeadCanvas
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

@Composable
fun HomeScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onStartGame: () -> Unit,
    onOpenSkins: () -> Unit,
    onOpenMarket: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val currentLevel = GameData.LEVEL_CONFIGS[uiState.currentLevelIdx.coerceIn(0, GameData.LEVEL_CONFIGS.size - 1)]

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0A1128), // Deep Dark Navy Canvas
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomGameNavBar(
                selectedTab = NavTab.HOME,
                onHomeClick = {
                    SoundSynth.playClick()
                },
                onMarketClick = {
                    SoundSynth.playClick()
                    onOpenMarket()
                },
                onSkinsClick = {
                    SoundSynth.playClick()
                    onOpenSkins()
                },
                onSettingsClick = {
                    SoundSynth.playClick()
                    onOpenSettings()
                }
            )
        }
    ) { padding ->
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = statusBarPadding + 6.dp,
                    bottom = 32.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. TOP BAR (Hamburger Menu, Snake Avatar, Sound Volume Pill)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                // Left Hamburger Button ☰ (Food Bazaar & Sell Market)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(44.dp)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.5.dp, Color(0xFF10B981).copy(alpha = 0.8f), RoundedCornerShape(8.dp))
                        .clickable {
                            SoundSynth.playClick()
                            onOpenMarket()
                        }
                        .testTag("home_food_market_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Food Market",
                        tint = Color(0xFF10B981),
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Perfectly Center Circular Snake Avatar Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(52.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                        .border(2.dp, Color(0xFF059669), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🐍",
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Right Golden Coins Balance Chip 🪙
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .shadow(4.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.5.dp, Color(0xFFF59E0B), RoundedCornerShape(8.dp))
                        .clickable {
                            SoundSynth.playCoin()
                            onOpenSkins()
                        }
                        .padding(horizontal = 12.dp, vertical = 7.dp)
                        .testTag("home_coins_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Text(
                            text = "🪙",
                            fontSize = 15.sp
                        )
                        Text(
                            text = "${uiState.coins}",
                            color = Color(0xFFFBBF24), // Golden Amber
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "+",
                            color = Color(0xFF10B981),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // 2. HEADER TITLE & SUBTITLE (Exact style from screenshot)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Slinky Snake Adventures",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF10B981), // Bright Vibrant Mint Green
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "A magical journey filled with shining stars, spicy chilies, and golden crowns! ✨⭐",
                    fontSize = 11.5.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }

            // 3. MODE SELECTOR CONTAINER (Classic Mode vs Levels Mode)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF131D2E))
                    .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                    .padding(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    val isClassic = uiState.gameMode == GameMode.CLASSIC
                    val isLevels = uiState.gameMode == GameMode.LEVELS

                    // Left Pill: ♾️ Classic Mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isClassic) Color(0xFF10B981) else Color.Transparent
                            )
                            .border(
                                width = if (isClassic) 2.dp else 0.dp,
                                color = if (isClassic) Color(0xFF059669) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.setGameMode(GameMode.CLASSIC) }
                            .padding(vertical = 12.dp)
                            .testTag("mode_classic_card"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♾️ Classic Mode",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.5.sp,
                            color = if (isClassic) Color(0xFF0F172A) else Color(0xFF94A3B8)
                        )
                    }

                    // Right Pill: 🗺️ Levels Mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isLevels) Color(0xFF8B5CF6) else Color.Transparent
                            )
                            .border(
                                width = if (isLevels) 2.dp else 0.dp,
                                color = if (isLevels) Color(0xFF7C3AED) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.setGameMode(GameMode.LEVELS) }
                            .padding(vertical = 12.dp)
                            .testTag("mode_levels_card"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🗺️ Levels Mode",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.5.sp,
                            color = if (isLevels) Color(0xFF0F172A) else Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // 4. WORLD SELECTOR CARD (Shown when Levels Mode is selected)
            AnimatedVisibility(
                visible = uiState.gameMode == GameMode.LEVELS,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF131D2E))
                        .border(2.dp, Color(0xFF8B5CF6), RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // World Header Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🗺️ SELECT WORLD (SWIPE/SCROLL ➔)",
                                color = Color(0xFFC084FC),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.8.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF3B0764)
                            ) {
                                Text(
                                    text = "${uiState.unlockedLevel}/25 Unlocked",
                                    color = Color(0xFFE9D5FF),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                )
                            }
                        }

                        // World Horizontal Scroll
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(GameData.LEVEL_CONFIGS) { index, level ->
                                val isUnlocked = level.level <= uiState.unlockedLevel
                                val isSelected = index == uiState.currentLevelIdx

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.clickable(enabled = isUnlocked) {
                                        viewModel.selectLevel(index)
                                    }
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(width = 64.dp, height = 60.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                when {
                                                    isSelected -> Color(0xFF8B5CF6)
                                                    isUnlocked -> Color(0xFF1E293B)
                                                    else -> Color(0xFF0F172A)
                                                }
                                            )
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) Color(0xFFC084FC) else Color(0xFF334155),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .testTag("level_item_${level.level}"),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column(
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center
                                        ) {
                                            Text(
                                                text = "WORLD",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)
                                            )
                                            if (isUnlocked) {
                                                Text(
                                                    text = "${level.level}",
                                                    fontSize = 18.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (isSelected) Color(0xFF0F172A) else Color.White
                                                )
                                            } else {
                                                Text(
                                                    text = "🔒",
                                                    fontSize = 14.sp
                                                )
                                            }
                                        }
                                    }

                                    // Indicator for selected world
                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(28.dp)
                                                .height(3.dp)
                                                .clip(RoundedCornerShape(2.dp))
                                                .background(Color(0xFF8B5CF6))
                                        )
                                    }
                                }
                            }
                        }

                        // Target Destination Card
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                .padding(horizontal = 14.dp, vertical = 9.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "📍 Target destination: ",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${currentLevel.theme.name} 🏡",
                                    color = Color(0xFFFB7185), // Coral Red Accent
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // In Classic mode: Show "Choose Your Snake Hero!" ABOVE the Start box
            if (uiState.gameMode == GameMode.CLASSIC) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF131D2E))
                        .border(2.dp, Color(0xFFF59E0B), RoundedCornerShape(10.dp)) // Amber Border
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎭 Choose Your Snake Hero! 🎭",
                                color = Color(0xFFF59E0B),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )

                            Text(
                                text = "View All ➔",
                                color = Color(0xFFFBBF24),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onOpenSkins() }
                            )
                        }

                        // Horizontal row of quick snake heroes
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(GameData.SNAKE_SKINS) { _, skin ->
                                val isSelected = skin.id == uiState.selectedSkin.id

                                Box(
                                    modifier = Modifier
                                        .size(width = 82.dp, height = 90.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF0F172A)
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { viewModel.selectSkin(skin) }
                                        .padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        SnakeHeadCanvas(skin = skin, size = 44.dp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = skin.name.split(" ").firstOrNull() ?: skin.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color(0xFFF59E0B) else Color.White,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 5. READY TO SLITHER? HERO CARD
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF132034))
                    .border(2.dp, Color(0xFF10B981), RoundedCornerShape(10.dp)) // Emerald Neon Border
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Center Snake Avatar with Real Board Theme Background & Snake Body
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(112.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .border(1.5.dp, Color(0xFF10B981).copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                            .clickable {
                                SoundSynth.playClick()
                                onOpenSkins()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        SnakeArenaPreviewCanvas(
                            skin = uiState.selectedSkin,
                            bgCol1 = if (uiState.gameMode == GameMode.LEVELS) currentLevel.theme.bgCol1 else uiState.boardThemeColor1,
                            bgCol2 = if (uiState.gameMode == GameMode.LEVELS) currentLevel.theme.bgCol2 else uiState.boardThemeColor2,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // READY TO SLITHER? & Name
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Text(
                            text = "READY TO SLITHER?",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = uiState.selectedSkin.name,
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    // Mode Details Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF0F1829))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "Your Skin: ${uiState.selectedSkin.name} 🧔",
                                color = Color(0xFFF59E0B), // Golden/Amber
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )

                            if (uiState.gameMode == GameMode.LEVELS) {
                                Text(
                                    text = "Level Mode: Entering Level ${currentLevel.level} – ${currentLevel.theme.name} 🏡!\nTarget score: ${currentLevel.targetScore} points.",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp
                                )
                            } else {
                                Text(
                                    text = "Classic Mode: No limits! Just eat delicious food and beat your highscore!",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 12.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    // Big Action Button: START
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .shadow(6.dp, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981)) // Bright Emerald Green
                            .border(2.dp, Color(0xFF059669), RoundedCornerShape(8.dp))
                            .clickable {
                                onStartGame()
                            }
                            .testTag("play_game_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "START",
                            color = Color(0xFF0F172A),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                    }
                }
            }

            // In Levels mode: Show "Choose Your Snake Hero!" BELOW the Start box
            if (uiState.gameMode == GameMode.LEVELS) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF131D2E))
                        .border(2.dp, Color(0xFFF59E0B), RoundedCornerShape(10.dp)) // Amber Border
                        .padding(12.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🎭 Choose Your Snake Hero! 🎭",
                                color = Color(0xFFF59E0B),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )

                            Text(
                                text = "View All ➔",
                                color = Color(0xFFFBBF24),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable { onOpenSkins() }
                            )
                        }

                        // Horizontal row of quick snake heroes
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            itemsIndexed(GameData.SNAKE_SKINS) { _, skin ->
                                val isSelected = skin.id == uiState.selectedSkin.id

                                Box(
                                    modifier = Modifier
                                        .size(width = 82.dp, height = 90.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            if (isSelected) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF0F172A)
                                        )
                                        .border(
                                            width = if (isSelected) 2.dp else 1.dp,
                                            color = if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B),
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { viewModel.selectSkin(skin) }
                                        .padding(6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        SnakeHeadCanvas(skin = skin, size = 44.dp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = skin.name.split(" ").firstOrNull() ?: skin.name,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isSelected) Color(0xFFF59E0B) else Color.White,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
