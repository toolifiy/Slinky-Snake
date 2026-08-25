package com.example.slinkysnake.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
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
fun InGameMenuDialog(
    currentThemeId: String,
    speedMultiplier: Float,
    isSoundEnabled: Boolean,
    soundVolume: Float,
    allowedFruits: Set<String>,
    selectedSkin: Skin,
    onSelectTheme: (String) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onFruitToggle: (String) -> Unit,
    onSelectSkin: (Skin) -> Unit,
    onDismiss: () -> Unit
) {
    val themeItems = listOf(
        ThemeGridItem("mint", "Mint", Color(0xFFC2F5D3), Color(0xFF86EFAC)),
        ThemeGridItem("crimson", "Crimson", Color(0xFFFEE2E2), Color(0xFFFDA4AF)),
        ThemeGridItem("butter", "Sweet", Color(0xFFFEF3C7), Color(0xFFFDE047)),
        ThemeGridItem("lavender", "Royal", Color(0xFFFAF5FF), Color(0xFFE9D5FF)),
        ThemeGridItem("sky", "Sky", Color(0xFFDBEAFE), Color(0xFF93C5FD)),
        ThemeGridItem("cyber", "Cyber", Color(0xFF38BDF8), Color(0xFF1E1B4B)),
        ThemeGridItem("chocolate", "Choco", Color(0xFFFFEDD5), Color(0xFFFED7AA)),
        ThemeGridItem("volcano", "Spicy", Color(0xFFFB7185), Color(0xFF451A03)),
        ThemeGridItem("neon_arcade", "Neon", Color(0xFFA855F7), Color(0xFF3B0764)),
        ThemeGridItem("gold_empire", "Gold", Color(0xFFFBBF24), Color(0xFF1E1B4B))
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF0A1128).copy(alpha = 0.88f))
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            // Main Pause Menu Card with Golden/Amber Neon Border (Exact match to screenshot)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(26.dp))
                    .background(Color(0xFF131D2E))
                    .border(2.5.dp, Color(0xFFF59E0B), RoundedCornerShape(26.dp))
                    .padding(14.dp)
                    .testTag("in_game_menu_dialog")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // TOP BAR: ⏸️ GAME PAUSED + Subtitle + ✕ Close Button
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "⏸️ GAME PAUSED",
                                color = Color(0xFFFBBF24), // Golden Amber
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Change configurations on the fly!",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Close ✕ Button (Top-Right)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.5.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                                .clickable {
                                    SoundSynth.playClick()
                                    onDismiss()
                                }
                                .testTag("close_in_game_menu"),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color(0xFFCBD5E1),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // 1. ⚡ SNAKE SPEED SECTION
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚡ SNAKE SPEED:",
                                    color = Color.White,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "x%.1f".format(speedMultiplier),
                                    color = Color(0xFFC084FC),
                                    fontSize = 13.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            Slider(
                                value = speedMultiplier,
                                onValueChange = { newVal -> onSpeedChange(newVal) },
                                valueRange = 0.6f..1.8f,
                                steps = 5,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF8B5CF6),
                                    activeTrackColor = Color(0xFF8B5CF6),
                                    inactiveTrackColor = Color(0xFF1E293B)
                                ),
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }

                    // 2. 🎨 BOARD BACKGROUND COMBOS (3-Column Grid)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "🎨 BOARD BACKGROUND COMBOS:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )

                        // Render in rows of 3
                        val chunkedThemes = themeItems.chunked(3)
                        for (row in chunkedThemes) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                for (item in row) {
                                    InGameThemePill(
                                        item = item,
                                        isSelected = item.id == currentThemeId,
                                        onSelect = {
                                            onSelectTheme(item.id)
                                            SoundSynth.playClick()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                                // If row has fewer than 3 items, fill space
                                if (row.size < 3) {
                                    for (i in 0 until (3 - row.size)) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    // 3. AUDIO CONTROLS ROW (🔊 AUDIO: [ON] & VOL: 80% [SLIDER])
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Left: 🔊 AUDIO: [ON/OFF]
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                                .padding(horizontal = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "🔊 AUDIO:",
                                    color = Color.White,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black
                                )

                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSoundEnabled) Color(0xFFF59E0B) else Color(0xFF334155))
                                        .clickable {
                                            onSoundToggle(!isSoundEnabled)
                                            SoundSynth.playClick()
                                        }
                                        .padding(horizontal = 12.dp, vertical = 6.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = if (isSoundEnabled) "ON" else "OFF",
                                        color = if (isSoundEnabled) Color(0xFF0F172A) else Color(0xFFCBD5E1),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }

                        // Right: VOL: 80% + Slider
                        Box(
                            modifier = Modifier
                                .weight(1.2f)
                                .height(56.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "VOL:",
                                        color = Color.White,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = "${(soundVolume * 100).toInt()}%",
                                        color = Color(0xFFFBBF24),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                                Slider(
                                    value = soundVolume,
                                    onValueChange = { newVal -> onVolumeChange(newVal) },
                                    valueRange = 0f..1f,
                                    colors = SliderDefaults.colors(
                                        thumbColor = Color(0xFFF59E0B),
                                        activeTrackColor = Color(0xFFF59E0B),
                                        inactiveTrackColor = Color(0xFF1E293B)
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        }
                    }

                    // 4. 🍎 ALLOWED FOOD SPAWNS (2-Column Grid Card)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "🍎 ALLOWED FOOD SPAWNS:",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 160.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(18.dp))
                                .padding(8.dp)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(GameData.ALL_FOOD_TEMPLATES) { food ->
                                    val isChecked = allowedFruits.contains(food.type)

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable {
                                                onFruitToggle(food.type)
                                                SoundSynth.playClick()
                                            }
                                            .padding(vertical = 4.dp, horizontal = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = food.emoji,
                                                fontSize = 15.sp
                                            )
                                            Text(
                                                text = food.name,
                                                color = Color.White,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        // Green Square Checkbox
                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(RoundedCornerShape(5.dp))
                                                .background(
                                                    if (isChecked) Color(0xFF10B981) else Color(0xFF1E293B)
                                                )
                                                .border(
                                                    width = 1.2.dp,
                                                    color = if (isChecked) Color(0xFF059669) else Color(0xFF475569),
                                                    shape = RoundedCornerShape(5.dp)
                                                ),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            if (isChecked) {
                                                Icon(
                                                    Icons.Default.Check,
                                                    contentDescription = "Checked",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(14.dp)
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 5. 🎭 CHANGE SKIN MID-GAME SECTION (3-Column Grid)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "🎭 CHANGE SKIN MID-GAME:",
                            color = Color(0xFFFBBF24), // Gold Accent
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 180.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(18.dp))
                                .padding(8.dp)
                        ) {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(3),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxSize()
                            ) {
                                items(GameData.SNAKE_SKINS) { skin ->
                                    val isSelected = skin.id == selectedSkin.id

                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(14.dp))
                                            .background(
                                                if (isSelected) Color(0xFFF59E0B).copy(alpha = 0.2f) else Color(0xFF131D2E)
                                            )
                                            .border(
                                                width = if (isSelected) 2.dp else 1.dp,
                                                color = if (isSelected) Color(0xFFF59E0B) else Color(0xFF1E293B),
                                                shape = RoundedCornerShape(14.dp)
                                            )
                                            .clickable {
                                                onSelectSkin(skin)
                                            }
                                            .padding(vertical = 8.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        ) {
                                            SnakeHeadCanvas(
                                                skin = skin,
                                                size = 32.dp
                                            )
                                            Text(
                                                text = skin.name.split(" ").firstOrNull() ?: skin.name,
                                                color = if (isSelected) Color(0xFFFBBF24) else Color.White,
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Bold,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
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

@Composable
private fun InGameThemePill(
    item: ThemeGridItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(if (isSelected) Color(0xFF1E1B4B) else Color(0xFF1E293B))
            .border(
                width = if (isSelected) 1.5.dp else 1.dp,
                color = if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onSelect() }
            .padding(horizontal = 8.dp, vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.displayName,
                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            // Dual Color Swatch Capsule Preview
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0F172A))
                    .border(
                        1.dp,
                        if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 3.dp, vertical = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(item.color1)
                        .border(0.5.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(item.color2)
                        .border(0.5.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                )
            }
        }
    }
}
