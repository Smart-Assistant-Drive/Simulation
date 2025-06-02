package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import model.Point

@Suppress("ktlint:standard:function-naming")
@Composable
fun CarCanvas(cars: List<Point>) {
    val carWidth = 20f
    val carHeight = 40f
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (point in cars) {
            rotate(
                degrees = 45f,
                pivot = Offset(size.width / 2 + point.x.toFloat(), size.height / 2 - point.y.toFloat()),
            ) {
                drawRect(
                    color = Color.LightGray,
                    topLeft =
                        Offset(
                            x = size.width / 2 - carWidth / 2 + point.x.toFloat(),
                            y = size.height / 2 - carHeight / 2 - point.y.toFloat(),
                        ),
                    size =
                        androidx.compose.ui.geometry
                            .Size(carWidth, carHeight),
                )
            }
        }
    }
}
