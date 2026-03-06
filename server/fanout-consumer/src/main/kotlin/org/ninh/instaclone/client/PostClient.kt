package org.ninh.instaclone.client

import org.ninh.instaclone.dto.PostDto
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import java.util.UUID

data class PostSearchRequest(val postIds: List<UUID>)

@FeignClient(name = "fanout-consumer", url = "\${post-consumer.url}")
interface PostClient {
    @PostMapping("/posts/search")
    fun getPostById(@RequestBody request: PostSearchRequest): List<PostDto>
}