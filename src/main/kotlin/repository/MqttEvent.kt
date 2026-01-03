package repository

data class MqttEvent(
    val topic: String,
    val payload: String,
)
