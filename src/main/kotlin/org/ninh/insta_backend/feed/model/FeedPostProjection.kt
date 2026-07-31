package org.ninh.insta_backend.feed.model

import java.time.Instant
import java.util.UUID

interface FeedPostProjection {
    fun getPostId(): UUID
    fun getAuthorId(): UUID
    fun getCaption(): String?
    fun getMediaUrl(): String
    fun getCreatedAt(): Instant
    fun getCommentCount(): Long
    fun getLikeCount(): Long
}
