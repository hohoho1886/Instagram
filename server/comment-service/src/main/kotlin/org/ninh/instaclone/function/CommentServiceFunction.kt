package org.ninh.instaclone.function

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.service.CommentService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.Base64
import java.util.function.Function

data class SaveCommentEvent(
    val postId: String,
    val authorId: String,
    val parentCommentId: String?,
    val content: String
)

data class DeleteCommentEvent(
    val postId: String,
    val createdAt: String,
    val commentId: String
)

data class CommentResponse(
    val postId: String,
    val commentId: String,
    val createdAt: String,
    val authorId: String,
    val parentCommentId: String?,
    val content: String,
    val likesCount: Long
)

data class CommentByPost(
    val postId: String
)

@Configuration
class CommentServiceFunction(
    private val commentService: CommentService
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

    private fun decodePubSubMessage(body: String): String {
        val root = mapper.readTree(body)
        val data = root.path("message").path("data").asText()
        return String(Base64.getDecoder().decode(data))
    }

    @Bean
    fun saveComment(): Function<SaveCommentEvent, String> = Function { comment ->
        commentService.saveComment(comment)
        "Comment processed"
    }

    @Bean
    fun deleteComment(): Function<DeleteCommentEvent, String> = Function { comment ->
        commentService.deleteComment(comment)
        "Comment processed"
    }

    @Bean
    fun getCommentsByPost(): Function<CommentByPost, String> = Function{ post ->
        val dtos = commentService.getCommentsByPost(post.postId)
        mapper.writeValueAsString(dtos)
    }


}