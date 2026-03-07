package org.ninh.instaclone.service

import com.datastax.oss.driver.api.core.CqlSession
import com.datastax.oss.driver.api.core.cql.AsyncResultSet
import com.datastax.oss.driver.api.core.cql.PreparedStatement
import org.ninh.instaclone.client.PostClient
import org.ninh.instaclone.client.PostSearchRequest
import org.ninh.instaclone.dto.PostDto
import org.ninh.instaclone.model.Post
import org.ninh.instaclone.model.PostKey
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletionStage

typealias PostId = String
typealias ReceiverUserId = String

@Service
class FanoutConsumer(
    private val postClient: PostClient,
    private val session: CqlSession
) {
    private fun getPost(postIds: List<UUID>): List<PostDto> {
        return postClient.getPostById(PostSearchRequest(postIds = postIds))
    }

    private fun createFuture(postsToWrite: List<Post>): CompletableFuture<Void> {
        val future: List<CompletionStage<AsyncResultSet>> = postsToWrite.map { post ->
            val boundStatement = preparedUpdate.bind(
                post.key.receiverUserId,
                post.key.createdAt,
                post.key.postId,
                post.mediaUrls,
                post.likesCount,
                post.commentsCount
            )
            session.executeAsync(boundStatement)
                .whenComplete { session, throwable ->
                    if (throwable != null) {
                        throw RuntimeException("Error occurred while saving post")
                    }
                }
        }
        return CompletableFuture.allOf(*future.map { it.toCompletableFuture() }.toTypedArray())
    }

    fun asyncWritePush(postDto: PostDto, followers: List<ReceiverUserId>): CompletableFuture<Void>{
        val postsToWrite = followers.map {
            val postKey = PostKey(
                receiverUserId = it,
                createdAt = postDto.createdAt,
                postId = UUID.fromString(postDto.postId),
            )
            Post(
                key = postKey,
                mediaUrls = postDto.mediaUrl,
                likesCount = postDto.likesCount,
                commentsCount = postDto.commentsCount
            )
        }
        return createFuture(postsToWrite)
    }

    private fun asyncWritePull(postDtos: List<PostDto>, receiverUsername: String): CompletableFuture<Void>{
        val postsToWrite = postDtos.map {
            val postKey = PostKey(
                receiverUserId = receiverUsername,
                createdAt = it.createdAt,
                postId = UUID.fromString(it.postId)
            )
            Post(
                key = postKey,
                mediaUrls = it.mediaUrl,
                likesCount = it.likesCount,
                commentsCount = it.commentsCount
            )
        }
        return createFuture(postsToWrite)
    }
    fun savePostsPull(receiverUsername: String, postIds: List<PostId>): CompletableFuture<Void>{
        val postDtos = getPost(postIds.map { UUID.fromString(it) })
        return asyncWritePull(postDtos, receiverUsername)
    }

    fun savePostsPush(postId: PostId, followers: List<ReceiverUserId>): CompletableFuture<Void>{
        val postDto = getPost(listOf(UUID.fromString(postId)))[0]
        return asyncWritePush(postDto, followers)
    }

    private val preparedUpdate: PreparedStatement = session.prepare(
        "INSERT INTO posts (receiver_userId, created_at, post_id, media_urls, likes_count, comments_count) " +
                "VALUES (?, ?, ?, ?, ?, ?)"
    )
}