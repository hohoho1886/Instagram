package org.ninh.insta_backend.post.service

import org.ninh.insta_backend.post.controller.ContentCreatedEvent
import org.ninh.insta_backend.post.controller.ContentCreatedRes
import org.ninh.insta_backend.post.model.Post
import org.ninh.insta_backend.post.repository.PostRepository
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PostService(
    private val postRepository: PostRepository
) {
    fun savePost(contentCreatedEvent: ContentCreatedEvent): ContentCreatedRes {
        val post = Post(
            authorId = UUID.fromString(contentCreatedEvent.authorId),
            caption = contentCreatedEvent.caption,
            mediaUrl = contentCreatedEvent.mediaUrl
        )
        return try {
            postRepository.save(post)
            ContentCreatedRes()
        } catch (e: PSQLException) { // TODO: it seems that save does not throw when user register with same username
            ContentCreatedRes(error = e.message)
        }
    }
}
