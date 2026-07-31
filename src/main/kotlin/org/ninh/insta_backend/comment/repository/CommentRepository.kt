package org.ninh.insta_backend.comment.repository

import org.ninh.insta_backend.comment.model.Comment
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface CommentRepository: JpaRepository<Comment, UUID> {}
