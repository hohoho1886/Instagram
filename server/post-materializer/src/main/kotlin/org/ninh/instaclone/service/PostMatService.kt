package org.ninh.instaclone.service

import org.ninh.instaclone.dto.UploadMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SessionCallback
import org.springframework.stereotype.Service
import java.time.Duration

@Service
class PostMatService(
    private val redisTemplate: RedisTemplate<String, UploadMessage>,
    @param:Value("\${redis.post.ttl}")
    private val postTtl: Duration
) {
    fun pushToRedis(post: UploadMessage) {
        val userSetKey = "posts:${post.userId}"
        val postOwnershipKey = "postId:${post.postId}"
        val score = post.timestamp.toDouble()

        redisTemplate.execute(object: SessionCallback<List<Any>> {
            override fun <K : Any, V : Any> execute(operations: RedisOperations<K, V>): List<Any> {
                val ops = operations as RedisOperations<String, UploadMessage>
                ops.watch(postOwnershipKey)
                if (ops.hasKey(postOwnershipKey)){
                    ops.unwatch()
                    return emptyList()
                }
                ops.multi()
                ops.opsForZSet().add(userSetKey, post, score)
                ops.opsForZSet().add(postOwnershipKey, post, score)
                ops.expire(userSetKey, postTtl)
                ops.expire(postOwnershipKey, postTtl)
                return ops.exec()
            }
        })
    }
}