package org.ninh.instaclone.controller

import org.ninh.instaclone.dto.PostDto
import org.ninh.instaclone.service.PostConsumer
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

data class PostSearchRequest(val postIds: List<UUID>)

@RestController
@RequestMapping("/posts")
class PostController(
    private val postConsumer: PostConsumer
) {
    @PostMapping("/search")
    fun getPosts(@RequestBody request: PostSearchRequest): List<PostDto> {
        return postConsumer.getPostsByIds(request.postIds)
    }
}