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
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.ui.components.BottomGameNavBar
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

@Composable
fun SettingsScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onBackToHome: () -> Unit,
    onOpenMarket: () -> Unit,
    onOpenSkins: () -> Unit
) {
    var showResetConfirmDialog by remember { mutableStateOf(false) }

    val themeItems = listOf(
        ThemeGridItem("mint", "Mint Green", Color(0xFFC2F5D3), Color(0xFF86EFAC)),
        ThemeGridItem("crimson", "Crimson Rose", Color(0xFFFEE2E2), Color(0xFFFDA4AF)),
        ThemeGridItem("butter", "Sweet Honey", Color(0xFFFEF3C7), Color(0xFFFDE047)),
        ThemeGridItem("lavender", "Royal Violet", Color(0xFFFAF5FF), Color(0xFFE9D5FF)),
        ThemeGridItem("sky", "Sky Blue", Color(0xFFDBEAFE), Color(0xFF93C5FD)),
        ThemeGridItem("cyber", "Cyberpunk Dark", Color(0xFF38BDF8), Color(0xFF1E1B4B)),
        ThemeGridItem("chocolate", "Cocoa Crunch", Color(0xFFFFEDD5), Color(0xFFFED7AA)),
        ThemeGridItem("volcano", "Magma Volcano", Color(0xFFFB7185), Color(0xFF451A03)),
        ThemeGridItem("neon_arcade", "Neon Arcade", Color(0xFFA855F7), Color(0xFF3B0764)),
        ThemeGridItem("gold_empire", "Gold Empire", Color(0xFFFBBF24), Color(0xFF1E1B4B))
    )

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0A1128), // Deep Dark Gaming Canvas
        bottomBar = {
            BottomGameNavBar(
                selectedTab = NavTab.SETTINGS,
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
                    onOpenSkins()
                },
                onSettingsClick = {
                    SoundSynth.playClick()
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
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // 1. TOP HEADER (Title + Icon)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(42.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF64748B), Color(0xFF334155))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Settings,
                                contentDescription = "Settings",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "GAME PREFERENCES",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Audio, themes, speed & food filters",
                                fontSize = 11.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }


                }
            }

            // 2. PLAYER CAREER STATS SUMMARY CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.5.dp, Color(0xFF38BDF8).copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "📊 CAREER OVERVIEW",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF38BDF8),
                            letterSpacing = 1.sp
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CareerStatPill("🏆 HIGH SCORE", "${uiState.highScore}", Color(0xFFFBBF24))
                            CareerStatPill("🪙 COINS", "${uiState.coins}", Color(0xFFFDE68A))
                            CareerStatPill("🎭 SKINS", "${uiState.unlockedSkins.size}/${GameData.SNAKE_SKINS.size}", Color(0xFF34D399))
                            CareerStatPill("🗺️ LEVEL", "Lvl ${uiState.unlockedLevel}", Color(0xFFA78BFA))
                        }
                    }
                }
            }

            // 3. SOUND & AUDIO ADJUSTMENTS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = if (uiState.isSoundEnabled && uiState.soundVolume > 0f) "🔊" else "🔇",
                                    fontSize = 20.sp
                                )
                                Column {
                                    Text(
                                        text = "SOUND & SFX VOLUME",
                                        color = Color.White,
                                        fontSize = 13.5.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = if (uiState.isSoundEnabled && uiState.soundVolume > 0f) "Volume: ${(uiState.soundVolume * 100).toInt()}%" else "Sound Muted",
                                        color = if (uiState.isSoundEnabled && uiState.soundVolume > 0f) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Switch(
                                checked = uiState.isSoundEnabled && uiState.soundVolume > 0f,
                                onCheckedChange = { isChecked ->
                                    viewModel.setSoundEnabled(isChecked)
                                    if (isChecked && uiState.soundVolume <= 0f) {
                                        viewModel.setSoundVolume(0.8f)
                                    }
                                    SoundSynth.playClick()
                                },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFF38BDF8),
                                    checkedTrackColor = Color(0xFF0284C7),
                                    uncheckedThumbColor = Color(0xFF64748B),
                                    uncheckedTrackColor = Color(0xFF0F172A)
                                )
                            )
                        }

                        // Slider
                        Slider(
                            value = if (uiState.isSoundEnabled) uiState.soundVolume else 0f,
                            onValueChange = { newVal ->
                                if (!uiState.isSoundEnabled && newVal > 0f) {
                                    viewModel.setSoundEnabled(true)
                                }
                                viewModel.setSoundVolume(newVal)
                            },
                            onValueChangeFinished = {
                                SoundSynth.playCoin()
                            },
                            valueRange = 0f..1f,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF38BDF8),
                                activeTrackColor = Color(0xFF38BDF8),
                                inactiveTrackColor = Color(0xFF1E293B)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("settings_volume_slider")
                        )

                        // Segmented Presets (Matching Classic vs Levels mode segmented look)
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
                                val presets = listOf(
                                    Triple("MUTE 🔇", 0f, uiState.soundVolume == 0f || !uiState.isSoundEnabled),
                                    Triple("30% 🔉", 0.3f, uiState.soundVolume in 0.25f..0.35f && uiState.isSoundEnabled),
                                    Triple("70% 🔊", 0.7f, uiState.soundVolume in 0.65f..0.75f && uiState.isSoundEnabled),
                                    Triple("100% 📢", 1.0f, uiState.soundVolume >= 0.95f && uiState.isSoundEnabled)
                                )
                                presets.forEach { (label, vol, isSelected) ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) Color(0xFF0284C7) else Color.Transparent)
                                            .border(
                                                width = if (isSelected) 1.5.dp else 0.dp,
                                                color = if (isSelected) Color(0xFF38BDF8) else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                if (vol > 0f && !uiState.isSoundEnabled) {
                                                    viewModel.setSoundEnabled(true)
                                                }
                                                viewModel.setSoundVolume(vol)
                                                if (vol > 0f) SoundSynth.playClick()
                                            }
                                            .padding(vertical = 7.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                            fontSize = 10.5.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 4. SNAKE GAMEPLAY SPEED
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "⚡ SNAKE SPEED MULTIPLIER",
                                color = Color.White,
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black
                            )
                            Text(
                                text = "x%.1f".format(uiState.speedMultiplier),
                                color = Color(0xFFC084FC),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        Slider(
                            value = uiState.speedMultiplier,
                            onValueChange = { newVal ->
                                viewModel.setSpeedMultiplier(newVal)
                            },
                            valueRange = 0.5f..5.0f,
                            steps = 8,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFF8B5CF6),
                                activeTrackColor = Color(0xFF8B5CF6),
                                inactiveTrackColor = Color(0xFF1E293B)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("speed_slider")
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "SLOW 🐌 (0.5x)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "NORMAL 🐍 (1.0x)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "SUPER ⚡ (2.5x)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = "HYPER 🚀 (5.0x)",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFFC084FC)
                            )
                        }
                    }
                }
            }

            // 5. BOARD BACKGROUND THEMES (2-Column interactive grid)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🎨 BOARD COLOR THEMES",
                            color = Color.White,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black
                        )

                        for (rowIndex in 0 until themeItems.size step 2) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                val item1 = themeItems[rowIndex]
                                ThemePillItem(
                                    item = item1,
                                    isSelected = item1.id == uiState.boardThemeId,
                                    onSelect = {
                                        viewModel.setBoardTheme(item1.id)
                                        SoundSynth.playClick()
                                    },
                                    modifier = Modifier.weight(1f)
                                )

                                if (rowIndex + 1 < themeItems.size) {
                                    val item2 = themeItems[rowIndex + 1]
                                    ThemePillItem(
                                        item = item2,
                                        isSelected = item2.id == uiState.boardThemeId,
                                        onSelect = {
                                            viewModel.setBoardTheme(item2.id)
                                            SoundSynth.playClick()
                                        },
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 6. ALLOWED FOOD SPAWNS SELECTION
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
                    border = BorderStroke(1.dp, Color(0xFF1E293B))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = "🍎 ALLOWED FOOD & POWER SPAWNS",
                            color = Color.White,
                            fontSize = 13.5.sp,
                            fontWeight = FontWeight.Black
                        )

                        val foodChunks = GameData.ALL_FOOD_TEMPLATES.chunked(2)
                        for (row in foodChunks) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                for (food in row) {
                                    val isChecked = uiState.allowedFruits.contains(food.type)

                                    Row(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isChecked) Color(0xFF1E293B) else Color(0xFF131D2E))
                                            .border(1.dp, if (isChecked) Color(0xFF0284C7) else Color(0xFF1E293B), RoundedCornerShape(8.dp))
                                            .clickable {
                                                viewModel.toggleFruit(food.type)
                                                SoundSynth.playClick()
                                            }
                                            .padding(vertical = 8.dp, horizontal = 10.dp),
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
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                        }

                                        Box(
                                            modifier = Modifier
                                                .size(20.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(
                                                    if (isChecked) Color(0xFF0284C7) else Color(0xFF0F172A)
                                                )
                                                .border(
                                                    width = 1.5.dp,
                                                    color = if (isChecked) Color(0xFF38BDF8) else Color(0xFF475569),
                                                    shape = RoundedCornerShape(4.dp)
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
                                if (row.size < 2) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }
            }

            // 7. RESET ALL PROGRESS BUTTON
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F172A))
                        .border(1.5.dp, Color(0xFFEF4444).copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                        .clickable {
                            SoundSynth.playClick()
                            showResetConfirmDialog = true
                        }
                        .padding(16.dp)
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
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "RESET ALL PROGRESS & SCORES",
                            color = Color(0xFFEF4444),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }

    if (showResetConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showResetConfirmDialog = false },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFEF4444))
                    Text("Reset Everything?", color = Color.White, fontWeight = FontWeight.Black)
                }
            },
            text = {
                Text(
                    "Are you sure you want to reset your high score and adventure level progress? (Your unlocked skins and coins will be safely preserved)",
                    color = Color(0xFFCBD5E1),
                    fontSize = 13.sp
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.resetAllProgress()
                        showResetConfirmDialog = false
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Reset Progress", color = Color.White, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showResetConfirmDialog = false },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("Cancel", color = Color(0xFF94A3B8))
                }
            },
            containerColor = Color(0xFF1E293B),
            shape = RoundedCornerShape(10.dp)
        )
    }
}

@Composable
private fun CareerStatPill(title: String, value: String, valueColor: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Black,
            color = valueColor
        )
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF94A3B8)
        )
    }
}

@Composable
private fun ThemePillItem(
    item: ThemeGridItem,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) Color(0xFF1E1B4B) else Color(0xFF1E293B))
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onSelect() }
            .padding(horizontal = 10.dp, vertical = 8.dp)
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

            // Dual Color Swatch Capsule Preview
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(Color(0xFF0F172A))
                    .border(
                        1.dp,
                        if (isSelected) Color(0xFF8B5CF6) else Color(0xFF334155),
                        RoundedCornerShape(6.dp)
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
