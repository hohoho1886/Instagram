package org.ninh.insta_backend.feed.service

import org.ninh.insta_backend.feed.ContentRepository
import org.ninh.insta_backend.feed.model.FeedPostProjection
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class FeedService(
    private val contentRepository: ContentRepository
) {
    fun getUserFeed(currentUserId: String): List<FeedPostProjection> {
        return contentRepository.getLatestFeedForUser(UUID.fromString(currentUserId))
    }
}
