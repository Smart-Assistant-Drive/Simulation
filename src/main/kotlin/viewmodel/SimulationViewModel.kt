package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import model.domain.Car
import model.domain.Direction
import model.domain.Junction
import model.domain.JunctionConnection
import model.domain.RoadUtilities
import model.domain.TrafficLight
import model.domain.TrafficSign
import model.math.Point
import model.math.Vector2D
import repository.MqttRepository
import repository.RemoteRepository
import repository.RemoteStream
import viewmodel.mapper.Mapper.convert

class SimulationViewModel {
    private val _roadMap = MutableStateFlow<List<Direction>>(listOf())
    val roadMap: StateFlow<List<Direction>> = _roadMap.asStateFlow()

    private val _junctions = MutableStateFlow<Set<Junction>>(setOf())
    val junctions: StateFlow<Set<Junction>> = _junctions.asStateFlow()

    private val _trafficLights = MutableStateFlow<List<TrafficLight>>(listOf())
    val trafficLights = _trafficLights // Expose as read-only
    private val _cars = MutableStateFlow(setOf(Car(Point(0.0, 0.0))))
    val cars: StateFlow<Set<Car>> = _cars.asStateFlow()
    private val _trafficSigns = MutableStateFlow<Set<TrafficSign>>(setOf())
    val trafficSigns: StateFlow<Set<TrafficSign>> = _trafficSigns.asStateFlow()

    /*
    suspend fun getRoadMap() {
        // Simulate a network or database call
        val initialX = 100
        val initialY = 100
        val endX = 2000
        val endY = 1000
        val step = 50
        val distance = 100
        val points1 =
            (0..4)
                .map { i ->
                    when (i) {
                        0 -> {
                            (initialY + step downTo initialY + step step step).map {
                                Point(initialX.toDouble(), it.toDouble())
                            }
                        }

                        1 -> {
                            (initialX..endX - step step step).map {
                                Point(it.toDouble(), initialY.toDouble())
                            }
                        }

                        2 -> {
                            (initialY..endY - step step step).map {
                                Point(endX.toDouble(), it.toDouble())
                            }
                        }

                        3 -> {
                            (endX downTo initialX + step step step).map {
                                Point(it.toDouble(), endY.toDouble())
                            }
                        }

                        4 -> {
                            (endY downTo initialY + step step step).map {
                                Point(initialX.toDouble(), it.toDouble())
                            }
                        }

                        else -> {
                            emptyList()
                        }
                    }
                }.flatten()

        val points2 =
            (0..4)
                .map { i ->
                    when (i) {
                        0 -> {
                            (endY - step - distance..endY - step - distance step step).map {
                                Point(initialX.toDouble() + distance, it.toDouble())
                            }
                        }

                        1 -> {
                            (initialX + distance..endX - step - distance step step).map {
                                Point(it.toDouble(), endY.toDouble() - distance)
                            }
                        }

                        2 -> {
                            (endY - distance downTo initialY + step + distance step step).map {
                                Point(endX.toDouble() - distance, it.toDouble())
                            }
                        }

                        3 -> {
                            (endX - distance downTo initialX + step + distance step step).map {
                                Point(it.toDouble(), initialY.toDouble() + distance)
                            }
                        }

                        4 -> {
                            (initialY + distance..endY - distance - step step step).map {
                                Point(initialX.toDouble() + distance, it.toDouble())
                            }
                        }

                        else -> {
                            emptyList()
                        }
                    }
                }.flatten()

        val middleX = (initialX + endX) / 2
        val points3 =
            (initialY..endY step step).map {
                Point(middleX.toDouble() + distance / 4, it.toDouble())
            }
        val points4 =
            (endY downTo initialY step step).map {
                Point(middleX.toDouble() - distance / 4, it.toDouble())
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
                Junction(
                    Point(middleX.toDouble(), initialY.toDouble() + distance / 2),
                    calculateConnection(directionsData, Point(middleX.toDouble(), initialY.toDouble() + distance)).toSet(),
                ),
                Junction(
                    Point(middleX.toDouble(), endY.toDouble() - distance / 2),
                    calculateConnection(directionsData, Point(middleX.toDouble(), endY.toDouble() + distance)).toSet(),
                ),
            )

        val trafficLight =
            junctions
                .mapIndexed { i, j ->
                    (0..2).map {
                        when (it) {
                            0 -> {
                                TrafficLight(Point(j.position.x + distance, j.position.y), "1", TrafficLightState.GREEN)
                            }

                            // Green
                            1 -> {
                                TrafficLight(
                                    Point(
                                        j.position.x,
                                        if (i % 2 == 0) {
                                            j.position.y + distance.toDouble() + 0.25 * distance
                                        } else {
                                            j.position.y - distance.toDouble() - 0.25 * distance
                                        },
                                    ),
                                    "1",
                                    TrafficLightState.YELLOW,
                                )
                            }

                            // Yellow
                            else -> {
                                TrafficLight(Point(j.position.x - distance, j.position.y), "1", TrafficLightState.RED)
                            } // Red
                        }
                    }
                }.flatten()

        val trafficSigns =
            setOf(
                TrafficSign(Point(100.0, 100.0), "stop"),
                TrafficSign(Point(200.0, 200.0), "yield"),
            )

        withContext(Dispatchers.Main) {
            _roadMap.value = directionsData
            _junctions.value = junctions.toSet()
            _trafficLights.value = trafficLight
            _trafficSigns.value = trafficSigns // Update the state flow on the main thread.
        }
    }
     */

