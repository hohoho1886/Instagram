package org.ninh.insta_backend.like.repository

import org.ninh.insta_backend.like.model.Like
import org.ninh.insta_backend.like.model.LikeId
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import java.util.UUID

interface LikeRepository: JpaRepository<Like, LikeId> {
    @Modifying
    @Query(
        value = """
            INSERT INTO likes (post_id, author_id)
            VALUES (:postId, :authorId)
            ON CONFLICT (post_id, author_id) DO NOTHING
        """,
        nativeQuery = true
    )
    fun upsertLike(postId: UUID, authorId: UUID): Int
}
