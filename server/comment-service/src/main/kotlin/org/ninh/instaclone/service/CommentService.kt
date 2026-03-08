package org.ninh.instaclone.service

import org.ninh.instaclone.client.Action
import org.ninh.instaclone.client.CommentUpdateRequest
import org.ninh.instaclone.client.PostConsumerClient
import org.ninh.instaclone.function.CommentResponse
import org.ninh.instaclone.function.DeleteCommentEvent
import org.ninh.instaclone.function.SaveCommentEvent
import org.ninh.instaclone.model.Comment
import org.ninh.instaclone.model.CommentKey
import org.ninh.instaclone.repository.CommentRepository
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class CommentService(
    private val commentRepository: CommentRepository,
    private val postConsumerClient: PostConsumerClient
){
    fun saveComment(saveCommentEvent: SaveCommentEvent) {
        val key = CommentKey(
            postId = UUID.fromString(saveCommentEvent.postId)
        )
        val comment = Comment(
            key = key,
            authorId = UUID.fromString(saveCommentEvent.authorId),
            parentCommentId = UUID.fromString(saveCommentEvent.parentCommentId),
            content = saveCommentEvent.content
        )
        commentRepository.save(comment)
        postConsumerClient.updateCommentCount(
            CommentUpdateRequest(
            postId = saveCommentEvent.postId,
                action = Action.INCREMENT
        ))
    }

    fun deleteComment(deleteCommentEvent: DeleteCommentEvent) {
        val key = CommentKey(
            postId = UUID.fromString(deleteCommentEvent.postId),
            createdAt = kotlin.time.Instant.parse(deleteCommentEvent.createdAt),
            commentId = UUID.fromString(deleteCommentEvent.commentId)
        )
        commentRepository.deleteById(key)
        postConsumerClient.updateCommentCount(
            CommentUpdateRequest(
                postId = deleteCommentEvent.postId,
                action = Action.DECREMENT
            )
        )
    }

    fun getCommentsByPost(postId: String): List<CommentResponse> {
        return commentRepository
            .findByPostId(UUID.fromString(postId))
            .map {
                CommentResponse(
                    postId = it.key.postId.toString(),
                    commentId = it.key.commentId.toString(),
                    createdAt = it.key.createdAt.toString(),
                    authorId = it.authorId.toString(),
                    parentCommentId = it.parentCommentId.toString(),
                    content = it.content,
                    likesCount = it.likesCount
                )
            }
    }


}