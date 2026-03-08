package org.ninh.instaclone.repository

import org.ninh.instaclone.model.Like
import org.ninh.instaclone.model.LikeKey
import org.springframework.data.cassandra.repository.CassandraRepository

interface LikeRepository: CassandraRepository<Like, LikeKey> {

}