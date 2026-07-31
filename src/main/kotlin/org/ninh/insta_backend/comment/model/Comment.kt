package org.ninh.insta_backend.comment.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID
import kotlin.time.Clock

@Entity
@Table(name = "comments")
data class Comment(
    @Id
    @Column(name = "comment_id", nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "post_id", nullable = false)
    val postId: UUID,

    @Column(name = "author_id", nullable = false)
    val authorId: UUID,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
)
