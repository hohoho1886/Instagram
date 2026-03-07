package org.ninh.instaclone.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

data class FollowCounterEvent(
    val followeeId: String,
    val followerId: String,
    val type: FollowCounterType
)

enum class FollowCounterType {
    INCREMENT,
    DECREMENT
}

@FeignClient(name = "follow-service", url = "\${account-service.url}")
interface UpdateFollowClient {
    @PostMapping("/user/update/followingOrFollower")
    fun updateFollowCounter(@RequestBody request: FollowCounterEvent)
}