package org.ninh.instaclone.service

import jakarta.transaction.Transactional
import org.ninh.instaclone.dto.PostMessage
import org.ninh.instaclone.repository.PostRepository
import org.springframework.stereotype.Service
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.ninh.instaclone.model.Post
import java.util.UUID
import kotlin.time.Clock

@Service
class PostConsumer(
    private val postRepository: PostRepository
) {
    @Transactional
    fun save(postMessage: PostMessage){
        try {
            val newPost = Post(
                userId = UUID.fromString(postMessage.userId),
                mediaUrls = postMessage.mediaUrls,
                createdAt = Clock.System.now(),
                likesCount = postMessage.likesCount,
                commentsCount = postMessage.commentsCount
            )
            postRepository.save(newPost)
        } catch (e: Exception) {
            throw RuntimeException("Error saving post", e)
        }
    }
}