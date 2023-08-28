import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application

@Composable
fun tableLayout(game: Game) {
    BoxWithConstraints(
        modifier = Modifier
            .aspectRatio(0.5f)
            .fillMaxSize()
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .aspectRatio(0.5f)
                .wrapContentSize()
        ) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(outerBoxAspectRatio, matchHeightConstraintsFirst = true)
                    .clip(RoundedCornerShape(tableRounding.dp * game.scale.value))
                    .background(woodColor)
                    .align(Alignment.Center)
                    .padding(woodPart * maxWidth)
            ) {
                game.scale.value = (maxWidth.value / maxX).toFloat()
                game.layout()
            }
        }
    }
}


fun main() = application {
    val game = Game()

    var lastUpdate = 0L
    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { current ->
                if ((current - lastUpdate) > refreshTimeNanos) {
                    game.update()
                    lastUpdate = current
                }
            }
        }
    }

    Window(onCloseRequest = ::exitApplication, state = WindowState(width = width.dp, height = height.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding((outerPadding * game.scale.value).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    game.restart()
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(text = "Restart")
            }
            tableLayout(game)
        }
    }
}