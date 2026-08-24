package com.example.slinkysnake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import com.example.slinkysnake.model.Direction
import kotlin.math.abs

@Composable
fun DpadControls(
    currentDirection: Direction,
    onDirectionChange: (Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    val buttonBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.85f)
    val buttonTint = MaterialTheme.colorScheme.onSurfaceVariant

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // UP
            DpadButton(
                icon = { Icon(Icons.Default.KeyboardArrowUp, contentDescription = "Up", tint = buttonTint, modifier = Modifier.size(32.dp)) },
                onClick = { if (currentDirection != Direction.DOWN) onDirectionChange(Direction.UP) },
                testTag = "dpad_up"
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // LEFT
                DpadButton(
                    icon = { Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Left", tint = buttonTint, modifier = Modifier.size(32.dp)) },
                    onClick = { if (currentDirection != Direction.RIGHT) onDirectionChange(Direction.LEFT) },
                    testTag = "dpad_left"
                )

                // CENTER INDICATOR
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary)
                    )
                }

                // RIGHT
                DpadButton(
                    icon = { Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Right", tint = buttonTint, modifier = Modifier.size(32.dp)) },
                    onClick = { if (currentDirection != Direction.LEFT) onDirectionChange(Direction.RIGHT) },
                    testTag = "dpad_right"
                )
            }

            // DOWN
            DpadButton(
                icon = { Icon(Icons.Default.KeyboardArrowDown, contentDescription = "Down", tint = buttonTint, modifier = Modifier.size(32.dp)) },
                onClick = { if (currentDirection != Direction.UP) onDirectionChange(Direction.DOWN) },
                testTag = "dpad_down"
            )
        }
    }
}

@Composable
private fun DpadButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit,
    testTag: String
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 6.dp,
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(52.dp)
            .testTag(testTag)
    ) {
        Box(contentAlignment = Alignment.Center) {
            icon()
        }
    }
}

fun Modifier.swipeController(
    currentDirection: Direction,
    onDirectionChange: (Direction) -> Unit
): Modifier = this.pointerInput(Unit) {
    var totalX = 0f
    var totalY = 0f
    val threshold = 35f

    detectDragGestures(
        onDragStart = {
            totalX = 0f
            totalY = 0f
        },
        onDrag = { change, dragAmount ->
            change.consume()
            totalX += dragAmount.x
            totalY += dragAmount.y

            if (abs(totalX) > threshold || abs(totalY) > threshold) {
                if (abs(totalX) > abs(totalY)) {
                    if (totalX > 0 && currentDirection != Direction.LEFT) {
                        onDirectionChange(Direction.RIGHT)
                    } else if (totalX < 0 && currentDirection != Direction.RIGHT) {
                        onDirectionChange(Direction.LEFT)
                    }
                } else {
                    if (totalY > 0 && currentDirection != Direction.UP) {
                        onDirectionChange(Direction.DOWN)
                    } else if (totalY < 0 && currentDirection != Direction.DOWN) {
                        onDirectionChange(Direction.UP)
                    }
                }
                totalX = 0f
                totalY = 0f
            }
        }
    )
}
