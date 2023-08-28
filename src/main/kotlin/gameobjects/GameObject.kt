package gameobjects

import androidx.compose.runtime.Composable

sealed class GameObject {
    @Composable
    abstract fun layout()
}
