package org.ninh.insta_backend.like.service

import jakarta.transaction.Transactional
import org.ninh.insta_backend.like.controller.PostLikeEvent
import org.ninh.insta_backend.like.controller.PostLikeResponse
import org.ninh.insta_backend.like.repository.LikeRepository
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LikeService(
    private val likeRepository: LikeRepository
) {
    @Transactional
    fun saveLike(postLikeEvent: PostLikeEvent): PostLikeResponse {
        return try {
            likeRepository.upsertLike(
                postId = UUID.fromString(postLikeEvent.postId),
                authorId = UUID.fromString(postLikeEvent.authorId))
            PostLikeResponse()
        } catch (e: PSQLException) {
            PostLikeResponse(error = e.message)
        }
    }
}
