package org.ninh.instaclone.service

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.google.cloud.pubsub.v1.Publisher
import com.google.protobuf.ByteString
import com.google.pubsub.v1.ProjectTopicName
import com.google.pubsub.v1.PubsubMessage
import jakarta.transaction.Transactional
import org.ninh.instaclone.dto.FollowRequest
import org.ninh.instaclone.dto.HandleFollowRequest
import org.ninh.instaclone.model.Follow
import org.ninh.instaclone.model.FollowId
import org.ninh.instaclone.model.FollowStatus
import org.ninh.instaclone.repository.FollowRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.concurrent.TimeUnit

@Service
class FollowService(
    val followRepository: FollowRepository,
    @param:Value("\${spring.cloud.gcp.project-id}")
    private val projectId: String,
    @param:Value("\${spring.cloud.gcp.topic}")
    private val topic: String
) {
    private val objectMapper = jacksonObjectMapper()

    @Transactional
    fun createFollowRequest(request: FollowRequest){
        val followId = FollowId(
            followerUsername = request.followerUsername,
            followeeUsername = request.followeeUsername
        )
        val newFollow = Follow(id = followId)
        try {
            followRepository.save(newFollow)
        } catch (e: Exception){
            throw RuntimeException("Error create follow request:", e)
        }
    }

    @Transactional
    fun handleFollowRequest(followRequest: HandleFollowRequest){
        val followId = FollowId(
            followRequest.followeeUsername,
            followRequest.followerUsername
        )
        try {
            val follow = followRepository.findById(followId)
                .orElseThrow { IllegalArgumentException("Follow request not found") }
            when (followRequest.status) {
                FollowStatus.ACCEPTED -> {
                    followRepository.save(follow.copy(status = FollowStatus.ACCEPTED))
                    publishActiveFollowEvent(
                        FollowRequest(
                            followRequest.followeeUsername,
                            followRequest.followerUsername)
                    )
                }
                FollowStatus.REJECTED -> {
                    followRepository.delete(follow)
                }
                else -> {}
            }
        } catch (e: Exception){
            throw RuntimeException("Error handle follow request:", e)
        }
    }

    fun publishActiveFollowEvent(request: FollowRequest){
        val messageJson = objectMapper.writeValueAsString(request)
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