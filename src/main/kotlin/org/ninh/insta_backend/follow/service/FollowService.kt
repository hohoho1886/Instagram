package org.ninh.insta_backend.follow.service

import org.ninh.insta_backend.follow.controller.FollowEvent
import org.ninh.insta_backend.follow.controller.FollowResponse
import org.ninh.insta_backend.follow.model.Follow
import org.ninh.insta_backend.follow.model.FollowId
import org.ninh.insta_backend.follow.repository.FollowRepository
import org.postgresql.util.PSQLException
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FollowService(
    private val followRepository: FollowRepository
) {
    fun saveFollow(followEvent: FollowEvent): FollowResponse {
        val followId = FollowId(
            followerId =  UUID.fromString(followEvent.followerId),
            followingId = UUID.fromString(followEvent.followeeId)
        )
        val follow = Follow(followId)
        return try {
            followRepository.save(follow)
            FollowResponse()
        } catch (e: PSQLException) { // TODO: it seems that save does not throw when user register with same username
            FollowResponse(error = e.message)
        }
    }
}
