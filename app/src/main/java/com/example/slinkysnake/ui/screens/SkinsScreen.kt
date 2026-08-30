package com.example.slinkysnake.ui.screens

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.BoardTheme
import com.example.slinkysnake.model.Skin
import com.example.slinkysnake.ui.components.BackgroundThemeThumbnailCanvas
import com.example.slinkysnake.ui.components.BottomGameNavBar
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.ui.components.SnakeHeadCanvas
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

enum class WardrobeTab {
    SNAKE,
    BACKGROUND
}

@Composable
fun SkinsScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onBackToHome: () -> Unit,
    onOpenMarket: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var activeTab by remember { mutableStateOf(WardrobeTab.SNAKE) }

    val allSkins = GameData.SNAKE_SKINS
    val allThemes = GameData.BOARD_THEMES

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0A1128), // Deep Dark Luxury Gaming Canvas
        bottomBar = {
            BottomGameNavBar(
                selectedTab = NavTab.SKINS,
                onHomeClick = {
                    SoundSynth.playClick()
                    onBackToHome()
                },
                onMarketClick = {
                    SoundSynth.playClick()
                    onOpenMarket()
                },
                onSkinsClick = {
                    SoundSynth.playClick()
                },
                onSettingsClick = {
                    SoundSynth.playClick()
                    onOpenSettings()
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = statusBarPadding + 8.dp,
                    bottom = paddingValues.calculateBottomPadding()
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // 1. TOP HEADER with Coins Counter
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Palette,
                                contentDescription = "Wardrobe",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "VIP WARDROBE",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = if (activeTab == WardrobeTab.SNAKE) "Serpent hero avatars" else "Arena background themes",
                                fontSize = 11.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    // Coins Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFFF59E0B), RoundedCornerShape(20.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "${uiState.coins}",
                                color = Color(0xFFFDE68A),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                    }
                }
            }

            // 2. TAB SELECTOR (Snake Skins vs Background Themes) - Default Snake
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Snake Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (activeTab == WardrobeTab.SNAKE) Color(0xFF3B82F6) else Color.Transparent
                            )
                            .clickable {
                                activeTab = WardrobeTab.SNAKE
                                SoundSynth.playClick()
                            }
                            .padding(vertical = 10.dp)
                            .testTag("tab_snake_skins"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🐍 Snake Skins",
                            color = if (activeTab == WardrobeTab.SNAKE) Color.White else Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }

                    // Background Tab
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                if (activeTab == WardrobeTab.BACKGROUND) Color(0xFF06B6D4) else Color.Transparent
                            )
                            .clickable {
                                activeTab = WardrobeTab.BACKGROUND
                                SoundSynth.playClick()
                            }
                            .padding(vertical = 10.dp)
                            .testTag("tab_background_themes"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "🖼️ Backgrounds",
                            color = if (activeTab == WardrobeTab.BACKGROUND) Color.White else Color(0xFF94A3B8),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            }

            // 3. CONTENT BASED ON SELECTED TAB
            when (activeTab) {
                WardrobeTab.SNAKE -> {
                    val chunks = allSkins.chunked(2)
                    items(chunks) { rowSkins ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            for (skin in rowSkins) {
                                val isEquipped = uiState.selectedSkin.id == skin.id
                                val isUnlocked = uiState.unlockedSkins.contains(skin.id)
                                val canAfford = uiState.coins >= skin.price

                                Box(modifier = Modifier.weight(1f)) {
                                    SkinCardItem(
                                        skin = skin,
                                        isEquipped = isEquipped,
                                        isUnlocked = isUnlocked,
                                        canAfford = canAfford,
                                        onCardClick = {
                                            if (isUnlocked) {
                                                viewModel.selectSkin(skin)
                                                SoundSynth.playClick()
                                            } else if (canAfford) {
                                                viewModel.buySkin(skin)
                                            }
                                        },
                                        onBuyClick = {
                                            viewModel.buySkin(skin)
                                        }
                                    )
                                }
                            }
                            if (rowSkins.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                WardrobeTab.BACKGROUND -> {
                    val chunks = allThemes.chunked(2)
                    items(chunks) { rowThemes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            for (theme in rowThemes) {
                                val isEquipped = uiState.boardThemeId == theme.id
                                val isUnlocked = uiState.unlockedThemes.contains(theme.id) || theme.price == 0
                                val canAfford = uiState.coins >= theme.price

                                Box(modifier = Modifier.weight(1f)) {
                                    ThemeCardItem(
                                        theme = theme,
                                        isEquipped = isEquipped,
                                        isUnlocked = isUnlocked,
                                        canAfford = canAfford,
                                        onCardClick = {
                                            if (isUnlocked) {
                                                viewModel.setBoardTheme(theme.id)
                                                SoundSynth.playClick()
                                            } else if (canAfford) {
                                                viewModel.buyTheme(theme)
                                            }
                                        },
                                        onBuyClick = {
                                            viewModel.buyTheme(theme)
                                        }
                                    )
                                }
                            }
                            if (rowThemes.size < 2) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SkinCardItem(
    skin: Skin,
    isEquipped: Boolean,
    isUnlocked: Boolean,
    canAfford: Boolean,
    onCardClick: () -> Unit,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("skin_item_${skin.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isEquipped -> Color(0xFF112233)
                else -> Color(0xFF0F172A)
            }
        ),
        border = BorderStroke(
            width = if (isEquipped) 2.dp else 1.dp,
            color = when {
                isEquipped -> Color(0xFF10B981)
                isUnlocked -> Color(skin.primaryColor).copy(alpha = 0.4f)
                else -> Color(0xFF1E293B)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Avatar Box
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E293B))
                    .border(
                        1.5.dp,
                        if (isUnlocked) Color(skin.primaryColor).copy(alpha = 0.75f) else Color(0xFF334155),
                        RoundedCornerShape(10.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                SnakeHeadCanvas(
                    skin = skin,
                    modifier = Modifier.size(66.dp)
                )
            }

            // Skin Name
            Text(
                text = skin.name,
                fontSize = 12.5.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            // Status or Buy Button
            if (isEquipped) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF10B981).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFF10B981), RoundedCornerShape(6.dp))
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EQUIPPED ✅",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF34D399)
                    )
                }
            } else if (isUnlocked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF3B82F6).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFF3B82F6), RoundedCornerShape(6.dp))
                        .clickable { onCardClick() }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EQUIP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF60A5FA)
                    )
                }
            } else {
                Button(
                    onClick = onBuyClick,
                    enabled = canAfford,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B),
                        disabledContainerColor = Color(0xFF1E293B)
                    ),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = if (canAfford) Color(0xFF0F172A) else Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${skin.price}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (canAfford) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeCardItem(
    theme: BoardTheme,
    isEquipped: Boolean,
    isUnlocked: Boolean,
    canAfford: Boolean,
    onCardClick: () -> Unit,
    onBuyClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() }
            .testTag("theme_item_${theme.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isEquipped -> Color(0xFF112233)
                else -> Color(0xFF0F172A)
            }
        ),
        border = BorderStroke(
            width = if (isEquipped) 2.dp else 1.dp,
            color = when {
                isEquipped -> Color(0xFF10B981)
                isUnlocked -> Color(0xFF06B6D4).copy(alpha = 0.5f)
                else -> Color(0xFF1E293B)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Arena Background Thumbnail Preview Box (Pure arena background, NO food, NO snake)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(68.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        if (isEquipped) Color(0xFF10B981) else Color(0xFF334155),
                        RoundedCornerShape(8.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                BackgroundThemeThumbnailCanvas(
                    bgCol1 = theme.color1,
                    bgCol2 = theme.color2,
                    modifier = Modifier.fillMaxSize()
                )
            }

            // Theme Name
            Text(
                text = theme.name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )

            // Status or Buy Button
            if (isEquipped) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF10B981).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFF10B981), RoundedCornerShape(6.dp))
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EQUIPPED ✅",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF34D399)
                    )
                }
            } else if (isUnlocked) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF06B6D4).copy(alpha = 0.2f))
                        .border(1.dp, Color(0xFF06B6D4), RoundedCornerShape(6.dp))
                        .clickable { onCardClick() }
                        .padding(vertical = 4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "EQUIP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF22D3EE)
                    )
                }
            } else {
                Button(
                    onClick = onBuyClick,
                    enabled = canAfford,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(30.dp),
                    shape = RoundedCornerShape(6.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFF59E0B),
                        disabledContainerColor = Color(0xFF1E293B)
                    ),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = if (canAfford) Color(0xFF0F172A) else Color(0xFF64748B),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "${theme.price}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            color = if (canAfford) Color(0xFF0F172A) else Color(0xFF64748B)
                        )
                    }
                }
            }
        }
    }
}
