package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.dto.PostMessage
import org.ninh.instaclone.service.PostConsumer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

@Configuration
class PostConsumerFunction (
    private val postConsumer: PostConsumer
){
    private val mapper = jacksonObjectMapper()

    @Bean
    fun consumePost(): Function<String, String> = Function { body ->
        try {
            val pubSubJson = decodePubSubMessage(body)
            val dto = mapper.readValue(pubSubJson, PostMessage::class.java)
            postConsumer.save(dto)
            "Message processed"
        } catch (e: Exception){
            e.printStackTrace()
            throw RuntimeException("Processing failed")
        }

    }

    private fun decodePubSubMessage(body: String): String {
        val root = mapper.readTree(body)
        val data = root.path("message").path("data").asText()
        return String(Base64.getDecoder().decode(data))
    }
}