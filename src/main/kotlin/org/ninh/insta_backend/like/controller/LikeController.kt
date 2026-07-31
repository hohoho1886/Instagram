package org.ninh.insta_backend.like.controller

import org.ninh.insta_backend.like.service.LikeService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class PostLikeEvent(
    val postId: String,
    val authorId: String
)

data class PostLikeResponse(
    val error: String? = null
)


@RestController
@RequestMapping("/api/likes")
class LikeController(
    private val likeService: LikeService
) {
    @PostMapping("/post")
    fun saveLike(@RequestBody likeEvent: PostLikeEvent): ResponseEntity<PostLikeResponse> {
        return try {
            val res = likeService.saveLike(likeEvent)
            ResponseEntity.ok(res)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(PostLikeResponse(error = "Like save failed: ${ex.message}"))
        }
    }
}
