package org.ninh.insta_backend.comment.service

import org.ninh.insta_backend.comment.controller.PostCommentEvent
import org.ninh.insta_backend.comment.controller.PostCommentResponse
import org.ninh.insta_backend.comment.model.Comment
import org.ninh.insta_backend.comment.repository.CommentRepository
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Service
import java.util.UUID



@Service
class CommentService(
    private val commentRepository: CommentRepository,
) {
    fun saveComment(postCommentEvent: PostCommentEvent): PostCommentResponse{
        val comment = Comment(
            postId = UUID.fromString(postCommentEvent.postId),
            authorId = UUID.fromString(postCommentEvent.authorId),
            content = postCommentEvent.content
        )
        commentRepository.save(comment)
        return try {
            commentRepository.save(comment)
            PostCommentResponse()
        } catch (e: PSQLException) { // TODO: it seems that save does not throw when user register with same username
            PostCommentResponse(error = e.message)
        }
    }
}
