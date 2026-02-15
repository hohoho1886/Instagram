package org.ninh.instaclone.service

import jakarta.transaction.Transactional
import org.ninh.instaclone.dto.UploadMessage
import org.ninh.instaclone.repository.PostRepository
import org.springframework.stereotype.Service
import org.ninh.instaclone.model.Post
import java.util.UUID

@Service
class PostConsumer(
    private val postRepository: PostRepository
) {
    @Transactional
    fun save(uploadMessage: UploadMessage){
        try {
            val newPost = Post(
                postId = UUID.fromString(uploadMessage.postId),
                userId = UUID.fromString(uploadMessage.userId),
                mediaUrls = uploadMessage.mediaUrls
            )
            postRepository.save(newPost)
        } catch (e: Exception) {
            throw RuntimeException("Error saving post", e)
        }
    }
}