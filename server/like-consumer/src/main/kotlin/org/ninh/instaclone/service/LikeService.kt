package org.ninh.instaclone.service

import org.ninh.instaclone.function.LikeDto
import org.ninh.instaclone.model.Like
import org.ninh.instaclone.model.LikeKey
import org.ninh.instaclone.repository.LikeRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class LikeService(
    private val likeRepository: LikeRepository
) {
    fun save(likedto: LikeDto){
        val key = LikeKey(
            postId = UUID.fromString(likedto.postId),
            userId = UUID.fromString(likedto.userId)
        )
        val like = Like(key = key)
        likeRepository.save(like)
    }
}