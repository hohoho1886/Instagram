package org.ninh.instaclone.service

import org.ninh.instaclone.dto.UploadMessage
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class PostMatService(
    private val redisTemplate: RedisTemplate<String, UploadMessage>
) {
    fun pushToRedis(post: UploadMessage) {
        val key = post.postId
        redisTemplate.opsForValue().set(key, post, Duration.ofMinutes(5))
    }
}