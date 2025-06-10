import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import model.math.Point

@Suppress("ktlint:standard:function-naming")
@Composable
fun RoadCanvas(allDirections: List<List<List<Point>>>) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        if (allDirections.isNotEmpty()) {
            val roadWidth = 20f
            for (direction in allDirections) {
                for (int in 0 until direction.size) {
                    val roads = direction[int]
                    for (i in 0 until roads.size - 1) {
                        drawLine(
                            color = if (int == 0) Color.Blue else Color.Red, // Color.Black,
                            start =
                                Offset(
                                    roads[i].x.toFloat(),
                                    size.height - roads[i].y.toFloat(),
                                ),
                            end =
                                Offset(
                                    +roads[i + 1].x.toFloat(),
                                    size.height - roads[i + 1].y.toFloat(),
                                ),
                            strokeWidth = roadWidth,
                            cap = StrokeCap.Round,
                        )
                    }
                }
                for (i in 0 until direction.size - 1) {
                    for (j in 0 until direction[i].size - 1) {
                        drawLine(
                            color = Color.White,
                            start =
                                Offset(
                                    +(direction[i][j].x.toFloat() + direction[i + 1][j].x.toFloat()) / 2,
                                    size.height - (direction[i][j].y.toFloat() + direction[i + 1][j].y.toFloat()) / 2,
                                ),
                            end =
                                Offset(
                                    +(direction[i][j + 1].x.toFloat() + direction[i + 1][j + 1].x.toFloat()) / 2,
                                    size.height -
                                        (
                                            direction[i][j + 1].y.toFloat() +
                                                direction[i + 1][j + 1].y.toFloat()
                                        ) / 2,
                                ),
                            strokeWidth = 5f,
                            cap = StrokeCap.Butt,
                            pathEffect =
                                androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                                    floatArrayOf(20f, 20f),
                                    0f,
                                ),
                        )
                    }
                }
            }
            // Draw n black lines
        }
        /*for (lineIndex in 0 until n) {
                for (i in 0 until points.size - 1) {
                    // Calculate angle for perpendicular offset
                    val angle =
                        kotlin.math.atan2(
                            (points[i + 1].y - points[i].y).toFloat(),
                            (points[i + 1].x - points[i].x).toFloat(),
                        )
                    val perpAngle = angle + kotlin.math.PI.toFloat() / 2

                    // Draw white dashed lines between black lines
                    if (lineIndex < n - 1) {
                        val dashOffset = -roadWidth / 2 + (lineIndex + 0.5f) * spacing
                        drawLine(
                            color = Color.White,
                            start =
                                Offset(
                                    points[i].x.toFloat() + dashOffset * kotlin.math.cos(perpAngle),
                                    size.height - (points[i].y.toFloat() + dashOffset * kotlin.math.sin(perpAngle)),
                                ),
                            end =
                                Offset(
                                    points[i + 1].x.toFloat() + dashOffset * kotlin.math.cos(perpAngle),
                                    size.height - (points[i + 1].y.toFloat() + dashOffset * kotlin.math.sin(perpAngle)),
                                ),
                            strokeWidth = 4f,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f),
                        )
                    }
                }
            }*/
    }
}
