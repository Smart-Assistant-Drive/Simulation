package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import model.TrafficLight

@Suppress("ktlint:standard:function-naming")
@Composable
fun TrafficLightCanvas(trafficsLights: List<TrafficLight>) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (trafficLight in trafficsLights) {
            drawCircle(
                color =
                    when (trafficLight.state) {
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
                        x = trafficLight.position.x.toFloat(),
                        y = size.height - trafficLight.position.y.toFloat(),
                    ),
            )
            drawText(
                textMeasurer = textMeasurer,
                text = "Hello World",
                topLeft =
                    Offset(
                        x = trafficLight.position.x.toFloat() - 50f, // Adjusted to center text horizontally
                        y = size.height - trafficLight.position.y.toFloat() - 80f, // Adjusted to position text above the circle
                    ),
            )
        }
    }
}
