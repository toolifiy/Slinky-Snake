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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.audio.SoundSynth
import com.example.slinkysnake.data.GameData
import com.example.slinkysnake.model.FoodCategory
import com.example.slinkysnake.model.FoodTemplate
import com.example.slinkysnake.ui.components.BottomGameNavBar
import com.example.slinkysnake.ui.components.NavTab
import com.example.slinkysnake.viewmodel.GameUiState
import com.example.slinkysnake.viewmodel.GameViewModel

@Composable
fun MarketScreen(
    viewModel: GameViewModel,
    uiState: GameUiState,
    onBackToHome: () -> Unit,
    onOpenSkins: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val statusBarTop = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val navBarBottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

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

    val totalFoodStock = remember(uiState.foodInventory) {
        GameData.ALL_FOOD_TEMPLATES.sumOf { template ->
            uiState.foodInventory[template.type] ?: 0
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color(0xFF070D1E),
        bottomBar = {
            BottomGameNavBar(
                selectedTab = NavTab.MARKET,
                onHomeClick = onBackToHome,
                onMarketClick = { /* Already on Market */ },
                onSkinsClick = onOpenSkins,
                onSettingsClick = onOpenSettings
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = statusBarTop + 8.dp,
                    bottom = paddingValues.calculateBottomPadding(),
                    start = 14.dp,
                    end = 14.dp
                )
                .testTag("market_screen")
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
                    IconButton(
                        onClick = {
                            SoundSynth.playClick()
                            onBackToHome()
                        },
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF1E293B))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(8.dp))
                            .testTag("market_back_button")
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Color(0xFFF59E0B), Color(0xFFD97706))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Storefront,
                            contentDescription = "Market",
                            tint = Color(0xFF0F172A),
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "FOOD BAZAAR",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Sell harvested foods for coins",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                // Coins pill
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
                        Icon(
                            Icons.Default.MonetizationOn,
                            contentDescription = "Coins",
                            tint = Color(0xFFFBBF24),
                            modifier = Modifier.size(16.dp)
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

            Spacer(modifier = Modifier.height(10.dp))

            // 2. HARVEST SUMMARY BANNER
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
                            text = "Instant Coin Payout",
                            color = Color(0xFF38BDF8),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 3. CATEGORY SEGMENTED SELECTOR
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFF0B132B),
                border = BorderStroke(1.dp, Color(0xFF1E293B)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(3.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
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
                                .testTag("market_cat_${cat.name.lowercase()}"),
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

            // 4. FOOD SELL ITEMS LIST
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 60.dp)
            ) {
                items(filteredFoods, key = { it.type }) { food ->
                    val stock = uiState.foodInventory[food.type] ?: 0

                    FoodSellItemRow(
                        food = food,
                        stock = stock,
                        onSell = {
                            val earned = viewModel.sellFood(food.type)
                            if (earned > 0) {
                                soldBannerMessage = "Sold ${food.emoji} for +$earned 🪙!"
                            }
                        }
                    )
                }
            }
        }
    }
}
