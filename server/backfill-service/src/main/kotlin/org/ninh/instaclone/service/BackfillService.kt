package org.ninh.instaclone.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service

typealias PostId = String
typealias Username = String


data class UploadMessage(
    val postId: String,
    val userId: String,
    val mediaUrls: String,
    val timestamp: String
)

@Service
class BackfillService(private val redisTemplate: RedisTemplate<String, String>) {
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
}