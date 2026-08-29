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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.graphics.Brush
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
import com.example.slinkysnake.model.FoodCategory
import com.example.slinkysnake.model.FoodTemplate
import com.example.slinkysnake.viewmodel.GameUiState

enum class MarketCategory(val label: String, val emoji: String) {
    ALL("ALL", "🧺"),
    FRUITS("FRUITS", "🍎"),
    SWEETS("SWEETS", "🍰"),
    MEALS("MEALS", "🍕"),
    POWERS("POWERS", "⚡")
}

@Composable
fun FoodMarketDialog(
    uiState: GameUiState,
    onSellFood: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf(MarketCategory.ALL) }
    var soldBannerMessage by remember { mutableStateOf<String?>(null) }

    val filteredFoods = remember(selectedCategory) {
        when (selectedCategory) {
            MarketCategory.ALL -> GameData.ALL_FOOD_TEMPLATES
            MarketCategory.FRUITS -> GameData.ALL_FOOD_TEMPLATES.filter { it.category == FoodCategory.FRESH_FRUIT }
            MarketCategory.SWEETS -> GameData.ALL_FOOD_TEMPLATES.filter { it.category == FoodCategory.SWEET_TREAT }
            MarketCategory.MEALS -> GameData.ALL_FOOD_TEMPLATES.filter { it.category == FoodCategory.SAVORY_MEAL }
            MarketCategory.POWERS -> GameData.ALL_FOOD_TEMPLATES.filter { it.category == FoodCategory.POWER_UP }
        }
    }

    val sortedFoods = remember(filteredFoods, uiState.foodInventory) {
        filteredFoods.sortedWith(
            compareByDescending<FoodTemplate> { food ->
                val stock = uiState.foodInventory[food.type] ?: 0
                val canSell = stock >= food.unitsPerCoin
                when {
                    canSell -> 2
                    stock > 0 -> 1
                    else -> 0
                }
            }.thenByDescending { food ->
                uiState.foodInventory[food.type] ?: 0
            }
        )
    }

    val totalFoodStock = remember(uiState.foodInventory) {
        GameData.ALL_FOOD_TEMPLATES.sumOf { template ->
            uiState.foodInventory[template.type] ?: 0
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.96f)
                .fillMaxHeight(0.92f)
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF0F172A))
                .border(1.5.dp, Color(0xFF334155), RoundedCornerShape(10.dp))
                .padding(14.dp)
                .testTag("food_market_dialog")
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // 1. TOP HEADER
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFF10B981).copy(alpha = 0.6f), RoundedCornerShape(8.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu Icon",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                        }

                        Column {
                            Text(
                                text = "FOOD MARKET",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            )
                            Text(
                                text = "Sell foods & collect coins",
                                color = Color(0xFF94A3B8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Coins Pill
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFF1E293B),
                            border = BorderStroke(1.5.dp, Color(0xFFF59E0B))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("🪙", fontSize = 14.sp)
                                Text(
                                    text = "${uiState.coins}",
                                    color = Color(0xFFFBBF24),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black
                                )
                            }
                        }

                        // Close Button
                        IconButton(
                            onClick = {
                                SoundSynth.playClick()
                                onDismiss()
                            },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF1E293B))
                                .border(1.dp, Color(0xFF475569), RoundedCornerShape(8.dp))
                                .testTag("close_food_market_button")
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

                Spacer(modifier = Modifier.height(12.dp))

                // 2. STATUS & INVENTORY BANNER
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color(0xFF1E293B), Color(0xFF0F263E))
                            )
                        )
                        .border(1.dp, Color(0xFF38BDF8).copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text("🧺", fontSize = 16.sp)
                            Text(
                                text = "Total Harvest: $totalFoodStock Items",
                                color = Color(0xFFE2E8F0),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        if (soldBannerMessage != null) {
                            Text(
                                text = soldBannerMessage!!,
                                color = Color(0xFF10B981),
                                fontSize = 11.5.sp,
                                fontWeight = FontWeight.Black
                            )
                        } else {
                            Text(
                                text = "Instant One-Click Payout",
                                color = Color(0xFF38BDF8),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3. CATEGORY SELECTOR
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF1E293B),
                    border = BorderStroke(1.dp, Color(0xFF334155)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(3.dp),
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        MarketCategory.values().forEach { cat ->
                            val isSelected = selectedCategory == cat
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(34.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(
                                        if (isSelected) Color(0xFF10B981) else Color.Transparent
                                    )
                                    .clickable {
                                        SoundSynth.playClick()
                                        selectedCategory = cat
                                    }
                                    .testTag("dialog_market_cat_${cat.name.lowercase()}"),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(3.dp)
                                ) {
                                    Text(cat.emoji, fontSize = 11.sp)
                                    Text(
                                        text = cat.label,
                                        color = if (isSelected) Color(0xFF022C22) else Color(0xFF94A3B8),
                                        fontSize = 10.5.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 4. FOOD SELL LIST
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sortedFoods, key = { it.type }) { food ->
                        val stock = uiState.foodInventory[food.type] ?: 0
                        val coins = stock / food.unitsPerCoin

                        FoodSellItemRow(
                            food = food,
                            stock = stock,
                            onSell = {
                                onSellFood(food.type)
                                soldBannerMessage = "Sold ${food.emoji} for +$coins 🪙!"
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FoodSellItemRow(
    food: FoodTemplate,
    stock: Int,
    onSell: () -> Unit
) {
    val foodColor = Color(food.color)
    val unitsNeeded = food.unitsPerCoin
    val canSell = stock >= unitsNeeded
    val possibleCoins = stock / unitsNeeded
    val remainder = stock % unitsNeeded

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFF1E293B),
        border = BorderStroke(1.dp, if (canSell) Color(0xFFF59E0B).copy(alpha = 0.5f) else foodColor.copy(alpha = 0.3f)),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("food_row_${food.type.lowercase()}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Food Emoji Box
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(foodColor.copy(alpha = 0.15f))
                    .border(1.dp, foodColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = food.emoji,
                    fontSize = 24.sp
                )
            }

            // Food Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = food.name,
                    color = Color.White,
                    fontSize = 13.5.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(2.dp))

                // Rate Badge (e.g. "Rate: 10x = 1 🪙")
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Rate: ${unitsNeeded}x = 1 🪙",
                        color = Color(0xFFFBBF24),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "• +${food.points} pts",
                        color = Color(0xFF94A3B8),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                // Stock indicator
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("📦", fontSize = 10.sp)
                    val stockStatusText = when {
                        stock == 0 -> "Empty (0 in stock)"
                        canSell && remainder > 0 -> "Stock: x$stock (+$possibleCoins 🪙, $remainder will remain)"
                        canSell -> "Stock: x$stock (+$possibleCoins 🪙 ready)"
                        else -> "Stock: x$stock (Need ${unitsNeeded - stock} more for 1 🪙)"
                    }
                    Text(
                        text = stockStatusText,
                        color = if (canSell) Color(0xFF34D399) else if (stock > 0) Color(0xFFFACC15) else Color(0xFF94A3B8),
                        fontSize = 10.5.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Right Action: 1-Click SELL Button
            if (canSell) {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF59E0B),
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .clickable { onSell() }
                        .testTag("sell_${food.type.lowercase()}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "SELL",
                            color = Color(0xFF0F172A),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "+$possibleCoins 🪙",
                            color = Color(0xFF451A03),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }
            } else {
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFF0F172A),
                    border = BorderStroke(1.dp, Color(0xFF334155))
                ) {
                    Text(
                        text = if (stock > 0) "${stock}/${unitsNeeded}" else "0/${unitsNeeded}",
                        color = Color(0xFF64748B),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}
