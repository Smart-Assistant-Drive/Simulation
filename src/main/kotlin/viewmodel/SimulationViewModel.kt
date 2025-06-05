package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.domain.Car
import model.domain.Direction
import model.domain.DirectionFactory
import model.domain.RoadUtilities
import model.domain.TrafficLight
import model.math.Point
import model.math.Vector2D

class SimulationViewModel {
    private val _roadMap = MutableStateFlow<List<Direction>>(listOf())
    val roadMap: StateFlow<List<Direction>> = _roadMap.asStateFlow()

    private val _junctions = MutableStateFlow<List<Point>>(listOf())
    val junctions: StateFlow<List<Point>> = _junctions.asStateFlow()

    private val _trafficLight = MutableStateFlow<List<TrafficLight>>(listOf())
    val trafficLight = _trafficLight // Expose as read-only
    private val _cars = MutableStateFlow(listOf<Car>(Car(Point(0.0, 0.0))))
    val cars: StateFlow<List<Car>> = _cars.asStateFlow()

    suspend fun getRoadMap() {
        // Simulate a network or database call
        val initialX = 100
        val initialY = 100
        val endX = 2000
        val endY = 1000
        val step = 100
        val distance = 100
        val points1 =
            (0..4)
                .map { i ->
                    when (i) {
                        0 ->
                            (initialY + step downTo initialY + step step step).map {
                                Point(initialX.toDouble(), it.toDouble())
                            }

                        1 ->
                            (initialX..endX - step step step).map {
                                Point(it.toDouble(), initialY.toDouble())
                            }

                        2 ->
                            (initialY..endY - step step step).map {
                                Point(endX.toDouble(), it.toDouble())
                            }

                        3 ->
                            (endX downTo initialX + step step step).map {
                                Point(it.toDouble(), endY.toDouble())
                            }

                        4 ->
                            (endY downTo initialY + step step step).map {
                                Point(initialX.toDouble(), it.toDouble())
                            }

                        else -> emptyList()
                    }
                }.flatten()

        val points2 =
            (0..4)
                .map { i ->
                    when (i) {
                        0 ->
                            (endY - step - distance..endY - step - distance step step).map {
                                Point(initialX.toDouble() + distance, it.toDouble())
                            }

                        1 ->
                            (initialX + distance..endX - step - distance step step).map {
                                Point(it.toDouble(), endY.toDouble() - distance)
                            }

                        2 ->
                            (endY - distance downTo initialY + step + distance step step).map {
                                Point(endX.toDouble() - distance, it.toDouble())
                            }

                        3 ->
                            (endX - distance downTo initialX + step + distance step step).map {
                                Point(it.toDouble(), initialY.toDouble() + distance)
                            }

                        4 ->
                            (initialY + distance..endY - distance - step step step).map {
                                Point(initialX.toDouble() + distance, it.toDouble())
                            }

                        else -> emptyList()
                    }
                }.flatten()

        val middleX = (initialX + endX) / 2 - distance / 4
        val points3 =
            (initialY..endY step step).map {
                Point(middleX.toDouble(), it.toDouble())
            }
        val points4 =
            (endY downTo initialY step step).map {
                Point(middleX.toDouble() + distance / 2, it.toDouble())
            }

        val directionsData =
            listOf(
                DirectionFactory.createDirection(points1, 2),
                DirectionFactory.createDirection(points2, 2),
                DirectionFactory.createDirection(points3, 1),
                DirectionFactory.createDirection(points4, 1),
            )

        val junctions =
            listOf(
                Point(middleX.toDouble(), initialY.toDouble() + distance),
                Point(middleX.toDouble(), endY.toDouble()),
            )

        val trafficLight =
            junctions
                .mapIndexed { i, j ->
                    (0..2).map {
                        when (it) {
                            0 -> TrafficLight(Point(j.x + 1.25 * distance, j.y - distance / 2), 0) // Green
                            1 ->
                                TrafficLight(
                                    Point(
                                        j.x + distance / 4,
                                        if (i % 2 == 0) {
                                            j.y + distance.toDouble() - distance / 2
                                        } else {
                                            j.y - distance.toDouble() - distance / 2
                                        },
                                    ),
                                    1,
                                ) // Yellow
                            else -> TrafficLight(Point(j.x - 0.75 * distance, j.y - distance / 2), 0) // Red
                        }
                    }
                }.flatten()

        withContext(Dispatchers.Main) {
            _roadMap.value = directionsData
            _junctions.value = junctions
            _trafficLight.value = trafficLight
        }
    }

    fun startSimulation(cars: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            getRoadMap()
            createCars(cars)
            startTrafficLight()
            startCars()
            changeLine()
        }
    }

    suspend fun createCars(cars: Int) {
        val speed = 10 // Set a default speed for the cars
        val newCars =
            (0..cars - 1).map {
                val directionIndex = (0..1).random() // Randomly choose a direction
                val road = _roadMap.value[directionIndex].roads[0] // Get the first direction
                val roadPoints = road.size
                val pointIndex = (0 until roadPoints).random() // Randomly choose a point on the road
                val point = road[pointIndex]
                val nextPointIndex = (pointIndex + 1) % roadPoints // Wrap around to the start if at the end
                val direction =
                    Vector2D
                        .fromPoints(
                            point,
                            road[nextPointIndex], // Wrap around to the start if at the end
                        ).normalize() // Get the direction vector from the point to the next point)
                Car(
                    point,
                    speed,
                    direction,
                    directionIndex,
                    0,
                    nextPointIndex,
                ) // Create a car with a random point and fixed speed
            }
        withContext(Dispatchers.Main) {
            _cars.value = newCars // Update the state flow on the main thread.
        }
    }

    fun startTrafficLight() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(2000) // Delay for 1 second
                withContext(Dispatchers.Main) {
                    _trafficLight.value =
                        _trafficLight.value.map { trafficLight ->
                            trafficLight.changeState()
                        } // Update the state flow on the main thread.
                }
            }
        }
    }

    fun startCars() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(100) // Delay for 1 second
                val newCars =
                    _cars.value.map { car ->
                        RoadUtilities.carMoveSafety(
                            car,
                            _roadMap.value,
                        ) // Move the car safely along the road
                    }

                withContext(Dispatchers.Main) {
                    _cars.value = newCars // Update the state flow on the main thread.
                }
            }
        }
    }

    fun changeLine() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(5000) // Delay for 1 second
                val newCars =
                    _cars.value.map { car ->
                        RoadUtilities.changeLine(
                            car,
                            _roadMap.value,
                        ) // Change the line of the car
                    }
                withContext(Dispatchers.Main) {
                    _cars.value = newCars // Update the state flow on the main thread.
                }
            }
        }
    }
}
