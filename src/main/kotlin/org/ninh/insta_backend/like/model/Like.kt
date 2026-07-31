package org.ninh.insta_backend.like.model

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Index
import jakarta.persistence.Table
import java.io.Serializable
import java.util.UUID

@Embeddable
data class LikeId(
    @Column(name = "post_id", nullable = false)
    val postId: UUID,

    @Column(name = "author_id", nullable = false)
    val authorId: UUID
) : Serializable

@Entity
@Table(name = "likes")
data class Like(
    @EmbeddedId
    val id: LikeId,
)
