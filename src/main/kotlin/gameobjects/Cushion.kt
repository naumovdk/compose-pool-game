package gameobjects

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.unit.dp
import cushionColor
import org.openrndr.math.Vector2
import java.awt.geom.Line2D
import java.awt.geom.Point2D


class Cushion(
    private val A: Vector2,
    private val B: Vector2,
    private val C: Vector2,
    private val D: Vector2,
    private val cornerRounding: Float,
    scale: MutableState<Float>
) : GameObject(), Collidable {
    private val sides = arrayListOf<Pair<Vector2, Vector2>>()
    private val scale: Float by scale

    init {
        sides.add(Pair(A, B))
        sides.add(Pair(B, C))
        sides.add(Pair(C, D))
        sides.add(Pair(D, A))
    }

    @Composable
    override fun layout() {
        Canvas(modifier = Modifier.fillMaxWidth()) {
            fun scaleCoordinate(coordinate: Double) = (coordinate * scale).dp.toPx()
            val path = Path().apply {
                moveTo(scaleCoordinate(A.x), scaleCoordinate(A.y))
                lineTo(scaleCoordinate(B.x), scaleCoordinate(B.y))
                lineTo(scaleCoordinate(C.x), scaleCoordinate(C.y))
                lineTo(scaleCoordinate(D.x), scaleCoordinate(D.y))
                lineTo(scaleCoordinate(A.x), scaleCoordinate(A.y))
                close()
            }

            drawIntoCanvas { canvas ->
                canvas.drawOutline(
                    outline = Outline.Generic(path),
                    paint = Paint().apply {
                        color = cushionColor
                        pathEffect = PathEffect.cornerPathEffect(cornerRounding * scale)
                    }
                )
            }
        }
    }

    override fun collide(ball: Ball) {
        val (reboundSide, distance) = sides.map { side ->
            val start = side.first
            val end = side.second
            val segment = Line2D.Double(start.x, start.y, end.x, end.y)
            val ballPoint = Point2D.Double(ball.position.x, ball.position.y)
            side to segment.ptSegDist(ballPoint)
        }.filter { (_, distance) ->
            distance < ball.radius
        }.ifEmpty {
            return
        }.minBy { (_, distance) ->
            distance
        }

        val surfaceVector = reboundSide.first - reboundSide.second
        val surfaceNormal = surfaceVector.perpendicular().normalized

        val reboundNormal = if (surfaceNormal.dot(ball.velocity) < 0.0) {
            surfaceNormal
        } else {
            -surfaceNormal
        }

        val reflectedVelocity = ball.velocity.reflect(reboundNormal)
        ball.velocity = reflectedVelocity

        val correctionDirection = reboundNormal.normalized
        val correction = correctionDirection * 1.0 * (ball.radius - distance)
        ball.position += correction
    }
}