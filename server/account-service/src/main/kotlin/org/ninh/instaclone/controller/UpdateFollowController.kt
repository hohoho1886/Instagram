package org.ninh.instaclone.controller

import org.ninh.instaclone.dto.FollowCounterEvent
import org.ninh.instaclone.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/user/update")
class UpdateFollowController(
    private val userService: UserService
) {
    @PostMapping("/followingOrFollower")
    fun updateFollow(@RequestBody followCounterEvent: FollowCounterEvent) {
        userService.updateFollowCounter(followCounterEvent)
    }
}