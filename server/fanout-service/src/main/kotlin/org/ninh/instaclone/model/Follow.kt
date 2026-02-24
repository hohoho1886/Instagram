package org.ninh.instaclone.model

import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Embeddable
data class FollowId(
    val followeeUsername: String,
    val followerUsername: String
)

@Entity
@Table(name = "Follows")
data class Follow(
    @EmbeddedId
    val id: FollowId
)
