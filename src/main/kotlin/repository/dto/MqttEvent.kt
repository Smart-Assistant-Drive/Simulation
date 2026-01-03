package repository.dto

data class MqttEvent(
    val topic: String,
    val payload: String,
)
