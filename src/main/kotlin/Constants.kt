import androidx.compose.ui.graphics.Color
import org.openrndr.math.Vector2

const val width = 600.0
const val height = 1000.0
const val refreshTimeNanos = 8333333L // 120 fps

const val outerPadding = 30.0
const val woodPart = 0.05f
const val innerDimension = 1 - 2 * woodPart
const val outerBoxAspectRatio = (innerDimension + 2 * woodPart) / (2 * innerDimension + 2 * woodPart)
const val tableRounding = 20.0

val cushionColor = Color(0, 100, 0)
val surfaceColor = Color(153, 255, 153)
val woodColor = Color(102, 2, 2)
val pocketColor = Color(50, 2, 2)
val pocketDecorationColor = Color.White
val ballSelectColor = Color.White

const val maxX: Double = 410.0
const val maxY: Double = 820.0

const val longSide = maxX * 0.9
const val thickness = maxX * 0.08
const val padding = maxX * 0.07
const val constriction = longSide * 0.1
const val cornerRounding = thickness.toFloat() * 0.35f

data class Quadrangle(val A: Vector2, val B: Vector2, val C: Vector2, val D: Vector2)

val cushionsCoordinates = listOf(
    Quadrangle(
        Vector2(padding, 0.0),
        Vector2(padding + constriction, thickness),
        Vector2(maxX - padding - constriction, thickness),
        Vector2(maxX - padding, 0.0)
    ),
    Quadrangle(
        Vector2(padding, maxY),
        Vector2(padding + constriction, maxY - thickness),
        Vector2(maxX - padding - constriction, maxY - thickness),
        Vector2(maxX - padding, maxY),
    ),
    Quadrangle(
        Vector2(0.0, padding),
        Vector2(thickness, padding + constriction),
        Vector2(thickness, padding + longSide - constriction),
        Vector2(0.0, padding + longSide),
    ),
    Quadrangle(
        Vector2(0.0, maxY - padding),
        Vector2(0.0, maxY - padding - longSide),
        Vector2(thickness, maxY - padding - longSide + constriction),
        Vector2(thickness, maxY - padding - constriction),
    ),
    Quadrangle(
        Vector2(maxX, padding),
        Vector2(maxX - thickness, padding + constriction),
        Vector2(maxX - thickness, padding + longSide - constriction),
        Vector2(maxX, padding + longSide),
    ),
    Quadrangle(
        Vector2(maxX, maxY - padding),
        Vector2(maxX - thickness, maxY - padding - constriction),
        Vector2(maxX - thickness, maxY - padding - longSide + constriction),
        Vector2(maxX, maxY - padding - longSide),
    )
)

const val pocketRadius = padding * 0.75
const val pocketPadding = padding * 0.5
const val pocketDecorationRadius = pocketRadius * 1.1

val pocketsCoordinates = listOf(
    Pair(pocketPadding, pocketPadding),
    Pair(pocketPadding, maxY / 2),
    Pair(pocketPadding, maxY - pocketPadding),
    Pair(maxX - pocketPadding, maxY - pocketPadding),
    Pair(maxX - pocketPadding, maxY / 2),
    Pair(maxX - pocketPadding, pocketPadding)
)

const val ballRadius = pocketRadius * 0.75
const val ballSelectRadiusScale = 1.3
const val kickForceCoefficient = 5e-2
const val maxKickLength = maxX * 0.5
const val frictionCoefficient = 0.992

data class BallInitial(val position: Vector2, val color: Color)

const val cos60 = 0.86602540378
val bottomRow = Vector2(maxX * 0.5, maxY * 0.3)
val middleRowLeft = Vector2(bottomRow.x - ballRadius, bottomRow.y - 2 * cos60 * ballRadius)
val middleRowRight = Vector2(bottomRow.x + ballRadius, bottomRow.y - 2 * cos60 * ballRadius)
val topRowLeft = Vector2(bottomRow.x - 2 * ballRadius, middleRowLeft.y - 2 * cos60 * ballRadius)
val topRowMiddle = Vector2(bottomRow.x, middleRowLeft.y - 2 * cos60 * ballRadius)
val topRowRight = Vector2(bottomRow.x + 2 * ballRadius, middleRowLeft.y - 2 * cos60 * ballRadius)
val kick = Vector2(maxX * 0.5, maxY * 0.7)

val ballsInitials = listOf(
    BallInitial(
        bottomRow,
        Color.Cyan
    ),
    BallInitial(
        middleRowLeft,
        Color.Red
    ),
    BallInitial(
        middleRowRight,
        Color.Yellow
    ),
    BallInitial(
        topRowMiddle,
        Color.Blue
    ),
    BallInitial(
        topRowLeft,
        Color.Black
    ),
    BallInitial(
        topRowRight,
        Color.Magenta
    ),
    BallInitial(
        kick,
        Color.White
    ),
)

val ballHidePosition = Vector2(-maxX, -maxY)