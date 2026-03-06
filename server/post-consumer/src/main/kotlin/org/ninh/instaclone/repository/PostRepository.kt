package org.ninh.instaclone.repository

import org.ninh.instaclone.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.UUID

interface PostRepository: JpaRepository<Post, String> {
    @Query("select p from Post p where p.postId in :ids")
    fun findAllByPostIds(@Param("ids") ids: List<UUID>): List<Post>
}