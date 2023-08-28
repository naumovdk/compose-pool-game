package gameobjects

import androidx.compose.runtime.MutableState
import org.openrndr.math.Vector2
import pocketDecorationColor

class PocketDecoration(radius: Double, position: Vector2, scale: MutableState<Float>) :
    CircleShaped(radius, position, pocketDecorationColor, scale)