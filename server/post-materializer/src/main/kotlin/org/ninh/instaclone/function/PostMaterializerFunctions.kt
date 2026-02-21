package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.dto.UploadMessage
import org.ninh.instaclone.service.PostMatService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

@Configuration
class PostMaterializerFunctions(
    private val postMatService: PostMatService
) {
    private val mapper = jacksonObjectMapper()
    @Bean
    fun saveInCache(): Function<String, String> = Function { body ->
        try {
            val pubSubJson = decodePubSubMessage(body)
            val dto = mapper.readValue(pubSubJson, UploadMessage::class.java)
            postMatService.pushToRedis(dto)
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