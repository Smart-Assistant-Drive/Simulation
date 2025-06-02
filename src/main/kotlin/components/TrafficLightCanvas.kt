package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import model.Point

@Suppress("ktlint:standard:function-naming")
@Composable
fun TrafficLightCanvas(trafficsLights: List<Pair<Point, Int>>) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxSize()) {
        for ((point, trafficLight) in trafficsLights) {
            drawCircle(
                color =
                    when (trafficLight) {
                        0 -> {
                            Color.Green
                        }

                        1 -> {
                            Color.Yellow
                        }

                        else -> {
                            Color.Red
                        }
                    },
                radius = 50f,
                center =
                    Offset(
                        x = size.width / 2 + point.x.toFloat(),
                        y = size.height / 2 - point.y.toFloat(),
                    ),
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "Hello World",
                topLeft =
                    Offset(
                        x = size.width / 2 + point.x.toFloat() - 50f, // Adjusted to center text horizontally
                        y = size.height / 2 - point.y.toFloat() - 80f, // Adjusted to position text above the circle
                    ),
            )
        }
    }
}
