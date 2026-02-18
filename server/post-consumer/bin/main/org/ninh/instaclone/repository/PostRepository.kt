package org.ninh.instaclone.repository

import org.ninh.instaclone.model.Post
import org.springframework.data.jpa.repository.JpaRepository

interface PostRepository: JpaRepository<Post, String> {
}