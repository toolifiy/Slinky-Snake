package com.example.slinkysnake.ui.screens

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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
    allowedPowers: Set<String> = emptySet(),
    onSelectTheme: (String) -> Unit,
    onSpeedChange: (Float) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onFruitToggle: (String) -> Unit,
    onPowerToggle: (String) -> Unit = {},
    onResetProgress: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
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
            val scrollState = rememberScrollState()

            // Main Unified Glowing Arcade Settings Card (Modern Sleek Light Dark Slate - Elevated above nav bar)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.88f)
                    .testTag("settings_dialog"),
                shape = RoundedCornerShape(26.dp),
                color = Color(0xFF1E293B), // Modern Sleek Light-Dark Slate
                border = BorderStroke(2.dp, Color(0xFF475569)),
                tonalElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Header with Title, Icon & ✕ Close Button INSIDE the curved box
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
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFF334155))
                                    .border(1.5.dp, Color(0xFF64748B), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "⚙️",
                                    fontSize = 18.sp
                                )
                            }
                            Column {
                                Text(
                                    text = "GAME SETTINGS",
                                    color = Color(0xFFF1F5F9),
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Black,
                                    letterSpacing = 0.5.sp
                                )
                                Text(
                                    text = "Customize audio, themes & controls",
                                    color = Color(0xFF94A3B8),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        // ✕ Close Button
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFF0F172A))
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
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    // Scrollable Settings Content Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .verticalScroll(scrollState),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                        // Section 0: 🔊 SOUND & AUDIO ADJUSTMENT
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.5.dp, Color(0xFF334155), RoundedCornerShape(16.dp))
                                .padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
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
                                        text = if (isSoundEnabled && soundVolume > 0f) "🔊" else "🔇",
                                        fontSize = 18.sp
                                    )
                                    Column {
                                        Text(
                                            text = "SOUND & SFX VOLUME",
                                            color = Color.White,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Black
                                        )
                                        Text(
                                            text = if (isSoundEnabled && soundVolume > 0f) "Volume: ${(soundVolume * 100).toInt()}%" else "Sound Muted",
                                            color = if (isSoundEnabled && soundVolume > 0f) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Switch(
                                    checked = isSoundEnabled && soundVolume > 0f,
                                    onCheckedChange = { isChecked ->
                                        onSoundToggle(isChecked)
                                        if (isChecked && soundVolume <= 0f) {
                                            onVolumeChange(0.8f)
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

                            // Volume Slider
                            Slider(
                                value = if (isSoundEnabled) soundVolume else 0f,
                                onValueChange = { newVal ->
                                    if (!isSoundEnabled && newVal > 0f) {
                                        onSoundToggle(true)
                                    }
                                    onVolumeChange(newVal)
                                },
                                onValueChangeFinished = {
                                    SoundSynth.playCoin()
                                },
                                valueRange = 0f..1f,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF38BDF8),
                                    activeTrackColor = Color(0xFF38BDF8),
                                    inactiveTrackColor = Color(0xFF0F172A)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("settings_volume_slider")
                            )

                            // Quick Volume Preset Buttons
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                val presets = listOf(
                                    Triple("MUTE 🔇", 0f, soundVolume == 0f || !isSoundEnabled),
                                    Triple("30% 🔉", 0.3f, soundVolume in 0.25f..0.35f && isSoundEnabled),
                                    Triple("70% 🔊", 0.7f, soundVolume in 0.65f..0.75f && isSoundEnabled),
                                    Triple("100% 📢", 1.0f, soundVolume >= 0.95f && isSoundEnabled)
                                )
                                presets.forEach { (label, vol, isSelected) ->
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) Color(0xFF0284C7) else Color(0xFF0F172A))
                                            .border(1.dp, if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155), RoundedCornerShape(8.dp))
                                            .clickable {
                                                if (vol > 0f && !isSoundEnabled) {
                                                    onSoundToggle(true)
                                                }
                                                onVolumeChange(vol)
                                                if (vol > 0f) SoundSynth.playClick()
                                            }
                                            .padding(vertical = 6.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = label,
                                            color = if (isSelected) Color.White else Color(0xFFCBD5E1),
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }
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
                                valueRange = 0.5f..5.0f,
                                steps = 8,
                                colors = SliderDefaults.colors(
                                    thumbColor = Color(0xFF38BDF8),
                                    activeTrackColor = Color(0xFF38BDF8),
                                    inactiveTrackColor = Color(0xFF1E293B)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("speed_slider")
                            )
                        }

                        // Section 2: 🍎 ALLOWED FOOD SPAWNS: (Flat non-nested 2-col list)
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

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF0F172A))
                                    .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(16.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val foodChunks = GameData.ALL_FOOD_TEMPLATES.chunked(2)
                                    for (row in foodChunks) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            for (food in row) {
                                                val isChecked = allowedFruits.contains(food.type)

                                                Row(
                                                    modifier = Modifier
                                                        .weight(1f)
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
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }

                                                    // Modern Light-Dark Cyan Checkbox
                                                    Box(
                                                        modifier = Modifier
                                                            .size(20.dp)
                                                            .clip(RoundedCornerShape(5.dp))
                                                            .background(
                                                                if (isChecked) Color(0xFF0284C7) else Color(0xFF1E293B)
                                                            )
                                                            .border(
                                                                width = 1.5.dp,
                                                                color = if (isChecked) Color(0xFF38BDF8) else Color(0xFF475569),
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
                                            if (row.size < 2) {
                                                Spacer(modifier = Modifier.weight(1f))
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        // Section 3.5: ⚡ ALLOWED SUPER POWERS: (Box right below Food box)
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "⚡ ALLOWED SUPER POWERS:",
                                    color = Color(0xFF38BDF8),
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Surface(
                                    shape = RoundedCornerShape(4.dp),
                                    color = Color(0xFF0369A1)
                                ) {
                                    Text(
                                        text = "${allowedPowers.size}/${GameData.ALL_POWER_TEMPLATES.size} ACTIVE",
                                        color = Color(0xFFE0F2FE),
                                        fontSize = 9.5.sp,
                                        fontWeight = FontWeight.Black,
                                        modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                    )
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(Color(0xFF0C192E))
                                    .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f), RoundedCornerShape(16.dp))
                                    .padding(10.dp)
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val powerChunks = GameData.ALL_POWER_TEMPLATES.chunked(2)
                                    for (row in powerChunks) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            for (power in row) {
                                                val isChecked = allowedPowers.contains(power.type)

                                                Row(
                                                    modifier = Modifier
                                                        .weight(1f)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .clickable {
                                                             onPowerToggle(power.type)
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
                                                            text = power.emoji,
                                                            fontSize = 16.sp
                                                        )
                                                        Text(
                                                            text = power.name,
                                                            color = Color.White,
                                                            fontSize = 11.5.sp,
                                                            fontWeight = FontWeight.Bold,
                                                            maxLines = 1,
                                                            overflow = TextOverflow.Ellipsis
                                                        )
                                                    }

                                                    // Modern Light-Blue Checkbox
                                                    Box(
                                                        modifier = Modifier
                                                            .size(20.dp)
                                                            .clip(RoundedCornerShape(5.dp))
                                                            .background(
                                                                if (isChecked) Color(0xFF0284C7) else Color(0xFF0F172A)
                                                            )
                                                            .border(
                                                                width = 1.5.dp,
                                                                color = if (isChecked) Color(0xFF38BDF8) else Color(0xFF475569),
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
                                            if (row.size < 2) {
                                                Spacer(modifier = Modifier.weight(1f))
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
                                .background(Color(0xFF0F172A))
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

                // Scroll Down Cue Indicator (visible when more items are below)
                if (scrollState.canScrollForward) {
                    Surface(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFF0F172A).copy(alpha = 0.95f),
                        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(
                                text = "📜 Scroll down for more",
                                color = Color(0xFFBAE6FD),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "↓",
                                color = Color(0xFF38BDF8),
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
}
