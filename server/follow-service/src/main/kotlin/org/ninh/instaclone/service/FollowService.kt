package org.ninh.instaclone.service

import jakarta.transaction.Transactional
import org.ninh.instaclone.client.FollowCounterEvent
import org.ninh.instaclone.client.FollowCounterType
import org.ninh.instaclone.client.UpdateFollowClient
import org.ninh.instaclone.dto.FollowRequest
import org.ninh.instaclone.dto.HandleFollowRequest
import org.ninh.instaclone.model.Follow
import org.ninh.instaclone.model.FollowId
import org.ninh.instaclone.model.FollowStatus
import org.ninh.instaclone.repository.FollowRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FollowService(
    private val followRepository: FollowRepository,
    @param:Value("\${spring.cloud.gcp.project-id}")
    private val projectId: String,
    @param:Value("\${spring.cloud.gcp.topic}")
    private val topic: String,
    private val updateFollowClient: UpdateFollowClient
) {
    @Transactional
    fun createFollowRequest(request: FollowRequest){
        val followId = FollowId(
            followerId = UUID.fromString(request.followerId),
            followeeId = UUID.fromString(request.followeeId)
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
            UUID.fromString(followRequest.followeeId),
            UUID.fromString(followRequest.followerId)
        )
        try {
            val follow = followRepository.findById(followId)
                .orElseThrow { IllegalArgumentException("Follow request not found") }
            when (followRequest.status) {
                FollowStatus.ACCEPTED -> {
                    followRepository.save(follow.copy(status = FollowStatus.ACCEPTED))
                    updateCounter(
                        FollowCounterEvent(
                            followeeId = followRequest.followeeId,
                            followerId = followRequest.followerId,
                            type = FollowCounterType.INCREMENT
                        )
                    )
                }
                FollowStatus.REJECTED -> {
                    followRepository.delete(follow)
                }
                FollowStatus.STOP -> {
                    updateCounter(
                        FollowCounterEvent(
                            followeeId = followRequest.followeeId,
                            followerId = followRequest.followerId,
                            type = FollowCounterType.DECREMENT
                        )
                    )
                    followRepository.delete(follow)
                }
                else -> {}
            }
        } catch (e: Exception){
            throw RuntimeException("Error handle follow request:", e)
        }
    }

    private fun updateCounter(update: FollowCounterEvent){
        updateFollowClient.updateFollowCounter(
            update
        )
    }
}