package org.ninh.instaclone.controller

import org.ninh.instaclone.repository.PostRepository
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class CommentUpdateRequest(
    val postId: String,
    val action: Action
)

enum class Action {
    INCREMENT,
    DECREMENT
}

@RestController
@RequestMapping("/commentsCount")
class CommentsCountController(
    private val postRepository: PostRepository
) {
    @PostMapping("/update")
    fun updateCommentsCount(@RequestBody request: CommentUpdateRequest) {
        if (request.action == Action.INCREMENT) {
            postRepository.incrementCommentsCount(UUID.fromString(request.postId))
        } else if (request.action == Action.DECREMENT) {
            postRepository.decrementCommentsCount(UUID.fromString(request.postId))
        }
    }
}