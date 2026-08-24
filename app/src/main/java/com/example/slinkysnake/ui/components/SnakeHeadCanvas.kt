package com.example.slinkysnake.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.slinkysnake.model.Accessory
import com.example.slinkysnake.model.Pattern
import com.example.slinkysnake.model.Skin

@Composable
fun SnakeHeadCanvas(
    skin: Skin,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp,
    mouthOpen: Boolean = false
) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        val w = this.size.width
        val h = this.size.height
        val scale = w / 64f

        drawSnakeHead(skin, w, h, scale, mouthOpen)
    }
}

fun DrawScope.drawSnakeHead(
    skin: Skin,
    w: Float,
    h: Float,
    scale: Float,
    mouthOpen: Boolean = false
) {
    val primary = Color(skin.primaryColor)
    val secondary = Color(skin.secondaryColor)
    val eyeColor = Color(skin.eyeColor)

    val centerX = 32f * scale
    val centerY = 32f * scale
    val headRadius = 20f * scale

    // 1. Tongue
    val tonguePath = Path().apply {
        moveTo(32f * scale, 48f * scale)
        lineTo(32f * scale, 55f * scale)
        moveTo(32f * scale, 55f * scale)
        lineTo(28f * scale, 60f * scale)
        moveTo(32f * scale, 55f * scale)
        lineTo(36f * scale, 60f * scale)
    }
    drawPath(
        path = tonguePath,
        color = Color(0xFFF43F5E),
        style = Stroke(width = 3.5f * scale, cap = StrokeCap.Round)
    )

    // 2. Head Circle Path for clipping
    val headCirclePath = Path().apply {
        addOval(
            androidx.compose.ui.geometry.Rect(
                center = Offset(centerX, centerY),
                radius = headRadius
            )
        )
    }

    // 3. Fill head
    if (skin.pattern == Pattern.GLOW) {
        val brush = Brush.radialGradient(
            colors = listOf(primary, secondary),
            center = Offset(centerX, centerY * 0.9f),
            radius = headRadius * 1.3f
        )
        drawCircle(brush = brush, radius = headRadius, center = Offset(centerX, centerY))
    } else {
        drawCircle(color = primary, radius = headRadius, center = Offset(centerX, centerY))
    }

    // 4. Draw patterns clipped inside head
    clipPath(headCirclePath) {
        if (skin.pattern == Pattern.STRIPES) {
            val stripePath = Path().apply {
                moveTo(10f * scale, 24f * scale)
                cubicTo(18f * scale, 20f * scale, 24f * scale, 24f * scale, 32f * scale, 20f * scale)
                cubicTo(40f * scale, 16f * scale, 46f * scale, 20f * scale, 54f * scale, 24f * scale)

                moveTo(10f * scale, 34f * scale)
                cubicTo(18f * scale, 30f * scale, 24f * scale, 34f * scale, 32f * scale, 30f * scale)
                cubicTo(40f * scale, 26f * scale, 46f * scale, 30f * scale, 54f * scale, 34f * scale)

                moveTo(10f * scale, 44f * scale)
                cubicTo(18f * scale, 40f * scale, 24f * scale, 44f * scale, 32f * scale, 40f * scale)
                cubicTo(40f * scale, 36f * scale, 46f * scale, 40f * scale, 54f * scale, 44f * scale)
            }
            drawPath(
                path = stripePath,
                color = secondary.copy(alpha = 0.85f),
                style = Stroke(width = 4f * scale)
            )
        } else if (skin.pattern == Pattern.SPOTS) {
            drawCircle(color = secondary.copy(alpha = 0.9f), radius = 3.5f * scale, center = Offset(20f * scale, 24f * scale))
            drawCircle(color = secondary.copy(alpha = 0.9f), radius = 3.5f * scale, center = Offset(44f * scale, 24f * scale))
            drawCircle(color = secondary.copy(alpha = 0.9f), radius = 4.5f * scale, center = Offset(32f * scale, 40f * scale))
            drawCircle(color = secondary.copy(alpha = 0.9f), radius = 2.5f * scale, center = Offset(16f * scale, 36f * scale))
            drawCircle(color = secondary.copy(alpha = 0.9f), radius = 2.5f * scale, center = Offset(48f * scale, 36f * scale))
        }
    }

    // Head Outline
    drawCircle(
        color = Color(0xFF0F172A),
        radius = headRadius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 2.5f * scale)
    )

    // 5. Eyes
    // Left Eye
    drawCircle(color = eyeColor, radius = 6f * scale, center = Offset(21f * scale, 26f * scale))
    drawCircle(color = Color(0xFF0F172A), radius = 6f * scale, center = Offset(21f * scale, 26f * scale), style = Stroke(width = 1.8f * scale))
    drawCircle(color = Color.Black, radius = 2.5f * scale, center = Offset(21f * scale, 26f * scale))
    drawCircle(color = Color.White, radius = 1.2f * scale, center = Offset(19.5f * scale, 24.5f * scale))

    // Right Eye
    drawCircle(color = eyeColor, radius = 6f * scale, center = Offset(43f * scale, 26f * scale))
    drawCircle(color = Color(0xFF0F172A), radius = 6f * scale, center = Offset(43f * scale, 26f * scale), style = Stroke(width = 1.8f * scale))
    drawCircle(color = Color.Black, radius = 2.5f * scale, center = Offset(43f * scale, 26f * scale))
    drawCircle(color = Color.White, radius = 1.2f * scale, center = Offset(41.5f * scale, 24.5f * scale))

    // Mouth / Cheerful Smile
    if (mouthOpen) {
        drawCircle(color = Color(0xFF7F1D1D), radius = 4f * scale, center = Offset(32f * scale, 42f * scale))
    }

    // 6. Accessories
    when (skin.accessory) {
        Accessory.CROWN -> {
            val crownPath = Path().apply {
                moveTo(18f * scale, 16f * scale)
                lineTo(14f * scale, 6f * scale)
                lineTo(23f * scale, 11f * scale)
                lineTo(32f * scale, 2f * scale)
                lineTo(41f * scale, 11f * scale)
                lineTo(50f * scale, 6f * scale)
                lineTo(46f * scale, 16f * scale)
                close()
            }
            drawPath(path = crownPath, color = Color(0xFFFACC15))
            drawPath(path = crownPath, color = Color(0xFF92400E), style = Stroke(width = 1.5f * scale, join = StrokeJoin.Round))

            // Jewels
            drawCircle(color = Color(0xFFEF4444), radius = 1.8f * scale, center = Offset(14f * scale, 6f * scale))
            drawCircle(color = Color(0xFF3B82F6), radius = 2f * scale, center = Offset(32f * scale, 2f * scale))
            drawCircle(color = Color(0xFFEF4444), radius = 1.8f * scale, center = Offset(50f * scale, 6f * scale))
            drawCircle(color = Color(0xFF10B981), radius = 1.5f * scale, center = Offset(32f * scale, 11f * scale))
        }
        Accessory.SUNGLASSES -> {
            // Left Lens
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(12f * scale, 21f * scale),
                size = Size(16f * scale, 10f * scale),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale)
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(12f * scale, 21f * scale),
                size = Size(16f * scale, 10f * scale),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale),
                style = Stroke(width = 1f * scale)
            )
            // Right Lens
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(36f * scale, 21f * scale),
                size = Size(16f * scale, 10f * scale),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale)
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(36f * scale, 21f * scale),
                size = Size(16f * scale, 10f * scale),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(4f * scale),
                style = Stroke(width = 1f * scale)
            )
            // Bridge
            drawRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(27f * scale, 24f * scale),
                size = Size(10f * scale, 2.5f * scale)
            )
            // Glints
            drawLine(
                color = Color.White.copy(alpha = 0.6f),
                start = Offset(15f * scale, 24f * scale),
                end = Offset(21f * scale, 28f * scale),
                strokeWidth = 1.2f * scale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.White.copy(alpha = 0.6f),
                start = Offset(39f * scale, 24f * scale),
                end = Offset(45f * scale, 28f * scale),
                strokeWidth = 1.2f * scale,
                cap = StrokeCap.Round
            )
        }
        Accessory.BANDANA -> {
            val bandanaPath = Path().apply {
                moveTo(14f * scale, 20f * scale)
                quadraticTo(32f * scale, 10f * scale, 50f * scale, 20f * scale)
                lineTo(48f * scale, 13f * scale)
                quadraticTo(32f * scale, 6f * scale, 16f * scale, 13f * scale)
                close()
            }
            drawPath(path = bandanaPath, color = Color(0xFFEF4444))
            drawPath(path = bandanaPath, color = Color(0xFF991B1B), style = Stroke(width = 1.5f * scale))

            // Knots
            val knotPath = Path().apply {
                moveTo(14f * scale, 17f * scale)
                lineTo(4f * scale, 11f * scale)
                lineTo(9f * scale, 22f * scale)
                close()
                moveTo(14f * scale, 17f * scale)
                lineTo(3f * scale, 18f * scale)
                lineTo(10f * scale, 25f * scale)
                close()
            }
            drawPath(path = knotPath, color = Color(0xFFEF4444))
            drawPath(path = knotPath, color = Color(0xFF991B1B), style = Stroke(width = 1.2f * scale, join = StrokeJoin.Round))
        }
        Accessory.MUSTACHE -> {
            val mustachePath = Path().apply {
                moveTo(32f * scale, 37f * scale)
                quadraticTo(24f * scale, 33f * scale, 18f * scale, 39f * scale)
                quadraticTo(16f * scale, 41f * scale, 18f * scale, 43f * scale)
                quadraticTo(22f * scale, 41f * scale, 27f * scale, 38f * scale)
                quadraticTo(30f * scale, 38f * scale, 32f * scale, 39f * scale)
                quadraticTo(34f * scale, 38f * scale, 37f * scale, 38f * scale)
                quadraticTo(42f * scale, 41f * scale, 46f * scale, 43f * scale)
                quadraticTo(48f * scale, 41f * scale, 46f * scale, 39f * scale)
                quadraticTo(40f * scale, 33f * scale, 32f * scale, 37f * scale)
                close()
            }
            drawPath(path = mustachePath, color = Color(0xFF78350F))
            drawPath(path = mustachePath, color = Color.Black, style = Stroke(width = 1f * scale, join = StrokeJoin.Round))
        }
        Accessory.NONE -> {}
    }
}
