package com.example.slinkysnake.ui.components

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import com.example.slinkysnake.model.Skin

/**
 * Ultra-high-end arcade stadium arena preview rendering dynamic 3D lighting,
 * rich checkered neon floor, particle sparkles, glowing arena borders, tightly connected slithering snake,
 * and perfectly grid-aligned appetizing fruit pickups.
 */
@Composable
fun SnakeArenaPreviewCanvas(
    skin: Skin,
    bgCol1: Long,
    bgCol2: Long,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Draw Checkered Arcade Stadium Grid
        val columns = 12
        val cellWidth = w / columns
        val rows = (h / cellWidth).toInt() + 2

        val col1 = Color(bgCol1)
        val col2 = Color(bgCol2)

        for (x in 0 until columns) {
            for (y in 0 until rows) {
                val tileColor = if ((x + y) % 2 == 0) col1 else col2
                drawRect(
                    color = tileColor,
                    topLeft = Offset(x * cellWidth, y * cellWidth),
                    size = Size(cellWidth, cellWidth)
                )
                // Subtle inner grid dot
                if ((x + y) % 2 == 0) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = cellWidth * 0.08f,
                        center = Offset(x * cellWidth + cellWidth / 2f, y * cellWidth + cellWidth / 2f)
                    )
                }
            }
        }

        // 2. High-Tech Cyber Corner Grid Lines
        for (i in 1..3) {
            val offsetVal = i * (cellWidth * 2f)
            drawLine(
                color = Color.White.copy(alpha = 0.06f),
                start = Offset(0f, offsetVal),
                end = Offset(offsetVal, 0f),
                strokeWidth = 1.5f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.06f),
                start = Offset(w, h - offsetVal),
                end = Offset(w - offsetVal, h),
                strokeWidth = 1.5f
            )
        }

        // 3. Dynamic Center Spotlight & Vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.12f),
                    Color.Transparent,
                    Color(0x8A000000)
                ),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.65f
            ),
            size = size
        )

        // 4. Radiant Neon Stadium Rim
        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    Color(0xFF10B981).copy(alpha = 0.7f),
                    Color(0xFF38BDF8).copy(alpha = 0.7f),
                    Color(0xFFF59E0B).copy(alpha = 0.7f),
                    Color(0xFFEC4899).copy(alpha = 0.7f),
                    Color(0xFF10B981).copy(alpha = 0.7f)
                ),
                center = Offset(w * 0.5f, h * 0.5f)
            ),
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 3.5f)
        )

        // 5. Sparkle Particle Stars
        val sparkles = listOf(
            Triple(0.18f, 0.22f, "✨"),
            Triple(0.88f, 0.18f, "⭐"),
            Triple(0.12f, 0.80f, "🌟")
        )
        val sparklePaint = Paint().apply {
            textSize = cellWidth * 0.6f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        sparkles.forEach { (sx, sy, icon) ->
            val py = h * sy - (sparklePaint.fontMetrics.ascent + sparklePaint.fontMetrics.descent) / 2f
            drawContext.canvas.nativeCanvas.drawText(icon, w * sx, py, sparklePaint)
        }

        // 6. Food Pickups Grid-Aligned and Proportional to Blocks
        // Primary Apple at grid col 9, row 2 (or 3)
        val appleCol = (columns * 0.80f).toInt().coerceIn(0, columns - 1)
        val appleRow = (rows * 0.46f).toInt().coerceIn(0, rows - 1)
        val foodX = appleCol * cellWidth + cellWidth / 2f
        val foodY = appleRow * cellWidth + cellWidth / 2f

        // Highlight the exact tile under the apple
        drawRect(
            color = Color(0x33EF4444),
            topLeft = Offset(appleCol * cellWidth, appleRow * cellWidth),
            size = Size(cellWidth, cellWidth)
        )

        // Food glowing aura around tile
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x88EF4444), Color.Transparent),
                center = Offset(foodX, foodY),
                radius = cellWidth * 1.1f
            ),
            radius = cellWidth * 1.1f,
            center = Offset(foodX, foodY)
        )

        // Emoji sized perfectly to fit inside grid cell
        val emojiPaint = Paint().apply {
            textSize = cellWidth * 0.76f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val metrics = emojiPaint.fontMetrics
        val baseline = foodY - (metrics.ascent + metrics.descent) / 2f
        drawContext.canvas.nativeCanvas.drawText("🍎", foodX, baseline, emojiPaint)

        // 7. Snake 3D Connected Slither Body (No Gaps between Balls!)
        val primaryColor = Color(skin.primaryColor)
        val secondaryColor = Color(skin.secondaryColor)

        // Body segments sized proportional to grid cells
        val headRadius = cellWidth * 0.48f
        val bodyRadius = cellWidth * 0.42f
        val tailRadius = cellWidth * 0.32f

        // Head position
        val headCenter = Offset(w * 0.58f, h * 0.48f)

        // Tightly connected 7-segment chain along a natural curve with overlapping steps
        val points = listOf(
            Offset(w * 0.12f, h * 0.52f), // Tail
            Offset(w * 0.19f, h * 0.46f), // Seg 1
            Offset(w * 0.26f, h * 0.42f), // Seg 2
            Offset(w * 0.33f, h * 0.44f), // Seg 3
            Offset(w * 0.40f, h * 0.50f), // Seg 4
            Offset(w * 0.46f, h * 0.54f), // Seg 5
            Offset(w * 0.52f, h * 0.51f), // Neck
        )

        val shadowAlpha = 0x55000000

        // Draw drop shadows first
        points.forEachIndexed { index, pt ->
            val rad = if (index == 0) tailRadius else bodyRadius
            drawCircle(color = Color(shadowAlpha), radius = rad * 1.12f, center = Offset(pt.x, pt.y + 4f))
        }
        drawCircle(color = Color(0x65000000), radius = headRadius * 1.2f, center = Offset(headCenter.x, headCenter.y + 5f))

        // Draw connected body balls with gradient shaders
        points.forEachIndexed { index, pt ->
            val rad = if (index == 0) tailRadius else bodyRadius
            val (c1, c2) = if (index % 2 == 0) Pair(secondaryColor, primaryColor) else Pair(primaryColor, secondaryColor)
            val brush = Brush.radialGradient(
                colors = listOf(c1, c2),
                center = Offset(pt.x - rad * 0.3f, pt.y - rad * 0.3f),
                radius = rad * 1.3f
            )
            drawCircle(brush = brush, radius = rad, center = pt)
            // Glossy 3D specular reflection
            drawCircle(
                color = Color.White.copy(alpha = 0.55f),
                radius = rad * 0.3f,
                center = Offset(pt.x - rad * 0.3f, pt.y - rad * 0.3f)
            )
        }

        // 8. Snake Hero Head with 3D Shader & Accessories
        drawRenderedSnakeHead(
            skin = skin,
            centerX = headCenter.x,
            centerY = headCenter.y,
            headRadius = headRadius,
            mouthOpen = true,
            tongueFlick = true
        )
    }
}

