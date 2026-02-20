package org.ninh.instaclone.service

import jakarta.transaction.Transactional
import org.ninh.instaclone.dto.FollowRequest
import org.ninh.instaclone.dto.HandleFollowRequest
import org.ninh.instaclone.model.Follow
import org.ninh.instaclone.model.FollowId
import org.ninh.instaclone.model.FollowStatus
import org.ninh.instaclone.repository.FollowRepository
import org.springframework.stereotype.Service

@Service
class FollowService(
    val followRepository: FollowRepository
) {
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
}