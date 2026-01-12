package repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import model.domain.Car
import model.domain.TrafficLightState
import model.math.Point
import model.math.Vector2D
import repository.dto.CarUpdateMessage
import repository.dto.TrafficLightMessage

class RemoteStream(
    // val brokerUrl: String = "tcp://127.0.0.1:1883",
) {
    companion object {
        private fun trafficLightTopic(id: String) = "semaphore/$id/change"

        private const val CAR_UPDATE_TOPIC =
            "trafficdt-digital-cars/+/cars/carUpdate"
    }

    private fun TrafficLightMessage.toState(): TrafficLightState =
        when (color.lowercase()) {
            "green" -> TrafficLightState.GREEN
            "yellow" -> TrafficLightState.YELLOW
            else -> TrafficLightState.RED
        }

    fun trafficLightStateStream(
        id: String,
        mqttRepository: MqttRepository,
    ): Flow<TrafficLightState> =
        mqttRepository.events
            .filter { it.topic == trafficLightTopic(id) }
            .mapNotNull { event ->
                runCatching {
                    mqttRepository.mapper
                        .readValue(event.payload, TrafficLightMessage::class.java)
                }.getOrNull()
            }.map { msg ->
                msg.toState()
            }.distinctUntilChanged()

    fun carsStream(mqttRepository: MqttRepository): Flow<Car> =
        mqttRepository.events
            .filter { it.topic.matches(Regex(CAR_UPDATE_TOPIC.replace("+", "[^/]+"))) }
            .mapNotNull { event ->
                runCatching {
                    mqttRepository.mapper
                        .readValue(event.payload, CarUpdateMessage::class.java)
                }.getOrNull()
            }.map {
                Car(
                    id = it.idCar,
                    position = Point(x = it.positionX, y = it.positionY),
                    speed = it.currentSpeed,
                    direction = Vector2D(x = it.dPointX, y = it.dPointY).normalize(),
                    lineIndex = it.indexLane,
                )
            }
}