/**
 * Pure Stadium Arena Background Thumbnail Preview (NO snake, NO food)
 * Renders high-end checkered floor, 3D lighting, ambient cyber glow and sparkle stars.
 */
@Composable
fun BackgroundThemeThumbnailCanvas(
    bgCol1: Long,
    bgCol2: Long,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        val columns = 8
        val cellWidth = w / columns
        val rows = (h / cellWidth).toInt() + 2

        val col1 = Color(bgCol1)
        val col2 = Color(bgCol2)

        for (x in 0 until columns) {
            for (y in 0 until rows) {
                val tileColor = if ((x + y) % 2 == 0) col1 else col2
                drawRect(
                    color = tileColor,
                    topLeft = Offset(x * cellWidth, y * cellWidth),
                    size = Size(cellWidth, cellWidth)
                )
                if ((x + y) % 2 == 0) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.09f),
                        radius = cellWidth * 0.09f,
                        center = Offset(x * cellWidth + cellWidth / 2f, y * cellWidth + cellWidth / 2f)
                    )
                }
            }
        }

        // Cyber corner lines
        for (i in 1..2) {
            val offsetVal = i * (cellWidth * 1.6f)
            drawLine(
                color = Color.White.copy(alpha = 0.08f),
                start = Offset(0f, offsetVal),
                end = Offset(offsetVal, 0f),
                strokeWidth = 1.5f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.08f),
                start = Offset(w, h - offsetVal),
                end = Offset(w - offsetVal, h),
                strokeWidth = 1.5f
            )
        }

        // Radial lighting & vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.12f),
                    Color.Transparent,
                    Color(0x8A000000)
                ),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.7f
            ),
            size = size
        )

        // Glowing border rim
        drawRect(
            brush = Brush.sweepGradient(
                colors = listOf(
                    col1.copy(alpha = 0.85f),
                    col2.copy(alpha = 0.85f),
                    col1.copy(alpha = 0.85f)
                ),
                center = Offset(w * 0.5f, h * 0.5f)
            ),
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 2.5f)
        )

        // Sparkle stars
        val sparkles = listOf(
            Triple(0.20f, 0.28f, "✨"),
            Triple(0.78f, 0.72f, "⭐")
        )
        val sparklePaint = Paint().apply {
            textSize = cellWidth * 0.65f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        sparkles.forEach { (sx, sy, icon) ->
            val py = h * sy - (sparklePaint.fontMetrics.ascent + sparklePaint.fontMetrics.descent) / 2f
            drawContext.canvas.nativeCanvas.drawText(icon, w * sx, py, sparklePaint)
        }
    }
}

