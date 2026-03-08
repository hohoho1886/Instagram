package org.ninh.instaclone.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.util.UUID
import kotlin.time.Clock

@Entity
@Table(
    name = "post_db_content",
    indexes = [
        Index(name = "idx_user_id", columnList = "author_id"),
        Index(name = "idx_created_at", columnList = "created_at")
    ]
)
data class Post(
    @Id
    @Column(name = "post_id", nullable = false, updatable = false, columnDefinition = "UUID")
    val postId: UUID,

    @Column(name = "user_id", nullable = false)
    val authorId: UUID,

    @Column(name = "created_at", nullable = false)
    val createdAt: String = Clock.System.now().toString(),

    @Column(name = "media_url")
    val mediaUrls: String,

    @Column(name = "likes_count")
    val likesCount: Long = 0,

    @Column(name = "comments_count")
    val commentsCount: Long = 0
)