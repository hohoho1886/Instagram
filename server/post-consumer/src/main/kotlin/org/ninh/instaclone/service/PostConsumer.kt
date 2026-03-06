package org.ninh.instaclone.service

import jakarta.transaction.Transactional
import org.ninh.instaclone.dto.PostDto
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
                authorId = UUID.fromString(uploadMessage.authorId),
                mediaUrls = uploadMessage.mediaUrls,

            )
            postRepository.save(newPost)
        } catch (e: Exception) {
            throw RuntimeException("Error saving post", e)
        }
    }
    fun getPostsByIds(postIds: List<UUID>): List<PostDto> {
        try {
            val posts =  postRepository.findAllByPostIds(postIds)
            return posts.map {
                PostDto(
                    postId = it.postId.toString(),
                    mediaUrl = it.mediaUrls,
                    createdAt = it.createdAt,
                    likesCount = it.likesCount,
                    commentsCount = it.commentsCount,
                )
            }
        } catch (e: Exception){
            throw RuntimeException("Error getting post", e)
        }
    }

}