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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.Achievement
import com.example.slinkysnake.ui.components.BottomGameNavBar
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

enum class MissionCategory(val label: String, val code: String) {
    ALL("🌟 ALL", "ALL"),
    SCORE("🎯 SCORES", "SCORE"),
    POWER("⚡ POWERS", "POWER"),
    ADVENTURE("🗺️ ADVENTURE", "ADVENTURE"),
    COLLECTION("🎭 SKINS & FEATS", "COLLECTION")
}

@Composable
fun MissionsScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onBackToHome: () -> Unit,
    onOpenSkins: () -> Unit,
    onOpenSettings: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(MissionCategory.ALL) }

    val allAchievements = GameData.ACHIEVEMENTS
    val totalCount = allAchievements.size
    val unlockedCount = uiState.unlockedAchievements.size
    val claimedCount = uiState.claimedAchievements.size
    val unclaimedCompletedCount = allAchievements.count {
        uiState.unlockedAchievements.contains(it.id) && !uiState.claimedAchievements.contains(it.id)
    }

    val overallProgress = if (totalCount > 0) unlockedCount.toFloat() / totalCount.toFloat() else 0f

    val filteredList = remember(selectedCategory, uiState.unlockedAchievements, uiState.claimedAchievements) {
        if (selectedCategory == MissionCategory.ALL) {
            allAchievements
        } else {
            allAchievements.filter { it.category == selectedCategory.code }
        }
    }

    val statusBarPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF0A1128), // Deep Dark Navy Home Canvas
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomGameNavBar(
                selectedTab = NavTab.MISSIONS,
                onHomeClick = {
                    SoundSynth.playClick()
                    onBackToHome()
                },
                onMissionsClick = {
                    SoundSynth.playClick()
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
            // 1. TOP HEADER (Title + Coins pill)
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
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFF10B981), Color(0xFF059669))
                                    )
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = "Missions",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "STAR MISSIONS",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Black,
                                color = Color.White,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Complete quests & claim epic coin rewards",
                                fontSize = 11.5.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    // Coins Pill
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = Color(0xFF1E293B),
                        border = BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                Icons.Default.MonetizationOn,
                                contentDescription = "Coins",
                                tint = Color(0xFFFBBF24),
                                modifier = Modifier.size(18.dp)
                            )
                            Text(
                                text = "${uiState.coins}",
                                color = Color(0xFFFDE68A),
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            // 2. HERO MISSION OVERVIEW CARD
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF0F172A)
                    ),
                    border = BorderStroke(1.5.dp, Color(0xFF10B981).copy(alpha = 0.4f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "MISSION PROGRESS",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF10B981),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "$unlockedCount / $totalCount Quests Done (${(overallProgress * 100).toInt()}%)",
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            val rankTitle = when {
                                unlockedCount >= 20 -> "👑 SNAKE EMPEROR"
                                unlockedCount >= 14 -> "🐉 VIPER LORD"
                                unlockedCount >= 8 -> "⚡ MASTER RUNNER"
                                unlockedCount >= 3 -> "🌿 SCOUT SLITHERER"
                                else -> "🐣 ROOKIE"
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Color(0xFF1E293B))
                                    .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = rankTitle,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF38BDF8)
                                )
                            }
                        }

                        // Progress Bar
                        LinearProgressIndicator(
                            progress = { overallProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = Color(0xFF10B981),
                            trackColor = Color(0xFF1E293B)
                        )

                        // Quick Stats Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "🏆 $unlockedCount Completed",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8),
                                fontWeight = FontWeight.Medium
                            )
                            if (unclaimedCompletedCount > 0) {
                                Text(
                                    text = "🎁 $unclaimedCompletedCount Rewards Ready!",
                                    fontSize = 11.sp,
                                    color = Color(0xFFFBBF24),
                                    fontWeight = FontWeight.Black
                                )
                            } else {
                                Text(
                                    text = "✅ $claimedCount Claimed",
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }
            }

            // 3. CATEGORY SEGMENTED ROW
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF131D2E))
                        .border(2.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                        .padding(4.dp)
                ) {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(MissionCategory.values()) { category ->
                            val isSelected = selectedCategory == category
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        if (isSelected) Color(0xFFF59E0B) else Color.Transparent
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 0.dp,
                                        color = if (isSelected) Color(0xFFD97706) else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable {
                                        SoundSynth.playClick()
                                        selectedCategory = category
                                    }
                                    .padding(horizontal = 10.dp, vertical = 7.dp)
                            ) {
                                Text(
                                    text = category.label,
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isSelected) Color(0xFF0F172A) else Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }

            // 4. MISSIONS LIST
            items(filteredList, key = { it.id }) { ach ->
                val isUnlocked = uiState.unlockedAchievements.contains(ach.id)
                val isClaimed = uiState.claimedAchievements.contains(ach.id)

                MissionCard(
                    achievement = ach,
                    isUnlocked = isUnlocked,
                    isClaimed = isClaimed,
                    uiState = uiState,
                    onClaim = {
                        viewModel.claimAchievement(ach.id, ach.rewardCoins)
                    }
                )
            }
        }
    }
}

