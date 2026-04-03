package com.example.agrocalc.ui

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    val x: Float,
    val y: Float,
    val radius: Float,
    val speedX: Float,
    val speedY: Float,
    val alpha: Float,
    val color: Color
)

@Composable
fun ParticleBackground(modifier: Modifier = Modifier) {
    val particles = remember {
        List(60) {
            Particle(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                radius = Random.nextFloat() * 3f + 1f,
                speedX = (Random.nextFloat() - 0.5f) * 0.0008f,
                speedY = (Random.nextFloat() - 0.5f) * 0.0008f,
                alpha = Random.nextFloat() * 0.5f + 0.1f,
                color = listOf(
                    Color(0xFF00E5FF),
                    Color(0xFF00BFA5),
                    Color(0xFF1565C0),
                    Color(0xFF7C4DFF)
                ).random()
            )
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 30000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        drawRect(color = Color(0xFF0A0E1A))
        particles.forEach { particle ->
            val x = ((particle.x + particle.speedX * time) % 1f + 1f) % 1f
            val y = ((particle.y + particle.speedY * time) % 1f + 1f) % 1f
            drawCircle(
                color = particle.color.copy(alpha = particle.alpha),
                radius = particle.radius,
                center = Offset(x * size.width, y * size.height)
            )
        }
        // Líneas de conexión entre partículas cercanas
        particles.forEach { p1 ->
            particles.forEach { p2 ->
                val x1 = ((p1.x + p1.speedX * time) % 1f + 1f) % 1f
                val y1 = ((p1.y + p1.speedY * time) % 1f + 1f) % 1f
                val x2 = ((p2.x + p2.speedX * time) % 1f + 1f) % 1f
                val y2 = ((p2.y + p2.speedY * time) % 1f + 1f) % 1f
                val dist = Math.sqrt(
                    ((x1 - x2) * size.width).toDouble().pow(2) +
                            ((y1 - y2) * size.height).toDouble().pow(2)
                ).toFloat()
                if (dist < 120f) {
                    drawLine(
                        color = Color(0xFF00E5FF).copy(alpha = (1 - dist / 120f) * 0.15f),
                        start = Offset(x1 * size.width, y1 * size.height),
                        end = Offset(x2 * size.width, y2 * size.height),
                        strokeWidth = 0.5f
                    )
                }
            }
        }
    }
}

private fun Double.pow(n: Int): Double {
    var result = 1.0
    repeat(n) { result *= this }
    return result
}