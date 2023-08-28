package gameobjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.openrndr.math.Vector2

abstract class CircleShaped(val radius: Double, position: Vector2, val color: Color, scale: MutableState<Float>) :
    GameObject() {
    var position by mutableStateOf(position)
    val scale by scale

    @Composable
    override fun layout() {
        Box(
            modifier = Modifier
                .size((2.0 * radius * scale).dp)
                .offset(xOffset(), yOffset())
                .clip(CircleShape)
                .background(color)
        )
    }

    fun xOffset(): Dp {
        return (scale * (position.x - radius)).dp
    }

    fun yOffset(): Dp {
        return (scale * (position.y - radius)).dp
    }
}