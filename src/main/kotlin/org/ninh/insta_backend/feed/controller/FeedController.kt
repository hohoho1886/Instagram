package org.ninh.insta_backend.feed.controller

import org.ninh.insta_backend.feed.model.FeedPostProjection
import org.ninh.insta_backend.feed.service.FeedService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

data class FeedRequest(
    val userId: String
)

data class FeedResponse(
    val feed: List<FeedPostProjection>? = null,
    val error: String? = null
)
@RestController
@RequestMapping("/api/feed")
class FeedController(
    private val feedService: FeedService
) {
    @PostMapping("/get")
    fun getFeed(@RequestBody feedRequest: FeedRequest): ResponseEntity<FeedResponse> {
        return try {
            val start = System.currentTimeMillis()
            val posts = feedService.getUserFeed(feedRequest.userId)
            val res = FeedResponse(feed = posts)
            //println("Service time: ${System.currentTimeMillis() - start} ms")
            ResponseEntity.ok(res)
        } catch (ex: Exception) {
            ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(FeedResponse(error = "Cannot get feed: ${ex.message}"))
        }
    }
}
