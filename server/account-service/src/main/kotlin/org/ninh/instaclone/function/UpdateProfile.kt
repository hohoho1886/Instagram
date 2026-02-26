package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.dto.FollowCounterEvent
import org.ninh.instaclone.service.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

@Configuration
class UpdateProfile(
    private val userService: UserService
) {
    private val mapper = jacksonObjectMapper()
    @Bean
    fun followCounterHandler(): Function<String, String> = Function { body ->
        try {
            val pubSubJson = decodePubSubMessage(body)
            val event = mapper.readValue(pubSubJson, FollowCounterEvent::class.java)

            userService.updateFollowCounter(
                event.username,
                event.counterType,
                event.action
            )
            "Message processed"
        } catch (e: Exception) {
            throw RuntimeException("Processing failed: ${e.message}")
        }
    }


    private fun decodePubSubMessage(body: String): String {
        val root = mapper.readTree(body)
        val data = root.path("message").path("data").asText()
        return String(Base64.getDecoder().decode(data))
    }
}