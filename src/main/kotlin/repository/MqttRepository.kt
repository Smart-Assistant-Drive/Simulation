package repository

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class MqttRepository(
    brokerUrl: String = "tcp://127.0.0.1:1883",
) {
    val mapper: ObjectMapper =
        ObjectMapper()
            .registerKotlinModule()
            .configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false,
            )

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _events =
        MutableSharedFlow<MqttEvent>(
            replay = 0,
            extraBufferCapacity = 64,
        )

    val events: SharedFlow<MqttEvent> = _events.asSharedFlow()

    private val client: MqttClient =
        MqttClient(brokerUrl, "client-" + java.util.UUID.randomUUID())

    init {
        val options =
            MqttConnectOptions().apply {
                isAutomaticReconnect = true
                isCleanSession = true
                keepAliveInterval = 30
            }

        client.setCallback(
            object : MqttCallback {
                override fun messageArrived(
                    topic: String,
                    message: MqttMessage,
                ) {
                    val payload = message.payload.toString(Charsets.UTF_8)
                    _events.tryEmit(MqttEvent(topic, payload))
                }

                override fun connectionLost(cause: Throwable?) {
                    println("MQTT lost: $cause")
                }

                override fun deliveryComplete(token: IMqttDeliveryToken?) {}
            },
        )

        scope.launch {
            runCatching {
                client.connect(options)
                client.subscribe("semaphore/+/change", 1)
                client.subscribe(
                    "trafficdt-digital-cars/+/cars/carUpdate",
                    1,
                )
            }.onFailure {
                it.printStackTrace()
            }
        }
    }
}
