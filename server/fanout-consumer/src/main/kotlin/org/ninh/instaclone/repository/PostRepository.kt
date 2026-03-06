package org.ninh.instaclone.repository

import org.ninh.instaclone.model.Post
import org.ninh.instaclone.model.PostKey
import org.springframework.data.cassandra.repository.CassandraRepository

interface PostRepository: CassandraRepository<Post, PostKey> {
}