package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.service.FanoutService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

data class UploadMessage(
    val postId: String,
    val authorId: String,
    val mediaUrls: String
)


@Configuration
class FanoutServiceFunction(
    private val fanoutService: FanoutService
){
    private val mapper = jacksonObjectMapper()
    @Bean
    fun fanoutPush(): Function<String, String> = Function { body ->
        try {
            val pubSubJson = decodePubSubMessage(body)
            val dto = mapper.readValue(pubSubJson, UploadMessage::class.java)
            fanoutService.publishFanoutPush(dto)
            "Message processed"
        } catch (e: Exception){
            e.printStackTrace()
            throw RuntimeException("Processing failed: " + e.message)
        }
    }

    private fun decodePubSubMessage(body: String): String {
        val root = mapper.readTree(body)
        val data = root.path("message").path("data").asText()
        return String(Base64.getDecoder().decode(data))
    }
}