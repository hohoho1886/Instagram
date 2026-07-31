package org.ninh.insta_backend.follow.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.io.Serializable
import java.time.Instant
import java.util.UUID


@Embeddable
data class FollowId(
    @Column(name = "follower_id", nullable = false)
    val followerId: UUID,

    @Column(name = "following_id", nullable = false)
    val followingId: UUID
) : Serializable

@Entity
@Table(name = "follows")
data class Follow(
    @Id
    @Column(name = "follow_id", nullable = false, updatable = false)
    val id: FollowId,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: Instant = Instant.now()
)
