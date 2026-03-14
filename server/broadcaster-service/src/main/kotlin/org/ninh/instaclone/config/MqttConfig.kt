package org.ninh.instaclone.config

import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.UUID

@Configuration
class MqttConfig(
    @param:Value("\${mqtt.host}")
    private val host: String,
    @param:Value("\${mqtt.username}")
    private val username: String,
    @param:Value("\${mqtt.password}")
    private val password: String
) {
    @Bean
    fun mmqttClient(): Mqtt3AsyncClient {
        val client = Mqtt3Client.builder()
            .identifier(UUID.randomUUID().toString())
            .serverHost(host)
            .serverPort(8883)
            .sslWithDefaultConfig()
            .buildAsync()
        client.connectWith()
            .simpleAuth()
            .username(username)
            .password(password.toByteArray())
            .applySimpleAuth()
            .send()
            .whenComplete { connAck, throwable ->
                if (throwable != null) {
                    println("Connection failed: ${throwable.message}")
                } else {
                    println("Connected successfully! Status code: ${connAck?.toString()}")
                }
            }
        return client
    }
}