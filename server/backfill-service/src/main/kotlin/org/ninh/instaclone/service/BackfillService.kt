package org.ninh.instaclone.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PubsubMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

typealias PostId = String
typealias Username = String


data class UploadMessage(
    val postId: String,
    val userId: String,
    val mediaUrls: String,
    val timestamp: String
)

data class NewPosts(
    val userUsername: Username,
    val posts: List<PostId>
)


@Service
class BackfillService(
    private val redisTemplate: RedisTemplate<String, String>,
    @param:Value("\${spring.cloud.gcp.project-id}")
    private val projectId: String,
    @param:Value("\${spring.cloud.gcp.topic}")
    private val topic: String
) {
    private val objectMapper = jacksonObjectMapper()

    fun getPostsForFollowedUsers(followedUsersUsername: List<Username>): List<PostId>{
        val keys = followedUsersUsername.map { "posts:$it" }
        val result = redisTemplate.executePipelined { connection ->
            keys.forEach { key ->
                connection.zSetCommands().zRevRange(key.toByteArray(), 0, -1)
            }
            null
        }
        return result
            .filterIsInstance<Set<String>>()
            .flatten()
            .map { post ->
                val postDto = objectMapper.readValue(post, UploadMessage::class.java)
                postDto.postId
            }
    }

    fun publish(posts: NewPosts){
        val messageJson = objectMapper.writeValueAsString(posts)
        val topicName = ProjectTopicName.of(projectId, topic)
        val pubsubMessage = PubsubMessage.newBuilder()
            .setData(ByteString.copyFromUtf8(messageJson))
            .build()
        val publisher: Publisher = Publisher.newBuilder(topicName).build()
        try {
            val messageId = publisher.publish(pubsubMessage).get()
            println("Published message with ID: $messageId")
        } finally {
            publisher.shutdown()
            publisher.awaitTermination(1, TimeUnit.MINUTES)
        }
    }
}