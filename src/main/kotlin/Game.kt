import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import gameobjects.*
import org.openrndr.math.Vector2

class Game {
    var scale: MutableState<Float> = mutableStateOf(1.0f)

    private val surface = Surface
    private var balls = defaultBalls()
    private var goalsToRestart = balls.size

    private fun defaultBalls(): List<Ball> = ballsInitials.map { initial ->
        Ball(
            color = initial.color,
            position = initial.position,
            velocity = Vector2.ZERO,
            radius = ballRadius,
            scale = scale
        ) { ball -> onGoal(ball) }
    }

    private val cushions = cushionsCoordinates.map { Cushion(it.A, it.B, it.C, it.D, cornerRounding, scale) }

    private val pockets = pocketsCoordinates.map { (xPadding, yPadding) ->
        val position = Vector2(xPadding, yPadding)
        Pocket(pocketRadius, position, scale)
    }

    private val pocketDecorations = pocketsCoordinates.map { (xPadding, yPadding) ->
        val position = Vector2(xPadding, yPadding)
        PocketDecoration(pocketDecorationRadius, position, scale)
    }

    fun update() {
        findPairsToCheckCollision().forEach { (ball, collidable) ->
            collidable.collide(ball)
        }

        balls.forEach {
            it.update()
        }
    }

    private fun findPairsToCheckCollision(): ArrayList<Pair<Ball, Collidable>> { // todo spacial hashing
        val pairs = arrayListOf<Pair<Ball, Collidable>>()
        val collidables = balls + cushions + pockets
        for (ball in balls) {
            for (collidable in collidables) {
                if (ball !== collidable) {
                    pairs.add(Pair(ball, collidable))
                }
            }
        }
        return pairs
    }

    @Composable
    fun layout() {
        pocketDecorations.forEach {
            it.layout()
        }
        surface.layout()
        cushions.forEach {
            it.layout()
        }
        pockets.forEach {
            it.layout()
        }
        balls.forEach {
            it.layout()
        }
    }

    private fun onGoal(ball: Ball) {
        ball.position = ballHidePosition
        ball.velocity = Vector2.ZERO
        goalsToRestart -= 1

        if (goalsToRestart == 0) {
            restart()
            goalsToRestart = balls.size
        }
    }

    fun restart() {
        balls.zip(ballsInitials) { ball, init ->
            ball.velocity = Vector2.ZERO
            ball.position = init.position
        }
    }
}