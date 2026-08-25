package com.example.slinkysnake.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
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
import kotlin.math.sin

@Composable
fun SnakeHeadCanvas(
    skin: Skin,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    mouthOpen: Boolean = false
) {
    Canvas(
        modifier = modifier.size(size)
    ) {
        val w = this.size.width
        val h = this.size.height
        val centerX = w / 2f
        val centerY = h / 2f
        val radius = w * 0.38f

        drawRenderedSnakeHead(
            skin = skin,
            centerX = centerX,
            centerY = centerY,
            headRadius = radius,
            mouthOpen = mouthOpen,
            tongueFlick = true
        )
    }
}

/**
 * Highly polished, 3D-shaded, cute cartoon snake head renderer
 */
fun DrawScope.drawRenderedSnakeHead(
    skin: Skin,
    centerX: Float,
    centerY: Float,
    headRadius: Float,
    mouthOpen: Boolean = false,
    tongueFlick: Boolean = true
) {
    val scale = headRadius / 24f
    val primary = Color(skin.primaryColor)
    val secondary = Color(skin.secondaryColor)
    val eyeColor = Color(skin.eyeColor)

    // 0. Soft Drop Shadow
    drawCircle(
        color = Color(0x35000000),
        radius = headRadius * 1.05f,
        center = Offset(centerX, centerY + headRadius * 0.12f)
    )

    // 1. Animated Forked Tongue with realistic fluttering
    val time = System.currentTimeMillis()
    // Real snake tongue flick cycle: fast darting out & in with flutter vibration
    val cyclePeriod = 1100.0 // ms
    val phase = (time % cyclePeriod) / cyclePeriod
    val isFlicking = phase < 0.65 // flicking for 65% of the cycle

    val tongueExtension = if (tongueFlick && isFlicking) {
        val flickProgress = (phase / 0.65).toFloat()
        // Smooth sine arch for extending out and in
        (sin(flickProgress * Math.PI).toFloat() * 10f * scale).coerceAtLeast(0f)
    } else if (mouthOpen) {
        4f * scale
    } else {
        1.5f * scale
    }

    // High frequency flutter vibration when extended
    val flutterX = if (tongueFlick && isFlicking) (sin(time / 28.0).toFloat() * 1.6f * scale) else 0f
    val flutterTip = if (tongueFlick && isFlicking) (sin(time / 20.0).toFloat() * 1.8f * scale) else 0f

    val tongueBaseY = centerY + headRadius * 0.72f
    val tongueTipY = tongueBaseY + (9f * scale) + tongueExtension

    val forkWidth = (5f * scale) + flutterTip
    val forkLen = (5.5f * scale)

    val tonguePath = Path().apply {
        moveTo(centerX, tongueBaseY)
        // Main tongue stem with slight wiggle
        lineTo(centerX + flutterX, tongueTipY)
        // Left fork prong
        lineTo(centerX + flutterX - forkWidth, tongueTipY + forkLen)
        moveTo(centerX + flutterX, tongueTipY)
        // Right fork prong
        lineTo(centerX + flutterX + forkWidth, tongueTipY + forkLen)
    }
    // Tongue drop shadow
    drawPath(
        path = tonguePath,
        color = Color(0x55000000),
        style = Stroke(width = 3.6f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    // Tongue vibrant snake red
    drawPath(
        path = tonguePath,
        color = Color(0xFFF43F5E), // Vibrant Rose Red
        style = Stroke(width = 2.8f * scale, cap = StrokeCap.Round, join = StrokeJoin.Round)
    )
    // Glossy center highlight on tongue
    val highlightPath = Path().apply {
        moveTo(centerX, tongueBaseY)
        lineTo(centerX + flutterX * 0.5f, tongueTipY - 2f * scale)
    }
    drawPath(
        path = highlightPath,
        color = Color(0xFFFFB4C8),
        style = Stroke(width = 1.2f * scale, cap = StrokeCap.Round)
    )

    // 2. Head Base with 3D Radial Gradient Shading
    val headBrush = if (skin.pattern == Pattern.GLOW) {
        Brush.radialGradient(
            colors = listOf(
                primary.copy(alpha = 1f),
                secondary.copy(alpha = 0.95f),
                primary.copy(alpha = 0.6f)
            ),
            center = Offset(centerX - headRadius * 0.2f, centerY - headRadius * 0.25f),
            radius = headRadius * 1.4f
        )
    } else {
        Brush.radialGradient(
            colors = listOf(
                primary,
                primary,
                secondary
            ),
            center = Offset(centerX - headRadius * 0.25f, centerY - headRadius * 0.3f),
            radius = headRadius * 1.25f
        )
    }

    drawCircle(
        brush = headBrush,
        radius = headRadius,
        center = Offset(centerX, centerY)
    )

    // Outer subtle glow for glow pattern
    if (skin.pattern == Pattern.GLOW) {
        drawCircle(
            color = primary.copy(alpha = 0.3f),
            radius = headRadius * 1.2f,
            center = Offset(centerX, centerY),
            style = Stroke(width = 3f * scale)
        )
    }

    // 3. Patterns (Stripes / Spots) Clipped inside Head
    val headClip = Path().apply {
        addOval(Rect(center = Offset(centerX, centerY), radius = headRadius - 0.5f))
    }
    clipPath(headClip) {
        if (skin.pattern == Pattern.STRIPES) {
            // Cute arched stripes
            for (offsetY in listOf(-0.35f, 0.05f, 0.45f)) {
                val stripeY = centerY + headRadius * offsetY
                val stripePath = Path().apply {
                    moveTo(centerX - headRadius * 1.1f, stripeY - 3f * scale)
                    quadraticTo(centerX, stripeY + 6f * scale, centerX + headRadius * 1.1f, stripeY - 3f * scale)
                }
                drawPath(
                    path = stripePath,
                    color = secondary.copy(alpha = 0.85f),
                    style = Stroke(width = 4.5f * scale, cap = StrokeCap.Round)
                )
            }
        } else if (skin.pattern == Pattern.SPOTS) {
            // Cute decorative spots
            val spotColor = secondary.copy(alpha = 0.85f)
            drawCircle(color = spotColor, radius = headRadius * 0.2f, center = Offset(centerX - headRadius * 0.45f, centerY - headRadius * 0.2f))
            drawCircle(color = spotColor, radius = headRadius * 0.2f, center = Offset(centerX + headRadius * 0.45f, centerY - headRadius * 0.2f))
            drawCircle(color = spotColor, radius = headRadius * 0.22f, center = Offset(centerX, centerY + headRadius * 0.35f))
            drawCircle(color = spotColor, radius = headRadius * 0.12f, center = Offset(centerX - headRadius * 0.55f, centerY + headRadius * 0.3f))
            drawCircle(color = spotColor, radius = headRadius * 0.12f, center = Offset(centerX + headRadius * 0.55f, centerY + headRadius * 0.3f))
        }

        // 3D Specular Highlight Gloss Arc (Top Left)
        val glossBrush = Brush.linearGradient(
            colors = listOf(Color.White.copy(alpha = 0.55f), Color.White.copy(alpha = 0.0f)),
            start = Offset(centerX - headRadius * 0.6f, centerY - headRadius * 0.7f),
            end = Offset(centerX - headRadius * 0.1f, centerY - headRadius * 0.1f)
        )
        drawCircle(
            brush = glossBrush,
            radius = headRadius * 0.45f,
            center = Offset(centerX - headRadius * 0.32f, centerY - headRadius * 0.35f)
        )
    }

    // 4. Head Crisp Cartoon Outline
    drawCircle(
        color = Color(0xFF0F172A),
        radius = headRadius,
        center = Offset(centerX, centerY),
        style = Stroke(width = 2.4f * scale)
    )

    // 5. Cute Pink Blush Cheeks
    val blushColor = Color(0xFFFF6584).copy(alpha = 0.45f)
    drawCircle(
        color = blushColor,
        radius = headRadius * 0.24f,
        center = Offset(centerX - headRadius * 0.58f, centerY + headRadius * 0.18f)
    )
    drawCircle(
        color = blushColor,
        radius = headRadius * 0.24f,
        center = Offset(centerX + headRadius * 0.58f, centerY + headRadius * 0.18f)
    )

    // 6. Cute Cartoon Eyes
    val eyeOffsetX = headRadius * 0.42f
    val eyeOffsetY = centerY - headRadius * 0.2f
    val eyeRadius = headRadius * 0.32f

    // Left & Right Eyes
    for (sign in listOf(-1f, 1f)) {
        val eyeCenter = Offset(centerX + sign * eyeOffsetX, eyeOffsetY)

        // Sclera (White base)
        drawCircle(
            color = Color.White,
            radius = eyeRadius,
            center = eyeCenter
        )
        // Eye Dark Outline
        drawCircle(
            color = Color(0xFF0F172A),
            radius = eyeRadius,
            center = eyeCenter,
            style = Stroke(width = 2f * scale)
        )

        // Iris
        val irisColor = if (skin.eyeColor == 0xFFFFFFFF) Color(0xFF0284C7) else eyeColor
        val irisCenter = Offset(eyeCenter.x, eyeCenter.y + 1f * scale)
        drawCircle(
            color = irisColor,
            radius = eyeRadius * 0.65f,
            center = irisCenter
        )

        // Pupil
        drawCircle(
            color = Color(0xFF0F172A),
            radius = eyeRadius * 0.42f,
            center = irisCenter
        )

        // Sparkle Catchlights (Big + Small)
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.24f,
            center = Offset(irisCenter.x - eyeRadius * 0.2f, irisCenter.y - eyeRadius * 0.2f)
        )
        drawCircle(
            color = Color.White,
            radius = eyeRadius * 0.12f,
            center = Offset(irisCenter.x + eyeRadius * 0.2f, irisCenter.y + eyeRadius * 0.18f)
        )
    }

    // 7. Cute Mouth
    if (mouthOpen) {
        // Wide open happy mouth with pink tongue
        val mouthPath = Path().apply {
            moveTo(centerX - 7f * scale, centerY + headRadius * 0.35f)
            quadraticTo(
                centerX,
                centerY + headRadius * 0.75f,
                centerX + 7f * scale,
                centerY + headRadius * 0.35f
            )
            close()
        }
        drawPath(path = mouthPath, color = Color(0xFF881337))
        drawPath(path = mouthPath, color = Color(0xFF0F172A), style = Stroke(width = 1.8f * scale))
        // Inner tongue
        drawCircle(
            color = Color(0xFFFB7185),
            radius = 3.5f * scale,
            center = Offset(centerX, centerY + headRadius * 0.52f)
        )
    } else {
        // Happy cute smile
        val smilePath = Path().apply {
            moveTo(centerX - 6f * scale, centerY + headRadius * 0.38f)
            quadraticTo(
                centerX,
                centerY + headRadius * 0.55f,
                centerX + 6f * scale,
                centerY + headRadius * 0.38f
            )
        }
        drawPath(
            path = smilePath,
            color = Color(0xFF0F172A),
            style = Stroke(width = 2.2f * scale, cap = StrokeCap.Round)
        )
    }

    // 8. Accessories
    when (skin.accessory) {
        Accessory.CROWN -> {
            val crownY = centerY - headRadius * 0.82f
            val crownPath = Path().apply {
                moveTo(centerX - 16f * scale, crownY)
                lineTo(centerX - 19f * scale, crownY - 14f * scale)
                lineTo(centerX - 8f * scale, crownY - 7f * scale)
                lineTo(centerX, crownY - 18f * scale)
                lineTo(centerX + 8f * scale, crownY - 7f * scale)
                lineTo(centerX + 19f * scale, crownY - 14f * scale)
                lineTo(centerX + 16f * scale, crownY)
                close()
            }
            // Crown Gold Gradient
            val crownBrush = Brush.verticalGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFEAB308), Color(0xFFCA8A04)),
                startY = crownY - 18f * scale,
                endY = crownY
            )
            drawPath(path = crownPath, brush = crownBrush)
            drawPath(
                path = crownPath,
                color = Color(0xFF78350F),
                style = Stroke(width = 1.8f * scale, join = StrokeJoin.Round)
            )

            // Sparkling Jewels
            drawCircle(color = Color(0xFFEF4444), radius = 2.5f * scale, center = Offset(centerX - 19f * scale, crownY - 14f * scale))
            drawCircle(color = Color(0xFF38BDF8), radius = 3.2f * scale, center = Offset(centerX, crownY - 18f * scale))
            drawCircle(color = Color(0xFFEF4444), radius = 2.5f * scale, center = Offset(centerX + 19f * scale, crownY - 14f * scale))
            drawCircle(color = Color(0xFF10B981), radius = 2f * scale, center = Offset(centerX, crownY - 6f * scale))
        }
        Accessory.SUNGLASSES -> {
            val shadesY = eyeOffsetY - 2f * scale
            val lensW = 16f * scale
            val lensH = 12f * scale

            // Left Lens
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(centerX - eyeOffsetX - lensW / 2f, shadesY - lensH / 2f),
                size = Size(lensW, lensH),
                cornerRadius = CornerRadius(4f * scale, 4f * scale)
            )
            // Right Lens
            drawRoundRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(centerX + eyeOffsetX - lensW / 2f, shadesY - lensH / 2f),
                size = Size(lensW, lensH),
                cornerRadius = CornerRadius(4f * scale, 4f * scale)
            )
            // Bridge
            drawRect(
                color = Color(0xFF0F172A),
                topLeft = Offset(centerX - 5f * scale, shadesY - 2.5f * scale),
                size = Size(10f * scale, 4f * scale)
            )
            // Sleek Reflective Glare Stripes
            drawLine(
                color = Color(0xFF38BDF8).copy(alpha = 0.85f),
                start = Offset(centerX - eyeOffsetX - 4f * scale, shadesY - 3f * scale),
                end = Offset(centerX - eyeOffsetX + 3f * scale, shadesY + 4f * scale),
                strokeWidth = 2.2f * scale,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color(0xFF38BDF8).copy(alpha = 0.85f),
                start = Offset(centerX + eyeOffsetX - 4f * scale, shadesY - 3f * scale),
                end = Offset(centerX + eyeOffsetX + 3f * scale, shadesY + 4f * scale),
                strokeWidth = 2.2f * scale,
                cap = StrokeCap.Round
            )
        }
        Accessory.BANDANA -> {
            val bandanaY = centerY - headRadius * 0.45f
            val bandanaPath = Path().apply {
                moveTo(centerX - headRadius * 0.95f, bandanaY + 5f * scale)
                quadraticTo(centerX, bandanaY - 3f * scale, centerX + headRadius * 0.95f, bandanaY + 5f * scale)
                lineTo(centerX + headRadius * 0.9f, bandanaY - 5f * scale)
                quadraticTo(centerX, bandanaY - 12f * scale, centerX - headRadius * 0.9f, bandanaY - 5f * scale)
                close()
            }
            drawPath(path = bandanaPath, color = Color(0xFFDC2626))
            drawPath(path = bandanaPath, color = Color(0xFF7F1D1D), style = Stroke(width = 1.8f * scale))

            // Trailing Knot Ribbons
            val knotPath = Path().apply {
                moveTo(centerX - headRadius * 0.85f, bandanaY)
                lineTo(centerX - headRadius * 1.35f, bandanaY - 8f * scale)
                lineTo(centerX - headRadius * 1.15f, bandanaY + 4f * scale)
                close()
                moveTo(centerX - headRadius * 0.85f, bandanaY)
                lineTo(centerX - headRadius * 1.4f, bandanaY + 2f * scale)
                lineTo(centerX - headRadius * 1.05f, bandanaY + 12f * scale)
                close()
            }
            drawPath(path = knotPath, color = Color(0xFFDC2626))
            drawPath(path = knotPath, color = Color(0xFF7F1D1D), style = Stroke(width = 1.5f * scale, join = StrokeJoin.Round))
        }
        Accessory.MUSTACHE -> {
            val mustY = centerY + headRadius * 0.35f
            val mustachePath = Path().apply {
                moveTo(centerX, mustY)
                cubicTo(centerX - 8f * scale, mustY - 6f * scale, centerX - 18f * scale, mustY - 2f * scale, centerX - 20f * scale, mustY + 5f * scale)
                cubicTo(centerX - 16f * scale, mustY + 7f * scale, centerX - 10f * scale, mustY + 2f * scale, centerX, mustY + 3f * scale)
                cubicTo(centerX + 10f * scale, mustY + 2f * scale, centerX + 16f * scale, mustY + 7f * scale, centerX + 20f * scale, mustY + 5f * scale)
                cubicTo(centerX + 18f * scale, mustY - 2f * scale, centerX + 8f * scale, mustY - 6f * scale, centerX, mustY)
                close()
            }
            drawPath(path = mustachePath, color = Color(0xFF1E1B4B))
            drawPath(path = mustachePath, color = Color(0xFF0F172A), style = Stroke(width = 1.5f * scale, join = StrokeJoin.Round))
        }
        Accessory.NONE -> {}
    }
}
