package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import model.domain.TrafficLight
import model.domain.TrafficLightState

@Suppress("ktlint:standard:function-naming")
@Composable
fun TrafficLightCanvas(trafficsLights: List<TrafficLight>) {
    val textMeasurer = rememberTextMeasurer()
    Canvas(modifier = Modifier.fillMaxSize()) {
        val radius = 0.03f * size.minDimension
        for (trafficLight in trafficsLights) {
            drawCircle(
                color =
                    when (trafficLight.state) {
                        TrafficLightState.GREEN -> {
                            Color.Green
                        }

                        TrafficLightState.YELLOW -> {
                            Color.Yellow
                        }

                        else -> {
                            Color.Red
                        }
                    },
                radius = radius,
                center =
                    Offset(
                        x = trafficLight.position.x.toFloat(),
                        y = size.height - trafficLight.position.y.toFloat(),
                    ),
            )
            drawText(
                textMeasurer = textMeasurer,
                text = trafficLight.id,
                topLeft =
                    Offset(
                        x = trafficLight.position.x.toFloat() - radius,
                        y = size.height - trafficLight.position.y.toFloat() - radius,
                    ),
            )
        }
    }
}
