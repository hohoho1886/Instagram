package org.ninh.instaclone.service

import com.hivemq.client.mqtt.datatypes.MqttQos
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import org.springframework.stereotype.Service

@Service
class MqttPublisher(
    private val mqttClient: Mqtt3AsyncClient,
) {
    fun publish(topic: String, message: String) {
        mqttClient.publishWith()
            .topic(topic)
            .payload(message.toByteArray())
            .qos(MqttQos.AT_MOST_ONCE)
            .retain(true)
            .send()
            .whenComplete { mqtt3Publish, throwable ->
                if (throwable != null) {
                    println("Publish failed: ${throwable.message}")
                } else {
                    println("Publish successful! Status code: ${mqtt3Publish?.toString()}")
                }
            }
    }
}