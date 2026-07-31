package org.ninh.insta_backend.follow.controller

import org.ninh.insta_backend.follow.service.FollowService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class FollowEvent(
    val followerId: String,
    val followeeId: String
)

data class FollowResponse(
    val error: String? = null
)

@RestController
@RequestMapping("/api/follow")
class FollowController(
    private val followService: FollowService
) {

    @PostMapping("/post")
    fun saveLike(@RequestBody followEvent: FollowEvent): ResponseEntity<FollowResponse> {
        return try {
            val res = followService.saveFollow(followEvent)
            ResponseEntity.ok(res)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(FollowResponse(error = "Registration failed: ${ex.message}"))
        }
    }
}
