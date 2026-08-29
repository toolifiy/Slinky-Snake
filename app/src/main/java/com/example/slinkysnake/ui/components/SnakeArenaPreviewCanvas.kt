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
 * rich checkered neon floor, particle sparkles, glowing arena borders, glossy slithering snake,
 * and tasty appetizing fruit pickups.
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
        val columns = 14
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
                // Subtle high-tech inner grid dot
                if ((x + y) % 2 == 0) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.08f),
                        radius = cellWidth * 0.1f,
                        center = Offset(x * cellWidth + cellWidth / 2f, y * cellWidth + cellWidth / 2f)
                    )
                }
            }
        }

        // 2. High-Tech Cyber Corner Grid Lines
        for (i in 1..4) {
            val offsetVal = i * (cellWidth * 2.2f)
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
                start = Offset(0f, offsetVal),
                end = Offset(offsetVal, 0f),
                strokeWidth = 1.5f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.05f),
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
            style = Stroke(width = 4f)
        )

        // 5. Sparkle Particle Stars
        val sparkles = listOf(
            Triple(0.20f, 0.20f, "✨"),
            Triple(0.85f, 0.18f, "⭐"),
            Triple(0.12f, 0.80f, "🌟"),
            Triple(0.90f, 0.78f, "✨")
        )
        val sparklePaint = Paint().apply {
            textSize = h * 0.12f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        sparkles.forEach { (sx, sy, icon) ->
            val py = h * sy - (sparklePaint.fontMetrics.ascent + sparklePaint.fontMetrics.descent) / 2f
            drawContext.canvas.nativeCanvas.drawText(icon, w * sx, py, sparklePaint)
        }

        // 6. Radiant Juicy Targets: Primary Red Apple + Golden Star Snack
        val foodX = w * 0.82f
        val foodY = h * 0.48f

        // Food glowing aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x99EF4444), Color(0x33F59E0B), Color.Transparent),
                center = Offset(foodX, foodY),
                radius = h * 0.32f
            ),
            radius = h * 0.32f,
            center = Offset(foodX, foodY)
        )

        val emojiPaint = Paint().apply {
            textSize = h * 0.32f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val metrics = emojiPaint.fontMetrics
        val baseline = foodY - (metrics.ascent + metrics.descent) / 2f
        drawContext.canvas.nativeCanvas.drawText("🍎", foodX, baseline, emojiPaint)

        // Small Secondary Strawberry Pickup
        val berryX = w * 0.72f
        val berryY = h * 0.78f
        val berryPaint = Paint().apply {
            textSize = h * 0.18f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val bMetrics = berryPaint.fontMetrics
        val bBaseline = berryY - (bMetrics.ascent + bMetrics.descent) / 2f
        drawContext.canvas.nativeCanvas.drawText("🍓", berryX, bBaseline, berryPaint)

        // 7. Snake 3D Slither Path
        val primaryColor = Color(skin.primaryColor)
        val secondaryColor = Color(skin.secondaryColor)

        val headRadius = (h * 0.23f).coerceIn(28f, 52f)
        val bodyRadius = headRadius * 0.78f
        val tailRadius = headRadius * 0.58f

        // Natural curved s-curve slither path across arena
        val p0Tail = Offset(w * 0.12f, h * 0.62f)
        val p1Body1 = Offset(w * 0.22f, h * 0.42f)
        val p2Body2 = Offset(w * 0.34f, h * 0.58f)
        val p3Neck = Offset(w * 0.47f, h * 0.44f)
        val headCenter = Offset(w * 0.60f, h * 0.48f)

        // Realistic Drop Shadows under Snake Segments
        val shadowAlpha = 0x55000000
        drawCircle(color = Color(shadowAlpha), radius = tailRadius * 1.15f, center = Offset(p0Tail.x, p0Tail.y + 6f))
        drawCircle(color = Color(shadowAlpha), radius = bodyRadius * 1.15f, center = Offset(p1Body1.x, p1Body1.y + 6f))
        drawCircle(color = Color(shadowAlpha), radius = bodyRadius * 1.15f, center = Offset(p2Body2.x, p2Body2.y + 6f))
        drawCircle(color = Color(shadowAlpha), radius = bodyRadius * 1.15f, center = Offset(p3Neck.x, p3Neck.y + 6f))
        drawCircle(color = Color(0x65000000), radius = headRadius * 1.25f, center = Offset(headCenter.x, headCenter.y + 7f))

        // Segment 0 (Tail)
        val tailBrush = Brush.radialGradient(
            colors = listOf(secondaryColor, primaryColor),
            center = Offset(p0Tail.x - tailRadius * 0.3f, p0Tail.y - tailRadius * 0.3f),
            radius = tailRadius * 1.3f
        )
        drawCircle(brush = tailBrush, radius = tailRadius, center = p0Tail)
        drawCircle(color = Color.White.copy(alpha = 0.6f), radius = tailRadius * 0.32f, center = Offset(p0Tail.x - tailRadius * 0.3f, p0Tail.y - tailRadius * 0.3f))

        // Segment 1 (Body)
        val body1Brush = Brush.radialGradient(
            colors = listOf(primaryColor, secondaryColor),
            center = Offset(p1Body1.x - bodyRadius * 0.3f, p1Body1.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = body1Brush, radius = bodyRadius, center = p1Body1)
        drawCircle(color = Color.White.copy(alpha = 0.6f), radius = bodyRadius * 0.32f, center = Offset(p1Body1.x - bodyRadius * 0.3f, p1Body1.y - bodyRadius * 0.3f))

        // Segment 2 (Body)
        val body2Brush = Brush.radialGradient(
            colors = listOf(secondaryColor, primaryColor),
            center = Offset(p2Body2.x - bodyRadius * 0.3f, p2Body2.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = body2Brush, radius = bodyRadius, center = p2Body2)
        drawCircle(color = Color.White.copy(alpha = 0.6f), radius = bodyRadius * 0.32f, center = Offset(p2Body2.x - bodyRadius * 0.3f, p2Body2.y - bodyRadius * 0.3f))

        // Segment 3 (Neck)
        val neckBrush = Brush.radialGradient(
            colors = listOf(primaryColor, secondaryColor),
            center = Offset(p3Neck.x - bodyRadius * 0.3f, p3Neck.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = neckBrush, radius = bodyRadius, center = p3Neck)
        drawCircle(color = Color.White.copy(alpha = 0.6f), radius = bodyRadius * 0.32f, center = Offset(p3Neck.x - bodyRadius * 0.3f, p3Neck.y - bodyRadius * 0.3f))

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

