package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.service.LikeService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

data class LikeDto(
    val postId: String,
    val userId: String
)

@Configuration
class LikeConsumerFunctions(
    private val likeService: LikeService
) {
    private val mapper = jacksonObjectMapper()

    @Bean
    fun saveLike(): Function<String, String> = Function { body ->
        consumeMessage(body, LikeDto::class.java) { dto ->
            likeService.save(dto)
        }
    }

    @Bean
    fun test(): Function<LikeDto, String> = Function { req ->
        likeService.save(req)
        "ok"
    }

    private fun <T> consumeMessage(
        body: String,
        dtoClass: Class<T>,
        handler: (T) -> Unit
    ): String {
        return try {
            val pubSubJson = decodePubSubMessage(body)
            val dto = mapper.readValue(pubSubJson, dtoClass)
            handler(dto)
            "Message processed"
        } catch (e: Exception) {
            e.printStackTrace()
            throw RuntimeException("Processing failed: ${e.message}", e)
        }
    }


    private fun decodePubSubMessage(body: String): String {
        val root = mapper.readTree(body)
        val data = root.path("message").path("data").asText()
        return String(Base64.getDecoder().decode(data))
    }
}