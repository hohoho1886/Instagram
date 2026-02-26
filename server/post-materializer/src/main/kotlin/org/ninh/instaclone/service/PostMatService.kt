package org.ninh.instaclone.service

import org.ninh.instaclone.dto.UploadMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class PostMatService(
    private val redisTemplate: RedisTemplate<String, UploadMessage>,
    @param:Value("\${redis.post.ttl}")
    private val postTtl: Duration,
) {
    fun pushToRedis(post: UploadMessage) {
        val key = post.postId
        redisTemplate.opsForValue().set(key, post, postTtl)
    }
}