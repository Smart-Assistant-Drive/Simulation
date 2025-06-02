import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import components.CarCanvas
import components.TrafficLightCanvas
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import model.Point
import viewmodel.SimulationViewModel

@Composable
@Preview
@Suppress("FunctionName")
fun App(
    mainViewModel: SimulationViewModel,
    applicationScope: CoroutineScope,
) {
    val road by mainViewModel.roadMap.collectAsState() // Collect the StateFlow
    mainViewModel.getRoadMap()
    val count by mainViewModel.count.collectAsState() // Collect the StateFlow
    val message by mainViewModel.message.collectAsState()
    val trafficLight by mainViewModel.trafficLight.collectAsState() // Collect the StateFlow
    val textMeasurer = rememberTextMeasurer()
    mainViewModel.startTrafficLight()
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Compose Multiplatform App") })
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            RoadCanvas(
                road.map { direction -> direction.roads.map { road -> road.lines } },
            )
            TrafficLightCanvas(listOf(Pair(Point(100.0, 300.0), trafficLight)))
            CarCanvas(listOf(Point(0.0, 0.0), Point(100.0, 100.0)))
            /*Canvas(modifier = Modifier.fillMaxSize()) {
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
                    radius = 100f,
                    center =
                        Offset(
                            x = size.width / 2,
                            y = size.height / 2,
                        ),
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = "Hello World",
                    topLeft =
                        Offset(
                            x = center.x,
                            y = center.y,
                        ),
                )
            }

            /*Canvas(modifier = Modifier.fillMaxSize()) {
                drawRect(
                    color = Color.Red,
                    topLeft =
                        Offset(
                            x = size.width / 2 - 50,
                            y = size.height / 2 - 50,
                        ),
                    size =
                        androidx.compose.ui.geometry
                            .Size(20f, 40f),
                )
            }*/
            /*road.forEach { direction ->
                direction.roads.forEach { road ->
                    RoadCanvas(road.lines)
                }
            }
            /*RoadCanvas(

                /*listOf<Point>(
                    Point(1300, 100), // Top
                    Point(1271, 171), // Top-right
                    Point(1200, 200), // Right
                    Point(1200 - 71, 171), // Bottom-right
                    Point(1100, 100), // Bottom
                    Point(1200 - 71, 100 - 71), // Bottom-left
                    Point(1200, 0), // Left
                    Point(1271, 100 - 71), // Top-left
                    Point(1300, 100), // Top*/
                ),
                3,
            )
            RoadCanvas(
                listOf<Point>(
                    /*Point(200, 100), // Top
                    Point(171, 100 - 71), // Top-left
                    Point(100, 0), // Left
                    // Top-right
                    Point(100 - 71, 100 - 71), // Bottom-left
                    Point(0, 100), // Bottom
                    Point(100 - 71, 171), // Bottom-right
                    Point(100, 200), // Right
                    Point(171, 171),
                    Point(200, 100), // Top*/
                ),
                3,
            )
            /*Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = Color.Red,
                    radius = 100f,
                    center =
                        androidx.compose.ui.geometry.Offset(
                            x = size.width / 2,
                            y = size.height / 2,
                        ),
                )
                if (road.size > 1) {
                    for (i in 0 until road.size - 1) {
                        // Outer parallel line
                        val angle =
                            kotlin.math.atan2(
                                (road[i + 1].y - road[i].y).toFloat(),
                                (road[i + 1].x - road[i].x).toFloat(),
                            )
                        val offset = 35f
                        val perpAngle = angle + kotlin.math.PI.toFloat() / 2

                        drawLine(
                            start =
                                androidx.compose.ui.geometry.Offset(
                                    road[i].x.toFloat() + offset * kotlin.math.cos(perpAngle),
                                    size.height - (road[i].y.toFloat() + offset * kotlin.math.sin(perpAngle)),
                                ),
                            end =
                                androidx.compose.ui.geometry.Offset(
                                    road[i + 1].x.toFloat() + offset * kotlin.math.cos(perpAngle),
                                    size.height - (road[i + 1].y.toFloat() + offset * kotlin.math.sin(perpAngle)),
                                ),
                            color = Color.Blue,
                            strokeWidth = 30f,
                            cap = StrokeCap.Round,
                        )
                    }
                    for (i in 0 until road.size - 1) {
                        drawLine(
                            start =
                                androidx.compose.ui.geometry.Offset(
                                    road[i].x.toFloat(),
                                    size.height - road[i].y.toFloat(),
                                ),
                            end =
                                androidx.compose.ui.geometry.Offset(
                                    road[i + 1].x.toFloat(),
                                    size.height - road[i + 1].y.toFloat(),
                                ),
                            color = Color.LightGray,
                            strokeWidth = 30f,
                            cap = StrokeCap.Butt,
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(20f, 20f), 0f),
                        )
                    }
                    for (i in 0 until road.size - 1) {
                        drawLine(
                            start =
                                androidx.compose.ui.geometry.Offset(
                                    road[i].x.toFloat(),
                                    size.height - road[i].y.toFloat(),
                                ),
                            end =
                                androidx.compose.ui.geometry.Offset(
                                    road[i + 1].x.toFloat(),
                                    size.height - road[i + 1].y.toFloat(),
                                ),
                            color = Color.Black,
                            strokeWidth = 20f,
                            cap = StrokeCap.Round,
                        )
                    } // Draw inner parallel line
                    // Draw parallel lines
                }
            }*/
             */
             */*/
        }
    }
}

fun main() {
    application {
        val applicationScope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main) }
        Window(onCloseRequest = ::exitApplication, title = "Compose Multiplatform App") {
            // Create and pass the MainViewModel directly.
            val mainViewModel = remember { SimulationViewModel() }
            App(mainViewModel, applicationScope)
        }
    }
}
