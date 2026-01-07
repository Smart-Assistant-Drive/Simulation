package viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.domain.Car
import model.domain.Direction
import model.domain.Junction
import model.domain.JunctionConnection
import model.domain.TrafficLight
import model.domain.TrafficSign
import model.math.Point
import model.math.Vector2D
import repository.MqttRepository
import repository.RemoteRepository
import repository.RemoteStream
import viewmodel.mapper.Mapper.convert
import kotlin.collections.setOf

class SimulationViewModel {
    private val _roadMap = MutableStateFlow<List<Direction>>(listOf())
    val roadMap: StateFlow<List<Direction>> = _roadMap.asStateFlow()

    private val _junctions = MutableStateFlow<Set<Junction>>(setOf())
    val junctions: StateFlow<Set<Junction>> = _junctions.asStateFlow()

    private val _trafficLights = MutableStateFlow<List<TrafficLight>>(listOf())
    val trafficLights = _trafficLights // Expose as read-only
    private val _cars = MutableStateFlow(setOf<Car>())
    val cars: StateFlow<Set<Car>> = _cars.asStateFlow()
    private val _trafficSigns = MutableStateFlow<Set<TrafficSign>>(setOf())
    val trafficSigns: StateFlow<Set<TrafficSign>> = _trafficSigns.asStateFlow()

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

    fun startSimulation() {
        if (started) return
        started = true
        val mqttRepository = MqttRepository()
        CoroutineScope(Dispatchers.IO).launch {
            getRoadMap()
            startTrafficLight(mqttRepository, CoroutineScope(Dispatchers.IO))
            carsListener(mqttRepository, CoroutineScope(Dispatchers.IO))
        }
    }

    fun startTrafficLight(
        mqttRepository: MqttRepository,
        scope: CoroutineScope,
    ) {
        val remoteStream = RemoteStream()

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

    fun carsListener(
        mqttRepository: MqttRepository,
        scope: CoroutineScope,
    ) {
        val remoteStream = RemoteStream()
        scope.launch(Dispatchers.IO) {
            remoteStream
                .carsStream(mqttRepository)
                .collect { newCar ->
                    _cars.update { cars ->
                        val mutableCars = cars.toMutableSet()
                        val existingCar = mutableCars.find { it.id == newCar.id }
                        if (existingCar != null) {
                            mutableCars.remove(existingCar)
                        }
                        mutableCars.add(newCar)
                        mutableCars.toSet()
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
