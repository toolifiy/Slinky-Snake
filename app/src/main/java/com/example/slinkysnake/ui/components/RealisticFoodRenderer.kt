package com.example.slinkysnake.ui.components

import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.rotate
import com.example.slinkysnake.model.Food
import kotlin.math.cos
import kotlin.math.sin

/**
 * Realistic, high-fidelity custom graphical renderer for in-game foods and power-ups.
 * Renders rich 3D shading, realistic specular highlights, leaves, stems, seeds, and glows.
 */
object RealisticFoodRenderer {

    fun drawRealisticFood(
        scope: DrawScope,
        food: Food,
        cx: Float,
        cy: Float,
        cellSize: Float,
        time: Long,
        isPaused: Boolean
    ) {
        with(scope) {
            val r = cellSize * 0.40f

            // 1. Soft Realistic Drop Shadow on Floor
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0x66000000), Color(0x22000000), Color.Transparent),
                    center = Offset(cx, cy + r * 0.85f),
                    radius = r * 0.95f
                ),
                topLeft = Offset(cx - r * 0.85f, cy + r * 0.55f),
                size = Size(r * 1.7f, r * 0.6f)
            )

            // 2. Render specific food type with realistic vector 3D artwork
            when (food.type.uppercase()) {
                "APPLE", "RED_APPLE" -> drawRealisticApple(cx, cy, r, isGreen = false)
                "GREEN_APPLE" -> drawRealisticApple(cx, cy, r, isGreen = true)
                "WATERMELON" -> drawRealisticWatermelon(cx, cy, r)
                "BANANA" -> drawRealisticBanana(cx, cy, r)
                "STRAWBERRY" -> drawRealisticStrawberry(cx, cy, r)
                "ORANGE", "LEMON" -> drawRealisticCitrus(cx, cy, r, isLemon = food.type.uppercase() == "LEMON")
                "CHERRY" -> drawRealisticCherry(cx, cy, r)
                "GRAPES_RED" -> drawRealisticGrapes(cx, cy, r)
                "CAKE" -> drawRealisticCake(cx, cy, r)
                "BURGER" -> drawRealisticBurger(cx, cy, r)
                "PIZZA" -> drawRealisticPizza(cx, cy, r)
                "PINEAPPLE" -> drawRealisticPineapple(cx, cy, r)
                "PEACH", "MANGO" -> drawRealisticPeach(cx, cy, r, isMango = food.type.uppercase() == "MANGO")
                "AVOCADO" -> drawRealisticAvocado(cx, cy, r)
                "COCONUT" -> drawRealisticCoconut(cx, cy, r)
                "DOUGHNUT" -> drawRealisticDonut(cx, cy, r)
                "COOKIE" -> drawRealisticCookie(cx, cy, r)
                "CHEESE" -> drawRealisticCheese(cx, cy, r)
                "EGG" -> drawRealisticEgg(cx, cy, r)
                else -> drawRealisticGourmetFood(food, cx, cy, r)
            }
        }
    }

    fun drawRealisticPowerUp(
        scope: DrawScope,
        powerUp: Food,
        cx: Float,
        cy: Float,
        cellSize: Float,
        time: Long,
        isPaused: Boolean
    ) {
        with(scope) {
            val r = cellSize * 0.40f
            val pulse = if (isPaused) 1.0f else 1.0f + 0.08f * sin(time / 100.0).toFloat()
            val powerColor = Color(powerUp.color)

            // 1. Radiant Energy Aura Glow
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        powerColor.copy(alpha = 0.65f),
                        powerColor.copy(alpha = 0.25f),
                        Color.Transparent
                    ),
                    center = Offset(cx, cy),
                    radius = r * 1.5f * pulse
                ),
                radius = r * 1.4f * pulse,
                center = Offset(cx, cy)
            )

            // 2. Realistic 3D Powerup Artifact
            when (powerUp.type.uppercase()) {
                "GOLDEN_STAR" -> drawRealisticStar(cx, cy, r * pulse, powerColor, time)
                "BOOSTER" -> drawRealisticPotion(cx, cy, r, powerColor)
                "POWER_SPEED" -> drawRealisticSpeedLightning(cx, cy, r * pulse, powerColor)
                "POWER_SHIELD" -> drawRealisticShield(cx, cy, r, powerColor)
                "POWER_DOUBLE" -> drawRealisticGem(cx, cy, r * pulse, powerColor)
                "POWER_SHRINK" -> drawRealisticShroom(cx, cy, r, powerColor)
                "POWER_MAGNET" -> drawRealisticMagnet(cx, cy, r, powerColor)
                "POWER_FREEZE" -> drawRealisticIceCrystal(cx, cy, r * pulse, powerColor)
                else -> drawRealisticMagicalOrb(powerUp, cx, cy, r * pulse, powerColor)
            }
        }
    }

    // ==========================================
    // REALISTIC FRUITS & FOOD ARTWORK
    // ==========================================

    private fun DrawScope.drawRealisticApple(cx: Float, cy: Float, r: Float, isGreen: Boolean) {
        val appleTop = cy - r * 0.85f
        val appleBottom = cy + r * 0.95f
        val w = r * 0.92f

        val applePath = Path().apply {
            moveTo(cx, cy - r * 0.65f)
            // Top right lobe
            cubicTo(cx + w * 0.5f, cy - r * 0.95f, cx + w * 1.15f, cy - r * 0.45f, cx + w * 1.05f, cy + r * 0.25f)
            // Bottom right taper
            cubicTo(cx + w * 0.95f, cy + r * 0.85f, cx + w * 0.45f, appleBottom, cx, cy + r * 0.78f)
            // Bottom left taper
            cubicTo(cx - w * 0.45f, appleBottom, cx - w * 0.95f, cy + r * 0.85f, cx - w * 1.05f, cy + r * 0.25f)
            // Top left lobe
            cubicTo(cx - w * 1.15f, cy - r * 0.45f, cx - w * 0.5f, cy - r * 0.95f, cx, cy - r * 0.65f)
            close()
        }

        // 3D Spherical Lighting Gradient
        val baseGradient = if (isGreen) {
            Brush.radialGradient(
                colors = listOf(
                    Color(0xFF86EFAC),
                    Color(0xFF22C55E),
                    Color(0xFF15803D),
                    Color(0xFF052E16)
                ),
                center = Offset(cx - r * 0.35f, cy - r * 0.25f),
                radius = r * 1.4f
            )
        } else {
            Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFA5A5), // Top bright shine
                    Color(0xFFEF4444), // Vibrant crimson body
                    Color(0xFFB91C1C), // Deep ruby shadow
                    Color(0xFF450A0A)  // Rim depth shadow
                ),
                center = Offset(cx - r * 0.35f, cy - r * 0.25f),
                radius = r * 1.4f
            )
        }

        drawPath(path = applePath, brush = baseGradient)

        // Top Dimple Shadow (Stem Cavity)
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0x992B0606), Color.Transparent),
                center = Offset(cx, cy - r * 0.65f),
                radius = r * 0.3f
            ),
            topLeft = Offset(cx - r * 0.3f, cy - r * 0.75f),
            size = Size(r * 0.6f, r * 0.25f)
        )

        // Realistic Curved Woody Stem
        val stemPath = Path().apply {
            moveTo(cx, cy - r * 0.62f)
            cubicTo(cx + r * 0.05f, cy - r * 0.95f, cx + r * 0.25f, cy - r * 1.15f, cx + r * 0.28f, cy - r * 1.25f)
        }
        drawPath(
            path = stemPath,
            color = Color(0xFF5D4037),
            style = Stroke(width = r * 0.18f, cap = StrokeCap.Round)
        )
        // Stem highlight
        drawPath(
            path = stemPath,
            color = Color(0xFF8D6E63),
            style = Stroke(width = r * 0.06f, cap = StrokeCap.Round)
        )

        // Realistic Fresh Green Leaf with central vein
        val leafPath = Path().apply {
            moveTo(cx + r * 0.12f, cy - r * 0.95f)
            cubicTo(cx + r * 0.45f, cy - r * 1.45f, cx + r * 0.95f, cy - r * 1.25f, cx + r * 0.95f, cy - r * 1.05f)
            cubicTo(cx + r * 0.75f, cy - r * 0.75f, cx + r * 0.35f, cy - r * 0.85f, cx + r * 0.12f, cy - r * 0.95f)
            close()
        }
        drawPath(
            path = leafPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF4ADE80), Color(0xFF16A34A), Color(0xFF15803D)),
                start = Offset(cx + r * 0.12f, cy - r * 0.95f),
                end = Offset(cx + r * 0.95f, cy - r * 1.15f)
            )
        )

        // Glossy Specular Crescent Highlight
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.75f), Color.White.copy(alpha = 0.0f)),
                center = Offset(cx - r * 0.45f, cy - r * 0.35f),
                radius = r * 0.45f
            ),
            topLeft = Offset(cx - r * 0.65f, cy - r * 0.55f),
            size = Size(r * 0.45f, r * 0.35f)
        )
    }

    private fun DrawScope.drawRealisticWatermelon(cx: Float, cy: Float, r: Float) {
        val w = r * 1.25f
        val h = r * 1.05f

        // Wedge slice path
        val slicePath = Path().apply {
            moveTo(cx, cy + h * 0.65f) // bottom tip
            lineTo(cx - w, cy - h * 0.45f)
            cubicTo(cx - w * 0.5f, cy - h * 0.85f, cx + w * 0.5f, cy - h * 0.85f, cx + w, cy - h * 0.45f)
            close()
        }

        // 1. Dark Green Striped Outer Rind
        drawPath(
            path = slicePath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF166534), Color(0xFF14532D)),
                startY = cy - h,
                endY = cy + h
            )
        )

        // 2. White/Pale Lime Inner Rind
        val innerRindPath = Path().apply {
            moveTo(cx, cy + h * 0.58f)
            lineTo(cx - w * 0.92f, cy - h * 0.40f)
            cubicTo(cx - w * 0.46f, cy - h * 0.76f, cx + w * 0.46f, cy - h * 0.76f, cx + w * 0.92f, cy - h * 0.40f)
            close()
        }
        drawPath(
            path = innerRindPath,
            color = Color(0xFFDCFCE7)
        )

        // 3. Juicy Deep Crimson Red Flesh
        val fleshPath = Path().apply {
            moveTo(cx, cy + h * 0.50f)
            lineTo(cx - w * 0.84f, cy - h * 0.35f)
            cubicTo(cx - w * 0.42f, cy - h * 0.68f, cx + w * 0.42f, cy - h * 0.68f, cx + w * 0.84f, cy - h * 0.35f)
            close()
        }
        drawPath(
            path = fleshPath,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFF87171), Color(0xFFEF4444), Color(0xFFB91C1C)),
                center = Offset(cx, cy - h * 0.1f),
                radius = r * 1.2f
            )
        )

        // 4. Black Teardrop Seeds with white shine
        val seedOffsets = listOf(
            Offset(cx - r * 0.4f, cy - r * 0.15f),
            Offset(cx + r * 0.35f, cy - r * 0.2f),
            Offset(cx, cy - r * 0.32f),
            Offset(cx - r * 0.18f, cy + r * 0.12f),
            Offset(cx + r * 0.2f, cy + r * 0.08f)
        )
        for (pos in seedOffsets) {
            drawOval(
                color = Color(0xFF18181B),
                topLeft = Offset(pos.x - r * 0.08f, pos.y - r * 0.12f),
                size = Size(r * 0.16f, r * 0.24f)
            )
            // Tiny seed specular highlight
            drawCircle(
                color = Color.White.copy(alpha = 0.8f),
                radius = r * 0.035f,
                center = Offset(pos.x - r * 0.03f, pos.y - r * 0.05f)
            )
        }

        // Top glossy flesh sheen
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.55f), Color.Transparent),
                center = Offset(cx - r * 0.3f, cy - r * 0.25f),
                radius = r * 0.4f
            ),
            topLeft = Offset(cx - r * 0.5f, cy - r * 0.35f),
            size = Size(r * 0.4f, r * 0.2f)
        )
    }

    private fun DrawScope.drawRealisticBanana(cx: Float, cy: Float, r: Float) {
        val bananaPath = Path().apply {
            moveTo(cx - r * 0.85f, cy - r * 0.7f) // Stem tip
            cubicTo(cx - r * 0.2f, cy - r * 0.2f, cx + r * 0.3f, cy + r * 0.4f, cx + r * 0.95f, cy + r * 0.35f) // Outer curve
            cubicTo(cx + r * 0.65f, cy + r * 0.85f, cx - r * 0.15f, cy + r * 0.7f, cx - r * 0.75f, cy - r * 0.35f) // Inner curve
            close()
        }

        // Rich 3D golden-yellow gradient
        drawPath(
            path = bananaPath,
            brush = Brush.linearGradient(
                colors = listOf(
                    Color(0xFFFEF08A),
                    Color(0xFFFACC15),
                    Color(0xFFEAB308),
                    Color(0xFFCA8A04)
                ),
                start = Offset(cx - r, cy - r),
                end = Offset(cx + r, cy + r)
            )
        )

        // Brown stem & blossom ends
        drawCircle(
            color = Color(0xFF713F12),
            radius = r * 0.14f,
            center = Offset(cx - r * 0.82f, cy - r * 0.65f)
        )
        drawCircle(
            color = Color(0xFF713F12),
            radius = r * 0.12f,
            center = Offset(cx + r * 0.92f, cy + r * 0.38f)
        )

        // Longitudinal ridge highlight
        val ridgePath = Path().apply {
            moveTo(cx - r * 0.65f, cy - r * 0.4f)
            cubicTo(cx - r * 0.1f, cy + r * 0.05f, cx + r * 0.35f, cy + r * 0.45f, cx + r * 0.75f, cy + r * 0.4f)
        }
        drawPath(
            path = ridgePath,
            color = Color.White.copy(alpha = 0.55f),
            style = Stroke(width = r * 0.1f, cap = StrokeCap.Round)
        )
    }

    private fun DrawScope.drawRealisticStrawberry(cx: Float, cy: Float, r: Float) {
        val berryPath = Path().apply {
            moveTo(cx, cy - r * 0.6f)
            cubicTo(cx + r * 0.95f, cy - r * 0.55f, cx + r * 0.85f, cy + r * 0.35f, cx, cy + r * 0.95f)
            cubicTo(cx - r * 0.85f, cy + r * 0.35f, cx - r * 0.95f, cy - r * 0.55f, cx, cy - r * 0.6f)
            close()
        }

        // 3D Juicy Ruby Gradient
        drawPath(
            path = berryPath,
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0xFFFFA5A5),
                    Color(0xFFEF4444),
                    Color(0xFFDC2626),
                    Color(0xFF991B1B)
                ),
                center = Offset(cx - r * 0.3f, cy - r * 0.2f),
                radius = r * 1.3f
            )
        )

        // Golden Seed Dimples
        val seeds = listOf(
            Offset(cx - r * 0.4f, cy - r * 0.2f),
            Offset(cx, cy - r * 0.3f),
            Offset(cx + r * 0.4f, cy - r * 0.2f),
            Offset(cx - r * 0.5f, cy + r * 0.1f),
            Offset(cx - r * 0.15f, cy + r * 0.05f),
            Offset(cx + r * 0.25f, cy + r * 0.1f),
            Offset(cx + r * 0.5f, cy + r * 0.15f),
            Offset(cx - r * 0.3f, cy + r * 0.4f),
            Offset(cx + r * 0.1f, cy + r * 0.45f),
            Offset(cx, cy + r * 0.7f)
        )
        for (s in seeds) {
            drawCircle(
                color = Color(0xFFFEF08A),
                radius = r * 0.06f,
                center = s
            )
            drawCircle(
                color = Color(0xFF7F1D1D),
                radius = r * 0.03f,
                center = Offset(s.x + r * 0.02f, s.y + r * 0.02f)
            )
        }

        // Green Leafy Calyx on Top
        val calyxPath = Path().apply {
            moveTo(cx, cy - r * 0.65f)
            lineTo(cx - r * 0.6f, cy - r * 0.75f)
            lineTo(cx - r * 0.25f, cy - r * 0.55f)
            lineTo(cx, cy - r * 0.85f)
            lineTo(cx + r * 0.25f, cy - r * 0.55f)
            lineTo(cx + r * 0.6f, cy - r * 0.75f)
            close()
        }
        drawPath(
            path = calyxPath,
            brush = Brush.verticalGradient(
                colors = listOf(Color(0xFF4ADE80), Color(0xFF15803D))
            )
        )

        // Gloss highlight
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.7f), Color.Transparent),
                center = Offset(cx - r * 0.35f, cy - r * 0.3f),
                radius = r * 0.35f
            ),
            topLeft = Offset(cx - r * 0.5f, cy - r * 0.45f),
            size = Size(r * 0.35f, r * 0.25f)
        )
    }

    private fun DrawScope.drawRealisticCitrus(cx: Float, cy: Float, r: Float, isLemon: Boolean) {
        val baseColors = if (isLemon) {
            listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFEAB308), Color(0xFFA16207))
        } else {
            listOf(Color(0xFFFED7AA), Color(0xFFFB923C), Color(0xFFEA580C), Color(0xFF9A3412))
        }

        // Spherical citrus body with radial lighting
        drawCircle(
            brush = Brush.radialGradient(
                colors = baseColors,
                center = Offset(cx - r * 0.35f, cy - r * 0.3f),
                radius = r * 1.3f
            ),
            radius = r * 0.88f,
            center = Offset(cx, cy)
        )

        // Green Leaf & Stem
        val leafPath = Path().apply {
            moveTo(cx, cy - r * 0.85f)
            cubicTo(cx + r * 0.3f, cy - r * 1.25f, cx + r * 0.75f, cy - r * 1.1f, cx + r * 0.7f, cy - r * 0.85f)
            cubicTo(cx + r * 0.5f, cy - r * 0.7f, cx + r * 0.2f, cy - r * 0.75f, cx, cy - r * 0.85f)
            close()
        }
        drawPath(
            path = leafPath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF4ADE80), Color(0xFF15803D))
            )
        )

        // Realistic Gloss Sheen
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.7f), Color.Transparent),
                center = Offset(cx - r * 0.35f, cy - r * 0.35f),
                radius = r * 0.4f
            ),
            topLeft = Offset(cx - r * 0.55f, cy - r * 0.55f),
            size = Size(r * 0.4f, r * 0.28f)
        )
    }

    private fun DrawScope.drawRealisticCherry(cx: Float, cy: Float, r: Float) {
        val leftX = cx - r * 0.35f
        val leftY = cy + r * 0.25f
        val rightX = cx + r * 0.38f
        val rightY = cy + r * 0.15f
        val berryR = r * 0.48f

        // Twin Stems joining at top
        val topStemX = cx + r * 0.1f
        val topStemY = cy - r * 0.95f

        val stem1 = Path().apply {
            moveTo(leftX, leftY - berryR * 0.7f)
            cubicTo(leftX + r * 0.1f, cy - r * 0.3f, topStemX - r * 0.1f, cy - r * 0.6f, topStemX, topStemY)
        }
        val stem2 = Path().apply {
            moveTo(rightX, rightY - berryR * 0.7f)
            cubicTo(rightX - r * 0.05f, cy - r * 0.3f, topStemX + r * 0.05f, cy - r * 0.6f, topStemX, topStemY)
        }
        drawPath(stem1, color = Color(0xFF4D7C0F), style = Stroke(width = r * 0.09f, cap = StrokeCap.Round))
        drawPath(stem2, color = Color(0xFF4D7C0F), style = Stroke(width = r * 0.09f, cap = StrokeCap.Round))

        // Top Leaf
        val leaf = Path().apply {
            moveTo(topStemX, topStemY)
            cubicTo(topStemX + r * 0.4f, topStemY - r * 0.35f, topStemX + r * 0.7f, topStemY - r * 0.15f, topStemX + r * 0.6f, topStemY + r * 0.05f)
            close()
        }
        drawPath(leaf, brush = Brush.linearGradient(colors = listOf(Color(0xFF4ADE80), Color(0xFF15803D))))

        // Two Shiny Ruby Cherries
        for (cherry in listOf(Offset(leftX, leftY), Offset(rightX, rightY))) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFA5A5), Color(0xFFEF4444), Color(0xFFB91C1C), Color(0xFF450A0A)),
                    center = Offset(cherry.x - berryR * 0.35f, cherry.y - berryR * 0.35f),
                    radius = berryR * 1.3f
                ),
                radius = berryR,
                center = cherry
            )
            // Specular Glint
            drawOval(
                brush = Brush.radialGradient(
                    colors = listOf(Color.White.copy(alpha = 0.85f), Color.Transparent),
                    center = Offset(cherry.x - berryR * 0.35f, cherry.y - berryR * 0.35f),
                    radius = berryR * 0.4f
                ),
                topLeft = Offset(cherry.x - berryR * 0.55f, cherry.y - berryR * 0.55f),
                size = Size(berryR * 0.4f, berryR * 0.3f)
            )
        }
    }

    private fun DrawScope.drawRealisticGrapes(cx: Float, cy: Float, r: Float) {
        // Vine stem
        drawPath(
            path = Path().apply {
                moveTo(cx, cy - r * 0.5f)
                lineTo(cx + r * 0.1f, cy - r * 0.9f)
            },
            color = Color(0xFF5D4037),
            style = Stroke(width = r * 0.12f, cap = StrokeCap.Round)
        )

        // Cluster of 3D Purple Grapes
        val grapeOffsets = listOf(
            Offset(cx - r * 0.3f, cy - r * 0.4f),
            Offset(cx + r * 0.3f, cy - r * 0.4f),
            Offset(cx, cy - r * 0.35f),
            Offset(cx - r * 0.45f, cy - r * 0.05f),
            Offset(cx, cy - r * 0.05f),
            Offset(cx + r * 0.45f, cy - r * 0.05f),
            Offset(cx - r * 0.25f, cy + r * 0.28f),
            Offset(cx + r * 0.25f, cy + r * 0.28f),
            Offset(cx, cy + r * 0.6f)
        )
        val grapeR = r * 0.26f
        for (g in grapeOffsets) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFE9D5FF), Color(0xFFA855F7), Color(0xFF7E22CE), Color(0xFF3B0764)),
                    center = Offset(g.x - grapeR * 0.35f, g.y - grapeR * 0.35f),
                    radius = grapeR * 1.3f
                ),
                radius = grapeR,
                center = g
            )
            drawCircle(
                color = Color.White.copy(alpha = 0.65f),
                radius = grapeR * 0.25f,
                center = Offset(g.x - grapeR * 0.3f, g.y - grapeR * 0.3f)
            )
        }
    }

    private fun DrawScope.drawRealisticCake(cx: Float, cy: Float, r: Float) {
        val w = r * 1.1f
        val h = r * 0.9f

        // Cake Slice Wedge 3D
        val spongePath = Path().apply {
            moveTo(cx + w * 0.8f, cy - h * 0.3f)
            lineTo(cx - w * 0.7f, cy + h * 0.1f)
            lineTo(cx - w * 0.7f, cy + h * 0.7f)
            lineTo(cx + w * 0.8f, cy + h * 0.3f)
            close()
        }
        drawPath(
            path = spongePath,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFDE047), Color(0xFFEAB308))
            )
        )

        // Cream Frosting Filling Stripe
        drawRect(
            color = Color(0xFFFFFBEB),
            topLeft = Offset(cx - w * 0.7f, cy + h * 0.25f),
            size = Size(w * 1.5f, h * 0.15f)
        )

        // Top Strawberry Pink Glaze Frosting
        val topFrosting = Path().apply {
            moveTo(cx, cy - h * 0.7f)
            lineTo(cx - w * 0.7f, cy + h * 0.1f)
            lineTo(cx + w * 0.8f, cy - h * 0.3f)
            close()
        }
        drawPath(
            path = topFrosting,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFF472B6), Color(0xFFEC4899), Color(0xFFDB2777))
            )
        )

        // Shiny Ruby Cherry on Top
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFA5A5), Color(0xFFEF4444), Color(0xFF991B1B)),
                center = Offset(cx - r * 0.05f, cy - h * 0.75f),
                radius = r * 0.25f
            ),
            radius = r * 0.22f,
            center = Offset(cx, cy - h * 0.65f)
        )
    }

    private fun DrawScope.drawRealisticBurger(cx: Float, cy: Float, r: Float) {
        val w = r * 1.2f
        val h = r * 0.95f

        // 1. Bottom Bun
        drawRoundRect(
            brush = Brush.verticalGradient(listOf(Color(0xFFFBBF24), Color(0xFFD97706))),
            topLeft = Offset(cx - w * 0.7f, cy + h * 0.25f),
            size = Size(w * 1.4f, h * 0.35f),
            cornerRadius = CornerRadius(8f, 8f)
        )

        // 2. Juicy Patty (Brown)
        drawRoundRect(
            color = Color(0xFF78350F),
            topLeft = Offset(cx - w * 0.75f, cy + h * 0.08f),
            size = Size(w * 1.5f, h * 0.22f),
            cornerRadius = CornerRadius(6f, 6f)
        )

        // 3. Melted Cheese (Golden Yellow with corner drip)
        val cheesePath = Path().apply {
            moveTo(cx - w * 0.72f, cy + h * 0.08f)
            lineTo(cx + w * 0.72f, cy + h * 0.08f)
            lineTo(cx + w * 0.5f, cy + h * 0.28f)
            lineTo(cx + w * 0.1f, cy + h * 0.08f)
            lineTo(cx - w * 0.3f, cy + h * 0.28f)
            close()
        }
        drawPath(cheesePath, color = Color(0xFFFACC15))

        // 4. Fresh Lettuce (Green Wave)
        drawRect(
            color = Color(0xFF22C55E),
            topLeft = Offset(cx - w * 0.78f, cy - h * 0.05f),
            size = Size(w * 1.56f, h * 0.14f)
        )

        // 5. Ripe Red Tomato Slice
        drawRoundRect(
            color = Color(0xFFEF4444),
            topLeft = Offset(cx - w * 0.7f, cy - h * 0.18f),
            size = Size(w * 1.4f, h * 0.15f),
            cornerRadius = CornerRadius(4f, 4f)
        )

        // 6. Top Seeded Brioche Bun (Dome)
        val topBun = Path().apply {
            moveTo(cx - w * 0.75f, cy - h * 0.15f)
            cubicTo(cx - w * 0.75f, cy - h * 0.85f, cx + w * 0.75f, cy - h * 0.85f, cx + w * 0.75f, cy - h * 0.15f)
            close()
        }
        drawPath(
            topBun,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFD97706), Color(0xFF92400E)),
                center = Offset(cx - r * 0.2f, cy - h * 0.5f),
                radius = r * 1.1f
            )
        )

        // Sesame Seeds on Top
        val sesamePositions = listOf(
            Offset(cx - r * 0.35f, cy - h * 0.45f),
            Offset(cx, cy - h * 0.55f),
            Offset(cx + r * 0.35f, cy - h * 0.42f),
            Offset(cx - r * 0.15f, cy - h * 0.32f),
            Offset(cx + r * 0.18f, cy - h * 0.32f)
        )
        for (pos in sesamePositions) {
            drawOval(
                color = Color(0xFFFFFBEB),
                topLeft = Offset(pos.x - r * 0.05f, pos.y - r * 0.03f),
                size = Size(r * 0.1f, r * 0.06f)
            )
        }
    }

    private fun DrawScope.drawRealisticPizza(cx: Float, cy: Float, r: Float) {
        val w = r * 1.1f
        val h = r * 1.0f

        val crustPath = Path().apply {
            moveTo(cx, cy + h * 0.65f)
            lineTo(cx - w * 0.85f, cy - h * 0.55f)
            cubicTo(cx - w * 0.4f, cy - h * 0.85f, cx + w * 0.4f, cy - h * 0.85f, cx + w * 0.85f, cy - h * 0.55f)
            close()
        }

        // Toasted Crust
        drawPath(
            crustPath,
            brush = Brush.verticalGradient(listOf(Color(0xFFD97706), Color(0xFF92400E)))
        )

        // Melted Mozzarella & Marinara
        val cheesePath = Path().apply {
            moveTo(cx, cy + h * 0.55f)
            lineTo(cx - w * 0.72f, cy - h * 0.45f)
            cubicTo(cx - w * 0.35f, cy - h * 0.68f, cx + w * 0.35f, cy - h * 0.68f, cx + w * 0.72f, cy - h * 0.45f)
            close()
        }
        drawPath(
            cheesePath,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFF97316)),
                center = Offset(cx, cy - h * 0.1f),
                radius = r * 1.0f
            )
        )

        // Pepperoni Slices
        val pepPositions = listOf(
            Offset(cx - r * 0.25f, cy - r * 0.2f),
            Offset(cx + r * 0.28f, cy - r * 0.15f),
            Offset(cx, cy + r * 0.18f)
        )
        for (pep in pepPositions) {
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFEF4444), Color(0xFF991B1B)),
                    center = Offset(pep.x - r * 0.05f, pep.y - r * 0.05f),
                    radius = r * 0.25f
                ),
                radius = r * 0.2f,
                center = pep
            )
        }
    }

    private fun DrawScope.drawRealisticPineapple(cx: Float, cy: Float, r: Float) {
        // Crown Leaves
        val leaves = Path().apply {
            moveTo(cx, cy - r * 0.3f)
            lineTo(cx - r * 0.5f, cy - r * 1.05f)
            lineTo(cx - r * 0.15f, cy - r * 0.65f)
            lineTo(cx, cy - r * 1.2f)
            lineTo(cx + r * 0.15f, cy - r * 0.65f)
            lineTo(cx + r * 0.5f, cy - r * 1.05f)
            close()
        }
        drawPath(leaves, brush = Brush.verticalGradient(listOf(Color(0xFF4ADE80), Color(0xFF15803D))))

        // Golden Textured Body
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFD97706), Color(0xFF78350F)),
                center = Offset(cx - r * 0.25f, cy),
                radius = r * 1.2f
            ),
            topLeft = Offset(cx - r * 0.65f, cy - r * 0.45f),
            size = Size(r * 1.3f, r * 1.4f)
        )
    }

    private fun DrawScope.drawRealisticPeach(cx: Float, cy: Float, r: Float, isMango: Boolean) {
        val colors = if (isMango) {
            listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFF97316), Color(0xFFDC2626))
        } else {
            listOf(Color(0xFFFED7AA), Color(0xFFFB923C), Color(0xFFF43F5E), Color(0xFFBE123C))
        }

        drawCircle(
            brush = Brush.radialGradient(
                colors = colors,
                center = Offset(cx - r * 0.3f, cy - r * 0.2f),
                radius = r * 1.3f
            ),
            radius = r * 0.85f,
            center = Offset(cx, cy)
        )

        // Leaf
        val leaf = Path().apply {
            moveTo(cx, cy - r * 0.8f)
            cubicTo(cx + r * 0.4f, cy - r * 1.2f, cx + r * 0.7f, cy - r * 1.0f, cx + r * 0.6f, cy - r * 0.8f)
            close()
        }
        drawPath(leaf, brush = Brush.linearGradient(listOf(Color(0xFF4ADE80), Color(0xFF15803D))))

        // Specular glow
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.7f), Color.Transparent),
                center = Offset(cx - r * 0.35f, cy - r * 0.3f),
                radius = r * 0.35f
            ),
            topLeft = Offset(cx - r * 0.5f, cy - r * 0.45f),
            size = Size(r * 0.35f, r * 0.25f)
        )
    }

    private fun DrawScope.drawRealisticAvocado(cx: Float, cy: Float, r: Float) {
        val body = Path().apply {
            moveTo(cx, cy - r * 0.85f)
            cubicTo(cx + r * 0.6f, cy - r * 0.75f, cx + r * 0.85f, cy + r * 0.3f, cx, cy + r * 0.95f)
            cubicTo(cx - r * 0.85f, cy + r * 0.3f, cx - r * 0.6f, cy - r * 0.75f, cx, cy - r * 0.85f)
            close()
        }
        // Outer dark green skin
        drawPath(body, color = Color(0xFF14532D))

        // Creamy light lime flesh
        val flesh = Path().apply {
            moveTo(cx, cy - r * 0.72f)
            cubicTo(cx + r * 0.48f, cy - r * 0.62f, cx + r * 0.7f, cy + r * 0.25f, cx, cy + r * 0.82f)
            cubicTo(cx - r * 0.7f, cy + r * 0.25f, cx - r * 0.48f, cy - r * 0.62f, cx, cy - r * 0.72f)
            close()
        }
        drawPath(
            flesh,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFA7F3D0), Color(0xFF16A34A)),
                center = Offset(cx, cy),
                radius = r * 1.0f
            )
        )

        // Large 3D Brown Pit/Seed
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFB45309), Color(0xFF78350F), Color(0xFF451A03)),
                center = Offset(cx - r * 0.12f, cy + r * 0.15f),
                radius = r * 0.5f
            ),
            radius = r * 0.38f,
            center = Offset(cx, cy + r * 0.25f)
        )
        // Pit highlight
        drawCircle(
            color = Color.White.copy(alpha = 0.65f),
            radius = r * 0.08f,
            center = Offset(cx - r * 0.12f, cy + r * 0.15f)
        )
    }

    private fun DrawScope.drawRealisticCoconut(cx: Float, cy: Float, r: Float) {
        // Brown hairy shell
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF92400E), Color(0xFF78350F), Color(0xFF451A03)),
                center = Offset(cx - r * 0.3f, cy - r * 0.3f),
                radius = r * 1.2f
            ),
            radius = r * 0.85f,
            center = Offset(cx, cy)
        )
        // 3 Indented dark eye holes
        val eyes = listOf(
            Offset(cx - r * 0.2f, cy - r * 0.25f),
            Offset(cx + r * 0.2f, cy - r * 0.25f),
            Offset(cx, cy - r * 0.05f)
        )
        for (e in eyes) {
            drawCircle(color = Color(0xFF291503), radius = r * 0.1f, center = e)
        }
    }

    private fun DrawScope.drawRealisticDonut(cx: Float, cy: Float, r: Float) {
        // Dough
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFBBF24), Color(0xFFD97706)),
                center = Offset(cx - r * 0.2f, cy - r * 0.2f),
                radius = r * 1.1f
            ),
            radius = r * 0.85f,
            center = Offset(cx, cy)
        )
        // Pink Icing
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFF472B6), Color(0xFFEC4899), Color(0xFFBE185D)),
                center = Offset(cx - r * 0.2f, cy - r * 0.2f),
                radius = r * 1.0f
            ),
            radius = r * 0.75f,
            center = Offset(cx, cy)
        )
        // Center Hole
        drawCircle(
            color = Color(0xFF0F172A),
            radius = r * 0.3f,
            center = Offset(cx, cy)
        )
        // Sprinkles
        val sprinkles = listOf(
            Offset(cx - r * 0.45f, cy - r * 0.25f) to Color(0xFF38BDF8),
            Offset(cx + r * 0.45f, cy - r * 0.2f) to Color(0xFFFACC15),
            Offset(cx - r * 0.2f, cy + r * 0.5f) to Color(0xFF4ADE80),
            Offset(cx + r * 0.35f, cy + r * 0.4f) to Color.White
        )
        for ((pos, col) in sprinkles) {
            drawRoundRect(
                color = col,
                topLeft = pos,
                size = Size(r * 0.16f, r * 0.07f),
                cornerRadius = CornerRadius(3f, 3f)
            )
        }
    }

    private fun DrawScope.drawRealisticCookie(cx: Float, cy: Float, r: Float) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFDE68A), Color(0xFFD97706), Color(0xFF92400E)),
                center = Offset(cx - r * 0.25f, cy - r * 0.25f),
                radius = r * 1.2f
            ),
            radius = r * 0.85f,
            center = Offset(cx, cy)
        )
        // Chocolate Chips
        val chips = listOf(
            Offset(cx - r * 0.35f, cy - r * 0.2f),
            Offset(cx + r * 0.25f, cy - r * 0.35f),
            Offset(cx, cy),
            Offset(cx - r * 0.28f, cy + r * 0.38f),
            Offset(cx + r * 0.35f, cy + r * 0.28f)
        )
        for (c in chips) {
            drawCircle(color = Color(0xFF451A03), radius = r * 0.12f, center = c)
        }
    }

    private fun DrawScope.drawRealisticCheese(cx: Float, cy: Float, r: Float) {
        val w = r * 1.1f
        val h = r * 0.85f
        val wedge = Path().apply {
            moveTo(cx - w * 0.7f, cy + h * 0.4f)
            lineTo(cx + w * 0.75f, cy + h * 0.4f)
            lineTo(cx - w * 0.2f, cy - h * 0.7f)
            close()
        }
        drawPath(
            wedge,
            brush = Brush.linearGradient(listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFEA580C)))
        )
        // Cheese holes
        val holes = listOf(
            Offset(cx - r * 0.2f, cy + r * 0.1f) to r * 0.14f,
            Offset(cx + r * 0.3f, cy + r * 0.2f) to r * 0.18f,
            Offset(cx + r * 0.05f, cy - r * 0.15f) to r * 0.11f
        )
        for ((hPos, hR) in holes) {
            drawCircle(color = Color(0xFFCA8A04), radius = hR, center = hPos)
        }
    }

    private fun DrawScope.drawRealisticEgg(cx: Float, cy: Float, r: Float) {
        // Fried Egg White (organic wave)
        val eggWhite = Path().apply {
            moveTo(cx - r * 0.75f, cy - r * 0.3f)
            cubicTo(cx - r * 0.8f, cy + r * 0.7f, cx + r * 0.2f, cy + r * 0.85f, cx + r * 0.85f, cy + r * 0.4f)
            cubicTo(cx + r * 0.9f, cy - r * 0.6f, cx + r * 0.1f, cy - r * 0.85f, cx - r * 0.75f, cy - r * 0.3f)
            close()
        }
        drawPath(
            eggWhite,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFFFFF), Color(0xFFF8FAFC), Color(0xFFE2E8F0)),
                center = Offset(cx, cy),
                radius = r * 1.1f
            )
        )
        // Golden Sunny Yolk
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFEA580C)),
                center = Offset(cx - r * 0.1f, cy - r * 0.1f),
                radius = r * 0.5f
            ),
            radius = r * 0.38f,
            center = Offset(cx, cy)
        )
        // Yolk Shine
        drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = r * 0.09f,
            center = Offset(cx - r * 0.12f, cy - r * 0.12f)
        )
    }

    // Generic Rich 3D Gourmet Sphere fallback
    private fun DrawScope.drawRealisticGourmetFood(food: Food, cx: Float, cy: Float, r: Float) {
        val foodCol = Color(food.color)

        // 3D Glassy/Organic Orb with rich radial illumination
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color.White.copy(alpha = 0.8f),
                    foodCol,
                    foodCol.copy(alpha = 0.85f),
                    Color(0xFF0F172A)
                ),
                center = Offset(cx - r * 0.35f, cy - r * 0.35f),
                radius = r * 1.3f
            ),
            radius = r * 0.85f,
            center = Offset(cx, cy)
        )

        // Gloss Specular Sheen
        drawOval(
            brush = Brush.radialGradient(
                colors = listOf(Color.White.copy(alpha = 0.85f), Color.Transparent),
                center = Offset(cx - r * 0.35f, cy - r * 0.35f),
                radius = r * 0.38f
            ),
            topLeft = Offset(cx - r * 0.55f, cy - r * 0.55f),
            size = Size(r * 0.42f, r * 0.28f)
        )
    }

    // ==========================================
    // REALISTIC 3D POWER-UP ARTIFACTS
    // ==========================================

    private fun DrawScope.drawRealisticStar(cx: Float, cy: Float, r: Float, col: Color, time: Long) {
        val points = 5
        val innerR = r * 0.45f
        val starPath = Path()
        val angleStep = Math.PI / points
        var angle = -Math.PI / 2.0

        for (i in 0 until points * 2) {
            val currR = if (i % 2 == 0) r else innerR
            val px = (cx + currR * cos(angle)).toFloat()
            val py = (cy + currR * sin(angle)).toFloat()
            if (i == 0) starPath.moveTo(px, py) else starPath.lineTo(px, py)
            angle += angleStep
        }
        starPath.close()

        drawPath(
            path = starPath,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFFFBEB), Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFD97706)),
                center = Offset(cx - r * 0.2f, cy - r * 0.2f),
                radius = r * 1.2f
            )
        )

        // Gold outline
        drawPath(
            path = starPath,
            color = Color(0xFFFFFFFF),
            style = Stroke(width = 1.5f)
        )
    }

    private fun DrawScope.drawRealisticPotion(cx: Float, cy: Float, r: Float, col: Color) {
        // Glass Flask Neck
        drawRoundRect(
            color = Color(0xFF94A3B8),
            topLeft = Offset(cx - r * 0.22f, cy - r * 0.9f),
            size = Size(r * 0.44f, r * 0.35f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        // Cork
        drawRoundRect(
            color = Color(0xFF78350F),
            topLeft = Offset(cx - r * 0.16f, cy - r * 1.05f),
            size = Size(r * 0.32f, r * 0.2f),
            cornerRadius = CornerRadius(3f, 3f)
        )

        // Spherical Glass Flask Body with Glowing Blue Elixir
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFBAE6FD), Color(0xFF38BDF8), Color(0xFF0284C7), Color(0xFF0C4A6E)),
                center = Offset(cx - r * 0.25f, cy + r * 0.05f),
                radius = r * 0.9f
            ),
            radius = r * 0.65f,
            center = Offset(cx, cy + r * 0.15f)
        )
        // Specular Bubble Glint
        drawCircle(
            color = Color.White.copy(alpha = 0.85f),
            radius = r * 0.16f,
            center = Offset(cx - r * 0.22f, cy)
        )
    }

    private fun DrawScope.drawRealisticSpeedLightning(cx: Float, cy: Float, r: Float, col: Color) {
        val bolt = Path().apply {
            moveTo(cx + r * 0.1f, cy - r * 0.9f)
            lineTo(cx - r * 0.6f, cy + r * 0.05f)
            lineTo(cx - r * 0.05f, cy + r * 0.05f)
            lineTo(cx - r * 0.25f, cy + r * 0.95f)
            lineTo(cx + r * 0.6f, cy - r * 0.05f)
            lineTo(cx + r * 0.05f, cy - r * 0.05f)
            close()
        }
        drawPath(
            bolt,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFFFFFFF), Color(0xFFBAE6FD), Color(0xFF38BDF8), Color(0xFF0284C7))
            )
        )
    }

    private fun DrawScope.drawRealisticShield(cx: Float, cy: Float, r: Float, col: Color) {
        val shield = Path().apply {
            moveTo(cx, cy - r * 0.85f)
            lineTo(cx + r * 0.75f, cy - r * 0.65f)
            cubicTo(cx + r * 0.75f, cy + r * 0.2f, cx + r * 0.35f, cy + r * 0.75f, cx, cy + r * 0.95f)
            cubicTo(cx - r * 0.35f, cy + r * 0.75f, cx - r * 0.75f, cy + r * 0.2f, cx - r * 0.75f, cy - r * 0.65f)
            close()
        }
        drawPath(
            shield,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFFFEF08A), Color(0xFFFACC15), Color(0xFFD97706), Color(0xFF78350F)),
                center = Offset(cx - r * 0.2f, cy - r * 0.2f),
                radius = r * 1.2f
            )
        )
        // Shield Rim
        drawPath(shield, color = Color.White, style = Stroke(width = 2f))
    }

    private fun DrawScope.drawRealisticGem(cx: Float, cy: Float, r: Float, col: Color) {
        val w = r * 0.85f
        val h = r * 0.85f
        val diamond = Path().apply {
            moveTo(cx, cy - h)
            lineTo(cx + w, cy)
            lineTo(cx, cy + h)
            lineTo(cx - w, cy)
            close()
        }
        drawPath(
            diamond,
            brush = Brush.linearGradient(
                colors = listOf(Color.White, Color(0xFFF472B6), Color(0xFFDB2777), Color(0xFF831843)),
                start = Offset(cx - w, cy - h),
                end = Offset(cx + w, cy + h)
            )
        )
        drawPath(diamond, color = Color.White, style = Stroke(width = 1.5f))
    }

    private fun DrawScope.drawRealisticShroom(cx: Float, cy: Float, r: Float, col: Color) {
        // Stem
        drawRoundRect(
            color = Color(0xFFFFFBEB),
            topLeft = Offset(cx - r * 0.22f, cy),
            size = Size(r * 0.44f, r * 0.75f),
            cornerRadius = CornerRadius(4f, 4f)
        )
        // Cap
        val cap = Path().apply {
            moveTo(cx - r * 0.85f, cy + r * 0.1f)
            cubicTo(cx - r * 0.85f, cy - r * 0.95f, cx + r * 0.85f, cy - r * 0.95f, cx + r * 0.85f, cy + r * 0.1f)
            close()
        }
        drawPath(
            cap,
            brush = Brush.radialGradient(
                colors = listOf(Color(0xFF6EE7B7), Color(0xFF10B981), Color(0xFF047857)),
                center = Offset(cx - r * 0.25f, cy - r * 0.35f),
                radius = r * 1.0f
            )
        )
        // White Spots
        drawCircle(color = Color.White, radius = r * 0.14f, center = Offset(cx - r * 0.35f, cy - r * 0.25f))
        drawCircle(color = Color.White, radius = r * 0.16f, center = Offset(cx + r * 0.3f, cy - r * 0.28f))
        drawCircle(color = Color.White, radius = r * 0.11f, center = Offset(cx, cy - r * 0.55f))
    }

    private fun DrawScope.drawRealisticMagnet(cx: Float, cy: Float, r: Float, col: Color) {
        val horseshoe = Path().apply {
            moveTo(cx - r * 0.7f, cy + r * 0.8f)
            lineTo(cx - r * 0.7f, cy - r * 0.2f)
            cubicTo(cx - r * 0.7f, cy - r * 0.9f, cx + r * 0.7f, cy - r * 0.9f, cx + r * 0.7f, cy - r * 0.2f)
            lineTo(cx + r * 0.7f, cy + r * 0.8f)
        }
        drawPath(
            horseshoe,
            brush = Brush.linearGradient(listOf(Color(0xFFEF4444), Color(0xFFB91C1C))),
            style = Stroke(width = r * 0.38f, cap = StrokeCap.Butt)
        )
        // Silver Magnetic Tips
        drawRect(color = Color(0xFFCBD5E1), topLeft = Offset(cx - r * 0.89f, cy + r * 0.45f), size = Size(r * 0.38f, r * 0.35f))
        drawRect(color = Color(0xFFCBD5E1), topLeft = Offset(cx + r * 0.51f, cy + r * 0.45f), size = Size(r * 0.38f, r * 0.35f))
    }

    private fun DrawScope.drawRealisticIceCrystal(cx: Float, cy: Float, r: Float, col: Color) {
        for (angle in 0 until 360 step 60) {
            rotate(angle.toFloat(), pivot = Offset(cx, cy)) {
                drawLine(
                    color = Color(0xFFE0F2FE),
                    start = Offset(cx, cy - r * 0.9f),
                    end = Offset(cx, cy + r * 0.9f),
                    strokeWidth = 2.5f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF38BDF8),
                    start = Offset(cx, cy - r * 0.45f),
                    end = Offset(cx - r * 0.25f, cy - r * 0.65f),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
                drawLine(
                    color = Color(0xFF38BDF8),
                    start = Offset(cx, cy - r * 0.45f),
                    end = Offset(cx + r * 0.25f, cy - r * 0.65f),
                    strokeWidth = 2f,
                    cap = StrokeCap.Round
                )
            }
        }
        drawCircle(color = Color.White, radius = r * 0.22f, center = Offset(cx, cy))
    }

    private fun DrawScope.drawRealisticMagicalOrb(powerUp: Food, cx: Float, cy: Float, r: Float, col: Color) {
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(Color.White, col, col.copy(alpha = 0.7f), Color(0xFF0F172A)),
                center = Offset(cx - r * 0.3f, cy - r * 0.3f),
                radius = r * 1.3f
            ),
            radius = r * 0.85f,
            center = Offset(cx, cy)
        )
        drawCircle(color = Color.White.copy(alpha = 0.85f), radius = r * 0.22f, center = Offset(cx - r * 0.25f, cy - r * 0.25f))
    }
}
