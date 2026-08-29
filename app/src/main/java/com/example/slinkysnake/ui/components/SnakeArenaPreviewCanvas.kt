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
 * Premium arcade arena preview rendering the vibrant stadium grid background,
 * glowing neon arena perimeter, glossy slithering snake body, expressive snake hero head,
 * and a radiant fruit target.
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

        // 1. Draw Checkered Arcade Grid
        val columns = 10
        val cellWidth = w / columns
        val rows = (h / cellWidth).toInt() + 1

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
            }
        }

        // 2. Soft Ambient Lighting / Vignette
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color(0x60000000)),
                center = Offset(w * 0.5f, h * 0.5f),
                radius = w * 0.65f
            ),
            size = size
        )

        // Subtle neon arena border
        drawRect(
            brush = Brush.horizontalGradient(
                listOf(Color(0xFF10B981).copy(alpha = 0.4f), Color(0xFF38BDF8).copy(alpha = 0.4f))
            ),
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 3f)
        )

        // 3. Draw Radiant Golden / Red Food Item (🍎)
        val foodX = w * 0.80f
        val foodY = h * 0.50f

        // Food glowing aura
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x80EF4444), Color.Transparent),
                center = Offset(foodX, foodY),
                radius = h * 0.30f
            ),
            radius = h * 0.30f,
            center = Offset(foodX, foodY)
        )

        val emojiPaint = Paint().apply {
            textSize = h * 0.34f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val metrics = emojiPaint.fontMetrics
        val baseline = foodY - (metrics.ascent + metrics.descent) / 2f
        drawContext.canvas.nativeCanvas.drawText("🍎", foodX, baseline, emojiPaint)

        // 4. Snake Setup
        val primaryColor = Color(skin.primaryColor)
        val secondaryColor = Color(skin.secondaryColor)

        val headRadius = (h * 0.26f).coerceIn(24f, 40f)
        val bodyRadius = headRadius * 0.76f
        val tailRadius = headRadius * 0.58f

        // Natural curved slither path
        val p0Tail = Offset(w * 0.14f, h * 0.56f)
        val p1Body1 = Offset(w * 0.25f, h * 0.44f)
        val p2Body2 = Offset(w * 0.37f, h * 0.54f)
        val p3Neck = Offset(w * 0.49f, h * 0.46f)
        val headCenter = Offset(w * 0.62f, h * 0.50f)

        // Drop shadows under snake
        val shadowAlpha = 0x40000000
        drawCircle(color = Color(shadowAlpha), radius = tailRadius * 1.1f, center = Offset(p0Tail.x, p0Tail.y + 4f))
        drawCircle(color = Color(shadowAlpha), radius = bodyRadius * 1.1f, center = Offset(p1Body1.x, p1Body1.y + 4f))
        drawCircle(color = Color(shadowAlpha), radius = bodyRadius * 1.1f, center = Offset(p2Body2.x, p2Body2.y + 4f))
        drawCircle(color = Color(shadowAlpha), radius = bodyRadius * 1.1f, center = Offset(p3Neck.x, p3Neck.y + 4f))
        drawCircle(color = Color(0x50000000), radius = headRadius * 1.15f, center = Offset(headCenter.x, headCenter.y + 5f))

        // Segment 0 (Tail)
        val tailBrush = Brush.radialGradient(
            colors = listOf(secondaryColor, primaryColor),
            center = Offset(p0Tail.x - tailRadius * 0.3f, p0Tail.y - tailRadius * 0.3f),
            radius = tailRadius * 1.3f
        )
        drawCircle(brush = tailBrush, radius = tailRadius, center = p0Tail)
        drawCircle(color = Color.White.copy(alpha = 0.55f), radius = tailRadius * 0.32f, center = Offset(p0Tail.x - tailRadius * 0.3f, p0Tail.y - tailRadius * 0.3f))

        // Segment 1 (Body)
        val body1Brush = Brush.radialGradient(
            colors = listOf(primaryColor, secondaryColor),
            center = Offset(p1Body1.x - bodyRadius * 0.3f, p1Body1.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = body1Brush, radius = bodyRadius, center = p1Body1)
        drawCircle(color = Color.White.copy(alpha = 0.55f), radius = bodyRadius * 0.32f, center = Offset(p1Body1.x - bodyRadius * 0.3f, p1Body1.y - bodyRadius * 0.3f))

        // Segment 2 (Body)
        val body2Brush = Brush.radialGradient(
            colors = listOf(secondaryColor, primaryColor),
            center = Offset(p2Body2.x - bodyRadius * 0.3f, p2Body2.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = body2Brush, radius = bodyRadius, center = p2Body2)
        drawCircle(color = Color.White.copy(alpha = 0.55f), radius = bodyRadius * 0.32f, center = Offset(p2Body2.x - bodyRadius * 0.3f, p2Body2.y - bodyRadius * 0.3f))

        // Segment 3 (Neck)
        val neckBrush = Brush.radialGradient(
            colors = listOf(primaryColor, secondaryColor),
            center = Offset(p3Neck.x - bodyRadius * 0.3f, p3Neck.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = neckBrush, radius = bodyRadius, center = p3Neck)
        drawCircle(color = Color.White.copy(alpha = 0.55f), radius = bodyRadius * 0.32f, center = Offset(p3Neck.x - bodyRadius * 0.3f, p3Neck.y - bodyRadius * 0.3f))

        // Snake Hero Head
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
