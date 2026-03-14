package org.ninh.instaclone.service

import com.google.cloud.tasks.v2.CloudTasksClient
import com.google.cloud.tasks.v2.HttpMethod
import com.google.cloud.tasks.v2.HttpRequest
import com.google.cloud.tasks.v2.QueueName
import com.google.cloud.tasks.v2.Task
import com.google.protobuf.Timestamp
import org.ninh.instaclone.function.LikeDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CacheService(
    private val redisTemplate: RedisTemplate<String, String>,
    @param:Value("\${spring.cloud.gcp.project-id}") private val projectId: String,
    @param:Value("\${spring.cloud.task.queue.region}") private val queueRegion: String,
    @param:Value("\${spring.cloud.task.queue.id}") private val queueName: String,
    @param:Value("\${target.url}") private val targetUrl: String
) {
    // TODO: make bloom filter
    private fun incrementIfUnique(postId: String, userId: String): Boolean {
        val setKey = "post:likes:users:$postId"
        val countKey = "post:likes:count:$postId"

        val isNewLike = redisTemplate.opsForSet().add(setKey, userId) == 1L

        if (isNewLike) {
            redisTemplate.opsForValue().increment(countKey)
        }
        return isNewLike
    }
    fun incrementLike(likeDto: LikeDto) {
        incrementIfUnique(likeDto.postId, likeDto.userId)
    }

    fun createTask() {
        CloudTasksClient.create().use { client ->
            val url = targetUrl

            val queuePath = QueueName.of(projectId, queueRegion, queueName).toString()

            val now = Instant.now()
            val alarmTime = Timestamp.newBuilder()
                .setSeconds(now.epochSecond + 1)
                .setNanos(now.nano)
                .build()

            val taskBuilder = Task.newBuilder()
                .setHttpRequest(
                    HttpRequest.newBuilder()
                        .setUrl(url)
                        .setHttpMethod(HttpMethod.POST)
                        .build()
                )
                .setScheduleTime(alarmTime)


            val task = client.createTask(queuePath, taskBuilder.build())

            println("Task created: ${task.name}")
        }
    }
}