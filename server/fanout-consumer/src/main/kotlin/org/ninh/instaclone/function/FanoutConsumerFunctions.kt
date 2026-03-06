package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.service.FanoutConsumer
import org.ninh.instaclone.service.PostId
import org.ninh.instaclone.service.ReceiverUserName
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

data class FanoutPullRequest(
    val posts: List<PostId>,
    val receiverUsername: String
)

data class FanoutPushRequest(
    val postId: PostId,
    val followersUsername: List<ReceiverUserName>
)

@Configuration
class FanoutConsumerFunctions(
    private val fanoutConsumer: FanoutConsumer
) {
    private val mapper = jacksonObjectMapper()

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

    @Bean
    fun consumePostPull(): Function<String, String> = Function { body ->
        consumeMessage(body, FanoutPullRequest::class.java) { dto ->
            fanoutConsumer.savePostsPull(dto.receiverUsername, dto.posts)
        }
    }

    @Bean
    fun consumePostPush(): Function<String, String> = Function { body ->
        consumeMessage(body, FanoutPushRequest::class.java) { dto ->
            fanoutConsumer.savePostsPush(dto.postId, dto.followersUsername)
        }
    }

    private fun decodePubSubMessage(body: String): String {
        val root = mapper.readTree(body)
        val data = root.path("message").path("data").asText()
        return String(Base64.getDecoder().decode(data))
    }
}