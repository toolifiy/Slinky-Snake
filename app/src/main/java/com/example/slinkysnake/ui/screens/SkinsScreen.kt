package com.example.slinkysnake.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.scale
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
import com.example.slinkysnake.model.Accessory
import com.example.slinkysnake.model.Pattern
import com.example.slinkysnake.model.Skin
import com.example.slinkysnake.ui.components.BottomGameNavBar
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.ui.components.SnakeHeadCanvas
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

@Composable
fun SkinsScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onBackToHome: () -> Unit,
    onOpenMarket: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var previewSkin by remember(uiState.selectedSkin) { mutableStateOf(uiState.selectedSkin) }

    val allSkins = GameData.SNAKE_SKINS
    val totalSkinsCount = allSkins.size
    val unlockedCount = uiState.unlockedSkins.size

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0A1128), // Deep Dark Luxury Gaming Canvas
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
                    bottom = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // 1. TOP HEADER (Title - Coins pill removed)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                            contentDescription = "Skins",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "VIP SKINS WARDROBE",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Unlock & equip custom serpent avatars",
                            fontSize = 11.5.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }
            }

            // 2. HERO PREVIEW CARD (Displays Live Preview of Selected/Inspected Skin)
            item {
                val isEquipped = uiState.selectedSkin.id == previewSkin.id
                val isUnlocked = uiState.unlockedSkins.contains(previewSkin.id)
                val canAfford = uiState.coins >= previewSkin.price

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("skin_hero_preview_card"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0F172A)
                    ),
                    border = BorderStroke(
                        2.dp,
                        Brush.horizontalGradient(
                            listOf(
                                Color(previewSkin.primaryColor),
                                Color(previewSkin.secondaryColor)
                            )
                        )
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            // Big Snake Avatar Preview Box
                            Box(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(
                                        1.5.dp,
                                        Color(previewSkin.primaryColor),
                                        RoundedCornerShape(8.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                SnakeHeadCanvas(
                                    skin = previewSkin,
                                    modifier = Modifier.size(54.dp)
                                )
                            }

                            // Skin Details Column
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = previewSkin.name,
                                        fontSize = 17.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )

                                    if (isEquipped) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF10B981).copy(alpha = 0.2f))
                                                .border(1.dp, Color(0xFF10B981), RoundedCornerShape(6.dp))
                                                .padding(horizontal = 7.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "EQUIPPED",
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Black,
                                                color = Color(0xFF34D399)
                                            )
                                        }
                                    }
                                }

                                Text(
                                    text = previewSkin.description,
                                    fontSize = 11.5.sp,
                                    color = Color(0xFF94A3B8),
                                    lineHeight = 15.sp,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )

                                // Badges Row (Pattern + Accessory)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    if (previewSkin.accessory != Accessory.NONE) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(0xFF1E293B))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = previewSkin.accessory.name,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFFE2E8F0)
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Hero Action Button (Equip / Selected / Buy)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Unlocked: $unlockedCount / $totalSkinsCount Skins",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B),
                                fontWeight = FontWeight.SemiBold
                            )

                            when {
                                isEquipped -> {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color(0xFF10B981).copy(alpha = 0.25f))
                                            .border(1.dp, Color(0xFF10B981), RoundedCornerShape(8.dp))
                                            .padding(horizontal = 14.dp, vertical = 8.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.Check,
                                                contentDescription = null,
                                                tint = Color(0xFF10B981),
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Text(
                                                text = "IN USE",
                                                color = Color(0xFF34D399),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                                isUnlocked -> {
                                    Button(
                                        onClick = {
                                            viewModel.selectSkin(previewSkin)
                                        },
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFF10B981)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                                        modifier = Modifier.testTag("hero_equip_btn")
                                    ) {
                                        Text(
                                            text = "EQUIP SKIN",
                                            color = Color.White,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                    }
                                }
                                else -> {
                                    Button(
                                        onClick = {
                                            if (canAfford) {
                                                viewModel.buySkin(previewSkin)
                                            }
                                        },
                                        enabled = canAfford,
                                        shape = RoundedCornerShape(8.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = Color(0xFFF59E0B),
                                            disabledContainerColor = Color(0xFF334155)
                                        ),
                                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                        modifier = Modifier.testTag("hero_buy_btn")
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                                        ) {
                                            Icon(
                                                if (canAfford) Icons.Default.ShoppingCart else Icons.Default.Lock,
                                                contentDescription = null,
                                                tint = if (canAfford) Color(0xFF0F172A) else Color(0xFF94A3B8),
                                                modifier = Modifier.size(15.dp)
                                            )
                                            Text(
                                                text = if (canAfford) "BUY FOR ${previewSkin.price} 🪙" else "NEED ${previewSkin.price} 🪙",
                                                color = if (canAfford) Color(0xFF0F172A) else Color(0xFF94A3B8),
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. SKINS GRID ITEMS (2-column cards, all skins visible)
            val chunks = allSkins.chunked(2)
            items(chunks) { rowSkins ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    for (skin in rowSkins) {
                        val isSelected = previewSkin.id == skin.id
                        val isEquipped = uiState.selectedSkin.id == skin.id
                        val isUnlocked = uiState.unlockedSkins.contains(skin.id)
                        val canAfford = uiState.coins >= skin.price

                        Box(modifier = Modifier.weight(1f)) {
                            SkinCardItem(
                                skin = skin,
                                isSelected = isSelected,
                                isEquipped = isEquipped,
                                isUnlocked = isUnlocked,
                                canAfford = canAfford,
                                onCardClick = {
                                    previewSkin = skin
                                    if (isUnlocked) {
                                        viewModel.selectSkin(skin)
                                    } else {
                                        SoundSynth.playClick()
                                    }
                                },
                                onBuyClick = {
                                    previewSkin = skin
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
    }
}

@Composable
private fun SkinCardItem(
    skin: Skin,
    isSelected: Boolean,
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
                isSelected -> Color(0xFF16243D)
                else -> Color(0xFF0F172A)
            }
        ),
        border = BorderStroke(
            width = if (isEquipped) 2.dp else if (isSelected) 1.5.dp else 1.dp,
            color = when {
                isEquipped -> Color(0xFF10B981)
                isSelected -> Color(0xFF38BDF8)
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
            // Top Preview Avatar Box - Large and 100% Unobstructed (No lock overlay)
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

            // Price or Status Badge
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
                // Buy Button with Exact Price
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
