package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import model.Car

@Suppress("ktlint:standard:function-naming")
@Composable
fun CarCanvas(cars: List<Car>) {
    val carWidth = 20f
    val carHeight = 40f
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (car in cars) {
            val deg = 90f - car.direction.degree().toFloat()
            rotate(
                degrees = deg, // Adjusting the rotation to match the coordinate system
                pivot = Offset(car.position.x.toFloat(), size.height - car.position.y.toFloat()),
            ) {
                drawRect(
                    color = Color.LightGray,
                    topLeft =
                        Offset(
                            x = car.position.x.toFloat() - carWidth / 2,
                            y = size.height - carHeight / 2 - car.position.y.toFloat(),
                        ),
                    size =
                        androidx.compose.ui.geometry
                            .Size(carWidth, carHeight),
                )
            }
        }
    }
}
