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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.model.Skin
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
                onHomeClick = { SoundSynth.playClick() },
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
                    start = 14.dp,
                    end = 14.dp,
                    top = statusBarPadding + 4.dp,
                    bottom = 18.dp
                ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // 1. TOP BAR (Hamburger Menu, Snake Avatar, Coins Balance)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                // Left Hamburger Button ☰ (Food Bazaar & Sell Market)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(42.dp)
                        .shadow(3.dp, RoundedCornerShape(8.dp))
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

                // Center Circular Snake Avatar Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(46.dp)
                        .shadow(6.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFF10B981))
                        .border(2.dp, Color(0xFF059669), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🐍",
                        fontSize = 24.sp,
                        textAlign = TextAlign.Center
                    )
                }

                // Right Golden Coins Balance Chip 🪙
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .shadow(3.dp, RoundedCornerShape(8.dp))
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFF1E293B))
                        .border(1.5.dp, Color(0xFFF59E0B), RoundedCornerShape(8.dp))
                        .clickable {
                            SoundSynth.playCoin()
                            onOpenSkins()
                        }
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                        .testTag("home_coins_chip"),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = "🪙", fontSize = 14.sp)
                        Text(
                            text = "${uiState.coins}",
                            color = Color(0xFFFBBF24),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "+",
                            color = Color(0xFF10B981),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // 2. HEADER TITLE & SUBTITLE
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "Slinky Snake Adventures",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF10B981),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "A magical journey filled with shining stars, spicy chilies, and golden crowns! ✨⭐",
                    fontSize = 11.sp,
                    color = Color(0xFFCBD5E1),
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp,
                    modifier = Modifier.padding(horizontal = 6.dp)
                )
            }

            // 3. MODE SELECTOR (Classic Mode vs Levels Mode)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF131D2E))
                    .border(1.5.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                    .padding(3.dp)
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
                            .background(if (isClassic) Color(0xFF10B981) else Color.Transparent)
                            .border(
                                width = if (isClassic) 1.5.dp else 0.dp,
                                color = if (isClassic) Color(0xFF059669) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.setGameMode(GameMode.CLASSIC) }
                            .padding(vertical = 10.dp)
                            .testTag("mode_classic_card"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "♾️ Classic Mode",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
                            color = if (isClassic) Color(0xFF0F172A) else Color(0xFF94A3B8)
                        )
                    }

                    // Right Pill: 🗺️ Levels Mode
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isLevels) Color(0xFF8B5CF6) else Color.Transparent)
                            .border(
                                width = if (isLevels) 1.5.dp else 0.dp,
                                color = if (isLevels) Color(0xFF7C3AED) else Color.Transparent,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { viewModel.setGameMode(GameMode.LEVELS) }
                            .padding(vertical = 10.dp)
                            .testTag("mode_levels_card"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🗺️ Levels Mode",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp,
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
                        .border(1.5.dp, Color(0xFF8B5CF6), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "🗺️ SELECT WORLD (SWIPE ➔)",
                                color = Color(0xFFC084FC),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.6.sp
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFF3B0764)
                            ) {
                                Text(
                                    text = "${uiState.unlockedLevel}/25 Unlocked",
                                    color = Color(0xFFE9D5FF),
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        // World Horizontal Scroll
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            contentPadding = PaddingValues(vertical = 2.dp)
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
                                            .size(width = 60.dp, height = 56.dp)
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
                                                fontSize = 8.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)
                                            )
                                            if (isUnlocked) {
                                                Text(
                                                    text = "${level.level}",
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Black,
                                                    color = if (isSelected) Color(0xFF0F172A) else Color.White
                                                )
                                            } else {
                                                Text(text = "🔒", fontSize = 13.sp)
                                            }
                                        }
                                    }

                                    if (isSelected) {
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Box(
                                            modifier = Modifier
                                                .width(24.dp)
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
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 7.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = "📍 Target destination: ",
                                    color = Color(0xFFCBD5E1),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Text(
                                    text = "${currentLevel.theme.name} 🏡",
                                    color = Color(0xFFFB7185),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }
                    }
                }
            }

            // 5. CHOOSE YOUR SNAKE HERO (with lock icon on locked skins, no coins text)
            ChooseSnakeHeroSection(
                uiState = uiState,
                onSelectSkin = { skin ->
                    viewModel.selectSkin(skin)
                },
                onOpenSkins = onOpenSkins
            )

            // 6. READY TO SLITHER? HERO CARD WITH START BUTTON
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF132034))
                    .border(2.dp, Color(0xFF10B981), RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Center Snake Avatar with Real Board Theme Background & Snake Body
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(118.dp)
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
                        verticalArrangement = Arrangement.spacedBy(1.dp)
                    ) {
                        Text(
                            text = "READY TO SLITHER?",
                            color = Color(0xFF94A3B8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.2.sp
                        )
                        Text(
                            text = uiState.selectedSkin.name,
                            color = Color.White,
                            fontSize = 22.sp,
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
                            .padding(10.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(3.dp)
                        ) {
                            Text(
                                text = "Your Skin: ${uiState.selectedSkin.name} 🧔",
                                color = Color(0xFFF59E0B),
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black
                            )

                            if (uiState.gameMode == GameMode.LEVELS) {
                                Text(
                                    text = "Level Mode: Level ${currentLevel.level} – ${currentLevel.theme.name} 🏡\nTarget score: ${currentLevel.targetScore} points.",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.5.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 15.sp
                                )
                            } else {
                                Text(
                                    text = "Classic Mode: No limits! Eat delicious food and beat highscore!",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.5.sp,
                                    textAlign = TextAlign.Center,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }

                    // Big Action Button: START (Prominent & Clear)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .shadow(6.dp, RoundedCornerShape(8.dp))
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF10B981))
                            .border(2.dp, Color(0xFF059669), RoundedCornerShape(8.dp))
                            .clickable {
                                onStartGame()
                            }
                            .testTag("play_game_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Start Game",
                                tint = Color(0xFF0F172A),
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                text = "START GAME",
                                color = Color(0xFF0F172A),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ChooseSnakeHeroSection(
    uiState: GameUiState,
    onSelectSkin: (Skin) -> Unit,
    onOpenSkins: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF131D2E))
            .border(1.5.dp, Color(0xFFF59E0B), RoundedCornerShape(10.dp))
            .padding(10.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🎭 Choose Your Snake Hero! 🎭",
                    color = Color(0xFFF59E0B),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black
                )

                Text(
                    text = "View All ➔",
                    color = Color(0xFFFBBF24),
                    fontSize = 11.5.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onOpenSkins() }
                )
            }

            // Horizontal row of quick snake heroes
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 2.dp)
            ) {
                itemsIndexed(GameData.SNAKE_SKINS) { _, skin ->
                    val isSelected = skin.id == uiState.selectedSkin.id
                    val isUnlocked = uiState.unlockedSkins.contains(skin.id)

                    Box(
                        modifier = Modifier
                            .size(width = 78.dp, height = 86.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (isSelected) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF0F172A)
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { onSelectSkin(skin) }
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                SnakeHeadCanvas(skin = skin, size = 40.dp)
                                if (!isUnlocked) {
                                    // Lock overlay icon ONLY (no coins text per user requirement)
                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .background(Color(0xCC0F172A))
                                            .border(1.dp, Color(0xFFF59E0B), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            Icons.Default.Lock,
                                            contentDescription = "Locked",
                                            tint = Color(0xFFFBBF24),
                                            modifier = Modifier.size(13.dp)
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(3.dp))
                            Text(
                                text = skin.name.split(" ").firstOrNull() ?: skin.name,
                                fontSize = 9.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color(0xFFF59E0B) else if (isUnlocked) Color.White else Color(0xFF94A3B8),
                                maxLines = 1
                            )
                        }
                    }
                }
            }
        }
    }
}
