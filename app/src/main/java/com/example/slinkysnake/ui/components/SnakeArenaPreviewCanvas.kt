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
 * Renders the real Board Theme background (checkered grid tiles)
 * AND the snake skin (slithering body segments + head + fruit) together!
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

        // 1. Draw Board Theme Checkered Grid
        val columns = 9
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

        // Subtle arena grid border lines
        drawRect(
            color = Color.Black.copy(alpha = 0.15f),
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 2f)
        )

        // 2. Draw Food Item (Apple 🍎) on the right
        val foodX = w * 0.78f
        val foodY = h * 0.5f
        val emojiPaint = Paint().apply {
            textSize = h * 0.32f
            textAlign = Paint.Align.CENTER
            isAntiAlias = true
        }
        val metrics = emojiPaint.fontMetrics
        val baseline = foodY - (metrics.ascent + metrics.descent) / 2f
        drawContext.canvas.nativeCanvas.drawText("🍎", foodX, baseline, emojiPaint)

        // 3. Draw Snake Body Segments & Head (Left to Center)
        val primaryColor = Color(skin.primaryColor)
        val secondaryColor = Color(skin.secondaryColor)

        val headRadius = h * 0.28f
        val bodyRadius = headRadius * 0.72f
        val tailRadius = headRadius * 0.55f

        // Segment positions curving gently
        val seg0Tail = Offset(w * 0.18f, h * 0.55f)
        val seg1Body = Offset(w * 0.30f, h * 0.45f)
        val seg2Neck = Offset(w * 0.42f, h * 0.52f)
        val headCenter = Offset(w * 0.56f, h * 0.48f)

        // Drop shadow under snake
        drawCircle(color = Color(0x30000000), radius = tailRadius * 1.1f, center = Offset(seg0Tail.x, seg0Tail.y + 4f))
        drawCircle(color = Color(0x30000000), radius = bodyRadius * 1.1f, center = Offset(seg1Body.x, seg1Body.y + 4f))
        drawCircle(color = Color(0x30000000), radius = bodyRadius * 1.1f, center = Offset(seg2Neck.x, seg2Neck.y + 4f))
        drawCircle(color = Color(0x35000000), radius = headRadius * 1.1f, center = Offset(headCenter.x, headCenter.y + 5f))

        // Tail segment
        val tailBrush = Brush.radialGradient(
            colors = listOf(secondaryColor, primaryColor),
            center = Offset(seg0Tail.x - tailRadius * 0.3f, seg0Tail.y - tailRadius * 0.3f),
            radius = tailRadius * 1.3f
        )
        drawCircle(brush = tailBrush, radius = tailRadius, center = seg0Tail)
        drawCircle(color = Color.White.copy(alpha = 0.45f), radius = tailRadius * 0.3f, center = Offset(seg0Tail.x - tailRadius * 0.3f, seg0Tail.y - tailRadius * 0.3f))

        // Body segment 1
        val bodyBrush1 = Brush.radialGradient(
            colors = listOf(primaryColor, secondaryColor),
            center = Offset(seg1Body.x - bodyRadius * 0.3f, seg1Body.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = bodyBrush1, radius = bodyRadius, center = seg1Body)
        drawCircle(color = Color.White.copy(alpha = 0.5f), radius = bodyRadius * 0.3f, center = Offset(seg1Body.x - bodyRadius * 0.3f, seg1Body.y - bodyRadius * 0.3f))

        // Body segment 2 (Neck)
        val bodyBrush2 = Brush.radialGradient(
            colors = listOf(secondaryColor, primaryColor),
            center = Offset(seg2Neck.x - bodyRadius * 0.3f, seg2Neck.y - bodyRadius * 0.3f),
            radius = bodyRadius * 1.3f
        )
        drawCircle(brush = bodyBrush2, radius = bodyRadius, center = seg2Neck)
        drawCircle(color = Color.White.copy(alpha = 0.5f), radius = bodyRadius * 0.3f, center = Offset(seg2Neck.x - bodyRadius * 0.3f, seg2Neck.y - bodyRadius * 0.3f))

        // Snake Head
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
