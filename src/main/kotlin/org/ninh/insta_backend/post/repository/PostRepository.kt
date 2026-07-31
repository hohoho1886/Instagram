package org.ninh.insta_backend.post.repository

import org.ninh.insta_backend.post.model.Post
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PostRepository: JpaRepository<Post, UUID> {}
