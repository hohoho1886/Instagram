package org.ninh.instaclone.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "users")
data class User(
    @Id
    @Column(name = "user_id", columnDefinition = "UUID")
    val userId: UUID = UUID.randomUUID(),

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "email", nullable = false, unique = true)
    val email: String,

    @Column(name = "profile_picture_url")
    val profilePictureUrl: String? = null,

    @Column(name = "followers_count")
    val followersCount: Int = 0,

    @Column(name = "following_count")
    val followingCount: Int = 0
)
