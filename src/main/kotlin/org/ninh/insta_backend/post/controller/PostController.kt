package org.ninh.insta_backend.post.controller

import org.ninh.insta_backend.post.service.PostService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class ContentCreatedEvent(
    val authorId: String,
    val caption: String?,
    val mediaUrl: String
)

data class ContentCreatedRes(
    val error: String? = null
)

@RestController
@RequestMapping("/api/content")
class PostController(
    private val postService: PostService
) {
    @PostMapping("/post")
    fun savePost(@RequestBody contentCreatedEvent: ContentCreatedEvent): ResponseEntity<ContentCreatedRes> {
        return try {
            val res = postService.savePost(contentCreatedEvent)
            ResponseEntity.ok(res)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ContentCreatedRes(error = "Registration failed: ${ex.message}"))
        }
    }
}
