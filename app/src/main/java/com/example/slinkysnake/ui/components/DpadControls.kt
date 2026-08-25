package com.example.slinkysnake.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.slinkysnake.model.Direction
import kotlin.math.abs

@Composable
fun ArcadeDpadControls(
    currentDirection: Direction,
    onDirectionChange: (Direction) -> Unit,
    onPauseClick: () -> Unit,
    isPaused: Boolean,
    modifier: Modifier = Modifier
) {
    // Outer dashed Arcade Box
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 14.dp, vertical = 2.dp)
            .drawBehind {
                val stroke = Stroke(
                    width = 2.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 10f), 0f)
                )
                drawRoundRect(
                    color = Color(0xFF334155),
                    topLeft = Offset(0f, 0f),
                    size = Size(size.width, size.height),
                    cornerRadius = CornerRadius(24.dp.toPx(), 24.dp.toPx()),
                    style = stroke
                )
            }
            .padding(top = 10.dp, bottom = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Arcade Controller Header
            Text(
                text = "🕹️ ARCADE CONTROLLER 🕹️",
                color = Color(0xFF94A3B8),
                fontSize = 13.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.5.sp
            )

            Spacer(modifier = Modifier.height(2.dp))

            // UP Button (Pink/Coral Red with white triangle)
            ArcadeDirButton(
                backgroundColor = Color(0xFFFF4B72),
                shadowColor = Color(0xFFBE123C),
                direction = Direction.UP,
                onClick = { if (currentDirection != Direction.DOWN) onDirectionChange(Direction.UP) },
                testTag = "dpad_up"
            )

            // Middle Row: LEFT, PAUSE/RESUME, RIGHT
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // LEFT (Bright Green)
                ArcadeDirButton(
                    backgroundColor = Color(0xFF10B981),
                    shadowColor = Color(0xFF047857),
                    direction = Direction.LEFT,
                    onClick = { if (currentDirection != Direction.RIGHT) onDirectionChange(Direction.LEFT) },
                    testTag = "dpad_left"
                )

                // CENTER PAUSE (Round Orange Button)
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .shadow(8.dp, CircleShape)
                        .clip(CircleShape)
                        .background(Color(0xFFF59E0B))
                        .border(2.5.dp, Color(0xFFD97706), CircleShape)
                        .clickable { onPauseClick() }
                        .testTag("arcade_pause_btn"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (isPaused) "PLAY" else "PAUSE",
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF0F172A),
                            lineHeight = 10.sp
                        )
                        Text(
                            text = "🐍",
                            fontSize = 14.sp
                        )
                    }
                }

                // RIGHT (Ocean Slate Blue)
                ArcadeDirButton(
                    backgroundColor = Color(0xFF0284C7),
                    shadowColor = Color(0xFF0369A1),
                    direction = Direction.RIGHT,
                    onClick = { if (currentDirection != Direction.LEFT) onDirectionChange(Direction.RIGHT) },
                    testTag = "dpad_right"
                )
            }

            // DOWN Button (Purple/Lavender with white triangle)
            ArcadeDirButton(
                backgroundColor = Color(0xFFA855F7),
                shadowColor = Color(0xFF7E22CE),
                direction = Direction.DOWN,
                onClick = { if (currentDirection != Direction.UP) onDirectionChange(Direction.DOWN) },
                testTag = "dpad_down"
            )
        }
    }
}

@Composable
private fun ArcadeDirButton(
    backgroundColor: Color,
    shadowColor: Color,
    direction: Direction,
    onClick: () -> Unit,
    testTag: String
) {
    Box(
        modifier = Modifier
            .size(width = 68.dp, height = 62.dp)
            .shadow(6.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(backgroundColor)
            .border(2.dp, shadowColor.copy(alpha = 0.6f), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        // Draw crisp solid white triangle arrow
        androidx.compose.foundation.Canvas(modifier = Modifier.size(24.dp)) {
            val w = size.width
            val h = size.height
            val path = Path()

            when (direction) {
                Direction.UP -> {
                    path.moveTo(w / 2f, h * 0.15f)
                    path.lineTo(w * 0.85f, h * 0.85f)
                    path.lineTo(w * 0.15f, h * 0.85f)
                    path.close()
                }
                Direction.DOWN -> {
                    path.moveTo(w / 2f, h * 0.85f)
                    path.lineTo(w * 0.85f, h * 0.15f)
                    path.lineTo(w * 0.15f, h * 0.15f)
                    path.close()
                }
                Direction.LEFT -> {
                    path.moveTo(w * 0.15f, h / 2f)
                    path.lineTo(w * 0.85f, h * 0.15f)
                    path.lineTo(w * 0.85f, h * 0.85f)
                    path.close()
                }
                Direction.RIGHT -> {
                    path.moveTo(w * 0.85f, h / 2f)
                    path.lineTo(w * 0.15f, h * 0.15f)
                    path.lineTo(w * 0.15f, h * 0.85f)
                    path.close()
                }
            }

            drawPath(path = path, color = Color.White)
        }
    }
}

fun Modifier.arcadeSwipeController(
    currentDirection: Direction,
    onDirectionChange: (Direction) -> Unit
): Modifier = this.pointerInput(Unit) {
    var totalX = 0f
    var totalY = 0f
    val threshold = 30f

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
