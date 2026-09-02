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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.slinkysnake.audio.SoundSynth

@Composable
fun InGameMenuDialog(
    speedMultiplier: Float,
    isSoundEnabled: Boolean,
    soundVolume: Float,
    onSpeedChange: (Float) -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onVolumeChange: (Float) -> Unit,
    onResume: () -> Unit,
    onRestart: () -> Unit,
    onExit: () -> Unit,
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
        val effectiveTopPadding = (statusBarPadding + 10.dp).coerceAtLeast(20.dp)
        val effectiveBottomPadding = (navBarPadding + 20.dp).coerceAtLeast(30.dp)
        val scrollState = rememberScrollState()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    top = effectiveTopPadding,
                    bottom = effectiveBottomPadding
                ),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("in_game_menu_dialog"),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF0F172A),
                border = BorderStroke(2.dp, Color(0xFFF59E0B)),
                shadowElevation = 14.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                        .verticalScroll(scrollState),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // TOP TITLE ROW WITH CLOSE BUTTON
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                text = "⚙️ GAME MENU",
                                color = Color(0xFFFBBF24),
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Game is Paused",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        // Close ✕ Button (Top-Right)
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .size(34.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
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
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // 1. ⚡ SNAKE SPEED CONTROL CARD
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(text = "⚡", fontSize = 16.sp)
                                Text(
                                    text = "SNAKE SPEED",
                                    color = Color.White,
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                            Text(
                                text = "x%.1f".format(speedMultiplier),
                                color = Color(0xFFC084FC),
                                fontSize = 13.5.sp,
                                fontWeight = FontWeight.Black
                            )
                        }

                        // Speed Slider
                        Slider(
                            value = speedMultiplier,
                            onValueChange = { newVal ->
                                onSpeedChange(newVal)
                            },
                            valueRange = 0.5f..4.0f,
                            steps = 6,
                            colors = SliderDefaults.colors(
                                thumbColor = Color(0xFFC084FC),
                                activeTrackColor = Color(0xFFA855F7),
                                inactiveTrackColor = Color(0xFF0F172A)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(26.dp)
                                .testTag("in_game_speed_slider")
                        )

                        // Quick Speed Presets
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            val speedPresets = listOf(
                                "0.8x" to 0.8f,
                                "1.0x" to 1.0f,
                                "1.5x" to 1.5f,
                                "2.0x" to 2.0f
                            )
                            speedPresets.forEach { (label, speedVal) ->
                                val isSelected = kotlin.math.abs(speedMultiplier - speedVal) < 0.08f
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color(0xFFA855F7) else Color(0xFF0F172A))
                                        .border(
                                            1.dp,
                                            if (isSelected) Color(0xFFC084FC) else Color(0xFF334155),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            onSpeedChange(speedVal)
                                            SoundSynth.playClick()
                                        }
                                        .padding(vertical = 5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // 2. 🔊 GAME VOLUME CONTROL CARD
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(14.dp))
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = if (isSoundEnabled && soundVolume > 0f) "🔊" else "🔇",
                                    fontSize = 16.sp
                                )
                                Column {
                                    Text(
                                        text = "GAME AUDIO",
                                        color = Color.White,
                                        fontSize = 12.5.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Text(
                                        text = if (isSoundEnabled && soundVolume > 0f) "${(soundVolume * 100).toInt()}% Volume" else "Muted",
                                        color = if (isSoundEnabled && soundVolume > 0f) Color(0xFF38BDF8) else Color(0xFF94A3B8),
                                        fontSize = 10.5.sp,
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
                                ),
                                modifier = Modifier.size(38.dp, 24.dp)
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
                                .height(26.dp)
                                .testTag("in_game_volume_slider")
                        )

                        // Quick Volume Presets
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(5.dp)
                        ) {
                            val volPresets = listOf(
                                Triple("MUTE", 0f, soundVolume == 0f || !isSoundEnabled),
                                Triple("30%", 0.3f, soundVolume in 0.25f..0.35f && isSoundEnabled),
                                Triple("70%", 0.7f, soundVolume in 0.65f..0.75f && isSoundEnabled),
                                Triple("100%", 1.0f, soundVolume >= 0.95f && isSoundEnabled)
                            )
                            volPresets.forEach { (label, volVal, isSelected) ->
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (isSelected) Color(0xFF0284C7) else Color(0xFF0F172A))
                                        .border(
                                            1.dp,
                                            if (isSelected) Color(0xFF38BDF8) else Color(0xFF334155),
                                            RoundedCornerShape(8.dp)
                                        )
                                        .clickable {
                                            if (volVal > 0f && !isSoundEnabled) {
                                                onSoundToggle(true)
                                            }
                                            onVolumeChange(volVal)
                                            if (volVal > 0f) SoundSynth.playClick()
                                        }
                                        .padding(vertical = 5.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = label,
                                        color = if (isSelected) Color.White else Color(0xFF94A3B8),
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // 3. ▶️ RESUME BUTTON (Green)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .shadow(4.dp, RoundedCornerShape(12.dp))
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF10B981))
                            .clickable {
                                SoundSynth.playClick()
                                onResume()
                            }
                            .padding(vertical = 12.dp)
                            .testTag("in_game_resume_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = "Resume",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = "RESUME",
                                color = Color.White,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // 4. 🔄 RESTART BUTTON (Amber)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF59E0B))
                            .clickable {
                                SoundSynth.playClick()
                                onRestart()
                            }
                            .padding(vertical = 12.dp)
                            .testTag("in_game_restart_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Refresh,
                                contentDescription = "Restart",
                                tint = Color(0xFF0F172A),
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = "RESTART",
                                color = Color(0xFF0F172A),
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    // 5. 🚪 EXIT BUTTON (Red)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFEF4444))
                            .clickable {
                                SoundSynth.playClick()
                                onExit()
                            }
                            .padding(vertical = 12.dp)
                            .testTag("in_game_exit_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.ExitToApp,
                                contentDescription = "Exit",
                                tint = Color.White,
                                modifier = Modifier.size(19.dp)
                            )
                            Text(
                                text = "EXIT",
                                color = Color.White,
                                fontSize = 14.5.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
