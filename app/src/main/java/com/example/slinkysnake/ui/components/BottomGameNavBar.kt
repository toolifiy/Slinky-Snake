package com.example.slinkysnake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.audio.SoundSynth

enum class NavTab {
    HOME, MARKET, SKINS, SETTINGS
}

@Composable
fun BottomGameNavBar(
    selectedTab: NavTab = NavTab.HOME,
    onHomeClick: () -> Unit,
    onMarketClick: () -> Unit,
    onSkinsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    val navBarBottomInset = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = 8.dp,
                end = 8.dp,
                top = 4.dp,
                bottom = (navBarBottomInset + 4.dp).coerceAtLeast(6.dp)
            )
    ) {
        // Bordered Segmented Bar (direct fit without outer black strip)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFF131D2E))
                .border(1.5.dp, Color(0xFF1E293B), RoundedCornerShape(10.dp))
                .padding(3.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(3.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 1. 🏠 HOME
                GameSegmentedNavItem(
                    icon = Icons.Default.Home,
                    label = "HOME",
                    isSelected = selectedTab == NavTab.HOME,
                    activeBgColor = Color(0xFF10B981), // Mint Emerald
                    activeBorderColor = Color(0xFF059669),
                    onClick = onHomeClick,
                    modifier = Modifier.weight(1f),
                    testTag = "bottom_nav_home"
                )

                // 2. 🧺 MARKET
                GameSegmentedNavItem(
                    icon = Icons.Default.Storefront,
                    label = "MARKET",
                    isSelected = selectedTab == NavTab.MARKET,
                    activeBgColor = Color(0xFFF59E0B), // Golden Amber
                    activeBorderColor = Color(0xFFD97706),
                    onClick = onMarketClick,
                    modifier = Modifier.weight(1f),
                    testTag = "bottom_nav_market"
                )

                // 3. 🎨 SKINS
                GameSegmentedNavItem(
                    icon = Icons.Default.Palette,
                    label = "SKINS",
                    isSelected = selectedTab == NavTab.SKINS,
                    activeBgColor = Color(0xFF38BDF8), // Cyan Sky
                    activeBorderColor = Color(0xFF0284C7),
                    onClick = onSkinsClick,
                    modifier = Modifier.weight(1f),
                    testTag = "bottom_nav_skins"
                )

                // 4. ⚙️ SETTINGS
                GameSegmentedNavItem(
                    icon = Icons.Default.Settings,
                    label = "SETTINGS",
                    isSelected = selectedTab == NavTab.SETTINGS,
                    activeBgColor = Color(0xFF8B5CF6), // Royal Violet
                    activeBorderColor = Color(0xFF7C3AED),
                    onClick = onSettingsClick,
                    modifier = Modifier.weight(1f),
                    testTag = "bottom_nav_settings"
                )
            }
        }
    }
}

@Composable
private fun GameSegmentedNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    activeBgColor: Color,
    activeBorderColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    testTag: String
) {
    val containerShape = RoundedCornerShape(8.dp)

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(containerShape)
            .background(
                if (isSelected) activeBgColor else Color.Transparent
            )
            .border(
                width = if (isSelected) 1.5.dp else 0.dp,
                color = if (isSelected) activeBorderColor else Color.Transparent,
                shape = containerShape
            )
            .clickable {
                SoundSynth.playClick()
                onClick()
            }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 4.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) Color(0xFF022C22) else Color(0xFF94A3B8),
                modifier = Modifier.size(17.dp)
            )

            Spacer(modifier = Modifier.size(4.dp))

            Text(
                text = label,
                color = if (isSelected) Color(0xFF022C22) else Color(0xFF94A3B8),
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.4.sp,
                maxLines = 1
            )
        }
    }
}
