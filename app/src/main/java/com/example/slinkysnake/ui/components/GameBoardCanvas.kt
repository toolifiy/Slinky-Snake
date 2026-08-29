package com.example.slinkysnake.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.example.slinkysnake.model.ActiveEffects
import com.example.slinkysnake.model.Direction
import com.example.slinkysnake.model.FloatingText
import com.example.slinkysnake.model.Food
import com.example.slinkysnake.model.GameMode
import com.example.slinkysnake.model.LevelConfig
import com.example.slinkysnake.model.Particle
import com.example.slinkysnake.model.Pattern
import com.example.slinkysnake.model.Position
import com.example.slinkysnake.model.Skin
import kotlin.math.sin

private const val GRID_SIZE = 20

@Composable
fun GameBoardCanvas(
    snake: List<Position>,
    prevSnake: List<Position>,
    moveProgress: Float,
    food: Food?,
    obstacles: List<Position>,
    selectedSkin: Skin,
    gameMode: GameMode,
    levelConfig: LevelConfig,
    boardThemeColor1: Long,
    boardThemeColor2: Long,
    activeEffects: ActiveEffects,
    particles: List<Particle>,
    floatingTexts: List<FloatingText>,
    screenShake: Float,
    direction: Direction,
    mouthOpen: Boolean,
    isPaused: Boolean = false,
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clipToBounds()
    ) {
        val boardWidth = size.width
        val cellWidth = boardWidth / GRID_SIZE

        clipRect(0f, 0f, size.width, size.height) {
            // Screen Shake calculation
            val shakeX = if (screenShake > 0f && !isPaused) (Math.random().toFloat() - 0.5f) * screenShake * 2.0f else 0f
            val shakeY = if (screenShake > 0f && !isPaused) (Math.random().toFloat() - 0.5f) * screenShake * 2.0f else 0f

            val time = System.currentTimeMillis()

            // 1. Draw Checkered Mint/Lime Board Tiles (Respects selected board theme)
            val col1 = if (gameMode == GameMode.LEVELS) Color(levelConfig.theme.bgCol1) else Color(boardThemeColor1)
            val col2 = if (gameMode == GameMode.LEVELS) Color(levelConfig.theme.bgCol2) else Color(boardThemeColor2)

            for (x in 0 until GRID_SIZE) {
                for (y in 0 until GRID_SIZE) {
                    val tileColor = if ((x + y) % 2 == 0) col1 else col2
                    val tileLeft = x * cellWidth
                    val tileTop = y * cellWidth

                    drawRect(
                        color = tileColor,
                        topLeft = Offset(tileLeft, tileTop),
                        size = Size(cellWidth, cellWidth)
                    )
                }
            }

            // 2. Draw Obstacles (Level Mode)
            if (gameMode == GameMode.LEVELS && obstacles.isNotEmpty()) {
                val obstacleBaseColor = Color(0xFF1E293B)
                val obstacleLightColor = Color(0xFF334155)
                val obstacleAccentColor = Color(levelConfig.theme.borderColor)

                for (obs in obstacles) {
                    val left = obs.x * cellWidth + shakeX
                    val top = obs.y * cellWidth + shakeY

                    // Drop Shadow
                    drawRoundRect(
                        color = Color(0x50000000),
                        topLeft = Offset(left + 2f, top + 3f),
                        size = Size(cellWidth - 3f, cellWidth - 3f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Main Stone Block
                    drawRoundRect(
                        color = obstacleBaseColor,
                        topLeft = Offset(left + 1f, top + 1f),
                        size = Size(cellWidth - 2f, cellWidth - 2f),
                        cornerRadius = CornerRadius(6f, 6f)
                    )

                    // Neon Outline Accent
                    drawRoundRect(
                        color = obstacleAccentColor.copy(alpha = 0.8f),
                        topLeft = Offset(left + 1f, top + 1f),
                        size = Size(cellWidth - 2f, cellWidth - 2f),
                        cornerRadius = CornerRadius(6f, 6f),
                        style = Stroke(width = 1.5f)
                    )
                }
            }

            // 3. Draw Food (Croissant / Apple / Powerup inside green circular badge like screenshot)
            if (food != null) {
                val fx = food.position.x * cellWidth + cellWidth / 2f + shakeX
                val bobY = if (isPaused) 0f else sin(time / 140.0).toFloat() * (cellWidth * 0.03f)
                val fy = food.position.y * cellWidth + cellWidth / 2f + shakeY + bobY

                val foodColor = Color(food.color)
                val baseRadius = cellWidth * 0.44f

                // Original Green/colored translucent circular backdrop ring behind emoji
                drawCircle(
                    color = Color(0xFF10B981).copy(alpha = 0.35f),
                    radius = baseRadius * 1.15f,
                    center = Offset(fx, fy)
                )

                // Food Expiration Timer Ring (Depletes smoothly from spawn without delay or dark backgrounds)
                val timeLeftFraction = (food.remainingLifeMs / 15000f).coerceIn(0f, 1f)
                val ringRadius = baseRadius * 1.15f
                if (timeLeftFraction > 0f) {
                    drawArc(
                        color = Color(0xFF10B981).copy(alpha = 0.9f),
                        startAngle = -90f,
                        sweepAngle = 360f * timeLeftFraction,
                        useCenter = false,
                        topLeft = Offset(fx - ringRadius, fy - ringRadius),
                        size = Size(ringRadius * 2f, ringRadius * 2f),
                        style = Stroke(width = 2.5f, cap = StrokeCap.Round)
                    )
                }

                // Draw Crisp Emoji with Native Canvas
                val emojiPaint = Paint().apply {
                    textSize = cellWidth * 0.76f
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                }
                val metrics = emojiPaint.fontMetrics
                val baseline = fy - (metrics.ascent + metrics.descent) / 2f
                drawContext.canvas.nativeCanvas.drawText(food.emoji, fx, baseline, emojiPaint)
            }

            // 4. Draw Snake (Clean Block-by-Block Movement with Arcade Polishing)
            if (snake.isNotEmpty()) {
                val primaryColor = Color(selectedSkin.primaryColor)
                val secondaryColor = Color(selectedSkin.secondaryColor)
                val isImmortal = activeEffects.immortal > 0L
                val currentAlpha = if (isImmortal) 0.75f else 1.0f

                val segmentPositions = mutableListOf<Offset>()
                val segmentRadii = mutableListOf<Float>()

                val totalSegments = snake.size
                for (i in snake.indices) {
                    val currentPos = snake[i]
                    val posX = currentPos.x * cellWidth + cellWidth / 2f + shakeX
                    val posY = currentPos.y * cellWidth + cellWidth / 2f + shakeY
                    val defaultRadius = (cellWidth * 0.42f)
                    val radius = when {
                        i == totalSegments - 1 && totalSegments >= 3 -> defaultRadius * 0.75f // Tip of tail (increased from 0.55f, slightly smaller than 2nd-last)
                        i == totalSegments - 2 && totalSegments >= 4 -> defaultRadius * 0.88f // 2nd to last ball
                        else -> defaultRadius
                    }
                    segmentPositions.add(Offset(posX, posY))
                    segmentRadii.add(radius)
                }

                // A. Drop shadows under segments
                for (i in segmentPositions.indices.reversed()) {
                    val pos = segmentPositions[i]
                    val r = segmentRadii[i]
                    drawCircle(
                        color = Color(0x30000000),
                        radius = r * 1.02f,
                        center = Offset(pos.x, pos.y + r * 0.12f)
                    )
                }

                // B. Draw Body Segments (Tail to Neck)
                for (i in (1 until segmentPositions.size).reversed()) {
                    val pos = segmentPositions[i]
                    val r = segmentRadii[i]

                    val segColor = if (i % 2 == 0) primaryColor else secondaryColor

                    // 3D Ball Radial Gradient
                    val segBrush = Brush.radialGradient(
                        colors = listOf(
                            segColor.copy(alpha = currentAlpha),
                            segColor.copy(alpha = currentAlpha),
                            secondaryColor.copy(alpha = currentAlpha)
                        ),
                        center = Offset(pos.x - r * 0.25f, pos.y - r * 0.25f),
                        radius = r * 1.25f
                    )

                    drawCircle(
                        brush = segBrush,
                        radius = r,
                        center = pos
                    )

                    // 3D Juicy Gloss Reflection
                    drawCircle(
                        color = Color.White.copy(alpha = 0.5f * currentAlpha),
                        radius = r * 0.32f,
                        center = Offset(pos.x - r * 0.3f, pos.y - r * 0.3f)
                    )

                    // Subtle outline
                    drawCircle(
                        color = Color(0xFF991B1B).copy(alpha = 0.6f * currentAlpha),
                        radius = r,
                        center = pos,
                        style = Stroke(width = 1.5f)
                    )
                }

                // C. Draw Animated Head
                val headPos = segmentPositions[0]
                val headRadius = cellWidth * 0.45f

                val headAngle = when (direction) {
                    Direction.DOWN -> 0f
                    Direction.UP -> 180f
                    Direction.LEFT -> 90f
                    Direction.RIGHT -> 270f
                }

                rotate(degrees = headAngle, pivot = headPos) {
                    drawRenderedSnakeHead(
                        skin = selectedSkin,
                        centerX = headPos.x,
                        centerY = headPos.y,
                        headRadius = headRadius,
                        mouthOpen = mouthOpen,
                        tongueFlick = !isPaused
                    )
                }
            }

            // 5. Draw Sparkles & Burst Particles
            for (p in particles) {
                val alpha = (1f - (p.life / p.maxLife)).coerceIn(0f, 1f)
                val pColor = Color(p.color).copy(alpha = alpha)

                // Convert grid particle coordinates to actual pixel coordinates
                val px = p.x * cellWidth + shakeX
                val py = p.y * cellWidth + shakeY

                drawCircle(
                    color = pColor,
                    radius = p.size * (cellWidth / 20f),
                    center = Offset(px, py)
                )
            }

            // 6. Draw Floating Score Texts (Directly on spot where food was eaten)
            for (ft in floatingTexts) {
                val alpha = (ft.life / 30f).coerceIn(0f, 1f)
                val scale = (1.2f - (ft.life / 30f) * 0.2f).coerceIn(1f, 1.2f)

                val textPaint = Paint().apply {
                    color = Color(ft.color).copy(alpha = alpha).toArgb()
                    textSize = cellWidth * 0.70f * scale
                    typeface = Typeface.DEFAULT_BOLD
                    textAlign = Paint.Align.CENTER
                    isAntiAlias = true
                    setShadowLayer(5f, 0f, 2f, android.graphics.Color.argb((alpha * 200).toInt(), 0, 0, 0))
                }

                val textX = ft.x * cellWidth + shakeX
                val textY = ft.y * cellWidth + shakeY

                drawContext.canvas.nativeCanvas.drawText(
                    ft.text,
                    textX,
                    textY,
                    textPaint
                )
            }
        }
    }
}
