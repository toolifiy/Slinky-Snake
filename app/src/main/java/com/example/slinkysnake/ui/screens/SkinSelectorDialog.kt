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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.Skin
import com.example.slinkysnake.ui.components.SnakeHeadCanvas

@Composable
fun SkinSelectorDialog(
    selectedSkin: Skin,
    coins: Int,
    unlockedSkins: Set<String>,
    onSelectSkin: (Skin) -> Unit,
    onBuySkin: (Skin) -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
        val navBarPadding = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
        val effectiveTopPadding = (statusBarPadding + 14.dp).coerceAtLeast(24.dp)
        val effectiveBottomPadding = (navBarPadding + 32.dp).coerceAtLeast(46.dp)

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A1128).copy(alpha = 0.88f))
                .padding(
                    start = 14.dp,
                    end = 14.dp,
                    top = effectiveTopPadding,
                    bottom = effectiveBottomPadding
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .testTag("skin_selector_dialog"),
                shape = RoundedCornerShape(28.dp),
                color = Color(0xFF131D2E),
                border = BorderStroke(2.dp, Color(0xFF3B82F6).copy(alpha = 0.8f)),
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header with Title, Coins Balance, and Close Button
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF2563EB)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Palette,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "Skins Wardrobe",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "Unlock with 🪙 Coins",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Coin Balance Chip
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color(0xFF1E293B),
                                border = BorderStroke(1.5.dp, Color(0xFFF59E0B))
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = "🪙", fontSize = 13.sp)
                                    Text(
                                        text = "$coins",
                                        color = Color(0xFFFBBF24),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }

                            // ✕ Close Button
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                                    .clickable {
                                        SoundSynth.playClick()
                                        onDismiss()
                                    }
                                    .testTag("close_skin_dialog"),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color(0xFFCBD5E1),
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }

                    // Skins Grid
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(GameData.SNAKE_SKINS) { skin ->
                            val isSelected = skin.id == selectedSkin.id
                            val isUnlocked = skin.price == 0 || skin.id == "slinky" || unlockedSkins.contains(skin.id)
                            val canAfford = coins >= skin.price

                            Card(
                                onClick = {
                                    if (isUnlocked) {
                                        onSelectSkin(skin)
                                    } else if (canAfford) {
                                        onBuySkin(skin)
                                    } else {
                                        SoundSynth.playCrash()
                                    }
                                },
                                shape = RoundedCornerShape(18.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        isSelected -> Color(0xFF1E3A8A).copy(alpha = 0.7f)
                                        isUnlocked -> Color(0xFF1E293B)
                                        else -> Color(0xFF0F172A)
                                    }
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = when {
                                        isSelected -> Color(0xFF38BDF8)
                                        isUnlocked -> Color(0xFF334155)
                                        canAfford -> Color(0xFFF59E0B).copy(alpha = 0.7f)
                                        else -> Color(0xFF334155).copy(alpha = 0.5f)
                                    }
                                ),
                                modifier = Modifier.testTag("skin_item_${skin.id}")
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    // Snake Head Preview & Status Badge
                                    Box(
                                        modifier = Modifier.size(56.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        SnakeHeadCanvas(
                                            skin = skin,
                                            size = 52.dp
                                        )
                                        if (isSelected) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF10B981)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = "Selected",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(13.dp)
                                                )
                                            }
                                        } else if (!isUnlocked) {
                                            Box(
                                                modifier = Modifier
                                                    .align(Alignment.TopEnd)
                                                    .size(20.dp)
                                                    .clip(CircleShape)
                                                    .background(Color(0xFF0F172A).copy(alpha = 0.9f))
                                                    .border(1.dp, Color(0xFFF59E0B), CircleShape),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Icon(
                                                    Icons.Default.Lock,
                                                    contentDescription = "Locked",
                                                    tint = Color(0xFFFBBF24),
                                                    modifier = Modifier.size(11.dp)
                                                )
                                            }
                                        }
                                    }

                                    // Skin Name
                                    Text(
                                        text = skin.name,
                                        color = if (isSelected) Color(0xFF38BDF8) else Color.White,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Bold,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        textAlign = TextAlign.Center
                                    )

                                    // Action / Status Button
                                    when {
                                        isSelected -> {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF10B981).copy(alpha = 0.2f),
                                                border = BorderStroke(1.dp, Color(0xFF10B981))
                                            ) {
                                                Text(
                                                    text = "EQUIPPED ✓",
                                                    color = Color(0xFF34D399),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                        isUnlocked -> {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF0284C7).copy(alpha = 0.25f),
                                                border = BorderStroke(1.dp, Color(0xFF38BDF8))
                                            ) {
                                                Text(
                                                    text = "EQUIP",
                                                    color = Color(0xFF38BDF8),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                                )
                                            }
                                        }
                                        canAfford -> {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFFD97706),
                                                border = BorderStroke(1.dp, Color(0xFFFBBF24))
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                                ) {
                                                    Text(
                                                        text = "BUY 🪙${skin.price}",
                                                        color = Color.White,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Black
                                                    )
                                                }
                                            }
                                        }
                                        else -> {
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = Color(0xFF1E293B),
                                                border = BorderStroke(1.dp, Color(0xFF475569))
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
                                                    verticalAlignment = Alignment.CenterVertically,
                                                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                                                ) {
                                                    Text(
                                                        text = "🪙 ${skin.price}",
                                                        color = Color(0xFF94A3B8),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold
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
        }
    }
}
