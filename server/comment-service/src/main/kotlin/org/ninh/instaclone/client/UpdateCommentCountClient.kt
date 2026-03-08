package org.ninh.instaclone.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

data class CommentUpdateRequest(
    val postId: String,
    val action: Action
)

enum class Action {
    INCREMENT,
    DECREMENT
}

@FeignClient(name = "comment-service", url = "\${post-consumer.url}")
interface PostConsumerClient {
    @PostMapping("/commentsCount/update")
    fun updateCommentCount(@RequestBody request: CommentUpdateRequest)
}
