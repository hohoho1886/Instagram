package org.ninh.instaclone.repository

import jakarta.transaction.Transactional
import org.ninh.instaclone.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface PostRepository: JpaRepository<Post, String> {
    @Query("select p from Post p where p.postId in :ids")
    fun findAllByPostIds(@Param("ids") ids: List<UUID>): List<Post>

    @Modifying
    @Transactional
    @Query("""
        UPDATE Post p
        SET p.commentsCount = p.commentsCount + 1
        WHERE p.postId = :postId
    """)
    fun incrementCommentsCount(@Param("postId") postId: UUID): Int

    @Modifying
    @Transactional
    @Query("""
        UPDATE Post p
        SET p.commentsCount = p.commentsCount - 1
        WHERE p.postId = :postId
    """)
    fun decrementCommentsCount(@Param("postId") postId: UUID): Int
}