@Composable
private fun MissionCard(
    achievement: Achievement,
    isUnlocked: Boolean,
    isClaimed: Boolean,
    uiState: GameUiState,
    onClaim: () -> Unit
) {
    val accentColor = Color(achievement.accentColor)

    // Calculate dynamic progress values
    val (currentVal, targetVal) = when (achievement.id) {
        "first_bite" -> if (isUnlocked) 1 to 1 else 0 to 1
        "half_century" -> uiState.highScore.coerceAtMost(50) to 50
        "century" -> uiState.highScore.coerceAtMost(100) to 100
        "snake_master" -> uiState.highScore.coerceAtMost(300) to 300
        "score_500" -> uiState.highScore.coerceAtMost(500) to 500
        "score_1000" -> uiState.highScore.coerceAtMost(1000) to 1000
        "hungry_slitherer" -> if (isUnlocked) 30 to 30 else 0 to 30
        "combo_king" -> if (isUnlocked) 8 to 8 else 0 to 8
        "all_skins" -> uiState.unlockedSkins.size.coerceAtMost(3) to 3
        "skin_collector" -> uiState.unlockedSkins.size.coerceAtMost(6) to 6
        "level_clear" -> uiState.unlockedLevel.coerceAtMost(1) to 1
        "level_3_master" -> uiState.unlockedLevel.coerceAtMost(3) to 3
        "volcano_conqueror" -> uiState.unlockedLevel.coerceAtMost(4) to 4
        "cyber_god" -> uiState.unlockedLevel.coerceAtMost(5) to 5
        else -> if (isUnlocked) 1 to 1 else 0 to 1
    }

    val progressFraction = if (isUnlocked) 1f else (currentVal.toFloat() / targetVal.toFloat()).coerceIn(0f, 1f)

    val infiniteTransition = rememberInfiniteTransition(label = "claim_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isUnlocked && !isClaimed) 1.05f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("mission_card_${achievement.id}"),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) Color(0xFF131E33) else Color(0xFF0F172A)
        ),
        border = BorderStroke(
            width = if (isUnlocked && !isClaimed) 1.8.dp else 1.dp,
            color = when {
                isUnlocked && !isClaimed -> Color(0xFFFBBF24)
                isUnlocked -> accentColor.copy(alpha = 0.5f)
                else -> Color(0xFF1E293B)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Left Icon Badge
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            if (isUnlocked) accentColor.copy(alpha = 0.25f) else Color(0xFF1E293B)
                        )
                        .border(
                            1.dp,
                            if (isUnlocked) accentColor else Color(0xFF334155),
                            RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = achievement.icon,
                        fontSize = 22.sp
                    )
                }

                // Title + Description
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = achievement.title,
                            fontSize = 14.5.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isUnlocked) Color.White else Color(0xFFE2E8F0)
                        )

                        if (isClaimed) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Claimed",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    Text(
                        text = achievement.description,
                        fontSize = 11.5.sp,
                        color = Color(0xFF94A3B8),
                        lineHeight = 15.sp
                    )
                }

                // Coin Reward Badge
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, Color(0xFFFBBF24).copy(alpha = 0.4f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = null,
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(13.dp)
                        )
                        Text(
                            text = "+${achievement.rewardCoins}",
                            fontSize = 11.5.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFDE68A)
                        )
                    }
                }
            }

            // Progress Bar & Action Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Progress details
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = if (isUnlocked) "COMPLETED" else "PROGRESS",
                            fontSize = 9.5.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isUnlocked) Color(0xFF10B981) else Color(0xFF64748B),
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = if (targetVal > 1) "$currentVal / $targetVal" else if (isUnlocked) "1/1" else "0/1",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFCBD5E1)
                        )
                    }

                    LinearProgressIndicator(
                        progress = { progressFraction },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isUnlocked) Color(0xFF10B981) else accentColor,
                        trackColor = Color(0xFF1E293B)
                    )
                }

                // Action / Claim Button
                when {
                    isClaimed -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "CLAIMED ✅",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                    isUnlocked -> {
                        Button(
                            onClick = onClaim,
                            modifier = Modifier
                                .scale(pulseScale)
                                .testTag("claim_btn_${achievement.id}"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFF59E0B)
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Stars,
                                    contentDescription = null,
                                    tint = Color(0xFF0F172A),
                                    modifier = Modifier.size(15.dp)
                                )
                                Text(
                                    text = "CLAIM",
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color(0xFF0F172A)
                                )
                            }
                        }
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B).copy(alpha = 0.6f))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = Color(0xFF64748B),
                                    modifier = Modifier.size(12.dp)
                                )
                                Text(
                                    text = "${(progressFraction * 100).toInt()}%",
                                    fontSize = 10.5.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
