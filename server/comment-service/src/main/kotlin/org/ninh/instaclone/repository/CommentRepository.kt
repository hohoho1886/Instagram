package org.ninh.instaclone.repository

import org.ninh.instaclone.model.Comment
import org.ninh.instaclone.model.CommentKey
import org.springframework.data.cassandra.repository.CassandraRepository
import java.util.UUID

interface CommentRepository: CassandraRepository<Comment, CommentKey> {
    fun findByPostId(postId: UUID): List<Comment>
}