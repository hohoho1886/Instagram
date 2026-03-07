package org.ninh.instaclone.controller

import org.ninh.instaclone.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class FollowersCountRequest(
    val userId: String
)

@RestController
@RequestMapping("/user")
class FollowersCountController(
    private val accountService: UserService
) {
    @PostMapping("/followersCount")
    fun getFollowersCount(@RequestBody request: FollowersCountRequest): Int {
        return accountService.getFollowersCount(request.userId)
    }
}