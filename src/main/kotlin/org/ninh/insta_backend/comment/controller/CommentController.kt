package org.ninh.insta_backend.comment.controller

import org.ninh.insta_backend.comment.service.CommentService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class PostCommentEvent(
    val postId: String,
    val authorId: String,
    val content: String
)

data class PostCommentResponse(
    val error: String? = null
)

@RestController
@RequestMapping("/api/comment")
class CommentController(
    private val commentService: CommentService
) {
    @PostMapping("/post")
    fun postComment(@RequestBody req: PostCommentEvent): ResponseEntity<PostCommentResponse> {
        return try {
            val res = commentService.saveComment(req)
            ResponseEntity.ok(res)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(PostCommentResponse(error = "Registration failed: ${ex.message}"))
        }
    }
}
