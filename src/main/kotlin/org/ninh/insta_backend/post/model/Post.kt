package org.ninh.insta_backend.post.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "posts")
data class Post(
    @Id
    @Column(name = "id", nullable = false, updatable = false)
    val id: UUID = UUID.randomUUID(),

    @Column(name = "author_id", nullable = false)
    val authorId: UUID,

    @Column(name = "caption", columnDefinition = "TEXT")
    val caption: String? = null,

    @Column(name = "media_url", nullable = false, columnDefinition = "TEXT")
    val mediaUrl: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
)
