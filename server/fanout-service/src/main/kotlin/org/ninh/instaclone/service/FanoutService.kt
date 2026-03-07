package org.ninh.instaclone.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PubsubMessage
import org.ninh.instaclone.client.FollowersCountClient
import org.ninh.instaclone.client.FollowersCountRequest
import org.ninh.instaclone.function.UploadMessage
import org.ninh.instaclone.repository.FollowRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID
import java.util.concurrent.TimeUnit

data class FanoutPushEvent(
    val postId: String,
    val followersIds: List<String>
)

@Service
class FanoutService(
    private val followRepository: FollowRepository,
    @param:Value($$"${spring.cloud.gcp.project-id}")
    private val projectId: String,
    @param:Value($$"${spring.cloud.gcp.topic}")
    private val topic: String,
    private val followersCountClient: FollowersCountClient,
    @param:Value($$"${limit.celebrity}")
    private val celebrityLimit: Int
) {
    private val objectMapper = jacksonObjectMapper()

    fun publishFanoutPush(dto: UploadMessage) {
        if (followersCountClient.getFollowersCount(
                FollowersCountRequest(dto.authorId)
        ) < celebrityLimit){
            val followers = followRepository
                .findAcceptedFollowers(UUID.fromString(dto.authorId))
                .map { it.toString() }
            val fanoutPushEvent = FanoutPushEvent(dto.postId, followers)
            publish(fanoutPushEvent)
        }
    }

    private fun publish(fanoutPushEvent: FanoutPushEvent) {
        val messageJson = objectMapper.writeValueAsString(fanoutPushEvent)

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