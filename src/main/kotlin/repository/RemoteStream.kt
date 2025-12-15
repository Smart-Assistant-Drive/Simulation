package repository

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.map
import model.domain.TrafficLightState
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttClient
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.eclipse.paho.client.mqttv3.MqttMessage

class RemoteStream(
    val brokerUrl: String = "tcp://127.0.0.1:1883",
) {
    private fun mqttFlow(
        brokerUrl: String,
        topic: String,
        clientId: String = MqttClient.generateClientId(),
    ): Flow<String> =
        callbackFlow {
            val client = MqttClient(brokerUrl, clientId, null)
            val options =
                MqttConnectOptions().apply {
                    isAutomaticReconnect = true
                    isCleanSession = true
                }

            client.setCallback(
                object : MqttCallback {
                    override fun messageArrived(
                        topic: String,
                        message: MqttMessage,
                    ) {
                        trySend(message.toString())
                    }

                    override fun connectionLost(cause: Throwable?) {
                        println("MQTT connection lost: $cause")
                    }

                    override fun deliveryComplete(token: IMqttDeliveryToken?) {}
                },
            )

            client.connect(options)
            client.subscribe(topic)

            awaitClose {
                client.disconnect()
                client.close()
            }
        }

    fun trafficLightStateStream(id: String): Flow<TrafficLightState> {
        val topic = "semaphore/$id/light"
        return mqttFlow(brokerUrl, topic).map { s ->
            return@map when (s) {
                "green" -> {
                    TrafficLightState.GREEN
                }

                "yellow" -> {
                    TrafficLightState.YELLOW
                }

                else -> {
                    TrafficLightState.RED
                }
            }
        }
    }
}
