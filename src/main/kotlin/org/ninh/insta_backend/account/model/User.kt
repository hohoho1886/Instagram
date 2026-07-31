package org.ninh.insta_backend.account.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.util.UUID

@Entity
@Table(name = "user_account")
data class User(
    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    val userId: UUID = UUID.randomUUID(),
    @Column(nullable = false, unique = true)
    val username: String,
    @Column(name = "password_hash", nullable = false)
    val passwordHash: String,

    @Column(name = "avatar_url", columnDefinition = "TEXT")
    val avatarUrl: String? = null
)
