package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import model.math.Point

@Suppress("ktlint:standard:function-naming")
@Composable
fun JunctionCanvas(points: List<Point>) {
    val distance = 100f

    Canvas(modifier = Modifier.fillMaxSize()) {
        for (point in points) {
            drawRect(
                brush = SolidColor(Color.Black),
                topLeft =
                    Offset(
                        x = point.x.toFloat() - 10f,
                        y = size.height - point.y.toFloat() - 10f,
                    ),
                size =
                    androidx.compose.ui.geometry
                        .Size(distance / 2 + 20f, distance + 20f),
            )
        }
    }
}
