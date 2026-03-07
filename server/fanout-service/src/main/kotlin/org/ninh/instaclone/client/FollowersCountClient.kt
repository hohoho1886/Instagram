package org.ninh.instaclone.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody

data class FollowersCountRequest(
    val username: String
)

@FeignClient(name = "fanout-consumer", url = "\${account-service.url}")
interface FollowersCountClient {
    @PostMapping("/user/followersCount")
    fun getFollowersCount(@RequestBody request: FollowersCountRequest): Int
}