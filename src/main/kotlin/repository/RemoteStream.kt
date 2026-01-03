package repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import model.domain.TrafficLightState
import repository.dto.TrafficLightMessage

class RemoteStream(
    // val brokerUrl: String = "tcp://127.0.0.1:1883",
) {
    fun trafficLightStateStream(
        id: String,
        mqttRepository: MqttRepository,
    ): Flow<TrafficLightState> =
        mqttRepository.events
            .filter { it.topic == "semaphore/$id/change" }
            .mapNotNull { event ->
                runCatching {
                    mqttRepository.mapper
                        .readValue(event.payload, TrafficLightMessage::class.java)
                }.getOrNull()
            }.map { msg ->
                when (msg.color) {
                    "green" -> TrafficLightState.GREEN
                    "yellow" -> TrafficLightState.YELLOW
                    else -> TrafficLightState.RED
                }
            }
}
