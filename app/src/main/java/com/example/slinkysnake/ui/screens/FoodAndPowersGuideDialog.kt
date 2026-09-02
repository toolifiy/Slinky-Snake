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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.FoodCategory
import com.example.slinkysnake.model.FoodTemplate
import com.example.slinkysnake.viewmodel.GameUiState

@Composable
fun FoodAndPowersGuideDialog(
    uiState: GameUiState,
    onDismiss: () -> Unit
) {
    val allFoods = remember { GameData.ALL_FOOD_TEMPLATES }
    val allPowers = remember { GameData.ALL_POWER_TEMPLATES }

    var selectedCategory by remember { mutableStateOf("ALL") }

    val filteredFoods = remember(selectedCategory) {
        when (selectedCategory) {
            "ALL" -> allFoods
            "FRUITS" -> allFoods.filter { it.category == FoodCategory.FRESH_FRUIT }
            "SWEETS" -> allFoods.filter { it.category == FoodCategory.SWEET_TREAT }
            "MEALS" -> allFoods.filter { it.category == FoodCategory.SAVORY_MEAL }
            "POWERS" -> emptyList()
            else -> allFoods
        }
    }

    val showPowers = selectedCategory == "ALL" || selectedCategory == "POWERS"

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
                .background(Color(0xFF070B18).copy(alpha = 0.92f))
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
                    .fillMaxHeight(0.92f)
                    .testTag("food_powers_guide_dialog"),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xFF0F172A),
                border = BorderStroke(2.dp, Color(0xFF10B981).copy(alpha = 0.85f)),
                shadowElevation = 16.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // 1. TOP HEADER (Title + Close Button)
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
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(Color(0xFF10B981), Color(0xFF059669))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.RestaurantMenu,
                                    contentDescription = "Menu",
                                    tint = Color.White,
                                    modifier = Modifier.size(22.dp)
                                )
                            }

                            Column {
                                Text(
                                    text = "🍎 FOOD & POWERS GUIDE",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White,
                                    letterSpacing = 0.8.sp
                                )
                                Text(
                                    text = "55+ Delicious Snacks & Super Power Items",
                                    fontSize = 11.sp,
                                    color = Color(0xFF38BDF8),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        IconButton(
                            onClick = {
                                SoundSynth.playClick()
                                onDismiss()
                            },
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF1E293B))
                        ) {
                            Icon(
                                Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // 2. CATEGORY FILTER CHIPS
                    val categories = listOf(
                        "ALL" to "🌟 All Items",
                        "FRUITS" to "🍎 Fresh Fruit",
                        "SWEETS" to "🍰 Sweet Treats",
                        "MEALS" to "🍕 Savory Meals",
                        "POWERS" to "⚡ Super Powers"
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { (catKey, catLabel) ->
                            val isSelected = selectedCategory == catKey
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) Color(0xFF10B981) else Color(0xFF1E293B),
                                border = BorderStroke(
                                    1.dp,
                                    if (isSelected) Color(0xFF34D399) else Color(0xFF334155)
                                ),
                                modifier = Modifier.clickable {
                                    SoundSynth.playClick()
                                    selectedCategory = catKey
                                }
                            ) {
                                Text(
                                    text = catLabel,
                                    color = if (isSelected) Color(0xFF0F172A) else Color(0xFFCBD5E1),
                                    fontSize = 11.5.sp,
                                    fontWeight = FontWeight.Black,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    // 3. SCROLLABLE LIST OF FOODS & POWERS
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF080D18))
                            .border(1.dp, Color(0xFF1E293B), RoundedCornerShape(12.dp))
                            .padding(6.dp)
                    ) {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            contentPadding = PaddingValues(vertical = 4.dp)
                        ) {
                            // FOOD SECTION
                            if (filteredFoods.isNotEmpty()) {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF0F172A),
                                        border = BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.5f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "🍎 DELICIOUS FOOD ITEMS (${filteredFoods.size})",
                                                color = Color(0xFF10B981),
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                            Text(
                                                text = "15s Timer",
                                                color = Color(0xFF64748B),
                                                fontSize = 9.5.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }

                                items(filteredFoods, key = { it.type }) { food ->
                                    FoodGuideCard(food = food, isPower = false)
                                }
                            }

                            // POWERS SECTION
                            if (showPowers) {
                                item {
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFF0C192E),
                                        border = BorderStroke(1.dp, Color(0xFF38BDF8).copy(alpha = 0.6f)),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, bottom = 2.dp)
                                    ) {
                                        Row(
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Text(
                                                text = "⚡ SUPER POWERS (${allPowers.size} Available)",
                                                color = Color(0xFF38BDF8),
                                                fontSize = 11.5.sp,
                                                fontWeight = FontWeight.Black
                                            )
                                            Surface(
                                                shape = RoundedCornerShape(4.dp),
                                                color = Color(0xFF0369A1)
                                            ) {
                                                Text(
                                                    text = "${uiState.allowedPowers.size}/${allPowers.size} ACTIVE (10s)",
                                                    color = Color(0xFFE0F2FE),
                                                    fontSize = 9.sp,
                                                    fontWeight = FontWeight.Black,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                )
                                            }
                                        }
                                    }
                                }

                                items(allPowers, key = { it.type }) { power ->
                                    FoodGuideCard(food = power, isPower = true)
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
private fun FoodGuideCard(
    food: FoodTemplate,
    isPower: Boolean = false
) {
    val foodColor = Color(food.color)

    Surface(
        shape = RoundedCornerShape(10.dp),
        color = if (isPower) Color(0xFF0C192E) else Color(0xFF111D30),
        border = BorderStroke(
            1.dp,
            if (isPower) Color(0xFF38BDF8).copy(alpha = 0.5f) else Color(0xFF1E3A5F)
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Emoji Box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(foodColor.copy(alpha = 0.14f))
                    .border(1.dp, foodColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = food.emoji,
                    fontSize = 24.sp
                )
            }

            // Text Info
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = food.name,
                        color = Color.White,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isPower) Color(0xFF0284C7).copy(alpha = 0.25f) else Color(0xFF0C1E38),
                        border = BorderStroke(
                            1.dp,
                            if (isPower) Color(0xFF38BDF8) else Color(0xFF0284C7).copy(alpha = 0.7f)
                        )
                    ) {
                        Text(
                            text = if (isPower) "⚡ EQUIPPED" else "+${food.points} XP",
                            color = if (isPower) Color(0xFF38BDF8) else Color(0xFF38BDF8),
                            fontSize = if (isPower) 9.5.sp else 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Text(
                    text = food.effectDescription,
                    color = Color(0xFF94A3B8),
                    fontSize = 11.sp,
                    lineHeight = 14.5.sp
                )
            }
        }
    }
}