    suspend fun getRoadMap() {
        val repository = RemoteRepository()
        val roads = repository.getRoads()
        val directionsData =
            roads.flatMap { road ->
                // val road = repository.getRoad(roadId)
                val tmp = repository.getFlows(road.roadNumber)
                return@flatMap tmp
            }
        _roadMap.value = directionsData.map { it.convert() }
        val trafficLight =
            directionsData.flatMap { direction ->
                repository.getTrafficLights(direction.roadId, direction.idDirection.toString())
            }
        _trafficLights.value = trafficLight.map { it.convert() }
        val signs =
            directionsData.flatMap { direction ->
                repository.getSigns(direction.roadId, direction.idDirection.toString()).signs
            }
        _trafficSigns.value = signs.map { it.convert() }.toSet()
    }

    private var started = false

    fun startSimulation(cars: Int) {
        if (started) return
        started = true
        CoroutineScope(Dispatchers.IO).launch {
            getRoadMap()
            // createCars(cars)
            startTrafficLight(CoroutineScope(Dispatchers.IO))
            // startCars()
            // changeLine()
        }
    }

    suspend fun createCars(cars: Int) {
        val speed = 10 // Set a default speed for the cars
        val newCars =
            (0..<cars)
                .map {
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
                            ).normalize() // Get the direction vector from the point to the next point
                    Car(
                        point,
                        speed,
                        direction,
                        directionIndex,
                        0,
                        nextPointIndex,
                    ) // Create a car with a random point and fixed speed
                }.toSet()
        withContext(Dispatchers.Main) {
            _cars.value = newCars // Update the state flow on the main thread.
        }
    }

    fun startTrafficLight(scope: CoroutineScope) {
        val remoteStream = RemoteStream()
        val mqttRepository = MqttRepository()

        _trafficLights.value.forEach { trafficLight ->
            scope.launch(Dispatchers.IO) {
                remoteStream
                    .trafficLightStateStream(trafficLight.id, mqttRepository)
                    .collect { newState ->
                        _trafficLights.update { list ->
                            list.map {
                                if (it.id == trafficLight.id) {
                                    it.changeState(newState)
                                } else {
                                    it
                                }
                            }
                        }
                    }
            }
        }
    }

    fun startCars() {
        CoroutineScope(Dispatchers.IO).launch {
            while (true) {
                delay(100) // Delay for 1 second
                val newCars =
                    _cars.value
                        .map { car ->
                            RoadUtilities.carMoveSafety(
                                car,
                                _roadMap.value,
                                _junctions.value,
                            ) // Move the car safely along the road
                        }.map { car ->
                            RoadUtilities.changeDirection(car, _junctions.value) // Change the direction of the car if needed
                        }.toSet()

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
                    _cars.value
                        .map { car ->
                            RoadUtilities.changeLine(
                                car,
                                _roadMap.value,
                            ) // Change the line of the car
                        }.toSet()
                withContext(Dispatchers.Main) {
                    _cars.value = newCars // Update the state flow on the main thread.
                }
            }
        }
    }

    fun calculateConnection(
        directions: List<Direction>,
        junction: Point,
    ): List<JunctionConnection> =
        directions.mapIndexed { index, direction ->
            val closestPoint =
                direction.roads[0].minBy { point ->
                    Vector2D.fromPoints(point, junction).magnitude()
                }
            val closestPointIndex = direction.roads[0].indexOf(closestPoint)
            JunctionConnection(
                index,
                closestPointIndex,
                closestPoint,
                closestPointIndex > 3,
                closestPointIndex < direction.roads[0].size - 3,
            )
        }
}
