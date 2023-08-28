package gameobjects

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.RadialGradientShader
import androidx.compose.ui.graphics.Shader
import androidx.compose.ui.graphics.ShaderBrush
import cushionColor
import surfaceColor

object Surface : GameObject() {
    @Composable
    override fun layout() {
        val largeRadialGradient = object : ShaderBrush() {
            override fun createShader(size: Size): Shader {
                val biggerDimension = maxOf(size.height, size.width)
                return RadialGradientShader(
                    colors = listOf(surfaceColor, cushionColor),
                    center = size.center,
                    radius = biggerDimension / 1.3f,
                    colorStops = listOf(0f, 0.95f)
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(largeRadialGradient)
        )
    }
}