package gameobjects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import ballSelectColor
import ballSelectRadiusScale
import frictionCoefficient
import kickForceCoefficient
import maxKickLength
import org.openrndr.math.Vector2

class Ball(
    color: Color,
    position: Vector2,
    var velocity: Vector2 = Vector2.ZERO,
    radius: Double,
    scale: MutableState<Float>,
    var onGoal: (Ball) -> Unit
) : CircleShaped(radius, position, color, scale), Collidable {
    private var kick by mutableStateOf<Vector2?>(null)

    override fun collide(ball: Ball) {
        val restitution = 1.0

        val distance = ball.position - this.position
        val overlap = this.radius + ball.radius - distance.length

        if (overlap <= 0.0) {
            return
        }

        val impactDirection = distance.normalized
        val correction = impactDirection * (overlap / 2.0)
        this.position -= correction
        ball.position += correction

        val v1 = this.velocity.dot(impactDirection)
        val v2 = ball.velocity.dot(impactDirection)

        val m1 = 1.0
        val m2 = 1.0

        val newV1 = (m1 * v1 + m2 * v2 - m2 * (v1 - v2) * restitution) / (m1 + m2)
        val newV2 = (m1 * v1 + m2 * v2 - m1 * (v2 - v1) * restitution) / (m1 + m2)

        this.velocity += impactDirection * (newV1 - v1)
        ball.velocity += impactDirection * (newV2 - v2)
    }

    @Composable
    override fun layout() {
        Box(
            modifier = Modifier
                .size((2.0 * radius * scale).dp)
                .offset(xOffset(), yOffset())
                .clip(CircleShape)
                .background(color)
                .pointerInput(Unit) {
                    fun toVector2(offset: Offset) = Vector2(
                        (offset.x.toDp().value / scale).toDouble(),
                        (offset.y.toDp().value / scale).toDouble()
                    )

                    var kickPosition: Offset? = null
                    detectDragGestures(
                        onDragStart = {
                            kick = Vector2.ZERO
                        },
                        onDrag = { change, _ ->
                            kickPosition = kickPosition ?: change.previousPosition

                            kick = toVector2(kickPosition!! - change.position)
                            if (kick!!.length > maxKickLength) {
                                kick = kick!!.normalized * maxKickLength
                            }
                        },
                        onDragEnd = {
                            velocity += kick!! * kickForceCoefficient
                            kick = null
                        }
                    )
                }
        )

        kick ?: return

        Canvas(modifier = Modifier.fillMaxSize(),
            onDraw = {
                fun toOffset(vector2: Vector2) = Offset(
                    vector2.x.dp.toPx() * scale,
                    vector2.y.dp.toPx() * scale
                )

                drawCircle(
                    ballSelectColor,
                    (ballSelectRadiusScale * radius * scale).dp.toPx(),
                    toOffset(position),
                    style = Stroke(width = (1.0 * scale).dp.toPx())
                )
                drawLine(
                    ballSelectColor,
                    toOffset(position),
                    toOffset(position + kick!!)
                )
            }
        )
    }

    fun update() {
        position += velocity
        velocity *= frictionCoefficient
    }
}