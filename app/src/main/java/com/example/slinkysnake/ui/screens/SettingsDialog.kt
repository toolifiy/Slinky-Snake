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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.audio.SoundSynth

data class ThemeGridItem(
    val id: String,
    val displayName: String,
    val color1: Color,
    val color2: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsDialog(
    currentThemeId: String,
    speedMultiplier: Float,
    isSoundEnabled: Boolean,
    soundVolume: Float,
    allowedFruits: Set<String>,
    onSelectTheme: (String) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onFruitToggle: (String) -> Unit,
    onResetProgress: () -> Unit,
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
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Top Action Bar with ✕ Close Button and 🔊 Volume Pill (Exact match to screenshot)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // ✕ Close Button
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.5.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                            .clickable {
                                SoundSynth.playClick()
                                onDismiss()
                            }
                            .testTag("close_settings_dialog"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color(0xFFCBD5E1),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // 🔊 Volume Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.5.dp, Color(0xFF334155), RoundedCornerShape(14.dp))
                            .clickable {
                                val nextVol = if (soundVolume > 0f) 0f else 0.8f
                                onVolumeChange(nextVol)
                                SoundSynth.playClick()
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = if (soundVolume > 0f) "🔊" else "🔇",
                                fontSize = 13.sp
                            )
                            Text(
                                text = "${(soundVolume * 100).toInt()}% ▾",
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Main Purple Glowing Arcade Cabinet Settings Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color(0xFF131D2E))
                        .border(2.5.dp, Color(0xFF8B5CF6), RoundedCornerShape(24.dp)) // Neon Violet Border
                        .padding(14.dp)
                        .testTag("settings_dialog")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Header: ⚙️ ARCADE CABINET SETTINGS
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "⚙️ ARCADE CABINET SETTINGS",
                                color = Color(0xFF94A3B8),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }

                        // Section 1: ⚡ SNAKE SPEED
                        Column(
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚡ SNAKE SPEED:",
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = "x%.1f".format(speedMultiplier),
                                    color = Color(0xFFC084FC),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }

                            // Speed Slider
                            Slider(
                                value = speedMultiplier,
                                onValueChange = { newVal ->
                                    onSpeedChange(newVal)
                                },
                                valueRange = 0.6f..1.8f,
                                steps = 5,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF8B5CF6),
                                    activeTrackColor = Color(0xFF8B5CF6),
                                    inactiveTrackColor = Color(0xFF1E293B)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("speed_slider")
                            )

                            // Slider Labels
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "SLOW 🐌",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = "NORMAL 🐍",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = "HYPER! 🚀",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }

                        // Section 2: 🎨 BOARD BACKGROUND COMBOS:
                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🎨 BOARD BACKGROUND COMBOS:",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black
                            )

                            // 2-Column Grid of 5 rows (10 items)
                            for (rowIndex in 0 until themeItems.size step 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val item1 = themeItems[rowIndex]
                                    ThemePillCard(
                                        item = item1,
                                        isSelected = item1.id == currentThemeId,
                                        onSelect = {
                                            onSelectTheme(item1.id)
                                            SoundSynth.playClick()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (rowIndex + 1 < themeItems.size) {
                                        val item2 = themeItems[rowIndex + 1]
                                        ThemePillCard(
                                            item = item2,
                                            isSelected = item2.id == currentThemeId,
                                            onSelect = {
                                                onSelectTheme(item2.id)
                                                SoundSynth.playClick()
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }
                                }
                            }
                        }

                        // Section 3: 🍎 ALLOWED FOOD SPAWNS: (Compact 2-column scrollable box matching screenshot)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = "🍎 ALLOWED FOOD SPAWNS:",
                                color = Color.White,
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Black
                            )

                            // Inner fixed-height scrollable card (2-Column Grid)
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(170.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
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
                                                    fontSize = 16.sp
                                                )
                                                Text(
                                                    text = food.name,
                                                    color = Color.White,
                                                    fontSize = 11.5.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1
                                                )
                                            }

                                            // Green Square Checkbox matching screenshot
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .clip(RoundedCornerShape(5.dp))
                                                    .background(
                                                        if (isChecked) Color(0xFF10B981) else Color(0xFF1E293B)
                                                    )
                                                    .border(
                                                        width = 1.5.dp,
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

                        // Section 4: RESET GAME PROGRESS
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFFEF4444).copy(alpha = 0.5f), RoundedCornerShape(14.dp))
                                .clickable {
                                    onResetProgress()
                                    SoundSynth.playClick()
                                }
                                .padding(12.dp)
                                .testTag("reset_progress_button"),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = Color(0xFFEF4444),
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Reset All Progress & Scores",
                                    color = Color(0xFFEF4444),
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

@Composable
private fun ThemePillCard(
    item: ThemeGridItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) Color(0xFF1E1B4B) else Color(0xFF1E293B))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable { onSelect() }
            .padding(horizontal = 10.dp, vertical = 7.dp)
            .testTag("theme_button_${item.id}")
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = item.displayName,
                color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            // Exact Dual Color Swatch Capsule from screenshot (col1 + col2 dots)
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0F172A))
                    .border(
                        1.dp,
                        if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                        RoundedCornerShape(10.dp)
                    )
                    .padding(horizontal = 4.dp, vertical = 3.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(item.color1)
                        .border(0.5.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .size(11.dp)
                        .clip(CircleShape)
                        .background(item.color2)
                        .border(0.5.dp, Color.White.copy(alpha = 0.25f), CircleShape)
                )
            }
        }
    }
}

