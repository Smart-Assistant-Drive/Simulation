package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.Car
import model.Direction
import model.DirectionFactory
import model.Point
import kotlin.math.atan

class SimulationViewModel {
    // Use MutableStateFlow for mutable state and StateFlow for read-only access.
    private val _count = MutableStateFlow(0) // Initialize directly.
    val count: StateFlow<Int> = _count.asStateFlow() // Expose as read-only

    private val _message = MutableStateFlow("Initial Message")
    val message: StateFlow<String> = _message.asStateFlow()

    private val _roadMap = MutableStateFlow<List<Direction>>(listOf())
    val roadMap: StateFlow<List<Direction>> = _roadMap.asStateFlow()

    private val _trafficLight = MutableStateFlow(0)
    val trafficLight = _trafficLight // Expose as read-only
    private val _cars = MutableStateFlow(listOf<Car>(Car(Point(0.0, 0.0))))
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()

    fun getRoadMap() {
        // Simulate a network or database call
        CoroutineScope(Dispatchers.IO).launch {
            // Simulate a delay for fetching data
            val roadMapData1 =
                listOf(
                    Point(0.0, 0.0),
                    Point(100.0, 100.0),
                    Point(100.0, 200.0),
                    Point(200.0, 150.0),
                )
            val roadMapData2 =
                listOf(
                    Point(200.0, 350.0),
                    Point(100.0, 400.0),
                    Point(100.0, 300.0),
                    Point(0.0, 200.0),
                )
                /*listOf(
                    Point(200.0, 100.0), // Top
                    Point(171.0, 171.0), // Top-right
                    Point(100.0, 300.0), // Right
                    Point(100.0 - 71, 171.0), // Bottom-right
                    Point(0.0, 100.0), // Bottom
                    Point(100.0 - 71, 100.0 - 71), // Bottom-left
                    Point(100.0, 0.0), // Left
                    Point(171.0, 100.0 - 71), // Top-left
                    Point(200.0, 100.0), // Top
                )*/
            val directionsData =
                listOf(
                    DirectionFactory.createDirection(roadMapData1, 2),
                    DirectionFactory.createDirection(roadMapData2, 2),
                )
            withContext(Dispatchers.Main) {
                _roadMap.value = directionsData
            }
        }
    }

    fun startTrafficLight() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(1000) // Delay for 1 second
                withContext(Dispatchers.Main) {
                    _trafficLight.value = (_trafficLight.value + 1) % 3 // Cycle through 0, 1, 2
                }
            }
        }
    }

    fun startCars() {
        CoroutineScope(Dispatchers.IO).launch {
            _cars.value.map { car ->
                car.setSpeed(10) // Set speed for each car
                car.setDirection(atan(1.0)) // Set direction for each car
            }
            while (true) {
                delay(1000) // Delay for 1 second
                withContext(Dispatchers.Main) {
                    val newCars =
                        _cars.value.map { car ->
                            car.move()
                        }
                    // _cars.value = newCars // Update the state flow on the main thread.
                }
            }
        }
    }

    // Function to increment the counter.  Now uses withContext.
    fun increment() {
        //  Use Dispatchers.IO for non-CPU-bound operations like database or network.
        //  In this simple example, the increment operation is very light,
        //  but using IO is a good practice for potentially blocking operations.
        CoroutineScope(Dispatchers.IO).launch {
            // launch a new coroutine
            val newCount = _count.value + 1
            withContext(Dispatchers.Main) {
                _count.value = newCount // Update the state flow on the main thread.
                _message.value = "Incremented! Count: $newCount"
            }
        }
    }

    // Function to decrement the counter.  Now uses withContext.
    fun decrement() {
        CoroutineScope(Dispatchers.IO).launch {
            val newCount = _count.value - 1
            withContext(Dispatchers.Main) {
                _count.value = newCount
                _message.value = "Decremented! Count: $newCount"
            }
        }
    }

    fun reset() {
        CoroutineScope(Dispatchers.IO).launch {
            withContext(Dispatchers.Main) {
                _count.value = 0
                _message.value = "Reset to zero!"
            }
        }
    }
}

/*suspend fun getRoadMap(): List<Point> = withContext(Dispatchers.IO) {
val jsonString = this::class.java.classLoader.getResource("road.json")?.readText()
        ?: throw IllegalStateException("Could not read road.json")
    Json.decodeFromString(jsonString)
}

fun getRoadMap(): List<Point> =
    listOf(
        Point(0, 0),
        Point(100, 100),
        Point(200, 50),
        Point(300, 150),
        Point(400, 100),
        Point(500, 200),
        Point(600, 50),
        Point(700, 150),
        Point(800, 100),
        Point(900, 200)
    )

}
*/
