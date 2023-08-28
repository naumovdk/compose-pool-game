package gameobjects

import androidx.compose.runtime.MutableState
import org.openrndr.math.Vector2
import pocketColor

class Pocket(radius: Double, position: Vector2, scale: MutableState<Float>) :
    CircleShaped(radius, position, pocketColor, scale), Collidable {

    override fun collide(ball: Ball) {
        val direction = ball.position - this.position
        if (direction.length > ball.radius) {
            return
        }
        ball.onGoal(ball)
    }
}