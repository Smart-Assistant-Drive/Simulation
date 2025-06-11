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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import components.CarCanvas
import components.JunctionCanvas
import components.TrafficLightCanvas
import components.TrafficSignCanvas
import viewmodel.SimulationViewModel

@Composable
@Preview
@Suppress("FunctionName")
fun App(mainViewModel: SimulationViewModel) {
    val road by mainViewModel.roadMap.collectAsState() // Collect the StateFlow
    val junctions by mainViewModel.junctions.collectAsState() // Collect the StateFlow
    val trafficLight by mainViewModel.trafficLight.collectAsState() // Collect the StateFlow
    val cars by mainViewModel.cars.collectAsState() // Collect the StateFlow
    val trafficSigns by mainViewModel.trafficSigns.collectAsState() // Collect the StateFlow
    mainViewModel.startSimulation(1)
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Compose Multiplatform App") })
        },
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize()) {
            RoadCanvas(
                road.map { direction -> direction.roads.map { road -> road } },
            )
            JunctionCanvas(junctions.map { it.position })
            TrafficLightCanvas(trafficLight)
            CarCanvas(cars)
            TrafficSignCanvas(trafficSigns) // Example traffic sign
        }
    }
}

fun main() {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Compose Multiplatform App",
            state = rememberWindowState(width = 1100.dp, height = 650.dp),
        ) {
            // Create and pass the MainViewModel directly.
            val mainViewModel = remember { SimulationViewModel() }
            App(mainViewModel)
        }
    }
}
