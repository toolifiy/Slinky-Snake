package com.example.slinkysnake.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
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
    modifier: Modifier = Modifier
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(8.dp)
            .shadow(12.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
    ) {
        val boardWidth = size.width
        val cellWidth = boardWidth / GRID_SIZE

        // Screen shake
        val shakeX = if (screenShake > 0f) (Math.random().toFloat() - 0.5f) * screenShake * 2f else 0f
        val shakeY = if (screenShake > 0f) (Math.random().toFloat() - 0.5f) * screenShake * 2f else 0f

        // 1. Draw Board Background Tiles
        val col1 = if (gameMode == GameMode.LEVELS) Color(levelConfig.theme.bgCol1) else Color(boardThemeColor1)
        val col2 = if (gameMode == GameMode.LEVELS) Color(levelConfig.theme.bgCol2) else Color(boardThemeColor2)

        for (x in 0 until GRID_SIZE) {
            for (y in 0 until GRID_SIZE) {
                val tileColor = if ((x + y) % 2 == 0) col1 else col2
                drawRect(
                    color = tileColor,
                    topLeft = Offset(x * cellWidth + shakeX, y * cellWidth + shakeY),
                    size = Size(cellWidth, cellWidth)
                )
            }
        }

        // 2. Draw Obstacles (Level Mode)
        if (gameMode == GameMode.LEVELS) {
            val obstacleColor = Color(0xFF1E293B)
            val obstacleAccent = Color(levelConfig.theme.borderColor)
            for (obs in obstacles) {
                val left = obs.x * cellWidth + shakeX
                val top = obs.y * cellWidth + shakeY
                drawRoundRect(
                    color = obstacleColor,
                    topLeft = Offset(left + 2f, top + 2f),
                    size = Size(cellWidth - 4f, cellWidth - 4f),
                    cornerRadius = CornerRadius(6f, 6f)
                )
                drawRoundRect(
                    color = obstacleAccent,
                    topLeft = Offset(left + 2f, top + 2f),
                    size = Size(cellWidth - 4f, cellWidth - 4f),
                    cornerRadius = CornerRadius(6f, 6f),
                    style = Stroke(width = 2.5f)
                )
            }
        }

        // 3. Draw Food
        if (food != null) {
            val fx = food.position.x * cellWidth + cellWidth / 2f + shakeX
            val fy = food.position.y * cellWidth + cellWidth / 2f + shakeY

            // Pulsing glow ring
            val pulse = (sin(System.currentTimeMillis() / 150.0).toFloat() * 0.15f) + 1.0f
            val foodRadius = (cellWidth * 0.42f) * pulse

            drawCircle(
                color = Color(food.color).copy(alpha = 0.35f),
                radius = foodRadius * 1.35f,
                center = Offset(fx, fy)
            )

            // Draw Emoji Food with native canvas paint
            val textPaint = Paint().apply {
                textSize = cellWidth * 0.75f * pulse
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            val fontMetrics = textPaint.fontMetrics
            val baseline = fy - (fontMetrics.ascent + fontMetrics.descent) / 2f
            drawContext.canvas.nativeCanvas.drawText(food.emoji, fx, baseline, textPaint)

            // Draw 10-second food expiration indicator ring
            val elapsed = System.currentTimeMillis() - food.spawnTime
            val timeLeftFraction = (1f - (elapsed / 10000f)).coerceIn(0f, 1f)
            if (timeLeftFraction < 0.6f) {
                drawArc(
                    color = Color(food.color).copy(alpha = 0.8f),
                    startAngle = -90f,
                    sweepAngle = 360f * timeLeftFraction,
                    useCenter = false,
                    topLeft = Offset(fx - foodRadius, fy - foodRadius),
                    size = Size(foodRadius * 2f, foodRadius * 2f),
                    style = Stroke(width = 3f)
                )
            }
        }

        // 4. Draw Snake Body & Head
        if (snake.isNotEmpty()) {
            val primaryColor = Color(selectedSkin.primaryColor)
            val secondaryColor = Color(selectedSkin.secondaryColor)
            val isImmortal = activeEffects.immortal > 0

            val currentAlpha = if (isImmortal) 0.65f else 1.0f

            // Draw segments from tail to head
            for (i in snake.indices.reversed()) {
                val currentPos = snake[i]
                val prevPos = if (i < prevSnake.size) prevSnake[i] else currentPos

                // Smooth position interpolation
                val interpX = (prevPos.x + (currentPos.x - prevPos.x) * moveProgress) * cellWidth + cellWidth / 2f + shakeX
                val interpY = (prevPos.y + (currentPos.y - prevPos.y) * moveProgress) * cellWidth + cellWidth / 2f + shakeY

                val segRadius = (cellWidth * 0.44f) * (1f - (i.toFloat() / snake.size) * 0.25f)

                if (i > 0) {
                    // Body Segment
                    val segColor = if (i % 2 == 0) primaryColor.copy(alpha = currentAlpha) else secondaryColor.copy(alpha = currentAlpha)

                    drawCircle(
                        color = segColor,
                        radius = segRadius,
                        center = Offset(interpX, interpY)
                    )
                    drawCircle(
                        color = Color(0xFF0F172A).copy(alpha = currentAlpha),
                        radius = segRadius,
                        center = Offset(interpX, interpY),
                        style = Stroke(width = 1.5f)
                    )

                    // Pattern spots or stripes
                    if (selectedSkin.pattern == Pattern.SPOTS && i % 2 == 1) {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.7f * currentAlpha),
                            radius = segRadius * 0.35f,
                            center = Offset(interpX, interpY)
                        )
                    }
                } else {
                    // Snake Head
                    val headDegrees = when (direction) {
                        Direction.UP -> 180f
                        Direction.DOWN -> 0f
                        Direction.LEFT -> 90f
                        Direction.RIGHT -> 270f
                    }

                    rotate(degrees = headDegrees, pivot = Offset(interpX, interpY)) {
                        val headScale = (cellWidth / 48f)
                        drawContext.canvas.save()
                        drawContext.canvas.translate(interpX - 32f * headScale, interpY - 32f * headScale)
                        drawSnakeHead(selectedSkin, 64f * headScale, 64f * headScale, headScale, mouthOpen)
                        drawContext.canvas.restore()
                    }
                }
            }
        }

        // 5. Draw Particles
        for (p in particles) {
            val alpha = (1f - (p.life / p.maxLife)).coerceIn(0f, 1f)
            drawCircle(
                color = Color(p.color).copy(alpha = alpha),
                radius = p.size,
                center = Offset(p.x + shakeX, p.y + shakeY)
            )
        }

        // 6. Draw Floating Texts
        for (ft in floatingTexts) {
            val alpha = (ft.life / 30f).coerceIn(0f, 1f)
            val textPaint = Paint().apply {
                color = Color(ft.color).copy(alpha = alpha).toArgb()
                textSize = cellWidth * 0.55f
                isFakeBoldText = true
                textAlign = Paint.Align.CENTER
                isAntiAlias = true
            }
            drawContext.canvas.nativeCanvas.drawText(ft.text, ft.x + shakeX, ft.y + shakeY, textPaint)
        }
    }
}
